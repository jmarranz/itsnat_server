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

package org.itsnat.feashow.features.comp.text;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;


public class TextAreaTreeNode extends FeatureTreeNode implements EventListener,DocumentListener
{
    protected ItsNatHTMLTextArea comp;

    public TextAreaTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.comp = (ItsNatHTMLTextArea)compMgr.createItsNatComponentById("compId");
        comp.setText("Change this text\n and lost the focus");

        comp.addEventListener("change",this);

        PlainDocument dataModel = (PlainDocument)comp.getDocument();
        dataModel.addDocumentListener(this);

        comp.focus();
        comp.select();
    }

    public void endExamplePanel()
    {
        this.comp.dispose();
        this.comp = null;
    }

    public void handleEvent(Event evt)
    {
        log(evt);
    }

    public void insertUpdate(DocumentEvent e)
    {
        javax.swing.text.Document docModel = e.getDocument();
        int offset = e.getOffset();
        int len = e.getLength();

        try
        {
            log(e.getClass() + " inserted, pos " + offset + "," + len + " chars,\"" + docModel.getText(offset,len) + "\"");
        }
        catch(BadLocationException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void removeUpdate(DocumentEvent e)
    {
        // javax.swing.text.Document docModel = e.getDocument();
        int offset = e.getOffset();
        int len = e.getLength();

        log(e.getClass() + " removed, pos " + offset + "," + len + " chars");
    }

    public void changedUpdate(DocumentEvent e)
    {
        // A PlainDocument has no attributes
    }
}
