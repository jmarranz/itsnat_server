/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.shared;

import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.http.ItsNatHttpSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author jmarranz
 */
public abstract class TestScriptInsertion implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;

    public TestScriptInsertion(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public abstract void addLoadEventListener();

    public void handleEvent(Event evt)
    {
        EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                testAddScripts();
            }
        };

        // Evitamos ejecutar el test como parte del evento load pues en el evento load el contexto es window (el currentTarget)
        // y queremos probar que el contexto sea automáticamente window
        ClientDocument clientDoc = ((ItsNatEvent)evt).getClientDocument();
        clientDoc.addContinueEventListener(null, listener);
    }

    public void testAddScripts()
    {
        testAddScript(true);
        testAddScript(false);
    }

    public void testAddScript(boolean before)
    {
        Document doc = itsNatDoc.getDocument();
        Element script = createScriptElement();
        // No usamos window.itsNatTestScript para comprobar que el contexto
        // de ejecución del <script> es window pues así vemos si
        // una función declarada en el <script> será visible a nivel de window (globalmente).
        StringBuffer code = new StringBuffer();
        code.append("if ((typeof window.itsNatTestScript != 'undefined') && window.itsNatTestScript) alert('ERROR: TestScriptInsertion, duplicated execution ' + window.itsNatTestScript);");
        code.append("itsNatTestScript = true; ");
        Node text;
        //if ("text/html".equals(itsNatDoc.getItsNatDocumentTemplate().getMIME()))
        if (doc instanceof HTMLDocument) // MIME HTML y XHTML
            text = doc.createTextNode(code.toString());
        else // SVG, XUL
            text = doc.createCDATASection(code.toString());

        Element rootElem = getScriptParentElement();
        if (before)
        {
            script.appendChild(text);
            rootElem.appendChild(script);
        }
        else
        {
            rootElem.appendChild(script);
            script.appendChild(text);
        }

        itsNatDoc.addCodeToSend("if (!window.itsNatTestScript) { alert('ERROR TestScriptInsertion'); throw 'ERROR'; } \n");
        itsNatDoc.addCodeToSend("window.itsNatTestScript = false; "); // Para detectar doble ejecución
        itsNatDoc.addCodeToSend("try{ delete window.itsNatTestScript; } catch(e){ }\n"); // Quitamos la propiedad, el try/catch es por si acaso el delete no vale.

        rootElem.removeChild(script);
    }

    public abstract Element getScriptParentElement();

    public abstract Element createScriptElement();
}
