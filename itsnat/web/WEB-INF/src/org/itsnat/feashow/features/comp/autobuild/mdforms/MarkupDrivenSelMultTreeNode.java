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

package org.itsnat.feashow.features.comp.autobuild.mdforms;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;


public class MarkupDrivenSelMultTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element parentElem;

    protected HTMLSelectElement selectElem;
    protected Element removeElem;
    protected HTMLInputElement itemElem;
    protected HTMLInputElement posElem;
    protected Element updateElem;
    protected Element insertElem;

    public MarkupDrivenSelMultTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.parentElem = doc.getElementById("compGroupId");

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.setMarkupDrivenComponents(true);
        compMgr.buildItsNatComponents(parentElem);

        this.selectElem = (HTMLSelectElement)doc.getElementById("selectId");
        ((EventTarget)selectElem).addEventListener("change",this,false);

        this.removeElem = doc.getElementById("removeId");
        ((EventTarget)removeElem).addEventListener("click",this,false);

        this.itemElem = (HTMLInputElement)doc.getElementById("itemId");
        this.posElem = (HTMLInputElement)doc.getElementById("posId");

        updateItemForm();

        this.updateElem = doc.getElementById("updateId");
        ((EventTarget)updateElem).addEventListener("click",this,false);

        this.insertElem = doc.getElementById("insertId");
        ((EventTarget)insertElem).addEventListener("click",this,false);
    }

    public void endExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        ((EventTarget)selectElem).removeEventListener("change",this,false);
        this.selectElem = null;

        ((EventTarget)removeElem).removeEventListener("click",this,false);
        this.removeElem = null;

        this.itemElem = null;
        this.posElem = null;

        ((EventTarget)updateElem).removeEventListener("click",this,false);
        this.updateElem = null;

        ((EventTarget)insertElem).removeEventListener("click",this,false);
        this.insertElem = null;

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.removeItsNatComponents(parentElem,true);
        compMgr.setMarkupDrivenComponents(false);

        this.parentElem = null;
    }

    public HTMLOptionElement getOptionElement(int index)
    {
        if (index < 0) return null;
        return (HTMLOptionElement)selectElem.getOptions().item(index);
    }

    public String getOptionText(int index)
    {
        HTMLOptionElement option = getOptionElement(index);
        if (option == null) return null;
        return option.getText();
    }

    public int indexOf(String item)
    {
        HTMLCollection col = selectElem.getOptions();
        int len = col.getLength();
        for(int i = 0; i < len; i++)
        {
            HTMLOptionElement option = (HTMLOptionElement)col.item(i);
            if (option.getText().equals(item))
                return i;
        }
        return -1;
    }

    public void updateItemForm()
    {
        int selectedIndex = selectElem.getSelectedIndex();
        itemElem.setValue(getOptionText(selectedIndex));
        posElem.setValue(Integer.toString(selectedIndex));
    }

    public void handleEvent(Event evt)
    {
        log(evt);

        EventTarget currentTarget = evt.getCurrentTarget();
        if (currentTarget == selectElem)
        {
            updateItemForm();
            StringBuffer msg = new StringBuffer();
            msg.append("Changed selection: ");
            HTMLCollection col = selectElem.getOptions();
            int len = col.getLength();
            for(int i = 0; i < len; i++)
            {
                HTMLOptionElement option = (HTMLOptionElement)col.item(i);
                msg.append( option.getText() + " (" + option.getSelected() + ") " );
            }
            log(msg.toString());
        }
        else if (currentTarget == removeElem)
        {
            HTMLCollection col = selectElem.getOptions();
            int len = col.getLength();
            HTMLOptionElement[] toRemove = new HTMLOptionElement[len];
            for(int i = 0; i < len; i++)
            {
                HTMLOptionElement option = (HTMLOptionElement)col.item(i);
                if (option.getSelected())
                    toRemove[i] = option;
            }

            for(int i = 0; i < len; i++)
            {
                HTMLOptionElement option = toRemove[i];
                if (option != null)
                    selectElem.removeChild(toRemove[i]);
            }
        }
        else if ((currentTarget == updateElem) ||
                 (currentTarget == insertElem))
        {
            Document doc = getItsNatDocument().getDocument();
            String newItem = itemElem.getValue();
            try
            {
                int pos = Integer.parseInt(posElem.getValue());
                if (currentTarget == updateElem)
                {
                    HTMLOptionElement option = getOptionElement(pos);
                    if (option != null)
                        ItsNatDOMUtil.setTextContent(option,newItem);
                    else
                        getItsNatDocument().addCodeToSend("alert('Bad Position');");
                }
                else
                {
                    HTMLOptionElement before = (HTMLOptionElement)getOptionElement(pos);
                    HTMLOptionElement added = (HTMLOptionElement)doc.createElement("option");
                    added.appendChild(doc.createTextNode(newItem));
                    selectElem.add(added, before);
                    selectElem.setSelectedIndex(pos);
                }
            }
            catch(NumberFormatException ex)
            {
                getItsNatDocument().addCodeToSend("alert('Bad Position');");
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {
                getItsNatDocument().addCodeToSend("alert('Bad Position');");
            }
        }
    }

}
