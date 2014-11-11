package org.itsnat.droid.impl.model;

import org.itsnat.droid.impl.util.ValueUtil;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jmarranz on 31/10/14.
 */
public abstract class ElementParsed
{
    protected String name;
    protected ElementParsed parentElement; // Si es null es el root
    protected ArrayList<AttrParsed> attribs;
    protected LinkedList<ElementParsed> childList;

    public ElementParsed(String name,ElementParsed parentElement)
    {
        this.name = name;
        this.parentElement = parentElement;
    }

    public String getName()
    {
        return name;
    }

    public ElementParsed getParentElement()
    {
        return parentElement;
    }

    public ArrayList<AttrParsed> getAttributeList()
    {
        return attribs;
    }

    public void initAttribList(int count)
    {
        // Aunque luego sea alguno menos (el style de los View no se guarda aquí por ej.) no importa, así evitamos que reconstruya el array interno
        this.attribs = new ArrayList<AttrParsed>(count);
    }

    public void addAttribute(AttrParsed attr)
    {
        attribs.add(attr);
    }

    public AttrParsed findAttribute(String namespaceURI, String name)
    {
        for(int i = 0; i < attribs.size(); i++)
        {
            AttrParsed attr = attribs.get(i);
            String currNamespaceURI = attr.getNamespaceURI();
            if (!ValueUtil.equalsNullAllowed(currNamespaceURI, namespaceURI)) continue;
            String currName = attr.getName(); // El nombre devuelto no contiene el namespace
            if (!name.equals(currName)) continue;
            return attr;
        }
        return null;
    }

    public LinkedList<ElementParsed> getChildList()
    {
        return childList;
    }

    public void addChild(ElementParsed viewParsed)
    {
        if (childList == null) this.childList = new LinkedList<ElementParsed>();
        childList.add(viewParsed);
    }

}
