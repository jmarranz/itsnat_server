/*
 * TestAsyncServerTask.java
 *
 * Created on 3 de enero de 2007, 12:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.xul;

import java.io.Serializable;
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
import test.shared.EventListenerSerial;


/**
 *
 * @author jmarranz
 */
public class TestXULModalLayer implements EventListener,ChangeListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element button;
    protected ItsNatFreeCheckBox cleanModeCheck1;
    protected ItsNatFreeCheckBox cleanModeCheck2;

    /**
     * Creates a new instance of TestAsyncServerTask
     */
    public TestXULModalLayer(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        load("testModalLayerId");
    }

    public void load(String linkId)
    {
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        Document doc = itsNatDoc.getDocument();

        this.button = doc.getElementById(linkId);
        ((EventTarget)button).addEventListener("command", this, false);

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
        //int height = 3500;
        String code;

        code = "<vbox width='500' height='500'>" +
               "  <description>This is a Modal Layer (1)</description>" +
               "  <button>Click To Exit</button>" +
               "  <button>Check Server</button>" +
               "</vbox>";

        showModalView(code,isCleanMode1(),50,50);

        code = "<vbox width='300' height='300'>" +
               "  <description>This is a Modal Layer (2)</description>" +
               "  <button>Click To Exit</button>" +
               "  <button>Check Server</button>" +
               "</vbox>";

        showModalView(code,isCleanMode2(),80,80);
    }

    public void showModalView(String code,boolean cleanMode,final int x,final int y)
    {
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        final ItsNatModalLayer modalLayer = compMgr.createItsNatModalLayer(null,cleanMode,0.7f,"green",null);

        EventListener unexpEvtListener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                StringBuilder code = new StringBuilder();
                code.append("if (confirm('Received an unexpected event by a hidden element. Reload?')) ");
                code.append("  window.location.reload(true);");

                itsNatDoc.addCodeToSend(code.toString());
            }
        };
        modalLayer.addUnexpectedEventListener(unexpEvtListener);


        final Element panel = modalLayer.getElement();

        panel.setAttribute("left",Integer.toString(x));
        panel.setAttribute("top",Integer.toString(y));

        DocumentFragment frag = itsNatDoc.toDOM(code);
        final Element elem = (Element)frag.getFirstChild(); // vbox
        panel.appendChild(elem);

        NodeList buttons = elem.getElementsByTagName("button");
        final Element buttonExit = (Element)buttons.item(0);
        final Element buttonCheckServ = (Element)buttons.item(1);

        // Link simplemente para actualizar el cliente con cambios en el servidor
        final EventListener listenerNothing = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
            }
        };
        ((EventTarget)buttonCheckServ).addEventListener("command",listenerNothing,false);

        final EventListener listenerExit = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                ((EventTarget)buttonCheckServ).removeEventListener("command",listenerNothing,false);
                ((EventTarget)buttonExit).removeEventListener("command",this,false);
                modalLayer.dispose(); // Removes the <panel>
            }
        };

        ((EventTarget)buttonExit).addEventListener("command",listenerExit,false);

        EventListener listenerExit2 = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                ((EventTarget)buttonCheckServ).removeEventListener("command",listenerNothing,false);
                ((EventTarget)buttonExit).removeEventListener("command",listenerExit,false);
            }
        };
        modalLayer.addEventListener("popuphidden",listenerExit2,true); // before

        EventListener listenerExit3 = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
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
