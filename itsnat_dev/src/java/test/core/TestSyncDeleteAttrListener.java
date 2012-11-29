/*
 * OnClickAddRowListenerExample.java
 *
 * Created on 2 de octubre de 2006, 21:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.NodeAttributeTransport;
import org.itsnat.core.event.ParamTransport;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestSyncDeleteAttrListener implements EventListener,Serializable
{

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestSyncDeleteAttrListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element deleteAttrSyncOnClick = itsNatDoc.getDocument().getElementById("deleteAttrSyncOnClick");
        itsNatDoc.addEventListener((EventTarget)deleteAttrSyncOnClick,"click",this,false,new ParamTransport[]{new NodeAttributeTransport("pos")});
    }

    public void handleEvent(Event evt)
    {
        Element target = (Element)evt.getCurrentTarget();
        // Para que se vea en el navegador:
        Attr pos = target.getAttributeNode("pos"); // Inicialmente pos existía en DOM de carga y se elimina en el cliente
        Text text;
        if (pos == null)
           text = target.getOwnerDocument().createTextNode(" OK");
        else
           text = target.getOwnerDocument().createTextNode(" BAD");
        target.appendChild(text);
    }

}
