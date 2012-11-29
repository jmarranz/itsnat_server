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
                ItsNatHttpSession session = (ItsNatHttpSession)((ItsNatEvent)evt).getClientDocument().getItsNatSession();
                boolean netFront = session.getUserAgent().indexOf("NetFront") != -1;
                testAddScripts(netFront);
            }
        };

        // Evitamos ejecutar el test como parte del evento load pues en el evento load el contexto es window (el currentTarget)
        // y queremos probar que el contexto sea automáticamente window
        ClientDocument clientDoc = ((ItsNatEvent)evt).getClientDocument();
        clientDoc.addContinueEventListener(null, listener);
    }

    public void testAddScripts(boolean netFront)
    {
        testAddScript(true,netFront);
        testAddScript(false,netFront);
    }

    public void testAddScript(boolean before,boolean netFront)
    {
        if (netFront && before) return; // El importante es before = false y ejecutar los dos test a la vez no se puede con NetFront (habría que cambiar el diseño del test)

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

        if (netFront) // NetFront ejecuta el <script> después de que termine este script
            itsNatDoc.addCodeToSend("var func = function() {");


        itsNatDoc.addCodeToSend("if (!window.itsNatTestScript) { alert('ERROR TestScriptInsertion'); throw 'ERROR'; } \n");
        itsNatDoc.addCodeToSend("window.itsNatTestScript = false; "); // Para detectar doble ejecución
        itsNatDoc.addCodeToSend("try{ delete window.itsNatTestScript; } catch(e){ }\n"); // Quitamos la propiedad, el try/catch es por si acaso el delete no vale.

        // En Renesis 1.1.1 da un falso error debido a que la llamada
        // window.itsNatTestScript = false; y el delete NO HACEN NADA y sigue estando a true
        // por lo que da un error de doble ejecución.

        if (netFront)
            itsNatDoc.addCodeToSend("}; window.setTimeout(func,0);");

        rootElem.removeChild(script);
    }

    public abstract Element getScriptParentElement();

    public abstract Element createScriptElement();
}
