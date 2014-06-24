package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflater;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewMgr;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase se accede via script beanshell y representa el "ClientDocument" en el lado Android simétrico a los objetos JavaScript en el modo web
 * Created by jmarranz on 9/06/14.
 */
public class ItsNatDocImpl implements ItsNatDoc
{
    protected PageImpl page;
    protected Map<String,View> nodeCacheById = new HashMap<String,View>();
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

    public void setAttribute(View node,String name,String value)
    {
        setAttributeNS(node,null,name,value);
    }

    public void setAttribute2(Object[] idObj,String name,String value)
    {
        setAttributeNS2(idObj, null, name, value);
    }

    public void setAttributeNS(View node,String namespaceURI,String name,String value)
    {
        //name = removePrefix(name); // Por si acaso filtramos un posible prefijo, nos nos interesa un valor con prefijo que nos envíen ej "android:background" por una parte porque no tiene sentido (ni en DOM ni en Views) y porque gestionamos los nombres de atributos del namespace android sin el prefijo

        ClassDescViewBase viewClassDesc = ClassDescViewMgr.get(node);
        viewClassDesc.setAttribute(node,namespaceURI,name,value,null,page.getInflatedLayoutImpl());
    }

    public void setAttributeNS2(Object[] idObj,String namespaceURI,String name,String value)
    {
        View elem = getNode(idObj);
        setAttributeNS(elem, namespaceURI, name, value);
    }

    @Override
    public void removeAttribute(View node, String name)
    {
        removeAttributeNS(node,null,name);
    }

    @Override
    public void removeAttribute2(Object[] idObj, String name)
    {
        removeAttributeNS2(idObj,null,name);
    }

    @Override
    public void removeAttributeNS(View node, String namespaceURI, String name)
    {
        ClassDescViewBase viewClassDesc = ClassDescViewMgr.get(node);
        viewClassDesc.removeAttribute(node, namespaceURI, name, page.getInflatedLayoutImpl());
    }

    @Override
    public void removeAttributeNS2(Object[] idObj, String namespaceURI, String name)
    {
        View node = getNode(idObj);
        removeAttributeNS(node, namespaceURI, name);
    }

    public View getNode(Object[] idObj)
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


    private View getNode(String id,String path,String cachedParentId,Object[] newCachedParentIds)
    {
        View cachedParent = null;
        if (cachedParentId != null)
        {
            cachedParent = getNodeCached(cachedParentId);
            if (cachedParent == null) throw new ItsNatDroidException("Unexpected error");
        }

        View node = getNode2(cachedParent,new Object[]{id,path});
        if (newCachedParentIds != null)
        {
            View parentNode = getParentNode(node);
            int len = newCachedParentIds.length;
            for(int i = 0; i < len; i++)
            {
                addNodeCache2((String)newCachedParentIds[i],parentNode);
                parentNode = getParentNode(parentNode);
            }
        }
        return node;
    }

    private View getNode2(View parentNode,String id)
    {
        return getNode2(parentNode,new Object[]{id});
    }

    private View getNode2(View parentNode,Object[] idObj) // No es público
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
            View node = pathResolver.getNodeFromPath(path,parentNode);
            if (id != null) addNodeCache2(id,node);
            return node;
        }
    }

    private View getNodeCached(String id) // No es público
    {
        if (id == null) return null;
        return nodeCacheById.get(id);
    }

    private void addNodeCache2(String id,View node)
    {
        if (id == null) return; // si id es null cache desactivado
        nodeCacheById.put(id,node);
    }

    public View getParentNode(View parentNode)
    {
        return (View)parentNode.getParent();
    }

    public View createElement(String name)
    {
        return createElementNS(null,name);
    }

    public View createElementNS(String namespaceURI,String name)
    {
        // El namespaceURI es irrelevante
        ClassDescViewBase classDesc = XMLLayoutInflater.getClassDescViewBase(name);
        return classDesc.createAndAddViewObject(null, 0, page.getInflatedLayoutImpl().getContext());
    }

    private int getChildIndex(ViewGroup parentView,View view)
    {
        // Esto es una chapuza pero no hay opción
        int size = parentView.getChildCount();
        for(int i = 0; i < size; i++)
        {
            if (parentView.getChildAt(i) == view)
                return i;
        }
        return -1;
    }

    public void insertBefore(View parentNode,View newChild,View childRef)
    {
        if (childRef == null) { appendChild(parentNode, newChild); return; }
        else
        {
            ViewGroup parentView = (ViewGroup)parentNode;
            parentView.addView(newChild, getChildIndex(parentView,childRef));
        }
    }

    public void insertBefore2(View parentNode,View newChild,View childRef,String newId)
    {
        insertBefore(parentNode, newChild, childRef);
        if (newId != null) addNodeCache2(newId,newChild);
    }

    public void insertBefore3(Object[] parentIdObj,View newChild,Object[] childRefIdObj,String newId)
    {
        View parentNode = getNode(parentIdObj);
        View childRef = getNode2(parentNode,childRefIdObj);
        this.insertBefore2(parentNode,newChild,childRef,newId);
    }

    public void appendChild(View parentNode,View newChild)
    {
        ((ViewGroup)parentNode).addView(newChild);
    }

    public void appendChild2(View parentNode,View newChild,String newId)
    {
        this.appendChild(parentNode,newChild);
        if (newId != null) addNodeCache2(newId,newChild);
    }

    public void appendChild3(Object[] idObj,View newChild,String newId)
    {
        View parentNode = getNode(idObj);
        appendChild2(parentNode, newChild, newId);
    }

}
