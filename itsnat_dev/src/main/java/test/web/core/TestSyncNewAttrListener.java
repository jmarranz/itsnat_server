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
import org.itsnat.core.event.NodeAllAttribTransport;
import org.itsnat.core.event.ParamTransport;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestSyncNewAttrListener implements EventListener,Serializable
{

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestSyncNewAttrListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element newAttrSyncOnClick = itsNatDoc.getDocument().getElementById("newAttrSyncOnClick");
        //AttributeSyncEventListener attrListener = itsNatDoc.createAttributeSyncEventListener(this);
        // Nos interesa el atributo "pos" y style pero nos traernos todos los del elemento
        itsNatDoc.addEventListener((EventTarget)newAttrSyncOnClick,"click",this,false,new ParamTransport[]{new NodeAllAttribTransport()});
    }

    public void handleEvent(Event evt)
    {
        Element target = (Element)evt.getCurrentTarget();
        // Para que se vea en el navegador:
        String pos = target.getAttribute("pos"); // Inicialmente pos no existía en DOM de carga se crea (y cambia) en el cliente
        Text posNode = target.getOwnerDocument().createTextNode(pos);
        target.appendChild(posNode);

        String styleValue = target.getAttribute("style"); // Atributo problemático
//System.out.println(styleValue);
        TestUtil.checkError((styleValue.indexOf("red") != -1) ||
                (styleValue.indexOf("#ff0000") != -1) ||  // Opera lo convierte a formato RGB
                (styleValue.indexOf("#FF0000") != -1) // Idem BlackBerry 4.6 pero en mayúsculas.
                );
    }

}
