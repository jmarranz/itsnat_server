/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package manual.stateless;

import org.itsnat.core.ClientDocument;
import org.itsnat.core.event.ItsNatEventStateless;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;


public class StlessGlobalEventListener implements EventListener
{
    public StlessGlobalEventListener()
    {
    }

    public void handleEvent(Event evt)
    {
        ItsNatEventStateless itsNatEvt = (ItsNatEventStateless)evt;
        
        if (itsNatEvt.getItsNatDocument() == null)
        {
            ClientDocument clientDoc = itsNatEvt.getClientDocument();                
            //ServletRequest request = itsNatEvt.getItsNatServletRequest().getServletRequest();
            String docName = (String)itsNatEvt.getExtraParam("itsnat_doc_name");
            if (docName != null)
                clientDoc.addCodeToSend("alert('Received stateless event with not found itsnat_doc_name: " + docName + " from the page with title: " + itsNatEvt.getExtraParam("title") + "');"); 
            else
                clientDoc.addCodeToSend("alert('Received a custom stateless event from the page with title: " + itsNatEvt.getExtraParam("title") + "');"); 
        }
    }
}
