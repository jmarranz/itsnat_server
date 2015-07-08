/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.ItsNatKeyEvent;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLTextAreaElement;
import test.web.shared.BrowserUtil2;
import test.web.shared.TestBaseHTMLDocument;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestHTMLTextArea extends TestBaseHTMLDocument implements EventListener,DocumentListener
{

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLTextArea(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        super(itsNatDoc);

        initTextArea(request);
    }

    public void initTextArea(ItsNatServletRequest request)
    {
        org.w3c.dom.Document doc = itsNatDoc.getDocument();
        HTMLTextAreaElement elem = (HTMLTextAreaElement)doc.getElementById("textAreaId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLTextArea comp = (ItsNatHTMLTextArea)componentMgr.findItsNatComponent(elem);
        PlainDocument dataModel = new PlainDocument();
        comp.setDocument(dataModel);

        comp.setText("Initial Text");

        comp.addEventListener("change",this);
        if (!BrowserUtil2.isS60WebKit(request)) // Symbian detecta como teclas los movimientos del cursor, al final no se envía el change
            comp.addEventListener("keydown",this);
        comp.addEventListener("keyup",this);
        comp.addEventListener("keypress",this);
        
        dataModel.addDocumentListener(this);
    }

    public void handleEvent(Event evt)
    {
        // Evento "change" o "keyup" o "keydown"
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLTextArea comp = (ItsNatHTMLTextArea)componentMgr.findItsNatComponent((Node)evt.getCurrentTarget());

        if (evt.getType().equals("change"))
            comp.setText(comp.getText() + "-ok-" ); // Para testear que hemos recibido el valor del cliente y que podemos cambiarlo desde el servidor. Al llegar al navegador no provoca a su vez un nuevo evento change pues el evento está asociado a la pérdida de foco (lo cual no ocurre en la actualización via JavaScript)

        outText("OK " + evt.getType() + " "); // Para que se vea

        String type = evt.getType();
        if (type.equals("keyup") || type.equals("keydown"))
            outText( Integer.toString( ((ItsNatKeyEvent)evt).getKeyCode()) + " " );
        else if (type.equals("keypress"))
            outText( Integer.toString( ((ItsNatKeyEvent)evt).getCharCode()) + " " );
    }

    public void insertUpdate(DocumentEvent e)
    {
        javax.swing.text.Document docModel = e.getDocument();
        int offset = e.getOffset();
        int len = e.getLength();
        //TestUtil.checkError((offset == 0) && (len == docModel.getLength()));

        try
        {
            outText("OK inserted " + docModel.getText(offset,len) + " "); // Para que se vea
        }
        catch(BadLocationException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void removeUpdate(DocumentEvent e)
    {
        javax.swing.text.Document docModel = e.getDocument();
        int offset = e.getOffset();
        int len = e.getLength();
        //TestUtil.checkError((offset == 0) && (docModel.getLength() == 0));

        try
        {
            outText("OK removed " + offset + " " + len + " chars " + docModel.getText(0,docModel.getLength()) + " "); // Para que se vea
        }
        catch(BadLocationException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void changedUpdate(DocumentEvent e)
    {
        TestUtil.checkError(false); // No hay atributos, no debe llamarse
    }

}
