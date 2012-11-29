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
public class TestSyncExistingAttrListener implements EventListener,Serializable
{

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestSyncExistingAttrListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element attrSyncOnClick = itsNatDoc.getDocument().getElementById("attrSyncOnClick");
        //AttributeSyncEventListener attrListener = itsNatDoc.createAttributeSyncEventListener(new String[]{"pos","style"},this);
        // Nos interesa sólo el atributo "pos" y style los cuales existen en el servidor
        // desde el principio.
        itsNatDoc.addEventListener((EventTarget)attrSyncOnClick,"click",this,false,new NodeAttributeTransport[] { new NodeAttributeTransport("pos"),new NodeAttributeTransport("style")});
    }

    public void handleEvent(Event evt)
    {
        Element target = (Element)evt.getCurrentTarget();
        // Para que se vea en el navegador:
        String pos = target.getAttribute("pos");
        Text posNode = target.getOwnerDocument().createTextNode(pos);
        target.appendChild(posNode);

        String styleValue = target.getAttribute("style"); // Atributo problemático
        TestUtil.checkError((styleValue.indexOf("red") != -1) ||
                (styleValue.indexOf("#ff0000") != -1) ||  // Opera lo convierte a formato RGB
                (styleValue.indexOf("#FF0000") != -1) // Idem BlackBerry 4.6 pero en mayúsculas.
                );
    }

}
