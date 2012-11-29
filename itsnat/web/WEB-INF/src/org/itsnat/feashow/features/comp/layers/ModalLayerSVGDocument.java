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
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class ModalLayerSVGDocument implements EventListener,ChangeListener
{
    protected ItsNatDocument itsNatDoc;
    protected Element link;
    protected ItsNatFreeCheckBox cleanModeCheck1;
    protected ItsNatFreeCheckBox cleanModeCheck2;

    public ModalLayerSVGDocument(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        Document doc = itsNatDoc.getDocument();

        this.link = doc.getElementById("startId");
        ((EventTarget)link).addEventListener("click",this,false);

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

        code = "<g>" +
               "  <rect x='10%' y='10%' width='80%' height='80%' fill='aqua' />" +
               "  <text x='20%' y='20%'>Modal Layer 1</text>" +
               "  <a xlink:href='javascript:;' xmlns:xlink='http://www.w3.org/1999/xlink'>" +
               "    <text x='20%' y='30%'>Click To Exit</text>" +
               "  </a>" +
               "</g>";

        showModalView(code,isCleanMode1(),(float)0.5,"black");

        code = "<g>" +
               "  <rect x='20%' y='20%' width='60%' height='60%' fill='yellow' />" +
               "  <text x='30%' y='30%'>Modal Layer 2</text>" +
               "  <a xlink:href='javascript:;' xmlns:xlink='http://www.w3.org/1999/xlink'>" +
               "    <text x='30%' y='40%'>Click To Exit</text>" +
               "  </a>" +
               "</g>";

        showModalView(code,isCleanMode2(),(float)0.1,"black");
    }

    public void showModalView(String code,boolean cleanMode,float opacity,String background)
    {
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        final ItsNatModalLayer modalLayer = compMgr.createItsNatModalLayer(null,cleanMode,opacity,background,null);

        final Document doc = itsNatDoc.getDocument();
        DocumentFragment frag = itsNatDoc.toDOM(code);
        final Element elem = (Element)frag.getFirstChild();
        doc.getDocumentElement().appendChild(elem);

        NodeList links = elem.getElementsByTagName("a");
        final Element linkExit = (Element)links.item(0);

        EventListener listenerExit = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ((EventTarget)linkExit).removeEventListener("click",this,false);
                doc.getDocumentElement().removeChild(elem);
                modalLayer.dispose();
            }
        };

        ((EventTarget)linkExit).addEventListener("click",listenerExit,false);
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
            elem.setAttribute("fill","red");
        else
            elem.setAttribute("fill","blue");
    }
}
