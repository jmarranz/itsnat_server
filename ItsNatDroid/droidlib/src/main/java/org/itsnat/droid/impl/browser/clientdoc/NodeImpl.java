package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

/**
 * Created by jmarranz on 25/06/14.
 */
public class NodeImpl implements Node
{
    protected View view;

    public NodeImpl()
    {
    }

    public NodeImpl(View view)
    {
        this.view = view;
    }

    public static NodeImpl create(View view)
    {
        if (view == null) return null;
        return new NodeImpl(view);
    }

    public static View getView(Node node)
    {
        if (node == null) return null;
        return ((NodeImpl)node).getView(); // En el caso de NodeToInsertImpl podría ser null, pero será raro
    }

    public View getView()
    {
        return view;
    }


}
