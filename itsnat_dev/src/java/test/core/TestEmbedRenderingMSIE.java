/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.core;

import java.io.Serializable;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public class TestEmbedRenderingMSIE implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;

    public TestEmbedRenderingMSIE(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();
        ((EventTarget)view).addEventListener("load",this,false);
    }

    public void handleEvent(Event evt)
    {
        // Nota sobre BlackBerry: BlackBerry no reconoce el elemento <embed>, es desconocido
        // por lo que como el parser (en text/html MIME) genera <embed> sin cerrar (aunque cierre <embed /> da igual
        // en HTML este cierre no se reconoce) BlackBerry espera un </embed>, como no existe
        // todo el markup que sigue queda dentro del <embed> y curiosamente NO se visualiza.
        // Este test funciona en BlackBerry puesto que no hay más markup seguido del <embed>
        // Podríamos generar <embed></embed> para BlackBerry pero lo mejor sencillamente ES NO USAR
        // <embed> con BlackBerry pues total no sirve para nada.


        StringBuilder code = new StringBuilder();
        code.append("var elem = document.getElementById('embedTest1Id');");
        code.append("if (elem.childNodes.length != 1) alert('ERROR TestEmbedRenderingMSIE');");

        code.append("var elem = document.getElementById('embedTest2Id');");
        code.append("if (elem.childNodes.length != 1) alert('ERROR TestEmbedRenderingMSIE');");
        
        itsNatDoc.addCodeToSend(code);
    }
}
