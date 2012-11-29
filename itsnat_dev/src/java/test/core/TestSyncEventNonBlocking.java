/*
 * TestAsyncServerResponse.java
 *
 * Created on 3 de enero de 2007, 12:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import java.io.Serializable;
import org.itsnat.core.CommMode;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestSyncEventNonBlocking implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element button;

    /**
     * Creates a new instance of TestAsyncServerResponse
     */
    public TestSyncEventNonBlocking(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        this.button = doc.getElementById("syncNoBlockTestId");
        itsNatDoc.addEventListener((EventTarget)button,"click",this,false,CommMode.XHR_ASYNC_HOLD,null,null,-1);
    }

    public void handleEvent(final Event evt)
    {
        // En el cliente se deberían bloquear los eventos sin bloquear el navegador

        try
        {
            Thread.sleep(3000);
        }
        catch(InterruptedException ex) { }

        Text text = (Text)button.getFirstChild();
        text.setData(text.getData() + " => OK");
    }
}
