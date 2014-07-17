/*
 * TestAsyncServerResponse.java
 *
 * Created on 3 de enero de 2007, 12:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatFreeButtonNormal;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLDocument;
import test.web.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestComponentGCTestId extends TestBaseHTMLDocument implements EventListener
{
    protected ItsNatHTMLButton button;
    protected transient WeakHashMap<ItsNatFreeButtonNormal,Object> weakMap = new WeakHashMap<ItsNatFreeButtonNormal,Object>();

    /**
     * Creates a new instance of TestAsyncServerResponse
     */
    public TestComponentGCTestId(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        load();
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        Map<ItsNatFreeButtonNormal,Object> mapTmp = new HashMap<ItsNatFreeButtonNormal,Object>(weakMap);
        out.writeObject(mapTmp);

        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        @SuppressWarnings("unchecked")
        Map<ItsNatFreeButtonNormal,Object> mapTmp = (Map<ItsNatFreeButtonNormal,Object>)in.readObject();
        this.weakMap = new WeakHashMap<ItsNatFreeButtonNormal,Object>(mapTmp);

        in.defaultReadObject();
    }
    
    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        HTMLButtonElement buttonElm = (HTMLButtonElement)doc.getElementById("componentGCTestId");

        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.button = (ItsNatHTMLButton)componentMgr.findItsNatComponent(buttonElm);

        button.addEventListener("click",this);
    }

    public void handleEvent(Event evt)
    {
        if (!itsNatDoc.getItsNatDocumentTemplate().isAutoCleanEventListeners())
        {
            itsNatDoc.addCodeToSend("alert('AutoCleanEventListeners must be enabled');");
            return;
        }

        // Este test lo que busca es ver que aunque se pulse el botón
        // muchas veces el número de componentes no recogidos por el GC
        // y el número de listeners registrados en el cliente
        // no aumenta continuamente. Eso se traduce en que ni la memoria
        // Java ni la del navegador aumentan continuamente.

        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        ItsNatHTMLComponentManager componentMgr = itsNatDoc.getItsNatHTMLComponentManager();

        Element elem = doc.getElementById("componentGCTestId2");
        Element child = doc.createElement("b");
        elem.appendChild(child);

        for(int i = 0; i < 1000; i++)
        {
            // El componente por defecto se asocia sí mismo como listener al evento "click"
            ItsNatFreeButtonNormal comp = componentMgr.createItsNatFreeButtonNormal(child,null);
            weakMap.put(comp,null);
        }

        elem.removeChild(child);

        // Al eliminarse el nodo el mutation event elimina
        // los listeners asociados al mismo y a sus hijos,
        // al eliminar el registro de los listeners los componentes quedan libres
        // pues un registro de listener sujeta al componente si dicho
        // componente es el propio listener. Entonces actúa el GC.
        // La eliminación del registro del listener se propaga también al servidor

        System.gc();

        String code = "";
        code += "var i = 0; var list = itsNatDoc.domListeners; \n"; // domListeners no es pública
        code += "for(var id in list) i++; \n";
        code += "alert('Server:' + " + weakMap.size() + " + ' Client:' + i); \n";
        itsNatDoc.addCodeToSend(code);

        outText("OK " + evt.getType() + " "); // Para que se vea

    }
}
