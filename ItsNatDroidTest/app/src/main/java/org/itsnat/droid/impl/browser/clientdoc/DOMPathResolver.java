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

    public View getRootView()
    {
        InflatedLayoutImpl layout = itsNatDoc.getPageImpl().getInflatedLayoutImpl();
        return layout.getRootView();
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
        View viewRoot = getRootView();
        if (arrayPath.length == 1)
        {
            String firstPos = arrayPath[0];
            if (firstPos.equals("window")) throw new ItsNatDroidException("Unexpected");
            else if (firstPos.equals("document")) throw new ItsNatDroidException("Unexpected");
            else if (firstPos.equals("doctype")) throw new ItsNatDroidException("Unexpected");
            else if (firstPos.indexOf("eid:") == 0)
            {
                InflatedLayoutImpl layout = itsNatDoc.getPageImpl().getInflatedLayoutImpl();
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
        if (posStr.equals("de")) return NodeImpl.create(getRootView());

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

    public String getStringPathFromNode(Node node,Node topParent)
    {
        if (node == null) return null;
        String[] path = getNodePathArray(node,topParent);
        if (path == null) return null;
        return getStringPathFromArray(path);
    }

    public int getNodeDeep(Node node,Node topParent)
    {
        int i = 0;
        while(node.getView() != topParent.getView())
        {
            i++;
            node = itsNatDoc.getParentNode(node);
            if (node == null) return -1; // el nodo no esta bajo topParent
        }
        return i;
    }

    public String[] getNodePathArray(Node nodeLeaf,Node topParent)
    {
        if (nodeLeaf == null) return null;
        if (topParent == null) topParent = NodeImpl.create(getRootView());

        // No usamos el locById porque los atributos especiales itsnat deberían de desaparecer en el cliente, no es imprescindible y en stateless por aquí no pasamos

        Node node = nodeLeaf;
        int len = getNodeDeep(node,topParent);
        if (len < 0) return null;
        String[] path = new String[len];
        for(int i = len - 1; i >= 0; i--)
        {
            String pos = getNodeChildPosition(node);
            path[i] = pos;
            node = itsNatDoc.getParentNode(node);
        }
        // path[len - 1] += this.getSuffix(nodeLeaf);
        return path;
    }

    public String getNodeChildPosition(Node node)
    {
        if (node.getView() == getRootView()) return "de";
        Node parentNode = itsNatDoc.getParentNode(node);
        if (parentNode == null) throw new ItsNatDroidException("Unexpected error");

        ViewGroup parentView = (ViewGroup)parentNode.getView();
        View view = node.getView();
        int size = parentView.getChildCount();
        int pos = 0;
        for(int i = 0; i < size; i++)
        {
            if (parentView.getChildAt(i) == view) return "" + pos;

            pos++;
        }

        return "-1";
    }

    public String getStringPathFromArray(String[] path)
    {
        String code = "";
        int len = path.length;
        for(int i = 0; i < len; i++)
        {
            if (i != 0) code += ",";
            code += path[i];
        }
        return code;
    }
}
