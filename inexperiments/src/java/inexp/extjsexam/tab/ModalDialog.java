package inexp.extjsexam.tab;

import org.itsnat.comp.layer.ItsNatModalLayer;
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
                code.append("if (confirm('Received an unexpected event by a hidden element. Reload?')) ");
                code.append("   window.location.href = window.location.href;");                
                getItsNatHTMLDocument().addCodeToSend(code.toString());
            }
        };
        modalLayer.addUnexpectedEventListener(unexpEvtListener);
    }
}
