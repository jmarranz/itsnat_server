/*
 * CoreExampleProcessor.java
 *
 * Created on 2 de mayo de 2007, 16:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comptut;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.ItsNatHTMLForm;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLDocument;

public class CompExampleDocument implements EventListener,DocumentListener
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected ItsNatHTMLInputText inputComp;

    /** Creates a new instance of CoreExampleProcessor */
    public CompExampleDocument(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

        this.inputComp = (ItsNatHTMLInputText)componentMgr.addItsNatComponentById("inputId");
        inputComp.setText("Change this text and lost the focus");

        inputComp.addEventListener("change",this);

        PlainDocument dataModel = (PlainDocument)inputComp.getDocument();
        dataModel.addDocumentListener(this);

        ItsNatHTMLForm formComp = inputComp.getItsNatHTMLForm();
        formComp.reset();

        inputComp.focus();
        inputComp.select();
    }

    public void handleEvent(Event evt)
    {
        log("Text has been changed");
    }

    public void insertUpdate(DocumentEvent e)
    {
        javax.swing.text.Document docModel = e.getDocument();
        int offset = e.getOffset();
        int len = e.getLength();

        try
        {
            log("Text inserted: " + offset + "-" + len + " chars,\"" + docModel.getText(offset,len) + "\"");
        }
        catch(BadLocationException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void removeUpdate(DocumentEvent e)
    {
        int offset = e.getOffset();
        int len = e.getLength();

        log("Text removed: " + offset + "-" + len + " chars");
    }

    public void changedUpdate(DocumentEvent e)
    {
        // A PlainDocument has no attributes
    }

    public void log(String msg)
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Element noteElem = doc.createElement("p");
        noteElem.appendChild(doc.createTextNode(msg));
        doc.getBody().appendChild(noteElem);
    }
}
