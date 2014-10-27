package org.itsnat.droid.impl.parser;

import org.itsnat.droid.impl.util.ValueUtil;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jmarranz on 27/10/14.
 */
public class ViewParsed
{
    protected String name;
    protected ViewParsed viewParent; // Si es null es el root
    protected String styleAttr;
    protected ArrayList<Attribute> attribs;
    protected LinkedList<ViewParsed> childViews;

    public ViewParsed(String name,ViewParsed viewParent)
    {
        this.name = name;
        this.viewParent = viewParent;
    }

    public String getName()
    {
        return name;
    }

    public ViewParsed getViewParent()
    {
        return viewParent;
    }

    public String getStyleAttr()
    {
        return styleAttr;
    }

    public void setStyleAttr(String styleAttr)
    {
        this.styleAttr = styleAttr;
    }

    public ArrayList<Attribute> getAttributeList()
    {
        return attribs;
    }

    public void initAttribList(int count)
    {
        // Aunque luego sea alguno menos (el style no se guarda aquí) no importa, así evitamos que reconstruya el array interno
        this.attribs = new ArrayList<Attribute>(count);
    }

    public void addAttribute(String namespaceURI,String name,String value)
    {
        attribs.add(new Attribute(namespaceURI,name,value));
    }

    public String findAttribute(String namespaceURI, String name)
    {
        for(int i = 0; i < attribs.size(); i++)
        {
            Attribute attr = attribs.get(i);
            String currNamespaceURI = attr.getNamespaceURI();
            if (!ValueUtil.equalsNullAllowed(currNamespaceURI, namespaceURI)) continue;
            String currName = attr.getName(); // El nombre devuelto no contiene el namespace
            if (!name.equals(currName)) continue;
            String value = attr.getValue();
            return value;
        }
        return null;
    }

    public LinkedList<ViewParsed> getChildViewList()
    {
        return childViews;
    }

    public void addChildView(ViewParsed viewParsed)
    {
        if (childViews == null) this.childViews = new LinkedList<ViewParsed>();
        childViews.add(viewParsed);
    }
}
