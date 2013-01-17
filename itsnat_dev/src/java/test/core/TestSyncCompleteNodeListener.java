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
import org.itsnat.core.event.NodeCompleteTransport;
import org.itsnat.core.event.ParamTransport;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.shared.EventListenerSerial;
import test.shared.BrowserUtil;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestSyncCompleteNodeListener implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestSyncCompleteNodeListener(ItsNatDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;
        load(request);
    }

    public void load(ItsNatServletRequest request)
    {
        Element nodeCompleteSyncOnClick = itsNatDoc.getDocument().getElementById("nodeCompleteSyncOnClick");

        itsNatDoc.addEventListener((EventTarget)nodeCompleteSyncOnClick,"click",this,false,new ParamTransport[]{new NodeCompleteTransport()});        
    }

    public void handleEvent(Event evt)
    {
        Element target = (Element)evt.getCurrentTarget();

        // Para que se vea en el navegador:
        NodeList children = target.getChildNodes();
        Node child = children.item(children.getLength() - 1);

        String pos = target.getAttribute("pos"); // Inicialmente pos no existía en DOM de carga se crea (y cambia) en el cliente
        Text text = child.getOwnerDocument().createTextNode(" " + Integer.toString(children.getLength() - 1) + " " + pos);
        child.appendChild(text);

        String styleValue = target.getAttribute("style"); // Atributo problemático
        TestUtil.checkError((styleValue.indexOf("red") != -1) ||
                (styleValue.indexOf("#ff0000") != -1) ||  // Opera lo convierte a formato RGB
                (styleValue.indexOf("#FF0000") != -1) // Idem BlackBerry 4.6 pero en mayúsculas.
                );
    }

}
