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
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatKeyEvent;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class InputTextKeyUpDownTreeNode extends FeatureTreeNode implements EventListener,DocumentListener
{
    protected ItsNatHTMLInputText inputComp;

    public InputTextKeyUpDownTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.inputComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("inputId");
        inputComp.setText("Write any text, use SHIFT/ALT/CTRL too");

        inputComp.addEventListener("change",this);
        inputComp.addEventListener("keyup",this);
        inputComp.addEventListener("keydown",this);

        PlainDocument dataModel = (PlainDocument)inputComp.getDocument();
        dataModel.addDocumentListener(this);

        inputComp.focus();
        inputComp.select();

        if (isUCWEB() || isBolt())
        {
            Element ucwebElem = itsNatDoc.getDocument().getElementById("ucwebId");
            ucwebElem.removeAttribute("style");
        }
    }

    public void endExamplePanel()
    {
        this.inputComp.dispose();
        this.inputComp = null;
    }

    public void handleEvent(Event evt)
    {
        String type = evt.getType();
        if (type.equals("change"))
            log(evt);
        else
        {
            ItsNatKeyEvent keyEvt = (ItsNatKeyEvent)evt;
            String msg = "";
            msg += evt.getClass() + " " + type + " ";
            msg += ", char code: " + keyEvt.getCharCode();
            msg += ", key code: " + keyEvt.getKeyCode() + " ";
            if (keyEvt.getShiftKey())
                msg += " SHIFT";
            if (keyEvt.getCtrlKey())
                msg += " CTRL";
            if (keyEvt.getAltKey())
                msg += " ALT";

            if (type.equals("keyup"))
                msg += " text: " + inputComp.getText();

            log(msg);
        }
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
