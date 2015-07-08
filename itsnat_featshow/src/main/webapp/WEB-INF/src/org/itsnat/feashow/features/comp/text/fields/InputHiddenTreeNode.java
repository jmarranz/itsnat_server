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

package org.itsnat.feashow.features.comp.text.fields;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchor;
import org.itsnat.comp.text.ItsNatHTMLInputHidden;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class InputHiddenTreeNode extends FeatureTreeNode implements EventListener,DocumentListener
{
    protected ItsNatHTMLInputHidden inputComp;
    protected ItsNatHTMLAnchor linkComp;

    public InputHiddenTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.inputComp = (ItsNatHTMLInputHidden)compMgr.createItsNatComponentById("inputId");
        inputComp.setText("Initial Hidden Value");

        PlainDocument dataModel = (PlainDocument)inputComp.getDocument();
        dataModel.addDocumentListener(this);

        this.linkComp = (ItsNatHTMLAnchor)compMgr.createItsNatComponentById("linkId");
        linkComp.addEventListener("click",this);
    }

    public void endExamplePanel()
    {
        this.inputComp.dispose();
        this.inputComp = null;

        this.linkComp.dispose();
        this.linkComp = null;
    }

    public void handleEvent(Event evt)
    {
        log(evt);

        ItsNatDocument itsNatDoc = getItsNatDocument();
        String code = "var input = document.getElementById('inputId');" +
                      "var oldValue = input.value;";
        itsNatDoc.addCodeToSend(code);

        inputComp.setText("A New Hidden Value");

        code = "var newValue = input.value;" +
               "alert('Old hidden value: ' + oldValue + '\\n' + 'New hidden value: ' + newValue);";
        itsNatDoc.addCodeToSend(code);
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
