package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;

/**
 * Created by jmarranz on 13/06/14.
 */
public class DOMPathResolver
{
    protected ItsNatDocImpl itsNatDoc;

    public DOMPathResolver(ItsNatDocImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public Node getNodeFromPath(String pathStr,Node topParent)
    {
        String[] path = getArrayPathFromString(pathStr);
        if (path == null) return null;
        return getNodeFromArrayPath(path,topParent);
    }

    public String[] getArrayPathFromString(String pathStr)
    {
        if (pathStr == null) return null;
        return pathStr.split(",");
    }

    public Node getNodeFromArrayPath(String[] arrayPath,Node topParent)
    {
        InflatedLayoutImpl layout = itsNatDoc.getPage().getInflatedLayoutImpl();
        View viewRoot = layout.getRootView();
        if (arrayPath.length == 1)
        {
            String firstPos = arrayPath[0];
            if (firstPos.equals("window")) throw new ItsNatDroidException("Unexpected");
            else if (firstPos.equals("document")) throw new ItsNatDroidException("Unexpected");
            else if (firstPos.equals("doctype")) throw new ItsNatDroidException("Unexpected");
            else if (firstPos.indexOf("eid:") == 0)
            {
                String id = firstPos.substring("eid:".length());
                View viewRes = layout.getElementById(id);
                return NodeImpl.create(viewRes);
            }
        }

        if (topParent == null) topParent = NodeImpl.create(viewRoot);
        Node node = topParent;

        int len = arrayPath.length;
        for(int i = 0; i < len; i++)
        {
            String posStr = arrayPath[i];
            node = getChildNodeFromStrPos(node,posStr);
        }

        return node;
    }

    public Node getChildNodeFromStrPos(Node parentNode,String posStr)
    {
        if (posStr.equals("de")) return NodeImpl.create(getViewRoot());

        int posBracket = posStr.indexOf('[');
        if (posBracket == -1)
        {
            int pos = Integer.parseInt(posStr);
            return getChildNodeFromPos(parentNode,pos,false);
        }
        else
        {
            return null; // Ni atributo ni nodo de texto son válidos en este contexto
        }
    }

    public Node getChildNodeFromPos(Node parentNode,int pos,boolean isTextNode)
    {
        View parentView = parentNode.getView();
        if (!(parentView instanceof ViewGroup)) return null;
        return NodeImpl.create(((ViewGroup)parentView).getChildAt(pos));
    }

    private boolean isFiltered(View node)
    {
        return false;
    }

    protected View getViewRoot()
    {
        InflatedLayoutImpl layout = itsNatDoc.getPage().getInflatedLayoutImpl();
        return layout.getRootView();
    }

}
