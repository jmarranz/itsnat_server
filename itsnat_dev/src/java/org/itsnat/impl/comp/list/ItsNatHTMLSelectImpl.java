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

package org.itsnat.impl.comp.list;

import org.itsnat.impl.comp.ItsNatHTMLFormCompChangeBasedSharedImpl;
import org.itsnat.impl.comp.ItsNatHTMLFormCompChangeBased;
import org.itsnat.impl.comp.ItsNatHTMLFormComponentImpl;
import org.itsnat.comp.list.ItsNatListCellEditor;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.comp.list.ItsNatHTMLSelect;
import org.itsnat.comp.list.ItsNatListUI;
import org.itsnat.core.ItsNatException;
import javax.swing.event.ListDataEvent;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.comp.list.ItsNatHTMLSelectUI;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.scriptren.jsren.JSRenderMethodCallImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLSelectImpl extends ItsNatHTMLFormComponentImpl implements ItsNatHTMLSelect, ItsNatListInternal, ItsNatHTMLFormCompChangeBased
{
    protected ItsNatListCellRenderer renderer;
    protected ItsNatHTMLSelectMarkupDrivenUtil markupDrivenUtil;
    protected ItsNatListSharedImpl listDelegate = createItsNatListShared();
    protected ItsNatHTMLFormCompChangeBasedSharedImpl changeBasedDelegate = new ItsNatHTMLFormSelectCompSharedImpl(this);

    /**
     * Creates a new instance of ItsNatHTMLSelectImpl
     */
    public ItsNatHTMLSelectImpl(HTMLSelectElement selectElem, NameValue[] artifacts, ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(selectElem, artifacts, componentMgr);

        this.markupDrivenUtil = ItsNatHTMLSelectMarkupDrivenUtil.initMarkupDriven(this);

        setItsNatListCellRenderer(componentMgr.createDefaultItsNatListCellRenderer());
    }

    @Override
    public void init()
    {
        changeBasedDelegate.init();

        super.init();
    }

    @Override
    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        changeBasedDelegate.enableEventListenersByDoc();
    }

    @Override
    public void enableEventListenersByClient(ItsNatCompNormalEventListenersByClientImpl evtListeners)
    {
        super.enableEventListenersByClient(evtListeners);

        changeBasedDelegate.enableEventListenersByClient(evtListeners);
    }

    @Override
    public void processNormalEvent(Event evt)
    {
        changeBasedDelegate.processNormalEvent(evt);

        super.processNormalEvent(evt);
    }

    public void postHandleEventOnChange(Event evt)
    {
        // Nada que hacer
    }

    public ItsNatListStructure getItsNatListStructure()
    {
        // La estructura es fija, no puede tener algo a medida
        return null;
    }

    public HTMLSelectElement getHTMLSelectElement()
    {
        return (HTMLSelectElement)node;
    }

    public HTMLFormElement getHTMLFormElement()
    {
        return getHTMLSelectElement().getForm();
    }

    public ItsNatListUI getItsNatListUI()
    {
        return (ItsNatListUI) compUI;
    }

    public ItsNatListUIInternal getItsNatListUIInternal()
    {
        return (ItsNatListUIInternal)compUI;
    }

    public ItsNatHTMLSelectUI getItsNatHTMLSelectUI()
    {
        return (ItsNatHTMLSelectUI) compUI;
    }

    public ItsNatHTMLSelectUIImpl getItsNatHTMLSelectUIImpl()
    {
        return (ItsNatHTMLSelectUIImpl) compUI;
    }

    public void bindDataModel()
    {
        listDelegate.bindDataModel();
    }

    public void unbindDataModel()
    {
        listDelegate.unbindDataModel();
    }

    public void initialSyncUIWithDataModel()
    {
        listDelegate.initialSyncUIWithDataModel();

        if (markupDrivenUtil != null)
            markupDrivenUtil.initialSyncUIWithDataModel();
    }

    public int indexOf(Object obj)
    {
        return ItsNatListSharedImpl.indexOf(obj, getListModel());
    }

    public void intervalAdded(ListDataEvent e)
    {
        listDelegate.intervalAdded(e);
    }

    public void intervalRemoved(ListDataEvent e)
    {
        listDelegate.intervalRemoved(e);
    }

    public void contentsChanged(ListDataEvent e)
    {
        listDelegate.contentsChanged(e);
    }

    public void insertElementAtInternal(int i, Object item)
    {
        if (!isUIEnabled()) return;

        ItsNatHTMLSelectUIImpl compUI = getItsNatHTMLSelectUIImpl();
        compUI.insertElementAt(i, item);
    }

    public void removeElementRangeInternal(int fromIndex, int toIndex)
    {
        if (!isUIEnabled()) return;
   
        ItsNatHTMLSelectUIImpl compUI = getItsNatHTMLSelectUIImpl();
        compUI.removeElementRange(fromIndex, toIndex);
    }

    public void blur()
    {
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocumentImpl();
        JSRenderMethodCallImpl.addCallMethodHTMLFormControlCode(getHTMLElement(),"blur",itsNatDoc);
    }

    public void focus()
    {
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocumentImpl();
        JSRenderMethodCallImpl.addCallMethodHTMLFormControlCode(getHTMLElement(),"focus",itsNatDoc);
    }

    public ItsNatListCellRenderer getItsNatListCellRenderer()
    {
        return renderer;
    }

    public void setItsNatListCellRenderer(ItsNatListCellRenderer renderer)
    {
        this.renderer = renderer;
    }

    public ItsNatListCellEditor getItsNatListCellEditor()
    {
        return null;
    }

    public void setItsNatListCellEditor(ItsNatListCellEditor cellEditor)
    {
        throw new ItsNatException("<select> options can not be graphically edited",this);
    }

    public void startEditingAt(int index)
    {
        throw new ItsNatException("<select> options can not be graphically edited",this);
    }

    public boolean isEditing()
    {
        return false;
    }

    public int getEditingIndex()
    {
        return -1;
    }

    public String getEditorActivatorEvent()
    {
        throw new ItsNatException("<select> options can not be graphically edited",this);
    }

    public void setEditorActivatorEvent(String event)
    {
        throw new ItsNatException("<select> options can not be graphically edited",this);
    }

    public boolean isEditingEnabled()
    {
        return false;
    }

    public void setEditingEnabled(boolean value)
    {
        if (value)
            throw new ItsNatException("<select> options can not be graphically edited",this);
    }

    public Node createDefaultNode()
    {
        Document doc = getItsNatDocument().getDocument();
        HTMLSelectElement elem = (HTMLSelectElement)doc.createElementNS(NamespaceUtil.XHTML_NAMESPACE,"select");
        elem.setMultiple(!isCombo());
        return elem;
    }

    public abstract boolean isCombo();

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
        ItsNatHTMLSelectMarkupDrivenUtil old = markupDrivenUtil;
        this.markupDrivenUtil = ItsNatHTMLSelectMarkupDrivenUtil.setMarkupDriven(this, markupDrivenUtil, value);
        if (markupDrivenUtil != old)
        {
            // Ha cambiado (de markup driven a no markup driven o viceversa)
            // Hay que recrear el UI porque el modo markup driven exige
            // modo slave del UI y si no es modo markup es mejor el modo master (más rápido)
            setDefaultItsNatComponentUI();
        }
    }

    public boolean isEnabled()
    {
        HTMLSelectElement element = getHTMLSelectElement();
        return !element.getDisabled();
    }

    public void setEnabled(boolean b)
    {
        HTMLSelectElement element = getHTMLSelectElement();
        element.setDisabled( ! b );
    }
}
