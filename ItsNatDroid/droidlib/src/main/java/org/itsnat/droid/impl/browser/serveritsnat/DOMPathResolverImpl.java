package org.itsnat.droid.impl.browser.serveritsnat;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;

/**
 * Created by jmarranz on 13/06/14.
 */
public class DOMPathResolverImpl implements DOMPathResolver
{
    protected ItsNatDocImpl itsNatDoc;

    public DOMPathResolverImpl(ItsNatDocImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    private View getRootView()
    {
        InflatedLayoutImpl layout = itsNatDoc.getPageImpl().getInflatedLayoutPageImpl();
        return layout.getRootView();
    }

    private View getParentNode(View node)
    {
        return (View)node.getParent();
    }

    public Node getNodeFromPath(String pathStr,Node topParent)
    {
        String[] path = getArrayPathFromString(pathStr);
        if (path == null) return null;
        return NodeImpl.create(getNodeFromArrayPath(path,NodeImpl.getView(topParent)));
    }

    private String[] getArrayPathFromString(String pathStr)
    {
        if (pathStr == null) return null;
        return pathStr.split(",");
    }

    private View getNodeFromArrayPath(String[] arrayPath,View topParent)
    {
        View viewRoot = getRootView();
        if (arrayPath.length == 1)
        {
            String firstPos = arrayPath[0];
            if (firstPos.equals("window")) throw new ItsNatDroidException("Unexpected window");
            else if (firstPos.equals("document")) throw new ItsNatDroidException("Unexpected document node");
            else if (firstPos.equals("doctype")) throw new ItsNatDroidException("Unexpected doctype node");
            else if (firstPos.indexOf("eid:") == 0)
            {
                InflatedLayoutImpl layout = itsNatDoc.getPageImpl().getInflatedLayoutPageImpl();
                String id = firstPos.substring("eid:".length());
                View viewRes = layout.findViewByXMLId(id);
                return viewRes;
            }
        }

        if (topParent == null) topParent = viewRoot;
        View viewRes = topParent;

        int len = arrayPath.length;
        for(int i = 0; i < len; i++)
        {
            String posStr = arrayPath[i];
            viewRes = getChildNodeFromStrPos(viewRes,posStr);
        }

        return viewRes;
    }

    private View getChildNodeFromStrPos(View parentNode,String posStr)
    {
        if (posStr.equals("de")) return getRootView();

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

    private View getChildNodeFromPos(View parentNode,int pos,boolean isTextNode)
    {
        if (!(parentNode instanceof ViewGroup)) return null;
        return ((ViewGroup)parentNode).getChildAt(pos);
    }

    private boolean isFiltered(View node)
    {
        return false;
    }

    public String getStringPathFromNode(Node node,Node topParent)
    {
        if (node == null) return null;
        String[] path = getNodePathArray(NodeImpl.getView(node),NodeImpl.getView(topParent));
        if (path == null) return null;
        return getStringPathFromArray(path);
    }

    private int getNodeDeep(View node,View topParent)
    {
        int i = 0;
        while(node != topParent)
        {
            i++;
            node = getParentNode(node);
            if (node == null) return -1; // el nodo no esta bajo topParent
        }
        return i;
    }

    private String[] getNodePathArray(View nodeLeaf,View topParent)
    {
        if (nodeLeaf == null) return null;
        if (topParent == null) topParent = getRootView();

        // No usamos el locById porque los atributos especiales itsnat deberían de desaparecer en el cliente, no es imprescindible y en stateless por aquí no pasamos

        View node = nodeLeaf;
        int len = getNodeDeep(node,topParent);
        if (len < 0) return null;
        String[] path = new String[len];
        for(int i = len - 1; i >= 0; i--)
        {
            String pos = getNodeChildPosition(node);
            path[i] = pos;
            node = getParentNode(node);
        }
        // path[len - 1] += this.getSuffix(nodeLeaf);
        return path;
    }

    private String getNodeChildPosition(View node)
    {
        if (node == getRootView()) return "de";
        ViewGroup parentNode = (ViewGroup)getParentNode(node);
        if (parentNode == null) throw new ItsNatDroidException("Unexpected error");

        int size = parentNode.getChildCount();
        int pos = 0;
        for(int i = 0; i < size; i++)
        {
            if (parentNode.getChildAt(i) == node) return "" + pos;

            pos++;
        }

        return "-1";
    }

    private String getStringPathFromArray(String[] path)
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
