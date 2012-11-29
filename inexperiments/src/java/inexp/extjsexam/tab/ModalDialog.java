/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.extjsexam.tab;

import inexp.BrowserUtil;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class ModalDialog
{
    protected ItsNatModalLayer modalLayer;
    
    public ModalDialog()
    {
    }

    public ItsNatHTMLDocument getItsNatHTMLDocument()
    {
        return (ItsNatHTMLDocument)modalLayer.getItsNatDocument();
    }

    public void unexpectedEventDetection()
    {
        EventListener unexpEvtListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                StringBuilder code = new StringBuilder();
                ItsNatServletRequest request = ((ItsNatEvent)evt).getItsNatServletRequest();
                if (BrowserUtil.isUCWEB(request))
                    code.append("alert('Received an unexpected event by a hidden element. Reload recommended');");
                else
                {
                    code.append("if (confirm('Received an unexpected event by a hidden element. Reload?')) ");
                    code.append("   window.location.href = window.location.href;");
                }
                getItsNatHTMLDocument().addCodeToSend(code.toString());
            }
        };
        modalLayer.addUnexpectedEventListener(unexpEvtListener);
    }
}
