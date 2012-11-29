/*
 * TestJSFromServer.java
 *
 * Created on 17 de noviembre de 2006, 15:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp;

import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.impl.core.jsren.ScriptReference;
import javax.swing.ButtonModel;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.core.jsren.JSScriptUtilImpl;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public class TestJSFromServer extends TestButtonBase implements EventListener
{
    /**
     * Creates a new instance of TestJSFromServer
     */
    public TestJSFromServer(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        load();
    }

    public void load()
    {
        org.w3c.dom.Document doc = itsNatDoc.getDocument();
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("testJSFromServerId");
        ItsNatHTMLComponentManager componentMgr = itsNatDoc.getItsNatHTMLComponentManager();
        ItsNatHTMLInputButton input = (ItsNatHTMLInputButton)componentMgr.findItsNatComponent(inputElem);

        input.addEventListener("click",this);

        ButtonModel model = input.getButtonModel();
        model.addChangeListener(this);
    }

    public void handleEvent(Event evt)
    {
        // Evento "click"
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();

        String code;
        ScriptUtil codeGen = itsNatDoc.getScriptUtil();

        code = codeGen.getCallMethodCode(codeGen.createScriptExpr("window"),"alert",new Object[]{"First Alert"},true);
        itsNatDoc.addCodeToSend(code);

        Document doc = itsNatDoc.getDocument();
        AbstractView window = ((DocumentView)doc).getDefaultView();
        code = codeGen.getCallMethodCode(window,"alert",new Object[]{"Second Alert"},true);
        itsNatDoc.addCodeToSend(code);


        // ESTE es un caso de testeo de métodos todavía no públicos

        JSScriptUtilImpl codeGenImpl = (JSScriptUtilImpl)codeGen;
        ScriptReference winRef = codeGenImpl.createScriptReference(window);
        ScriptReference confirmRes = winRef.callMethod("confirm",new Object[]{"Confirm ?"},true);
        winRef.callMethod("alert",new Object[]{confirmRes},true);

        outText("OK " + evt.getType() + " "); // Para que se vea
    }

}
