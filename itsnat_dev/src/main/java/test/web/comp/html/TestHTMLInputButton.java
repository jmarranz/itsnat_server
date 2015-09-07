/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import javax.swing.DefaultButtonModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;
import test.shared.TestUtil;
import test.web.comp.TestButtonBase;
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestHTMLInputButton extends TestButtonBase implements EventListener
{
    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLInputButton(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initButton();
    }


    public TestHTMLInputButton() // Necesario para probar con RelProxy  
    {
        super(null);
    }     
    
    
    public void initButton()
    {
        Document doc = itsNatDoc.getDocument();
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("inputButtonId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputButton input = (ItsNatHTMLInputButton)componentMgr.findItsNatComponent(inputElem);
        DefaultButtonModel dataModel = new DefaultButtonModel();
        input.setButtonModel(dataModel);

        input.setLabelValue("Text Button");
        input.setEnabled(false); // Para testear la propagación al DOM (getDisabled)
        TestUtil.checkError(inputElem.getDisabled());
        input.setEnabled(true);

        input.addEventListener("click",this);
        input.addEventListener("mousedown",this);
        input.addEventListener("mouseup",this);
        input.addEventListener("mouseover",this);
        input.addEventListener("mouseout",this);

        dataModel.addChangeListener(this);
        dataModel.addActionListener(this);
        
        // Test RelProxy
        if (false)
        {
        input.addEventListener("click",new EventListenerSerial() {
            @Override
            public void handleEvent(Event evt) {
                itsNatDoc.addCodeToSend("alert('OK 3');");            
            }
        });        
        }
    }

    @Override
    public void handleEvent(Event evt)
    {
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputButton input = (ItsNatHTMLInputButton)componentMgr.findItsNatComponent((Node)evt.getCurrentTarget());
        outText("OK " + evt.getType() + " "); // Para que se vea

        input.setLabelValue("Button " + evt.getType()); //  + " HOLA"
    }

}
