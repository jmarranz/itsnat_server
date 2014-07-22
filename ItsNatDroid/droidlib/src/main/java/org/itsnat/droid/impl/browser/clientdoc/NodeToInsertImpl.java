package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

import org.itsnat.droid.impl.util.ValueUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 25/06/14.
 */
public class NodeToInsertImpl extends NodeImpl
{
    protected String viewName;
    protected Map<String,AttrImpl> attribs;
    protected boolean inserted = false;

    public NodeToInsertImpl(String viewName)
    {
        this.viewName = viewName;
        // view es null inicialmente
    }

    public void setView(View view)
    {
        this.view = view;
    }

    public boolean hasAttributes()
    {
        return (attribs != null && !attribs.isEmpty());
    }

    public Map<String,AttrImpl> getAttributes()
    {
        if (attribs == null) this.attribs = new HashMap<String,AttrImpl>();
        return attribs;
    }

    private static String toId(String namespaceURI,String name)
    {
        return ValueUtil.isEmpty(namespaceURI) ? name : namespaceURI + ":" + name;
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

    public AttrImpl getAttribute(String namespaceURI,String name)
    {
        if (attribs == null) return null;
        String id = toId(namespaceURI,name);
        return getAttributes().get(id);
    }

    public void setAttribute(String namespaceURI,String name,String value)
    {
        String id = toId(namespaceURI,name);
        getAttributes().put(id,new AttrImpl(namespaceURI,name,value));
    }
}
