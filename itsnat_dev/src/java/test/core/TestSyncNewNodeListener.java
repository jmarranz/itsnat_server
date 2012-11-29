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
import org.itsnat.core.event.NodeInnerTransport;
import org.itsnat.core.event.ParamTransport;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestSyncNewNodeListener implements EventListener,Serializable
{

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestSyncNewNodeListener(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element newNodeSyncOnClick = itsNatDoc.getDocument().getElementById("newNodeSyncOnClick");
        itsNatDoc.addEventListener((EventTarget)newNodeSyncOnClick,"click",this,false,new ParamTransport[]{new NodeInnerTransport()});
    }

    public void handleEvent(Event evt)
    {
        Element target = (Element)evt.getCurrentTarget();
        // Para que se vea en el navegador:
        NodeList children = target.getChildNodes();
        Node child = children.item(children.getLength() - 1);
        Text text = child.getOwnerDocument().createTextNode(" " + Integer.toString(children.getLength()) + " OK ");
        child.appendChild(text);
    }

}
