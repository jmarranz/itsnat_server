package org.itsnat.droid.impl.browser.serveritsnat;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.ClientErrorMode;
import org.itsnat.droid.EventMonitor;
import org.itsnat.droid.GenericHttpClient;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDoc;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.OnHttpRequestListener;
import org.itsnat.droid.OnServerStateLostListener;
import org.itsnat.droid.Page;
import org.itsnat.droid.event.Event;
import org.itsnat.droid.event.EventStateless;
import org.itsnat.droid.event.UserEvent;
import org.itsnat.droid.impl.browser.HttpUtil;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.servergeneric.GenericHttpClientImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.AttachedClientCometTaskRefreshEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.AttachedClientTimerRefreshEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.AttachedClientUnloadEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DOMExtEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidFocusEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidKeyEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidMotionEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidOtherEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidTextChangeEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.EventStatelessImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.UserEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.AsyncTaskEventListener;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.CometTaskEventListener;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.ContinueEventListener;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.DroidEventListener;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.TimerEventListener;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.UserEventListener;
import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.util.MapLightList;
import org.itsnat.droid.impl.util.MapList;
import org.itsnat.droid.impl.util.MapRealList;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;
import org.itsnat.droid.impl.xmlinflated.layout.page.InflatedLayoutPageImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Esta clase se accede via script beanshell y representa el "ClientDocument" en el lado Android simétrico a los objetos JavaScript en el modo web
 * Created by jmarranz on 9/06/14.
 */
public class ItsNatDocImpl implements ItsNatDoc,ItsNatDocPublic
{
    private static final String key_itsNatUserListenersByName = "itsNatUserListenersByName";

    protected PageImpl page;
    protected String itsNatServletPath; // Definido en el servidor
    protected int errorMode;
    protected String attachType;
    protected boolean eventsEnabled;
    protected boolean enableEvtMonitors = true;
    protected List<EventMonitor> evtMonitorList;
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
    protected ItsNatViewNullImpl nullView = new ItsNatViewNullImpl(this); // Viene a tener el rol del objeto Window en web, útil para registrar eventos unload etc
    protected DroidEventDispatcher eventDispatcher = new DroidEventDispatcher(this);
    protected FragmentLayoutInserter fragmentLayoutInserter = new FragmentLayoutInserter(this);

    public ItsNatDocImpl(PageImpl page)
    {
        this.page = page;
    }

    public GenericHttpClientImpl createGenericHttpClientImpl()
    {
        GenericHttpClientImpl client = new GenericHttpClientImpl(this);
        client.setOnHttpRequestErrorListener(getPageImpl().getOnHttpRequestErrorListener());
        return client;
    }

    public GenericHttpClient createGenericHttpClient()
    {
        return createGenericHttpClientImpl();
    }

    public PageImpl getPageImpl()
    {
        return page;
    }

    public String getPageURLBase()
    {
        return page.getURLBase(); // Para la carga de recursos (scripts, imágenes etc)
    }

    public EventManager getEventManager()
    {
        return evtManager;
    }

    public String getItsNatServletPath()
    {
        return itsNatServletPath;
    }

    public String getAttachType()
    {
        return attachType;
    }

    public ItsNatViewNullImpl getItsNatViewNull()
    {
        return nullView;
    }

    public DroidEventDispatcher getDroidEventDispatcher()
    {
        return eventDispatcher;
    }

    public boolean isEventsEnabled()
    {
        return eventsEnabled;
    }

    @Override
    public View getRootView()
    {
        return page.getInflatedLayoutPageImpl().getRootView();
    }

    @Override
    public View findViewByXMLId(String id)
    {
        return page.getInflatedLayoutPageImpl().findViewByXMLId(id);
    }

    @Override
    public int getResourceIdentifier(String name)
    {
        // Formato esperado: package:type/entry  ej my.app:id/someId  o bien simplemente someId

        String packageName;
        int posPkg = name.indexOf(':');
        if (posPkg != -1)
        {
            packageName = null; // Tiene package el value, ej "android:" delegamos en Resources.getIdentifier() que lo resuelva
            name = name.substring(posPkg + 1);
        }
        else
        {
            packageName = getContext().getPackageName(); // El package es necesario como parámetro sólo cuando no está en la string (recursos locales)
        }

        String type;
        int posType = name.indexOf('/');
        if (posType != -1)
        {
            type = null; // Se obtiene del name
            name = name.substring(posType + 1);
        }
        else
        {
            type = "id";
        }

        return getResourceIdentifier(name,type,packageName);
    }

    @Override
    public int getResourceIdentifier(String name, String defType, String defPackage)
    {
        // http://developer.android.com/reference/android/content/res/Resources.html#getIdentifier(java.lang.String, java.lang.String, java.lang.String)
        // Formato esperado: package:type/entry  ej my.app:id/someId  o bien type y package vienen dados como parámetros

        Context ctx = getContext();
        Resources res = ctx.getResources();
        int id = res.getIdentifier(name, defType, defPackage);
        if (id > 0)
            return id;

        XMLInflateRegistry layoutService = page.getInflatedLayoutPageImpl().getXMLInflateRegistry();
        id = layoutService.findId(name);
        return id;
    }


    @Override
    public void setEnableEventMonitors(boolean value) { this.enableEvtMonitors = value; }

    @Override
    public void addEventMonitor(EventMonitor monitor)
    {
        if (evtMonitorList == null) this.evtMonitorList = new LinkedList<EventMonitor>();
        evtMonitorList.add(monitor);
    }

    @Override
    public boolean removeEventMonitor(EventMonitor monitor)
    {
        if (evtMonitorList == null) return false;
        return evtMonitorList.remove(monitor);
    }

    public void fireEventMonitors(boolean before,boolean timeout,Event evt)
    {
        if (!this.enableEvtMonitors) return;

        if (evtMonitorList == null) return;

        for(EventMonitor curr : evtMonitorList)
        {
            if (before) curr.before(evt);
            else curr.after(evt,timeout);
        }
    }

    @Override
    public ItsNatView getItsNatView(View view)
    {
        return getItsNatViewImpl(view);
    }

    public ItsNatViewImpl getItsNatViewImpl(View view)
    {
        return ItsNatViewImpl.getItsNatView(this,view);
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

    @Override
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

    @Override
    public void eval(String code)
    {
        Interpreter interp = page.getInterpreter();
        try
        {
//long start = System.currentTimeMillis();

            interp.eval(code);

//long end = System.currentTimeMillis();
//System.out.println("LAPSE" + (end - start));
        }
        catch (EvalError ex)
        {
            showErrorMessage(false, ex.getMessage());
            throw new ItsNatDroidScriptException(ex, code);
        }
        catch (Exception ex)
        {
            showErrorMessage(false, ex.getMessage());
            throw new ItsNatDroidScriptException(ex, code);
        }
    }


    public void showErrorMessage(boolean serverErr, String msg)
    {
        int errorMode = getErrorMode();
        showErrorMessage(serverErr,msg,errorMode);
    }

    public void showErrorMessage(boolean serverErr, String msg,int errorMode)
    {
        if (errorMode == ClientErrorMode.NOT_SHOW_ERRORS) return;

        if (serverErr) // Pagina HTML con la excepcion del servidor
        {
            if ((errorMode == ClientErrorMode.SHOW_SERVER_ERRORS) ||
                    (errorMode == ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS)) // 2 = ClientErrorMode.SHOW_SERVER_ERRORS, 4 = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS
                alert("SERVER ERROR: " + msg);
        }
        else
        {
            if ((errorMode == ClientErrorMode.SHOW_CLIENT_ERRORS) ||
                    (errorMode == ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS)) // 3 = ClientErrorMode.SHOW_CLIENT_ERRORS, 4 = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS
            {
                // Ha sido un error Beanshell
                alert(msg);
            }
        }
    }

    private View createViewObjectAndFillAttributesAndAdd(ClassDescViewBased classDesc, ViewGroup viewParent, NodeToInsertImpl newChildToIn, int index, InflatedLayoutImpl inflated,PendingPostInsertChildrenTasks pending)
    {
        View view = classDesc.createViewObjectFromRemote(this,newChildToIn,pending);

        newChildToIn.setView(view);

        OneTimeAttrProcess oneTimeAttrProcess = classDesc.createOneTimeAttrProcess(view,viewParent);
        fillViewAttributes(classDesc,newChildToIn,inflated,oneTimeAttrProcess);
        classDesc.addViewObject(viewParent, view, index,oneTimeAttrProcess,inflated.getContext());

        return view;
    }


    private void fillViewAttributes(ClassDescViewBased classDesc,NodeToInsertImpl newChildToIn,InflatedLayoutImpl inflated,OneTimeAttrProcess oneTimeAttrProcess)
    {
        View view = newChildToIn.getView();

        if (newChildToIn.hasAttributes())
        {
            for (Map.Entry<String, AttrParsed> entry : newChildToIn.getAttributes().entrySet())
            {
                AttrParsed attr = entry.getValue();
                inflated.setAttribute(classDesc, view, attr, oneTimeAttrProcess,(PendingPostInsertChildrenTasks)null);
            }
        }

        oneTimeAttrProcess.executeLastTasks();
    }

    private Context getContext()
    {
        return page.getInflatedLayoutPageImpl().getContext();
    }

    @Override
    public void init(String stdSessionId,String sessionToken,String sessionId,String clientId,String servletPath,int errorMode,String attachType,boolean eventsEnabled)
    {
        if (errorMode == ClientErrorMode.NOT_CATCH_ERRORS)
            throw new ItsNatDroidException("ClientErrorMode.NOT_CATCH_ERRORS is not supported"); // No tiene mucho sentido porque el objetivo es dejar fallar y si el usuario no ha registrado "error listeners" ItsNat Droid deja siempre fallar lanzando la excepción

        page.setSessionIdAndClientId(stdSessionId,sessionToken,sessionId, clientId);
        this.itsNatServletPath = servletPath;
        this.errorMode = errorMode;
        this.attachType = attachType;
        this.eventsEnabled = eventsEnabled;
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

    @Override
    public void postDelayed(Runnable task,long delay)
    {
        getHandler().postDelayed(task,delay);
    }

    @Override
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
        if (namespaceURI == null)
        {
            String prefix = getPrefix(name);
            if (prefix != null)
            {
                namespaceURI = getPageImpl().getInflatedLayoutPageImpl().getNamespace(prefix);
                if (namespaceURI != null) // Sólo se soportan namespaces declarados en el View root, si es null se procesará como un atributo desconocido
                    name = getLocalName(name);
            }
        }

        AttrParsed attr = AttrParsed.create(namespaceURI, name, value);

        if (node instanceof NodeToInsertImpl)
        {
            NodeToInsertImpl nodeToIn = (NodeToInsertImpl)node;
            if (!nodeToIn.isInserted())
            {
                nodeToIn.setAttribute(attr);
                return;
            }
        }

        View view = node.getView();
        page.getInflatedLayoutPageImpl().setAttribute(view,attr);
    }

    @Override
    public void setAttributeNS2(Object[] idObj,String namespaceURI,String name,String value)
    {
        Node elem = getNode(idObj);
        setAttributeNS(elem, namespaceURI, name, value);
    }

    @Override
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

    private static String getPrefix(String name)
    {
        int pos = name.indexOf(':');
        if (pos == -1) return null;
        return name.substring(0,pos);
    }

    private static String getLocalName(String name)
    {
        int pos = name.indexOf(':');
        if (pos == -1) return name;
        return name.substring(pos + 1);
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
        if (namespaceURI == null)
        {
            String prefix = getPrefix(name);
            if (prefix != null)
            {
                namespaceURI = getPageImpl().getInflatedLayoutPageImpl().getNamespace(prefix);
                if (namespaceURI != null) // Sólo se soportan namespaces declarados en el View root, si es null se procesará como un atributo desconocido
                    name = getLocalName(name);
            }
        }

        if (node instanceof NodeToInsertImpl)
        {
            // Esto es raro pero es por si cambia de opinión para un atributo recién definido
            NodeToInsertImpl nodeToIn = (NodeToInsertImpl)node;
            if (!nodeToIn.isInserted())
            {
                nodeToIn.removeAttribute(namespaceURI, name);
                return;
            }
        }


        View view = node.getView();
        page.getInflatedLayoutPageImpl().removeAttribute(view, namespaceURI, name);
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
        ItsNatViewImpl itsNatView = getItsNatViewImpl(view);
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
        ItsNatViewImpl itsNatView = getItsNatViewImpl(view);
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
        return new NodeToInsertImpl(name);
    }

    private static int getChildIndex(Node parentNode,Node node)
    {
        // Esto es una chapuza pero no hay opción
        ViewGroup parentView = (ViewGroup)parentNode.getView();
        View view = node.getView();
        return InflatedLayoutPageImpl.getChildIndex(parentView, view);
    }



    @Override
    public void insertBefore(Node parentNode,Node newChild,Node childRef)
    {
        NodeToInsertImpl newChildToIn = (NodeToInsertImpl)newChild;

        InflatedLayoutImpl inflated = page.getInflatedLayoutPageImpl();
        XMLInflateRegistry inflaterService = page.getInflatedLayoutPageImpl().getXMLInflateRegistry();
        ClassDescViewBased classDesc = inflaterService.getClassDescViewMgr().get(newChildToIn.getName());
        int index = childRef == null ? -1 : getChildIndex(parentNode,childRef);

        View view = createViewObjectAndFillAttributesAndAdd(classDesc, (ViewGroup) parentNode.getView(), newChildToIn, index, inflated,null);

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
            ItsNatViewImpl viewData = getItsNatViewImpl(view);
            viewData.setNodeCacheId(null);
        }
    }

    @Override
    public void clearNodeCache()
    {
        nodeCacheById.clear();
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
    public void appendFragment(View parentView, String markup)
    {
        insertFragment(parentView,markup,null);
    }

    @Override
    public void insertFragment(View parentView, String markup,View viewRef)
    {
        // Si el fragmento a insertar es suficientemente grande el rendimiento de insertFragment puede ser varias veces superior
        // a hacerlo elemento a elemento, atributo a atributo con la API debido a la lentitud de Beanshell
        // Por ejemplo 78ms con insertFragment (parseando markup) y 179ms con beanshell puro

        fragmentLayoutInserter.insertFragment((ViewGroup) parentView, markup, viewRef);
    }

    @Override
    public void setInnerXML(Node parentNode,String markup)
    {
        appendFragment(parentNode.getView(), markup);
    }

    @Override
    public void setInnerXML2(Object[] idObj,String markup)
    {
        Node parentNode = getNode(idObj);
        setInnerXML(parentNode,markup);
    }


    @Override
    public void addDroidEL(Object[] idObj,String type,String listenerId,CustomFunction customFunction,boolean useCapture,int commMode,long timeout,int eventGroupCode)
    {
        View currentTarget = getView(idObj);
        if (currentTarget == null /*&& (!type.equals("unload") && !type.equals("load")) */) // En el caso "unload" y "load" se permite que sea nulo el target
            throw new ItsNatDroidException("INTERNAL ERROR");
        ItsNatViewImpl viewData = getItsNatViewImpl(currentTarget);
        DroidEventListener listenerWrapper = new DroidEventListener(this,currentTarget,type,customFunction,listenerId,useCapture,commMode,timeout,eventGroupCode);
        getDroidEventListeners().put(listenerId, listenerWrapper);
        viewData.getEventListeners().add(type,listenerWrapper);

        if (viewData instanceof ItsNatViewNullImpl)
            return; // Nada más que hacer

        ((ItsNatViewNotNullImpl)viewData).registerEventListenerViewAdapter(type);
    }

    @Override
    public void removeDroidEL(String listenerId)
    {
        DroidEventListener listenerWrapper = getDroidEventListeners().remove(listenerId);
        View currentTarget = listenerWrapper.getCurrentTarget(); // En el caso "unload" y "load" puede ser nulo => ¡¡YA NO!!
        ItsNatViewImpl viewData = getItsNatViewImpl(currentTarget);
        viewData.getEventListeners().remove(listenerWrapper.getType(),listenerWrapper);
    }

    @Override
    public void addGlobalEL(GlobalEventListener listener)
    {
        // Por ahora no se usa pero por imitación del Web...
        if (globalEventListeners == null) this.globalEventListeners = new LinkedList<GlobalEventListener>();
        globalEventListeners.add(listener);
    }

    @Override
    public void removeGlobalEL(GlobalEventListener listener) { globalEventListeners.remove(listener); }

    @Override
    public void sendContinueEvent(Object[] idObj,String listenerId,CustomFunction customFunc,int commMode,long timeout)
    {
        Node currTarget = getNode(idObj); // idObj puede ser nulo
        View currTargetView = currTarget != null ? currTarget.getView() : null;
        ContinueEventListener listenerWrapper = new ContinueEventListener(this,currTargetView,customFunc,listenerId,commMode,timeout);
        DOMExtEventImpl evtWrapper = (DOMExtEventImpl)listenerWrapper.createNormalEvent(null);
        listenerWrapper.dispatchEvent(evtWrapper);
    }

    private MapList<String,UserEventListener> getUserEventListenersByName(View currTargetView)
    {
        MapList<String, UserEventListener> listenersByName;
        if (currTargetView == null) listenersByName = getUserEventListenersByName();
        else
        {
            ItsNatView itsNatView = getItsNatView(currTargetView);
            listenersByName = (MapList<String, UserEventListener>) itsNatView.getUserData().get(key_itsNatUserListenersByName);
        }
        return listenersByName;
    }

    @Override
    public void addUserEL(Object[] idObj,String name,String listenerId,CustomFunction customFunc,int commMode,long timeout)
    {
        View currTarget = getView(idObj);
        UserEventListener listenerWrapper = new UserEventListener(this,currTarget,name,customFunc,listenerId,commMode,timeout);
        getUserEventListenersById().put(listenerId, listenerWrapper);
        MapList<String,UserEventListener> listenersByName;
        if (currTarget == null) listenersByName = getUserEventListenersByName();
        else
        {
            ItsNatView itsNatView = getItsNatView(currTarget);
            listenersByName = (MapList<String,UserEventListener>)itsNatView.getUserData().get(key_itsNatUserListenersByName);
            if (listenersByName == null)
            {
                listenersByName = new MapLightList<String,UserEventListener>();
                itsNatView.getUserData().set(key_itsNatUserListenersByName,listenersByName);
            }
        }

        listenersByName.add(name,listenerWrapper);
    }

    @Override
    public void removeUserEL(String listenerId)
    {
        UserEventListener listenerWrapper = getUserEventListenersById().remove(listenerId);
        if (listenerWrapper == null) return;

        View currTargetView = listenerWrapper.getCurrentTarget();
        MapList<String,UserEventListener> listenersByName = getUserEventListenersByName(currTargetView);

        listenersByName.remove(listenerWrapper.getName(),listenerWrapper);
    }

    @Override
    public UserEvent createUserEvent(String name)
    {
        return new UserEventImpl(name);
    }

    @Override
    public void dispatchUserEvent(View currTargetView,UserEvent evt)
    {
        MapList<String,UserEventListener> listenersByName = getUserEventListenersByName(currTargetView);
        if (listenersByName == null) return;

        List<UserEventListener> listeners = listenersByName.get(evt.getName());
        if (listeners == null) return;
        for(UserEventListener listener : listeners)
        {
            UserEventImpl evt2 = (UserEventImpl)listener.createNormalEvent(evt);
            listener.dispatchEvent(evt2);
        }
    }

    @Override
    public void fireUserEvent(View currTargetView,String name)
    {
        UserEvent evt = createUserEvent(name);
        dispatchUserEvent(currTargetView, evt);
    }

    @Override
    public EventStateless createEventStateless()
    {
        return new EventStatelessImpl();
    }

    @Override
    public void dispatchEventStateless(EventStateless evt,int commMode,long timeout)
    {
        EventStatelessImpl evt2 = new EventStatelessImpl(this,(EventStatelessImpl)evt,commMode,timeout);
        evt2.sendEvent();
    }

    @Override
    public void sendAsyncTaskEvent(Object[] idObj,String listenerId,CustomFunction customFunc,int commMode,long timeout)
    {
        View currTarget = getView(idObj);
        AsyncTaskEventListener listenerWrapper = new AsyncTaskEventListener(this,currTarget,customFunc,listenerId,commMode,timeout);
        DOMExtEventImpl evtWrapper = (DOMExtEventImpl)listenerWrapper.createNormalEvent(null);
        listenerWrapper.dispatchEvent(evtWrapper);
    }

    @Override
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
                DOMExtEventImpl evtWrapper = (DOMExtEventImpl) listenerWrapper.createNormalEvent(null);
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

    @Override
    public void removeTimerEL(String listenerId)
    {
        TimerEventListener listenerWrapper = getTimerEventListeners().remove(listenerId);
        if (listenerWrapper == null) return;
        Runnable callback = listenerWrapper.getCallback();
        getHandler().removeCallbacks(callback);
    }

    @Override
    public void updateTimerEL(String listenerId,long delay)
    {
        TimerEventListener listenerWrapper = getTimerEventListeners().get(listenerId);
        if (listenerWrapper == null) return;
        Runnable callback = listenerWrapper.getCallback();
        getHandler().postDelayed(callback, delay);
    }

    @Override
    public void sendCometTaskEvent(String listenerId,CustomFunction customFunc,int commMode,long timeout)
    {
        CometTaskEventListener listenerWrapper = new CometTaskEventListener(this,listenerId,customFunc,commMode,timeout);
        DOMExtEventImpl evtWrapper = (DOMExtEventImpl)listenerWrapper.createNormalEvent(null);
        listenerWrapper.dispatchEvent(evtWrapper);
    }

    @Override
    public MotionEvent createMotionEvent(String type,float x, float y)
    {
        return DroidMotionEventImpl.createMotionEventNative(type, x, y);
    }

    @Override
    public KeyEvent createKeyEvent(String type,int keyCode)
    {
        return DroidKeyEventImpl.createKeyEventNative(type, keyCode);
    }

    @Override
    public Boolean createFocusEvent(boolean hasFocus)
    {
        return DroidFocusEventImpl.createFocusEventNative(hasFocus);
    }

    @Override
    public CharSequence createTextChangeEvent(CharSequence newText)
    {
        return DroidTextChangeEventImpl.createTextChangeEventNative(newText);
    }

    @Override
    public Object createOtherEvent()
    {
        return DroidOtherEventImpl.createOtherEventNative();
    }

    @Override
    public boolean dispatchEvent(Node node, String type, Object nativeEvt)
    {
        View currTarget = NodeImpl.getView(node);
        return dispatchDroidEvent(currTarget, type, nativeEvt);
    }

    @Override
    public boolean dispatchEvent2(Object[] idObj, String type, Object nativeEvt)
    {
        Node currTarget = getNode(idObj);
        return dispatchEvent(currTarget,type,nativeEvt);
    }

    private boolean dispatchDroidEvent(View target, String type, Object nativeEvt)
    {
        ItsNatViewImpl targetViewData = getItsNatViewImpl(target);
        eventDispatcher.dispatch(targetViewData, type, nativeEvt);
        return false; // No sabemos qué poner
    }

    public void sendUnloadEvent()
    {
        Object nativeEvt = createOtherEvent();

        dispatchDroidEvent(getRootView(), "unload", nativeEvt);

        if (attachUnloadCallback != null)
        {
            attachUnloadCallback.run();
        }
    }

    public void sendLoadEvent()
    {
        Object nativeEvt = createOtherEvent();
        dispatchDroidEvent(getRootView(), "load", nativeEvt);
    }

    @Override
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

    @Override
    public void stopAttachTimerRefresh()
    {
        getHandler().removeCallbacks(attachTimerRefreshCallback);
        this.attachTimerRefreshCallback = null;
    }

    @Override
    public void sendAttachCometTaskRefresh(String listenerId,int commMode,long timeout)
    {
        AttachedClientCometTaskRefreshEventImpl evt = new AttachedClientCometTaskRefreshEventImpl(this,listenerId,commMode,timeout);
        evt.sendEvent();
    }

    @Override
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

    @Override
    public void downloadScript(String src)
    {
        OnHttpRequestListener listener = new OnHttpRequestListener()
        {
            @Override
            public void onRequest(Page page,HttpRequestResult response)
            {
                eval(response.getResponseText());
            }
        };

        downloadFile(src,HttpUtil.MIME_BEANSHELL,listener);
    }

    public void downloadFile(String src,String mime,OnHttpRequestListener listener)
    {
        GenericHttpClientImpl client = createGenericHttpClientImpl();

        src = HttpUtil.composeAbsoluteURL(src,client.getPageURL());

        client.setURL(src)
        .setOverrideMimeType(mime)
        .setOnHttpRequestListener(listener)
        .requestAsync();
    }
}
