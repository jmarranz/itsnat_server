/*
 * TestAsyncServerTask.java
 *
 * Created on 3 de enero de 2007, 12:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.svg;

import java.io.Serializable;
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
import test.shared.EventListenerSerial;


/**
 *
 * @author jmarranz
 */
public class TestSVGModalLayer implements EventListener,ChangeListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element button;
    protected ItsNatFreeCheckBox cleanModeCheck1;
    protected ItsNatFreeCheckBox cleanModeCheck2;

    /**
     * Creates a new instance of TestAsyncServerTask
     */
    public TestSVGModalLayer(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        load("testModalLayerId");
    }

    public void load(String linkId)
    {
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        Document doc = itsNatDoc.getDocument();

        this.button = ItsNatDOMUtil.getElementById(linkId,doc);
        ((EventTarget)button).addEventListener("click", this, false);

        this.cleanModeCheck1 = (ItsNatFreeCheckBox)compMgr.createItsNatFreeCheckBox(doc.getElementById("cleanModeId1"), null);
        cleanModeCheck1.setSelected(false);
        cleanModeCheck1.getToggleButtonModel().addChangeListener(this);

        this.cleanModeCheck2 = (ItsNatFreeCheckBox)compMgr.createItsNatFreeCheckBox(doc.getElementById("cleanModeId2"), null);
        cleanModeCheck2.setSelected(false);
        cleanModeCheck2.getToggleButtonModel().addChangeListener(this);
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
        int height = 3500;
        String code;

        code = "<g>" +
               "  <rect x='5%' y='20px' width='90%' height='" + height + "px' fill='aqua' />" +
               "  <text x='7%' y='50px'>This is a Modal Layer (1)</text>" +
               "  <a xlink:href='javascript:;' xmlns:xlink='http://www.w3.org/1999/xlink'>" +
               "    <text x='7%' y='80px'>Click To Exit</text>" +
               "  </a>" +
               "  <a xlink:href='javascript:;' xmlns:xlink='http://www.w3.org/1999/xlink'>" +
               "    <text x='7%' y='110px'>Check Server</text>" +
               "  </a>" +
               "</g>";

        showModalView(code,isCleanMode1(),(float)0.5,"black");

        code = "<g>" +
               "  <rect x='15%' y='150px' width='70%' height='" + height + "px' fill='yellow' />" +
               "  <text x='17%' y='180px'>This is a Modal Layer (2)</text>" +
               "  <a xlink:href='javascript:;' xmlns:xlink='http://www.w3.org/1999/xlink'>" +
               "    <text x='17%' y='210px'>Click To Exit</text>" +
               "  </a>" +
               "  <a xlink:href='javascript:;' xmlns:xlink='http://www.w3.org/1999/xlink'>" +
               "    <text x='17%' y='240px'>Check Server</text>" +
               "  </a>" +
               "</g>";

        showModalView(code,isCleanMode2(),(float)0.1,"black");
    }

    public void showModalView(String code,boolean cleanMode,float opacity,String background)
    {
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        final ItsNatModalLayer modalLayer = compMgr.createItsNatModalLayer(null,cleanMode,opacity,background,null);

        EventListener unexpEvtListener = new EventListenerSerial()
        {
            // Por si acaso aunque en general el z-order funciona bien en los casos
            // de soporte SVG decente.
            public void handleEvent(Event evt)
            {
                StringBuffer code = new StringBuffer();
                code.append("if (true == confirm('Received an unexpected event by a hidden element. Reload?')) ");  // (true == ...) es para Batik porque confirm devuelve un objeto Boolean no un primitivo
                code.append("   window.location.href = window.location.href; "); // El window.location.reload(true) no funciona en ASV y el Batik esta sentencia funciona como un reload (simulado en el applet para ItsNat)
                itsNatDoc.addCodeToSend(code.toString());
            }
        };
        modalLayer.addUnexpectedEventListener(unexpEvtListener);

        final Document doc = itsNatDoc.getDocument();
        DocumentFragment frag = itsNatDoc.toDOM(code);
        final Element elem = (Element)frag.getFirstChild();
        doc.getDocumentElement().appendChild(elem);

        NodeList links = elem.getElementsByTagName("a");
        final Element linkExit = (Element)links.item(0);
        final Element linkCheckServ = (Element)links.item(1);

        // Link simplemente para actualizar el cliente con cambios en el servidor
        final EventListener listenerNothing = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
            }
        };
        ((EventTarget)linkCheckServ).addEventListener("click",listenerNothing,false);

        EventListener listenerExit = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                ((EventTarget)linkCheckServ).removeEventListener("click",listenerNothing,false);
                //((EventTarget)elem).removeEventListener("click",this,false);
                ((EventTarget)linkExit).removeEventListener("click",this,false);
                doc.getDocumentElement().removeChild(elem);
                modalLayer.dispose();
            }
        };

//        ((EventTarget)elem).addEventListener("click",listenerExit,false);
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
