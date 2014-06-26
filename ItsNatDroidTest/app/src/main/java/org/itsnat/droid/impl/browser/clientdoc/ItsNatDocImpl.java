package org.itsnat.droid.impl.browser.clientdoc;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewMgr;
import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase se accede via script beanshell y representa el "ClientDocument" en el lado Android simétrico a los objetos JavaScript en el modo web
 * Created by jmarranz on 9/06/14.
 */
public class ItsNatDocImpl implements ItsNatDoc
{
    protected PageImpl page;
    protected Map<String,Node> nodeCacheById = new HashMap<String,Node>();
    protected DOMPathResolver pathResolver = new DOMPathResolver(this);

    public ItsNatDocImpl(PageImpl page)
    {
        this.page = page;
    }

    public void init(String sessionId,String clientId)
    {
        page.setSessionIdAndClientId(sessionId, clientId);
    }

    protected PageImpl getPage()
    {
        return page;
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
    public Node getNode(Object[] idObj)
    {
        if (idObj == null) return null;
        String id = null;
        String cachedParentId = null;
        String path = null;
        Object[] newCachedParentIds = null;
        int len = idObj.length;
        // El caso len = 1 se recibe como una string directamente por otro método
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

    private Node getNodeCached(String id) // No es público
    {
        if (id == null) return null;
        return nodeCacheById.get(id);
    }

    private void addNodeCache2(String id,Node node)
    {
        if (id == null) return; // si id es null cache desactivado
        nodeCacheById.put(id,node);
    }

    public Node getParentNode(Node parentNode)
    {
        return NodeImpl.create((View) parentNode.getView().getParent());
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

        Context ctx = page.getInflatedLayoutImpl().getContext();
        InflatedLayoutImpl inflated = page.getInflatedLayoutImpl();
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
        this.insertBefore2(parentNode,newChild,childRef,newId);
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
}
