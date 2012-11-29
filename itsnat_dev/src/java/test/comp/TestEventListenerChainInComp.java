/*
 * TestInsertFragment.java
 *
 * Created on 9 de enero de 2007, 9:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.core.event.ItsNatEventListenerChain;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLButtonElement;
import test.shared.EventListenerSerial;
import test.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestEventListenerChainInComp extends TestBaseHTMLDocument
{
    protected ItsNatHTMLButton button;

    /** Creates a new instance of TestInsertFragment */
    public TestEventListenerChainInComp(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        HTMLButtonElement elem = (HTMLButtonElement)doc.getElementById("testEventListenerChainId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.button = (ItsNatHTMLButton)componentMgr.findItsNatComponent(elem);

        EventListener list1 = new EventListenerSerial()
        {
            public void handleEvent(final Event evt)
            {
                ItsNatEventListenerChain chain = ((ItsNatEvent)evt).getItsNatEventListenerChain();
                try
                {
                    chain.continueChain();
                }
                catch(Exception ex)
                {
                    if (!ex.getMessage().equals("Must be catched"))
                        throw new RuntimeException("FAILED TEST");

                    TestEventListenerChainInComp.this.outText(" OK Chain 1-2");
                    chain.continueChain();
                }
            }
        };
        button.addEventListener("click",list1);

        EventListener list2 = new EventListenerSerial()
        {
            public void handleEvent(final Event evt)
            {
                throw new RuntimeException("Must be catched");
            }
        };
        button.addEventListener("click",list2);

        EventListener list3 = new EventListenerSerial()
        {
            public void handleEvent(final Event evt)
            {
                TestEventListenerChainInComp.this.outText(" OK Chain 2-2");
            }
        };
        button.addEventListener("click",list3);
    }


}
