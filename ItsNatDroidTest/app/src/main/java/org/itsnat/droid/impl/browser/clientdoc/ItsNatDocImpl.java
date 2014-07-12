package org.itsnat.droid.impl.browser.clientdoc;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.itsnat.droid.Event;
import org.itsnat.droid.EventMonitor;
import org.itsnat.droid.ItsNatDoc;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.OnServerStateLostListener;
import org.itsnat.droid.Page;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DOMStdEventListener;
import org.itsnat.droid.impl.util.WeakMapWithValue;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewMgr;

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
    protected PageImpl page;
    protected String servletPath;
    protected Map<String,Node> nodeCacheById = new HashMap<String,Node>();
    protected DOMPathResolver pathResolver = new DOMPathResolverImpl(this);
    protected WeakMapWithValue<String,DOMStdEventListener> domListeners;
    protected EventManager evtManager = new EventManager(this);
    protected List<GlobalEventListener> globalEventListeners;
    protected boolean disabledEvents = false; // En Droid tiene poco sentido y no se usa, candidato a eliminarse
    protected boolean enableEvtMonitors = true;
    protected List<EventMonitor> evtMonitorList;

    public ItsNatDocImpl(PageImpl page)
    {
        this.page = page;
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

    public List<NameValuePair> genParamURL()
    {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("itsnat_client_id",page.getId()));
        params.add(new BasicNameValuePair("itsnat_session_token",page.getItsNatSessionImpl().getToken()));
        params.add(new BasicNameValuePair("itsnat_session_id",page.getItsNatSessionImpl().getId()));
        return params;
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

    public WeakMapWithValue<String,DOMStdEventListener> getDOMListeners()
    {
        if (domListeners == null) this.domListeners = new WeakMapWithValue<String,DOMStdEventListener>();
        return domListeners;
    }

    private View createAndAddViewObject(ClassDescViewBase classDesc,View viewParent,NodeToInsertImpl newChildToIn,int index,Context ctx)
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

    public void fillViewAttributes(ClassDescViewBase classDesc,NodeToInsertImpl newChildToIn,InflatedLayoutImpl inflated)
    {
        View view = newChildToIn.getView();
        OneTimeAttrProcess oneTimeAttrProcess = new OneTimeAttrProcess();
        for(Map.Entry<String,AttrImpl> entry : newChildToIn.getAttributes().entrySet())
        {
            AttrImpl attr = entry.getValue();
            String namespace = attr.getNamespaceURI();
            String name = attr.getName();
            String value = attr.getValue();
            classDesc.setAttribute(view,namespace, name, value, oneTimeAttrProcess,inflated);
        }

        if (oneTimeAttrProcess.neededSetLayoutParams)
            view.setLayoutParams(view.getLayoutParams()); // Para que los cambios que se han hecho en los objetos "stand-alone" *.LayoutParams se entere el View asociado (esa llamada hace requestLayout creo recordar), al hacerlo al final evitamos múltiples llamadas por cada cambio en LayoutParams
    }

    private Context getContext()
    {
        // Es un método
        return page.getInflatedLayoutImpl().getContext();
    }

    @Override
    public void init(String stdSessionId,String sessionToken,String sessionId,String clientId,String servletPath)
    {
        page.setSessionIdAndClientId(stdSessionId,sessionToken,sessionId, clientId);
        this.servletPath = servletPath;
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
        ClassDescViewBase viewClassDesc = viewMgr.get(view);
        viewClassDesc.setAttribute(view,namespaceURI,name,value,null,page.getInflatedLayoutImpl());
    }

    @Override
    public void setAttributeNS2(Object[] idObj,String namespaceURI,String name,String value)
    {
        Node elem = getNode(idObj);
        setAttributeNS(elem, namespaceURI, name, value);
    }

    public void setAttributeNSBatch(Node node,String namespaceURI,String[] attrNames,String[] attrValues)
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
        ClassDescViewBase viewClassDesc = viewMgr.get(view);
        viewClassDesc.removeAttribute(view, namespaceURI, name, page.getInflatedLayoutImpl());
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
        ItsNatViewImpl itsNatView = ItsNatViewImpl.getItsNatView(page,view);
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
        ItsNatViewImpl itsNatView = ItsNatViewImpl.getItsNatView(page,view);
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
        View view = classDesc.createAndAddViewObject(null, 0, ctx);
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
        ClassDescViewBase classDesc = inflaterService.getClassDescViewMgr().get(newChildToIn.getName());
        int index = childRef == null ? -1 : getChildIndex(parentNode,childRef);

        View view = createAndAddViewObject(classDesc,parentNode.getView(),newChildToIn,index,ctx);

        newChildToIn.setView(view);

        if (newChildToIn.hasAttributes())
            fillViewAttributes(classDesc,newChildToIn,inflated);

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
            ItsNatViewImpl viewData = ItsNatViewImpl.getItsNatView(page,view);
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
    public Node addDOMEL(Object[] idObj,String type,String listenerId,String customFunction,boolean useCapture,int commMode,long timeout,int typeCode)
    {
        Node node = getNode(idObj);
        View view = node.getView();
        ItsNatViewImpl viewData = ItsNatViewImpl.getItsNatView(page,view);
        CustomFunction customFunction_PROVISIONAL = null;
        DOMStdEventListener listenerWrapper = new DOMStdEventListener(this,view,type,customFunction_PROVISIONAL,listenerId,useCapture,commMode,timeout,typeCode);
        viewData.getEventListeners().add(type,listenerWrapper);
        getDOMListeners().put(listenerId,listenerWrapper);

        EventListenerViewAdapter evtListAdapter = viewData.getEventListenerViewAdapter();
        if (type.equals("click"))
        {
            // No sabemos si ha sido registrado ya antes el EventListenerViewAdapter, pero da igual puede llamarse todas las veces que se quiera
            view.setOnClickListener(evtListAdapter);
        }
        else if (type.startsWith("mouse"))
        {
            view.setOnTouchListener(evtListAdapter);
        }
        else if (type.startsWith("key"))
        {
            view.setOnKeyListener(evtListAdapter);
        }

        return node;
    }

    public void removeDOMEL(String listenerId)
    {
        DOMStdEventListener listenerWrapper = getDOMListeners().removeByKey(listenerId);
        ItsNatViewImpl viewData = ItsNatViewImpl.getItsNatView(page,listenerWrapper.getView());
        viewData.getEventListeners().remove(listenerWrapper.getType(),listenerWrapper);
    }

    public void addGlobalEL(GlobalEventListener listener)
    {
        if (globalEventListeners == null) this.globalEventListeners = new LinkedList<GlobalEventListener>();
        globalEventListeners.add(listener);
    }

    public void removeGlobalEL(GlobalEventListener listener) { globalEventListeners.remove(listener); }

    public void setEnableEventMonitors(boolean value) { this.enableEvtMonitors = value; }

    public void addEventMonitor(EventMonitor monitor)
    {
        if (evtMonitorList == null) this.evtMonitorList = new LinkedList<EventMonitor>();
        evtMonitorList.add(monitor);
    }

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


}
