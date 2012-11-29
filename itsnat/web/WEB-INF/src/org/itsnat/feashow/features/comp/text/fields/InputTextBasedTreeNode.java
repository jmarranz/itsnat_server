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
import org.itsnat.comp.text.ItsNatHTMLInputFile;
import org.itsnat.comp.text.ItsNatHTMLInputPassword;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.text.ItsNatTextComponent;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class InputTextBasedTreeNode extends FeatureTreeNode implements EventListener,DocumentListener
{
    protected ItsNatHTMLInputText inputTextComp;
    protected ItsNatHTMLInputPassword inputPassComp;
    protected ItsNatHTMLInputFile inputFileComp;

    public InputTextBasedTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.inputTextComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("inputTextId");
        inputTextComp.setText("Change this text and lost the focus");
        addListeners(inputTextComp);
        inputTextComp.focus();
        inputTextComp.select();

        this.inputPassComp = (ItsNatHTMLInputPassword)compMgr.createItsNatComponentById("inputPassId");
        inputPassComp.setText("Initial password");
        addListeners(inputPassComp);

        // The HTML input file element value attribute/property can not be changed with JavaScript:
        // is ignored in MSIE and throws an error in FireFox
        this.inputFileComp = (ItsNatHTMLInputFile)compMgr.createItsNatComponentById("inputFileId");
        // inputFileComp.setText("Change this text and lost the focus");
        addListeners(inputFileComp);

        if (isUCWEB() || isBolt())
        {
            Element ucwebElem = itsNatDoc.getDocument().getElementById("ucwebId");
            ucwebElem.removeAttribute("style");
        }
    }

    public void endExamplePanel()
    {
        this.inputTextComp.dispose();
        this.inputTextComp = null;

        this.inputPassComp.dispose();
        this.inputPassComp = null;

        this.inputFileComp.dispose();
        this.inputFileComp = null;
    }

    public void addListeners(ItsNatTextComponent inputComp)
    {
        inputComp.addEventListener("change",this);
        PlainDocument dataModel = (PlainDocument)inputComp.getDocument();
        dataModel.addDocumentListener(this);
    }

    public void handleEvent(Event evt)
    {
        log(evt,inputTextComp.getHTMLInputElement().getValue());
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
