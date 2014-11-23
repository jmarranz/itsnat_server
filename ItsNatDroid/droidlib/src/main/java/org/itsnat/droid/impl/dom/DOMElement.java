package org.itsnat.droid.impl.dom;

import org.itsnat.droid.impl.util.ValueUtil;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jmarranz on 31/10/14.
 */
public abstract class DOMElement
{
    protected String name;
    protected DOMElement parentElement; // Si es null es el root
    protected ArrayList<DOMAttr> attribs;
    protected LinkedList<DOMElement> childList;

    public DOMElement(String name,DOMElement parentElement)
    {
        this.name = name;
        this.parentElement = parentElement;
    }

    public String getName()
    {
        return name;
    }

    public DOMElement getParentDOMElement()
    {
        return parentElement;
    }

    public ArrayList<DOMAttr> getDOMAttributeList()
    {
        return attribs;
    }

    public void initDOMAttribList(int count)
    {
        // Aunque luego sea alguno menos (el style de los View no se guarda aquí por ej.) no importa, así evitamos que reconstruya el array interno
        this.attribs = new ArrayList<DOMAttr>(count);
    }

    public void addDOMAttribute(DOMAttr attr)
    {
        attribs.add(attr);
    }

    public DOMAttr findDOMAttribute(String namespaceURI, String name)
    {
        for(int i = 0; i < attribs.size(); i++)
        {
            DOMAttr attr = attribs.get(i);
            String currNamespaceURI = attr.getNamespaceURI();
            if (!ValueUtil.equalsNullAllowed(currNamespaceURI, namespaceURI)) continue;
            String currName = attr.getName(); // El nombre devuelto no contiene el namespace
            if (!name.equals(currName)) continue;
            return attr;
        }
        return null;
    }

    public LinkedList<DOMElement> getChildDOMElementList()
    {
        return childList;
    }

    public void addChildDOMElement(DOMElement domElement)
    {
        if (childList == null) this.childList = new LinkedList<DOMElement>();
        childList.add(domElement);
    }

}
