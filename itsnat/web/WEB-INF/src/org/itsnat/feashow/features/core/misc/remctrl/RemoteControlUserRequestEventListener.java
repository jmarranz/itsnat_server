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

package org.itsnat.feashow.features.core.misc.remctrl;

import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class RemoteControlUserRequestEventListener implements EventListener
{
    protected boolean readOnly;
    protected final boolean[] ready;
    protected final boolean[] answer;

    public RemoteControlUserRequestEventListener(boolean readOnly,boolean[] ready,boolean[] answer)
    {
        this.readOnly = readOnly;
        this.ready = ready;
        this.answer = answer;
    }

    public void handleEvent(Event evt)
    {
        String type = evt.getType();
        if (type.equals("unload")||type.equals("beforeunload"))
            return;

        // The user (or the rem ctrl timer) is active then we ask.

        final ItsNatDocument itsNatDoc = ((ItsNatEvent)evt).getItsNatDocument();

        EventListener listener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                String answerStr = (String)((ItsNatEvent)evt).getExtraParam("answer");
                answer[0] = Boolean.valueOf(answerStr).booleanValue();
                ready[0] = true;
                synchronized(ready)
                {
                    ready.notifyAll();
                }
                itsNatDoc.removeUserEventListener(null,"rem_ctrl_request",this);
            }
        };
        itsNatDoc.addUserEventListener(null,"rem_ctrl_request",listener);

        String readWriteStr = readOnly ? "Read Only" : "FULL CONTROL!!";

        StringBuffer code = new StringBuffer();
        code.append("var itsNatDoc = document.getItsNatDoc();");
        code.append("var evt = itsNatDoc.createUserEvent('rem_ctrl_request');");
        code.append("var res = confirm('A Remote User Wants To Monitor (" + readWriteStr + ") Your Page. Accept?');");
        code.append("evt.setExtraParam('answer',res);");
        code.append("itsNatDoc.dispatchUserEvent(null,evt);");
        ClientDocument clientOwner = itsNatDoc.getClientDocumentOwner();
        clientOwner.addCodeToSend(code.toString());

        itsNatDoc.removeEventListener(this); // No longer needed
    }
}
