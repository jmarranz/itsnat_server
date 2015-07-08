/*
 * TestBlurFocusSelectClick.java
 *
 * Created on 16 de noviembre de 2006, 13:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLTextAreaElement;
import test.web.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestBlurFocusSelect  extends TestBaseHTMLDocument implements EventListener
{

    /** Creates a new instance of TestBlurFocusSelectClick */
    public TestBlurFocusSelect(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        load();
    }

    public void load()
    {
        org.w3c.dom.Document doc = itsNatDoc.getDocument();
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("testBlurFocusSelectId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputButton input = (ItsNatHTMLInputButton)componentMgr.findItsNatComponent(inputElem);

        input.addEventListener("click",this);
        input.addEventListener("focus",this);
        input.addEventListener("blur",this);
    }

    public void handleEvent(Event evt)
    {
        // Evento "click"
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputButton input = (ItsNatHTMLInputButton)componentMgr.findItsNatComponent((Node)evt.getCurrentTarget());
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();

        outText("OK " + evt.getType() + " "); // Para que se vea

        String type = evt.getType();
        if (type.equals("click"))
        {
            input.focus();
        }
        else if (type.equals("focus"))
        {
            input.blur();
        }
        else if (type.equals("blur"))
        {
            HTMLTextAreaElement elemTextArea = (HTMLTextAreaElement)doc.getElementById("textAreaId");
            ItsNatHTMLTextArea textArea = (ItsNatHTMLTextArea)componentMgr.findItsNatComponent(elemTextArea);
            textArea.focus(); // No hace falta para seleccionar es para probar los dos métodos seguidos
            textArea.select();
        }
    }

}
