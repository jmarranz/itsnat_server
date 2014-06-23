package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
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

    @Override
    public void setAttribute2(String idObj, String name, String value)
    {
        setAttributeNS2(idObj, null, name, value);
    }

    public void setAttribute2(Object[] idObj,String name,String value)
    {
        setAttributeNS2(idObj, null, name, value);
    }

    public void setAttributeNS(View node,String namespaceURI,String name,String value)
    {
        //name = removePrefix(name); // Por si acaso filtramos un posible prefijo, nos nos interesa un valor con prefijo que nos envíen ej "android:background" por una parte porque no tiene sentido (ni en DOM ni en Views) y porque gestionamos los nombres de atributos del namespace android sin el prefijo

        ClassDescViewBase viewClassDesc = ClassDescViewMgr.get(node);
        viewClassDesc.setAttribute(node,namespaceURI,name,value,null,page.getInflateRequestImpl(),page.getInflatedLayoutImpl());
    }

    public void setAttributeNS2(String idObj,String namespaceURI,String name,String value)
    {
        View elem = getNode(idObj);
        setAttributeNS(elem, namespaceURI, name, value);
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
    public void removeAttribute2(String idObj, String name)
    {
        removeAttributeNS2(idObj,null,name);
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
        viewClassDesc.removeAttribute(node, namespaceURI, name, page.getInflateRequestImpl(),page.getInflatedLayoutImpl());
    }

    @Override
    public void removeAttributeNS2(String idObj, String namespaceURI, String name)
    {
        View node = getNode(idObj);
        removeAttributeNS(node, namespaceURI, name);
    }

    @Override
    public void removeAttributeNS2(Object[] idObj, String namespaceURI, String name)
    {
        View node = getNode(idObj);
        removeAttributeNS(node, namespaceURI, name);
    }

    public View getNode(String id)
    {
//if (true) return getPage().getInflatedLayout().getRootView();

        if (id == null) return null;
        String cachedParentId = null;
        String path = null;
        String[] newCachedParentIds = null;
        return getNode(id,path,cachedParentId,newCachedParentIds);
    }

    public View getNode(Object[] idObj)
    {
// if (true) return getPage().getInflatedLayout().getRootView();

        if (idObj == null) return null;
        String id = null;
        String cachedParentId = null;
        String path = null;
        Object[] newCachedParentIds = null;
        int len = idObj.length;
        // No existe el caso de len = 1 (en ese caso es una string directamente)
        if (len == 2)
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

/*
    private String removePrefix(String name)
    {
        int pos = name.indexOf(':');
        if (pos == -1) return name;
        return name.substring(pos + 1);
    }
*/
}
