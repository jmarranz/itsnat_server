package org.itsnat.droid.impl.parser;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jmarranz on 27/10/14.
 */
public class ViewParsed
{
    protected String viewName;
    protected ViewParsed viewParent; // Si es null es el root
    protected String styleAttr;
    protected ArrayList<Attribute> attribs;
    protected LinkedList<ViewParsed> childViews;

    public ViewParsed(String viewName,ViewParsed viewParent)
    {
        this.viewName = viewName;
        this.viewParent = viewParent;
    }

    public String getViewName()
    {
        return viewName;
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

    public void initAttribList(int count)
    {
        // Aunque luego sea alguno menos (el style no se guarda aquí) no importa, así evitamos que reconstruya el array interno
        this.attribs = new ArrayList<Attribute>(count);
    }

    public void addAttribute(String namespaceURI,String name,String value)
    {
        attribs.add(new Attribute(namespaceURI,name,value));
    }

    public void addChildView(ViewParsed viewParsed)
    {
        if (childViews == null) this.childViews = new LinkedList<ViewParsed>();
        childViews.add(viewParsed);
    }
}
