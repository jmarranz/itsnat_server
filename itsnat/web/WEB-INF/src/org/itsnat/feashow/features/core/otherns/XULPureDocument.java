/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.core.otherns;


import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.NodePropertyTransport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class XULPureDocument implements EventListener,Serializable
{
    public static final String XULNAMESPACE = "http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul";
    protected ItsNatDocument itsNatDoc;
    protected Element listElem;
    protected Element valueElem;
    protected Element posElem;
    protected Element buttonRemove;
    protected Element buttonUpdate;
    protected Element buttonAdd;

    public XULPureDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        this.listElem = doc.getElementById("listId");
        itsNatDoc.addEventListener((EventTarget)listElem,"select",this,false,new NodePropertyTransport("selectedIndex",false));

        this.buttonRemove = doc.getElementById("removeItemId");
        ((EventTarget)buttonRemove).addEventListener("command",this,false);

        this.valueElem = doc.getElementById("valueItemId");
        itsNatDoc.addEventListener((EventTarget)valueElem,"change",this,false,new NodePropertyTransport("value",false));

        this.posElem = doc.getElementById("posItemId");
        itsNatDoc.addEventListener((EventTarget)posElem,"change",this,false,new NodePropertyTransport("value",false));

        this.buttonUpdate = doc.getElementById("updateItemId");
        ((EventTarget)buttonUpdate).addEventListener("command",this,false);

        this.buttonAdd = doc.getElementById("addItemId");
        ((EventTarget)buttonAdd).addEventListener("command",this,false);

        addNewItem("Madrid");
        addNewItem("Sevilla");
        addNewItem("Segovia");
        addNewItem("Barcelona");
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == listElem)
        {
            try
            {
                String selIndexStr = (String)((ItsNatEvent)evt).getExtraParam("selectedIndex");
                int index = Integer.parseInt(selIndexStr);
                Element itemElem = syncSelectionInServer(index);
                if (itemElem != null)
                {
                    String value = itemElem.getAttribute("label");
                    valueElem.setAttribute("value",value);
                    posElem.setAttribute("value",Integer.toString(index));
                }
            }
            catch(NumberFormatException ex)
            {
                itsNatDoc.addCodeToSend("alert('Bad Position');");
            }
        }
        else if (currTarget == buttonRemove)
        {
            removeSelectedItem();
        }
        else if (currTarget == valueElem)
        {
            String value = (String)((ItsNatEvent)evt).getExtraParam("value");
            valueElem.setAttribute("value",value);
        }
        else if (currTarget == posElem)
        {
            String value = (String)((ItsNatEvent)evt).getExtraParam("value");
            posElem.setAttribute("value",value);
        }
        else if (currTarget == buttonUpdate)
        {
            try
            {
                String value = valueElem.getAttribute("value");
                int pos = Integer.parseInt(posElem.getAttribute("value"));
                Element itemElem = (Element)listElem.getChildNodes().item(pos);
                if (itemElem != null)
                    itemElem.setAttribute("label", value);
            }
            catch(NumberFormatException ex)
            {
                itsNatDoc.addCodeToSend("alert('Bad Position');");
            }
        }
        else if (currTarget == buttonAdd)
        {
            try
            {
                String value = valueElem.getAttribute("value");
                int pos = Integer.parseInt(posElem.getAttribute("value"));
                insertNewItem(pos,value);
            }
            catch(NumberFormatException ex)
            {
                itsNatDoc.addCodeToSend("alert('Bad Position');");
            }
        }
    }

    public void removeSelectedItem()
    {
        NodeList items = listElem.getChildNodes();
        for(int i = 0; i < items.getLength(); i++)
        {
            Element item = (Element)items.item(i);
            if (item.hasAttribute("selected"))
                listElem.removeChild(item);
        }
    }

    public void addNewItem(String label)
    {
        insertNewItem(listElem.getChildNodes().getLength(),label);
    }

    public void insertNewItem(int pos,String label)
    {
        Document doc = itsNatDoc.getDocument();
        Element newItem = doc.createElementNS(XULNAMESPACE,"listitem");
        newItem.setAttribute("label",label);
        Element nextItem = (Element)listElem.getChildNodes().item(pos); // May be null
        listElem.insertBefore(newItem, nextItem);
    }

    public Element syncSelectionInServer(int index)
    {
        // Doing this we save which item is selected (remote view/control would work too).
        // Of course we could save the current selected item with a variable too.
        Element selectedItem = null;
        NodeList items = listElem.getChildNodes();
        for(int i = 0; i < items.getLength(); i++)
        {
            Element item = (Element)items.item(i);
            if (i == index)
            {
                item.setAttribute("selected","true");
                selectedItem = item;
            }
            else item.removeAttribute("selected");
        }
        return selectedItem;
    }
}
