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

package org.itsnat.feashow.features.comp.layers;


import javax.swing.ButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBox;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class ModalLayerXULDocument implements EventListener,ChangeListener
{
    protected ItsNatDocument itsNatDoc;
    protected Element button;
    protected ItsNatFreeCheckBox cleanModeCheck1;
    protected ItsNatFreeCheckBox cleanModeCheck2;

    public ModalLayerXULDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        Document doc = itsNatDoc.getDocument();

        this.button = doc.getElementById("startId");
        ((EventTarget)button).addEventListener("command",this,false);

        this.cleanModeCheck1 = (ItsNatFreeCheckBox)compMgr.createItsNatFreeCheckBox(doc.getElementById("cleanModeId1"), null);
        cleanModeCheck1.setSelected(false);
        cleanModeCheck1.getToggleButtonModel().addChangeListener(this);

        this.cleanModeCheck2 = (ItsNatFreeCheckBox)compMgr.createItsNatFreeCheckBox(doc.getElementById("cleanModeId2"), null);
        cleanModeCheck2.setSelected(false);
        cleanModeCheck2.getToggleButtonModel().addChangeListener(this);
    }

    public ItsNatDocument getItsNatDocument()
    {
        return itsNatDoc;
    }

    public boolean isCleanMode1()
    {
        return cleanModeCheck1.isSelected();
    }

    public boolean isCleanMode2()
    {
        return cleanModeCheck2.isSelected();
    }

    public void handleEvent(final Event evt)
    {
        String code;

        code = "<vbox width='300' height='300'>" +
               "  <description>This is a Modal Layer (1)</description>" +
               "  <button>Click To Exit</button>" +
               "</vbox>";

        showModalView(code,isCleanMode1(),30,30);

        code = "<vbox width='200' height='200'>" +
               "  <description>This is a Modal Layer (2)</description>" +
               "  <button>Click To Exit</button>" +
               "</vbox>";

        showModalView(code,isCleanMode2(),50,50);
    }

    public void showModalView(String code,boolean cleanMode,final int x,final int y)
    {
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        final ItsNatModalLayer modalLayer = compMgr.createItsNatModalLayer(null,cleanMode,1,null,null);
        final Element panel = modalLayer.getElement();

        panel.setAttribute("left",Integer.toString(x));
        panel.setAttribute("top",Integer.toString(y));

        DocumentFragment frag = itsNatDoc.toDOM(code);
        final Element elem = (Element)frag.getFirstChild(); // vbox
        panel.appendChild(elem);

        NodeList buttons = elem.getElementsByTagName("button");
        final Element buttonExit = (Element)buttons.item(0);

        final EventListener listenerExit = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ((EventTarget)buttonExit).removeEventListener("command",this,false);
                modalLayer.dispose(); // Removes the <panel>
            }
        };
        ((EventTarget)buttonExit).addEventListener("command",listenerExit,false);

        EventListener listenerExit2 = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ((EventTarget)buttonExit).removeEventListener("command",listenerExit,false);
                // The component is automatically disposed if pophidden event is enabled
            }
        };
        modalLayer.addEventListener("popuphidden",listenerExit2,true); // before

        EventListener listenerExit3 = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                // The component is automatically disposed if pophidden event is enabled
                if (!modalLayer.isDisposed()) throw new RuntimeException("TEST ERROR");
            }
        };
        modalLayer.addEventListener("popuphidden",listenerExit3,false); // after
    }

    public void stateChanged(ChangeEvent e)
    {
        ButtonModel model = (ButtonModel)e.getSource();

        Element elem;
        if (model == cleanModeCheck1.getButtonModel())
            elem = cleanModeCheck1.getElement();
        else
            elem = cleanModeCheck2.getElement();
        if (model.isSelected())
            elem.setAttribute("style","color:red");
        else
            elem.removeAttribute("style");
    }
}
