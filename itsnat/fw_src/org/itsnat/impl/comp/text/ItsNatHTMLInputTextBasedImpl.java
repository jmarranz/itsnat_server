/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.comp.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.itsnat.impl.comp.ItsNatHTMLInputImpl;
import org.itsnat.comp.text.ItsNatHTMLInputTextBased;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.comp.text.ItsNatTextComponentUI;
import org.itsnat.comp.text.ItsNatTextFieldUI;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByClientImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLInputTextBasedImpl extends ItsNatHTMLInputImpl implements ItsNatHTMLInputTextBased, ItsNatHTMLFormTextComponentInternal
{
    protected ItsNatHTMLFormTextCompMarkupDrivenUtil markupDrivenUtil;
    protected ItsNatHTMLFormTextCompSharedImpl changeBasedDelegate = new ItsNatHTMLFormTextCompSharedImpl(this);

    /**
     * Creates a new instance of ItsNatHTMLInputTextBasedImpl
     */
    public ItsNatHTMLInputTextBasedImpl(HTMLInputElement element, NameValue[] artifacts, ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(element, artifacts, componentMgr);

        this.markupDrivenUtil = ItsNatHTMLFormTextCompMarkupDrivenUtil.initMarkupDriven(this);
    }

    public void init()
    {
        changeBasedDelegate.init();

        super.init();
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();

        changeBasedDelegate.writeListeners(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        changeBasedDelegate.readListeners(in);
    }

    public ItsNatHTMLFormTextCompSharedImpl getItsNatHTMLFormTextCompShared()
    {
        return changeBasedDelegate;
    }

    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        changeBasedDelegate.enableEventListenersByDoc();
    }

    public void enableEventListenersByClient(ItsNatCompDOMListenersByClientImpl domListeners)
    {
        super.enableEventListenersByClient(domListeners);

        changeBasedDelegate.enableEventListenersByClient(domListeners);
    }

    public void processDOMEvent(Event evt)
    {
        changeBasedDelegate.processDOMEvent(evt);

        super.processDOMEvent(evt);
    }

    public void handleEventOnChange(Event evt)
    {
        changeBasedDelegate.handleEventOnChange(evt);
    }

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        return changeBasedDelegate.getInternalParamTransports(type,clientDoc);
    }

    public void postHandleEventOnChange(Event evt)
    {
        // Redefinir si se quiere hacer algo
    }

    public void postHandleEventOnKeyUp(Event evt)
    {
        // Redefinir si se quiere hacer algo
    }

    public ItsNatTextFieldUI getItsNatTextFieldUI()
    {
        return (ItsNatTextFieldUI) compUI;
    }

    public String getText()
    {
        return changeBasedDelegate.getText();
    }

    public void setText(String t)
    {
        changeBasedDelegate.setText(t);
    }

    public javax.swing.text.Document getDocument()
    {
        // Ojo Document es javax.swing.text.Document
        return (javax.swing.text.Document) dataModel;
    }

    public void setDocument(javax.swing.text.Document dataModel)
    {
        setDataModel(dataModel);
    }

    public ItsNatTextComponentUI getItsNatTextComponentUI()
    {
        return (ItsNatTextComponentUI)compUI;
    }

    public ItsNatHTMLFormTextCompUIImpl getItsNatHTMLFormTextCompUIImpl()
    {
        return (ItsNatHTMLFormTextCompUIImpl)compUI;
    }

    public ItsNatHTMLInputTextBasedUIImpl getItsNatHTMLInputTextBasedUIImpl()
    {
        return (ItsNatHTMLInputTextBasedUIImpl)compUI;
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return createDefaultItsNatHTMLInputTextBasedUI();
    }

    public ItsNatTextComponentUI createDefaultItsNatHTMLInputTextBasedUI()
    {
        return new ItsNatHTMLInputTextBasedUIImpl(this);
    }

    public void bindDataModel()
    {
        changeBasedDelegate.bindDataModel();
    }

    public void unbindDataModel()
    {
        changeBasedDelegate.unbindDataModel();
    }

    public void initialSyncUIWithDataModel()
    {
        changeBasedDelegate.initialSyncUIWithDataModel();

        if (markupDrivenUtil != null)
            markupDrivenUtil.initialSyncUIWithDataModel();
    }

    public void insertUpdate(DocumentEvent e)
    {
        changeBasedDelegate.insertUpdate(e);
    }

    public void removeUpdate(DocumentEvent e)
    {
        changeBasedDelegate.removeUpdate(e);
    }

    public void changedUpdate(DocumentEvent e)
    {
        changeBasedDelegate.changedUpdate(e);
    }

    public void setNewValueOnChange(String newValue, Event evt)
    {
        // Al final se hará un setValue al HTMLInputElement pero a través del modelo de datos
        changeBasedDelegate.incrementalChange(newValue);
    }

    public void setNewValueOnKeyUp(String newValue, Event evt)
    {
        changeBasedDelegate.incrementalChange(newValue);
    }

    public Object createDefaultModelInternal()
    {
        return createDefaultDocument();
    }

    public Document createDefaultDocument()
    {
        return new PlainDocument();
    }

    public String getText(int offs, int len)
    {
        return changeBasedDelegate.getText(offs, len);
    }

    public void appendString(String str)
    {
        changeBasedDelegate.appendString(str);
    }

    public void replaceString(String str, int start, int end)
    {
        changeBasedDelegate.replaceString(str, start, end);
    }

    public void insertString(String str, int pos)
    {
        changeBasedDelegate.insertString(str, pos);
    }

    public void setDefaultDataModel(Object dataModel)
    {
        if (markupDrivenUtil != null)
            markupDrivenUtil.preSetDefaultDataModel(dataModel);

        super.setDefaultDataModel(dataModel);
    }

    public void disposeEffective(boolean updateClient)
    {
        super.disposeEffective(updateClient);

        if (markupDrivenUtil != null)
        {
            markupDrivenUtil.dispose();
            this.markupDrivenUtil = null;
        }
    }

    public boolean isMarkupDriven()
    {
        return markupDrivenUtil != null;
    }

    public void setMarkupDriven(boolean value)
    {
        this.markupDrivenUtil = ItsNatHTMLFormTextCompMarkupDrivenUtil.setMarkupDriven(this, markupDrivenUtil, value);
    }
}
