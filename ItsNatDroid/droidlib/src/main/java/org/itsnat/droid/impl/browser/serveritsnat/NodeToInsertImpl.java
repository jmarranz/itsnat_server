package org.itsnat.droid.impl.browser.serveritsnat;

import android.view.View;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.util.ValueUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 25/06/14.
 */
public class NodeToInsertImpl extends NodeImpl
{
    protected String viewName;
    protected Map<String,AttrParsed> attribs;
    protected boolean inserted = false;

    public NodeToInsertImpl(String viewName)
    {
        this.viewName = viewName;
        this.view = null; // para que quede claro que view es null inicialmente
    }

    public void setView(View view)
    {
        this.view = view;
    }

    public boolean hasAttributes()
    {
        return (attribs != null && !attribs.isEmpty());
    }

    public Map<String,AttrParsed> getAttributes()
    {
        if (attribs == null) this.attribs = new HashMap<String,AttrParsed>();
        return attribs;
    }

    private static String toKey(String namespaceURI, String name)
    {
        return ValueUtil.isEmpty(namespaceURI) ? name : (namespaceURI + ":" + name);
    }

    public String getName()
    {
        return viewName;
    }

    public boolean isInserted()
    {
        return inserted;
    }

    public void setInserted()
    {
        this.inserted = true;
        this.attribs = null;
    }

    public AttrParsed getAttribute(String namespaceURI,String name)
    {
        if (attribs == null) return null;
        String key = toKey(namespaceURI, name);
        return getAttributes().get(key);
    }

    /*
    public void setAttribute(String namespaceURI,String name,String value)
    {
        AttrParsed attr = AttrParsed.createAttrParsed(namespaceURI,name,value);
        setAttribute(attr);
    }
*/

    public void setAttribute(AttrParsed attr)
    {
        String namespaceURI = attr.getNamespaceURI();
        String name = attr.getName(); // El nombre devuelto no contiene el namespace
        String key = toKey(namespaceURI, name);
        getAttributes().put(key,attr);
    }

    public void removeAttribute(String namespaceURI,String name)
    {
        String key = toKey(namespaceURI, name);
        getAttributes().remove(key);
    }
}
