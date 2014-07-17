/*
 * TestHTMLInputTextBased.java
 *
 * Created on 16 de noviembre de 2006, 12:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import org.itsnat.comp.text.ItsNatHTMLInputTextBased;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.event.ItsNatKeyEvent;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import test.web.shared.TestBaseHTMLDocument;
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public abstract class TestHTMLInputTextBased extends TestBaseHTMLDocument implements DocumentListener
{

    /**
     * Creates a new instance of TestHTMLInputTextBased
     */
    public TestHTMLInputTextBased(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
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

    public void handleEvent(Event evt,boolean updateAgainToTest)
    {
        // Evento "change"
        ItsNatDOMStdEvent itsNatEvent = (ItsNatDOMStdEvent)evt;
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvent.getItsNatDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLInputTextBased input = (ItsNatHTMLInputTextBased)componentMgr.findItsNatComponent((Node)evt.getCurrentTarget());

        if (updateAgainToTest)
            input.setText(input.getText() + "-ok-"); // Para testear que hemos recibido el valor del cliente y que podemos cambiarlo desde el servidor. Al llegar al navegador no provoca a su vez un nuevo evento change pues el evento está asociado a la pérdida de foco (lo cual no ocurre en la actualización via JavaScript)

        outText("OK " + evt.getType() + " "); // Para que se vea
        
        String type = evt.getType();
        if (type.equals("keyup") || type.equals("keydown"))
            outText( Integer.toString( ((ItsNatKeyEvent)evt).getKeyCode()) + " " );
        else if (type.equals("keypress"))
            outText( Integer.toString( ((ItsNatKeyEvent)evt).getCharCode()) + " " );
    }
}
