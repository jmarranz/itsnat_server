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
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestCodeBeforeSendListener implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element linkToCodeBeforeSend;

    /**
     * Creates a new instance of OnClickAddRowListenerExample
     */
    public TestCodeBeforeSendListener(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
 //itsNatDoc.addCodeToSend(";try {");
        this.linkToCodeBeforeSend = itsNatDoc.getDocument().getElementById("linkToCodeBeforeSend");
        String code = "event.setExtraParam('in_client',' OK Fired a ' + event.getNativeEvent().type + ' event');\n";
//code = null;
        itsNatDoc.addEventListener((EventTarget)linkToCodeBeforeSend,"click",this,false,code);
 //itsNatDoc.addCodeToSend("} catch(ex1) { alert(ex1); }");
    }

    public void handleEvent(Event evt)
    {
        String res = (String)((ItsNatEvent)evt).getExtraParam("in_client");
        Text text = itsNatDoc.getDocument().createTextNode(res + " OK Click Received (Server)");
        linkToCodeBeforeSend.appendChild(text);
    }

}
