/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.shared;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 * El motivo de este test es comprobar que el evento load ha sido
 * recibido en un ejemplo de attached server, pues en XHTML y SVG en WebKit
 * el evento load se emite antes de realizarse el attachment
 * por lo que tenemos que forzarlo.
 *
 * @author jmarranz
 */
public class TestAttachedServerLoadEventReceived implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;

    public TestAttachedServerLoadEventReceived(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        if (itsNatDoc.getItsNatDocumentTemplate().getSource() != null)
            return; // No es attached server

        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();
        ((EventTarget)view).addEventListener("load",this,false);
    }

    public void handleEvent(Event evt)
    {
        itsNatDoc.addCodeToSend("alert('LOAD event received');");
    }
}
