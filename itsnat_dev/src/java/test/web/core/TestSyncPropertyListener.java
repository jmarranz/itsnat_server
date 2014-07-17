/*
 * OnClickAddRowListenerExample.java
 *
 * Created on 2 de octubre de 2006, 21:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.NodePropertyTransport;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class TestSyncPropertyListener implements EventListener,Serializable
{

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestSyncPropertyListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element addRowValueElem = itsNatDoc.getDocument().getElementById("add-row-value");
        itsNatDoc.addEventListener((EventTarget)addRowValueElem,"change",this,false,new NodePropertyTransport[]{ new NodePropertyTransport("value",String.class) });
    }

    public void handleEvent(Event evt)
    {
        Element target = (Element)evt.getCurrentTarget();
        HTMLInputElement inputElem = (HTMLInputElement)target;
        // Para comprobar que se ha hecho bien, si el evento "change" se lanza
        // es porque hemos cambiado el valor por defecto "" a otra cosa
        String value = inputElem.getValue();
        if ((value == null)||value.equals("")) throw new RuntimeException("TEST ERROR");
    }

}
