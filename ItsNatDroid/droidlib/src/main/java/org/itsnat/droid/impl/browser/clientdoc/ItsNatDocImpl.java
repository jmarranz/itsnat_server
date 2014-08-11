package org.itsnat.droid.impl.browser.clientdoc;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.ItsNatDoc;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.OnServerStateLostListener;
import org.itsnat.droid.Page;
import org.itsnat.droid.event.UserEvent;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.AttachedClientCometTaskRefreshEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.AttachedClientTimerRefreshEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.AttachedClientUnloadEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DOMExtEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidFocusEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidKeyEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidMotionEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidOtherEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidTextChangeEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.UserEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.ClickEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.DroidEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.FocusEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.KeyEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.TextChangeEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.TouchEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.AsyncTaskEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.CometTaskEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.ContinueEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.TimerEventListener;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.UserEventListener;
import org.itsnat.droid.impl.util.MapLightList;
import org.itsnat.droid.impl.util.MapList;
import org.itsnat.droid.impl.util.MapRealList;
import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Esta clase se accede via script beanshell y representa el "ClientDocument" en el lado Android simétrico a los objetos JavaScript en el modo web
 * Created by jmarranz on 9/06/14.
 */
public class ItsNatDocImpl implements ItsNatDoc,ItsNatDocPublic
{
    private static final String key_itsNatUserListenersByName = "itsNatUserListenersByName";

    protected PageImpl page;
    protected String servletPath;
    protected int errorMode;
    protected String attachType;
    protected Map<String,Node> nodeCacheById = new HashMap<String,Node>();
    protected DOMPathResolver pathResolver = new DOMPathResolverImpl(this);
    protected Map<String,DroidEventListener> droidEventListeners;
    protected Map<String,TimerEventListener> timerEventListeners;
    protected Map<String,UserEventListener> userListenersById;
    protected MapList<String,UserEventListener> userListenersByName;
    protected Handler handler;
    protected Runnable attachTimerRefreshCallback;
    protected Runnable attachUnloadCallback;
    protected EventManager evtManager = new EventManager(this);
    protected List<GlobalEventListener> globalEventListeners;
    protected boolean disabledEvents = false; // En Droid tiene poco sentido y no se usa, candidato a eliminarse
    protected ItsNatViewNullImpl nullView; // Viene a tener el rol del objeto Window en web, útil para registrar eventos unload etc

    public ItsNatDocImpl(PageImpl page)
    {
        this.page = page;
        this.nullView = new ItsNatViewNullImpl(page);
    }


    public PageImpl getPageImpl()
    {
        return page;
    }

    public EventManager getEventManager()
    {
        return evtManager;
    }

    public String getServletPath()
    {
        return servletPath;
    }

    public String getAttachType()
    {
        return attachType;
    }

    public ItsNatViewNullImpl getItsNatViewNull()
    {
        return nullView;
    }

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("itsnat_client_id",page.getId()));
        params.add(new BasicNameValuePair("itsnat_session_token",page.getItsNatSessionImpl().getToken()));
        params.add(new BasicNameValuePair("itsnat_session_id",page.getItsNatSessionImpl().getId()));
        return params;
    }

    public String getStringPathFromView(View view)
    {
        return getStringPathFromNode(NodeImpl.create(view));
    }

    public String getStringPathFromNode(Node node)
    {
        if (node == null) return null;

        String nodeId = getNodeCacheId(node);
        if (nodeId != null) return "id:" + nodeId; // es undefined si no esta cacheado (o null si se quito)
        else
        {
            String parentId = null;
            Node parentNode = node;
            do
            {
                parentNode = getParentNode(parentNode);
                parentId = getNodeCacheId(parentNode); // si parentNode es null devuelve null
            }
            while((parentId == null)&&(parentNode != null));

            String path = pathResolver.getStringPathFromNode(node,parentNode); // Si parentNode es null (parentId es null) devuelve un path absoluto
            if (parentNode != null) return "pid:" + parentId + ":" + path;
            return path; // absoluto
        }
    }

    public boolean isDisabledEvents()
    {
        return disabledEvents;
    }

    public void setDisabledEvents()
    {
        this.disabledEvents = true;
    }

    public Map<String,DroidEventListener> getDroidEventListeners()
    {
        if (droidEventListeners == null) this.droidEventListeners = new HashMap<String,DroidEventListener>();
        return droidEventListeners;
    }

    public Map<String,TimerEventListener> getTimerEventListeners()
    {
        if (timerEventListeners == null) this.timerEventListeners = new HashMap<String,TimerEventListener>();
        return timerEventListeners;
    }

    public Map<String,UserEventListener> getUserEventListenersById()
    {
        if (userListenersById == null) this.userListenersById = new HashMap<String,UserEventListener>();
        return userListenersById;
    }

    public MapList<String,UserEventListener> getUserEventListenersByName()
    {
        if (userListenersByName == null) this.userListenersByName = new MapRealList<String,UserEventListener>();
        return userListenersByName;
    }

    public Handler getHandler()
    {
        if (handler == null) this.handler = new Handler(); // Se asociará (debe) al hilo UI
        return handler;
    }

    private View createAndAddViewObject(ClassDescViewBased classDesc,View viewParent,NodeToInsertImpl newChildToIn,int index,Context ctx)
    {
        int idStyle = findStyleAttribute(newChildToIn, ctx);
        return classDesc.createAndAddViewObject(viewParent, index, idStyle, ctx);
    }

    public static int findStyleAttribute(NodeToInsertImpl newChildToIn,Context ctx)
    {
        AttrImpl styleAttr = newChildToIn.getAttribute(null,"style");
        if (styleAttr == null) return 0;
        String value = styleAttr.getValue();
        return AttrDesc.getIdentifier(value, ctx);
    }

    public void fillViewAttributes(ClassDescViewBased classDesc,NodeToInsertImpl newChildToIn,InflatedLayoutImpl inflated,PageImpl page)
    {
        View view = newChildToIn.getView();
        OneTimeAttrProcess oneTimeAttrProcess = new OneTimeAttrProcess();
        for(Map.Entry<String,AttrImpl> entry : newChildToIn.getAttributes().entrySet())
        {
            AttrImpl attr = entry.getValue();
            String namespace = attr.getNamespaceURI();
            String name = attr.getName();
            String value = attr.getValue();
            classDesc.setAttribute(page,view,namespace, name, value, oneTimeAttrProcess,inflated);
        }

        if (oneTimeAttrProcess.neededSetLayoutParams)
            view.setLayoutParams(view.getLayoutParams()); // Para que los cambios que se han hecho en los objetos "stand-alone" *.LayoutParams se entere el View asociado (esa llamada hace requestLayout creo recordar), al hacerlo al final evitamos múltiples llamadas por cada cambio en LayoutParams
    }

    private Context getContext()
    {
        return page.getInflatedLayoutImpl().getContext();
    }

    @Override
    public void init(String stdSessionId,String sessionToken,String sessionId,String clientId,String servletPath,int errorMode,String attachType)
    {
        if (errorMode == ClientErrorMode.NOT_CATCH_ERRORS)
            throw new ItsNatDroidException("ClientErrorMode.NOT_CATCH_ERRORS is not supported"); // No tiene mucho sentido porque el objetivo es dejar fallar y si el usuario no ha registrado "error listeners" ItsNat Droid deja siempre fallar lanzando la excepción

        page.setSessionIdAndClientId(stdSessionId,sessionToken,sessionId, clientId);
        this.servletPath = servletPath;
        this.errorMode = errorMode;
        this.attachType = attachType;
    }

    public Runnable getAttachTimerRefreshCallback()
    {
        return attachTimerRefreshCallback;
    }

    public int getErrorMode()
    {
        return errorMode;
    }

    @Override
    public Page getPage()
    {
        // Es un método público que puede ser interesante para acceder a info de la página desde beanshell (por ej acceder al Context de la página desde fuera)
        return page;
    }

    @Override
    public void alert(Object value)
    {
        alert("Alert", value);
    }

    @Override
    public void alert(String title,Object value)
    {
        String text = value != null ? value.toString() : "null";
        SimpleAlert.show(title,text,getContext());
    }

    @Override
    public void toast(Object value,int duration)
    {
        String text = value != null ? value.toString() : "null";
        Toast.makeText(getContext(),text, duration).show();
    }

    @Override
    public void toast(Object value)
    {
        toast(value,Toast.LENGTH_SHORT);
    }

    public void postDelayed(Runnable task,long delay)
    {
        getHandler().postDelayed(task,delay);
    }

    public void onServerStateLost()
    {
        OnServerStateLostListener listener = page.getOnServerStateLostListener();
        if (listener != null) listener.onServerStateLost(page);
    }

    @Override
    public void setAttribute(Node node,String name,String value)
    {
        setAttributeNS(node,null,name,value);
    }

    @Override
    public void setAttribute2(Object[] idObj,String name,String value)
    {
        setAttributeNS2(idObj, null, name, value);
    }

    @Override
    public void setAttributeNS(Node node,String namespaceURI,String name,String value)
    {
        if (node instanceof NodeToInsertImpl)
        {
            NodeToInsertImpl nodeToIn = (NodeToInsertImpl)node;
            if (!nodeToIn.isInserted())
            {
                nodeToIn.setAttribute(namespaceURI,name,value);
                return;
            }
        }

        View view = node.getView();
        ClassDescViewMgr viewMgr = page.getInflatedLayoutImpl().getXMLLayoutInflateService().getClassDescViewMgr();
        ClassDescViewBased viewClassDesc = viewMgr.get(view);
        viewClassDesc.setAttribute(page,view,namespaceURI,name,value,null,page.getInflatedLayoutImpl());
    }

    @Override
    public void setAttributeNS2(Object[] idObj,String namespaceURI,String name,String value)
    {
        Node elem = getNode(idObj);
        setAttributeNS(elem, namespaceURI, name, value);
    }

    public void setAttrBatch(Node node,String namespaceURI,String[] attrNames,String[] attrValues)
    {
        int len = attrNames.length;
        for(int i = 0; i < len; i++)
        {
            String name = attrNames[i];
            String value = attrValues[i];
            setAttributeNS(node,namespaceURI,name,value);
        }
    }

    @Override
    public void removeAttribute(Node node, String name)
    {
        removeAttributeNS(node,null,name);
    }

    @Override
    public void removeAttribute2(Object[] idObj, String name)
    {
        removeAttributeNS2(idObj,null,name);
    }

    @Override
    public void removeAttributeNS(Node node, String namespaceURI, String name)
    {
        View view = node.getView();
        ClassDescViewMgr viewMgr = page.getInflatedLayoutImpl().getXMLLayoutInflateService().getClassDescViewMgr();
        ClassDescViewBased viewClassDesc = viewMgr.get(view);
        viewClassDesc.removeAttribute(page,view, namespaceURI, name, page.getInflatedLayoutImpl());
    }

    @Override
    public void removeAttributeNS2(Object[] idObj, String namespaceURI, String name)
    {
        Node node = getNode(idObj);
        removeAttributeNS(node, namespaceURI, name);
    }

    @Override
    public View getView(Object[] idObj)
    {
        // Este método es llamado por ScriptUtil.getNodeReference(), el usuario espera que devuelva un View no nuestro Node wrapper
        Node node = getNode(idObj);
        if (node == null) return null;
        return node.getView();
    }

    @Override
    public Node getNode(Object[] idObj)
    {
        if (idObj == null) return null;
        String id = null;
        String cachedParentId = null;
        String path = null;
        Object[] newCachedParentIds = null;
        int len = idObj.length;
        if (len == 1)
        {
            id = (String)idObj[0];
        }
        else if (len == 2)
        {
            id = (String)idObj[0];
            newCachedParentIds = (Object[])idObj[1];
        }
        else if (len >= 3)
        {
            cachedParentId = (String)idObj[0];
            id =   (String)idObj[1];
            path = (String)idObj[2];
            if (len == 4) newCachedParentIds = (Object[])idObj[3];
        }
        return getNode(id,path,cachedParentId,newCachedParentIds);
    }


    private Node getNode(String id,String path,String cachedParentId,Object[] newCachedParentIds)
    {
        Node cachedParent = null;
        if (cachedParentId != null)
        {
            cachedParent = getNodeCached(cachedParentId);
            if (cachedParent == null) throw new ItsNatDroidException("Unexpected error");
        }

        Node node = getNode2(cachedParent,new Object[]{id,path});
        if (newCachedParentIds != null)
        {
            Node parentNode = getParentNode(node);
            int len = newCachedParentIds.length;
            for(int i = 0; i < len; i++)
            {
                addNodeCache2((String)newCachedParentIds[i],parentNode);
                parentNode = getParentNode(parentNode);
            }
        }
        return node;
    }

    private Node getNode2(Node parentNode,String id)
    {
        return getNode2(parentNode,new Object[]{id});
    }

    private Node getNode2(Node parentNode,Object[] idObj) // No es público
    {
        if (idObj == null) return null;
        String id = null;
        String path = null;

        id = (String)idObj[0];
        if (idObj.length == 2) path = (String)idObj[1];

        if ((id == null) && (path == null)) return null; // raro
        if (path == null) return getNodeCached(id); // Debe estar en la cache
        else
        {
            // si parentNode es null caso de path absoluto, si no, path relativo
            Node node = pathResolver.getNodeFromPath(path,parentNode);
            if (id != null) addNodeCache2(id,node);
            return node;
        }
    }

    private String getNodeCacheId(Node node)
    {
        View view = node.getView();
        ItsNatViewImpl itsNatView = page.getItsNatViewImpl(view);
        return itsNatView.getNodeCacheId();
    }

    private Node getNodeCached(String id) // No es público
    {
        if (id == null) return null;
        return nodeCacheById.get(id);
    }

    private void addNodeCache2(String id,Node node)
    {
        if (id == null) return; // si id es null cache desactivado
        nodeCacheById.put(id,node);
        View view = node.getView();
        ItsNatViewImpl itsNatView = page.getItsNatViewImpl(view);
        itsNatView.setNodeCacheId(id);
    }

    public Node getParentNode(Node node)
    {
        return NodeImpl.create((View) node.getView().getParent());
    }

    @Override
    public Node createElement(String name)
    {
        return createElementNS(null,name);
    }

    @Override
    public Node createElementNS(String namespaceURI,String name)
    {
        // El namespaceURI es irrelevante
        /*
        Context ctx = page.getInflatedLayoutImpl().getContext();
        XMLLayoutInflateService inflaterService = page.getInflatedLayoutImpl().getXMLLayoutInflateService();
        ClassDescViewBase classDesc = inflaterService.getClassDescViewBase(name);
        View currentTarget = classDesc.createAndAddViewObject(null, 0, ctx);
        */
        return new NodeToInsertImpl(name);
    }

    private int getChildIndex(Node parentNode,Node node)
    {
        // Esto es una chapuza pero no hay opción
        ViewGroup parentView = (ViewGroup)parentNode.getView();
        View view = node.getView();
        int size = parentView.getChildCount();
        for(int i = 0; i < size; i++)
        {
            if (parentView.getChildAt(i) == view)
                return i;
        }
        return -1;
    }


    @Override
    public void insertBefore(Node parentNode,Node newChild,Node childRef)
    {
        NodeToInsertImpl newChildToIn = (NodeToInsertImpl)newChild;

        InflatedLayoutImpl inflated = page.getInflatedLayoutImpl();
        Context ctx = inflated.getContext();
        XMLLayoutInflateService inflaterService = page.getInflatedLayoutImpl().getXMLLayoutInflateService();
        ClassDescViewBased classDesc = inflaterService.getClassDescViewMgr().get(newChildToIn.getName());
        int index = childRef == null ? -1 : getChildIndex(parentNode,childRef);

        View view = createAndAddViewObject(classDesc,parentNode.getView(),newChildToIn,index,ctx);

        newChildToIn.setView(view);

        if (newChildToIn.hasAttributes())
            fillViewAttributes(classDesc,newChildToIn,inflated,page);

        newChildToIn.setInserted();
    }

    @Override
    public void insertBefore2(Node parentNode,Node newChild,Node childRef,String newId)
    {
        insertBefore(parentNode, newChild, childRef);
        if (newId != null) addNodeCache2(newId,newChild);
    }

    @Override
    public void insertBefore3(Object[] parentIdObj,Node newChild,Object[] childRefIdObj,String newId)
    {
        Node parentNode = getNode(parentIdObj);
        Node childRef = getNode2(parentNode,childRefIdObj);
        insertBefore2(parentNode,newChild,childRef,newId);
    }

    @Override
    public void appendChild(Node parentNode,Node newChild)
    {
        insertBefore(parentNode,newChild,null);
    }

    @Override
    public void appendChild2(Node parentNode,Node newChild,String newId)
    {
        appendChild(parentNode, newChild);
        if (newId != null) addNodeCache2(newId,newChild);
    }

    @Override
    public void appendChild3(Object[] idObj,Node newChild,String newId)
    {
        Node parentNode = getNode(idObj);
        appendChild2(parentNode, newChild, newId);
    }

    @Override
    public void removeChild(Node child)
    {
        if (child == null) return; // Raro

        removeChild(child.getView());
    }

    private void removeChild(View child)
    {
        if (child == null) return; // Raro

        ViewGroup parentView = (ViewGroup)child.getParent();
        parentView.removeView(child);
    }

    @Override
    public void removeChild2(String id,boolean isText)
    {
        // isText es siempre false
        if (isText) throw new ItsNatDroidException("Unexpected");
        Node child = getNode(new Object[]{id});
        removeChild(child);
    }

    @Override
    public void removeChild3(Object[] parentIdObj,String childRelPath,boolean isText)
    {
        if (isText) throw new ItsNatDroidException("Unexpected");
        Node parentNode = getNode(parentIdObj);
        Node child = getNode2(parentNode,new Object[]{null,childRelPath});
        removeChild(child);
    }

    @Override
    public void removeNodeCache(String[] idList)
    {
        int len = idList.length;
        for(int i = 0; i < len; i++)
        {
            String id = idList[i];
            Node node = nodeCacheById.remove(id);
            if (node == null) continue; // por si acaso, no debería ocurrir
            View view = node.getView();
            ItsNatViewImpl viewData = page.getItsNatViewImpl(view);
            viewData.setNodeCacheId(null);
        }
    }

    private View getChildNode(int i,Node parentNode)
    {
        View parentView = parentNode.getView();
        if (parentView instanceof ViewGroup)
            return ((ViewGroup)parentView).getChildAt(i);
        return null;
    }

    private int getLenChildNodes(Node node)
    {
        View view = node.getView();
        if (view instanceof ViewGroup)
            return ((ViewGroup)view).getChildCount();
        return 0;
    }

    private void removeAllChild(Node parentNode) // No es público
    {
        while(getLenChildNodes(parentNode) > 0)
        {
            View child = getChildNode(0,parentNode);
            removeChild(child);
        }
    }

    @Override
    public void removeAllChild2(Object[] parentIdObj)
    {
        Node parentNode = getNode(parentIdObj);
        removeAllChild(parentNode);
    }

    @Override
    public Node addNodeCache(Object[] idObj)
    {
        return getNode(idObj);
    }

    @Override
    public void addDroidEL(Object[] idObj,String type,String listenerId,CustomFunction customFunction,boolean useCapture,int commMode,long timeout,int eventGroupCode)
    {
        View currentTarget = getView(idObj);
        if (currentTarget == null && !type.equals("unload")) // En el caso "unload" se permite que sea nulo el target
            throw new ItsNatDroidException("INTERNAL ERROR");
        ItsNatViewImpl viewData = page.getItsNatViewImpl(currentTarget);
        DroidEventListener listenerWrapper = new DroidEventListener(this,currentTarget,type,customFunction,listenerId,useCapture,commMode,timeout,eventGroupCode);
        getDroidEventListeners().put(listenerId, listenerWrapper);
        viewData.getEventListeners().add(type,listenerWrapper);

        if (viewData instanceof ItsNatViewNullImpl)
            return; // Nada más que hacer

        if (type.equals("click"))
        {
            // No sabemos si ha sido registrado ya antes el ClickEventListenerViewAdapter, pero da igual puede llamarse todas las veces que se quiera
            ClickEventListenerViewAdapter evtListAdapter = viewData.getClickEventListenerViewAdapter();
            currentTarget.setOnClickListener(evtListAdapter);
        }
        else if (type.equals("change"))
        {
            // Como el listener nativo se puede registrar muchas veces nosotros tenemos que hacerlo UNA sola vez y necesitamos detectarlo
            // por ello evtListAdapter puede ser null
            TextChangeEventListenerViewAdapter evtListAdapter = viewData.getTextChangeEventListenerViewAdapter();
            if (evtListAdapter == null)
            {
                evtListAdapter = new TextChangeEventListenerViewAdapter(viewData);
                viewData.setTextChangeEventListenerViewAdapter(evtListAdapter);
                // El change está pensado para el componente EditText pero el método addTextChangedListener está a nivel de TextView, por si acaso
                ((TextView)currentTarget).addTextChangedListener(evtListAdapter); // Sólo registramos una vez
            }
        }
        else if (type.equals("focus") || type.equals("blur"))
        {
            FocusEventListenerViewAdapter evtListAdapter = viewData.getFocusEventListenerViewAdapter();
            currentTarget.setOnFocusChangeListener(evtListAdapter);
        }
        else if (type.startsWith("key"))
        {
            KeyEventListenerViewAdapter evtListAdapter = viewData.getKeyEventListenerViewAdapter();
            currentTarget.setOnKeyListener(evtListAdapter);
        }
        else if (type.startsWith("touch"))
        {
            TouchEventListenerViewAdapter evtListAdapter = viewData.getTouchEventListenerViewAdapter();
            currentTarget.setOnTouchListener(evtListAdapter);
        }
    }

    public void removeDroidEL(String listenerId)
    {
        DroidEventListener listenerWrapper = getDroidEventListeners().remove(listenerId);
        View currentTarget = listenerWrapper.getCurrentTarget(); // En el caso "unload" puede ser nulo
        ItsNatViewImpl viewData = page.getItsNatViewImpl(currentTarget);
        viewData.getEventListeners().remove(listenerWrapper.getType(),listenerWrapper);
    }

    public void addGlobalEL(GlobalEventListener listener)
    {
        // Por ahora no se usa pero por imitación del Web...
        if (globalEventListeners == null) this.globalEventListeners = new LinkedList<GlobalEventListener>();
        globalEventListeners.add(listener);
    }

    public void removeGlobalEL(GlobalEventListener listener) { globalEventListeners.remove(listener); }

    public void sendContinueEvent(Object[] idObj,String listenerId,CustomFunction customFunc,int commMode,long timeout)
    {
        Node currTarget = getNode(idObj); // idObj puede ser nulo
        View currTargetView = currTarget != null ? currTarget.getView() : null;
        ContinueEventListener listenerWrapper = new ContinueEventListener(this,currTargetView,customFunc,listenerId,commMode,timeout);
        DOMExtEventImpl evtWrapper = (DOMExtEventImpl)listenerWrapper.createEventWrapper(null);
        listenerWrapper.dispatchEvent(evtWrapper);
    }

    private MapList<String,UserEventListener> getUserEventListenersByName(View currTargetView)
    {
        MapList<String, UserEventListener> listenersByName;
        if (currTargetView == null) listenersByName = getUserEventListenersByName();
        else
        {
            ItsNatView itsNatView = getPageImpl().getItsNatView(currTargetView);
            listenersByName = (MapList<String, UserEventListener>) itsNatView.getUserData().get(key_itsNatUserListenersByName);
        }
        return listenersByName;
    }

    public void addUserEL(Object[] idObj,String name,String listenerId,CustomFunction customFunc,int commMode,long timeout)
    {
        View currTarget = getView(idObj);
        UserEventListener listenerWrapper = new UserEventListener(this,currTarget,name,customFunc,listenerId,commMode,timeout);
        getUserEventListenersById().put(listenerId, listenerWrapper);
        MapList<String,UserEventListener> listenersByName;
        if (currTarget == null) listenersByName = getUserEventListenersByName();
        else
        {
            ItsNatView itsNatView = getPageImpl().getItsNatView(currTarget);
            listenersByName = (MapList<String,UserEventListener>)itsNatView.getUserData().get(key_itsNatUserListenersByName);
            if (listenersByName == null)
            {
                listenersByName = new MapLightList<String,UserEventListener>();
                itsNatView.getUserData().set(key_itsNatUserListenersByName,listenersByName);
            }
        }

        listenersByName.add(name,listenerWrapper);
    }

    public void removeUserEL(String listenerId)
    {
        UserEventListener listenerWrapper = getUserEventListenersById().remove(listenerId);
        if (listenerWrapper == null) return;

        View currTargetView = listenerWrapper.getCurrentTarget();
        MapList<String,UserEventListener> listenersByName = getUserEventListenersByName(currTargetView);

        listenersByName.remove(listenerWrapper.getName(),listenerWrapper);
    }

    public UserEvent createUserEvent(String name)
    {
        return new UserEventImpl(name);
    }

    public void dispatchUserEvent(View currTargetView,UserEvent evt)
    {
        MapList<String,UserEventListener> listenersByName = getUserEventListenersByName(currTargetView);
        if (listenersByName == null) return;

        List<UserEventListener> listeners = listenersByName.get(evt.getName());
        if (listeners == null) return;
        for(UserEventListener listener : listeners)
        {
            UserEventImpl evt2 = (UserEventImpl)listener.createEventWrapper(evt);
            listener.dispatchEvent(evt2);
        }
    }

    public void fireUserEvent(View currTargetView,String name)
    {
        UserEvent evt = createUserEvent(name);
        dispatchUserEvent(currTargetView, evt);
    }

    public void sendAsyncTaskEvent(Object[] idObj,String listenerId,CustomFunction customFunc,int commMode,long timeout)
    {
        View currTarget = getView(idObj);
        AsyncTaskEventListener listenerWrapper = new AsyncTaskEventListener(this,currTarget,customFunc,listenerId,commMode,timeout);
        DOMExtEventImpl evtWrapper = (DOMExtEventImpl)listenerWrapper.createEventWrapper(null);
        listenerWrapper.dispatchEvent(evtWrapper);
    }

    public void addTimerEL(Object[] idObj,String listenerId,CustomFunction customFunc,int commMode,long timeout,long delay)
    {
        View currTarget = getView(idObj);
        final TimerEventListener listenerWrapper = new TimerEventListener(this,currTarget,customFunc,listenerId,commMode,timeout);

        Runnable callback = new Runnable()
        {
            @Override
            public void run()
            {
                // Se ejecutará en el hilo UI
                DOMExtEventImpl evtWrapper = (DOMExtEventImpl) listenerWrapper.createEventWrapper(null);
                try
                {
                    listenerWrapper.dispatchEvent(evtWrapper);
                }
                catch(Exception ex)
                {
                    OnEventErrorListener errorListener = getPageImpl().getOnEventErrorListener();
                    if (errorListener != null)
                    {
                        errorListener.onError(ex, evtWrapper);
                        return;
                    }
                    else
                    {
                        if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException) ex;
                        else throw new ItsNatDroidException(ex);
                    }
                }
            }
        };
        listenerWrapper.setCallback(callback);
        getHandler().postDelayed(callback, delay);
        getTimerEventListeners().put(listenerId, listenerWrapper);
    }

    public void removeTimerEL(String listenerId)
    {
        TimerEventListener listenerWrapper = getTimerEventListeners().remove(listenerId);
        if (listenerWrapper == null) return;
        Runnable callback = listenerWrapper.getCallback();
        getHandler().removeCallbacks(callback);
    }

    public void updateTimerEL(String listenerId,long delay)
    {
        TimerEventListener listenerWrapper = getTimerEventListeners().get(listenerId);
        if (listenerWrapper == null) return;
        Runnable callback = listenerWrapper.getCallback();
        getHandler().postDelayed(callback, delay);
    }

    public void sendCometTaskEvent(String listenerId,CustomFunction customFunc,int commMode,long timeout)
    {
        CometTaskEventListener listenerWrapper = new CometTaskEventListener(this,listenerId,customFunc,commMode,timeout);
        DOMExtEventImpl evtWrapper = (DOMExtEventImpl)listenerWrapper.createEventWrapper(null);
        listenerWrapper.dispatchEvent(evtWrapper);
    }

    public MotionEvent createMotionEvent(String type,float x, float y)
    {
        return DroidMotionEventImpl.createMotionEventNative(type, x, y);
    }

    public KeyEvent createKeyEvent(String type,int keyCode)
    {
        return DroidKeyEventImpl.createKeyEventNative(type, keyCode);
    }

    public Boolean createFocusEvent(boolean hasFocus)
    {
        return DroidFocusEventImpl.createFocusEventNative(hasFocus);
    }

    public CharSequence createTextChangeEvent(CharSequence newText)
    {
        return DroidTextChangeEventImpl.createTextChangeEventNative(newText);
    }

    public Object createOtherEvent()
    {
        return DroidOtherEventImpl.createOtherEventNative();
    }

    public boolean dispatchEvent(Node node, String type, Object nativeEvt)
    {
        View currTarget = NodeImpl.getView(node);
        return dispatchDroidEvent(currTarget, type, nativeEvt);
    }

    public boolean dispatchEvent2(Object[] idObj, String type, Object nativeEvt)
    {
        Node currTarget = getNode(idObj);
        return dispatchEvent(currTarget,type,nativeEvt);
    }

    private boolean dispatchDroidEvent(View target, String type, Object nativeEvt)
    {
        ItsNatViewImpl targetViewData = getPageImpl().getItsNatViewImpl(target);
        DroidEventListenerViewAdapter.dispatch(targetViewData, type, nativeEvt);
        return false; // No sabemos qué poner
    }

    public void sendUnloadEvent()
    {
        Object nativeEvt = createOtherEvent();
        dispatchDroidEvent((View) null, "unload", nativeEvt);

        if (attachUnloadCallback != null)
        {
            attachUnloadCallback.run();
        }
    }

    public boolean dispatchUserEvent2(Object[] idObj,UserEvent evt)
    {
        View currTarget = getView(idObj);
        dispatchUserEvent(currTarget,evt);
        return false; // No sabemos qué poner;
    }

    @Override
    public void initAttachTimerRefresh(final int interval,final int commMode,final long timeout)
    {
        final ItsNatDocImpl itsNatDoc = this;
        this.attachTimerRefreshCallback = new Runnable()
        {
            @Override
            public void run()
            {

            AttachedClientTimerRefreshEventImpl evtWrapper = new AttachedClientTimerRefreshEventImpl(itsNatDoc,interval,commMode,timeout);
            try
            {
                evtWrapper.sendEvent();
            }
            catch(Exception ex)
            {
                OnEventErrorListener errorListener = getPageImpl().getOnEventErrorListener();
                if (errorListener != null)
                {
                    errorListener.onError(ex, evtWrapper);
                    return;
                }
                else
                {
                    if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException) ex;
                    else throw new ItsNatDroidException(ex);
                }
            }
            }
        };
        getHandler().postDelayed(attachTimerRefreshCallback, interval);
    }

    public void stopAttachTimerRefresh()
    {
        getHandler().removeCallbacks(attachTimerRefreshCallback);
        this.attachTimerRefreshCallback = null;
    }

    public void sendAttachCometTaskRefresh(String listenerId,int commMode,long timeout)
    {
        AttachedClientCometTaskRefreshEventImpl evt = new AttachedClientCometTaskRefreshEventImpl(this,listenerId,commMode,timeout);
        evt.sendEvent();
    }

    public void addAttachUnloadListener(final int commMode)
    {
        final ItsNatDocImpl itsNatDoc = this;
        this.attachUnloadCallback = new Runnable()
        {
            @Override
            public void run()
            {
                AttachedClientUnloadEventImpl evt = new AttachedClientUnloadEventImpl(itsNatDoc,commMode,-1);
                evt.sendEvent();
            }
        };
    }
}
