/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatNode;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestMutationEventsInternal implements EventListener,Serializable
{
    protected int ok = 0;

    public TestMutationEventsInternal(ItsNatDocument itsNatDoc)
    {
        // El método renameNode de DOM 3 es interesante porque

        DocumentImpl doc = (DocumentImpl)itsNatDoc.getDocument(); // Necesitamos testear las tripas de ItsNat


        Element root = doc.getDocumentElement();
        root.setAttribute("prueba","hola");
        Attr attr = root.getAttributeNode("prueba");

        boolean old = ((ItsNatNode)doc).isInternalMode();
        ((ItsNatNode)doc).setInternalMode(false);
        try
        {
            doc.addEventListener("DOMAttrNameChanged", this, false);
        }
        finally
        {
            ((ItsNatNode)doc).setInternalMode(old);
        }
        // Este método es interesante porque genera varios mutation events
        doc.renameNode(attr, null,"prueba2");

        old = ((ItsNatNode)doc).isInternalMode();
        ((ItsNatNode)doc).setInternalMode(false);
        try
        {
            doc.removeEventListener("DOMAttrNameChanged", this, false);
        }
        finally
        {
            ((ItsNatNode)doc).setInternalMode(old);
        }

        TestUtil.checkError( ok != 1 );

        // Comprobamos que tras los mutation events internos el createEvent queda en modo remoto (el esperado)
        Event evt = ((DocumentEvent)doc).createEvent("MouseEvents");
        TestUtil.checkError( evt instanceof ItsNatEvent );
    }

    public void handleEvent(Event evt)
    {
        ok++;
    }
}
