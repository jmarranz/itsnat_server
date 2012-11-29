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
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodeAllAttribTransport;
import org.itsnat.core.event.ParamTransport;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.shared.EventListenerSerial;
import test.shared.BrowserUtil;

/**
 *
 * @author jmarranz
 */
public class TestSyncDeleteAttrListenerMethod2 implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestSyncDeleteAttrListenerMethod2(ItsNatDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;
        load(request);
    }

    public void load(ItsNatServletRequest request)
    {
        Element deleteAttrSyncOnClick = itsNatDoc.getDocument().getElementById("deleteAttrSyncOnClick2");

        if (BrowserUtil.isPocketIE(request))
        {
            EventListener listener = new EventListenerSerial()
            {
                public void handleEvent(Event evt)
                {
                    itsNatDoc.addCodeToSend("alert('Do not work in PocketIE');");
                }
            };
            ((EventTarget)deleteAttrSyncOnClick).addEventListener("click",listener,false);
        }
        else
        {
            // Nos traemos todos los atributos
            itsNatDoc.addEventListener((EventTarget)deleteAttrSyncOnClick,"click",this,false,new ParamTransport[]{new NodeAllAttribTransport()});        
        }
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
