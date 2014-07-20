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

import java.util.ArrayList;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocJoystickImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientJoystickImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocImpl;
import org.itsnat.impl.comp.listener.JoystickModeComponent;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.comp.list.ItsNatFreeList;
import org.itsnat.comp.list.ItsNatListUI;
import javax.swing.event.ListDataEvent;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.comp.list.ItsNatListCellUI;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.*;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersJoystick;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersJoystickSharedImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MouseEvent;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatFreeListImpl extends ItsNatFreeElementComponentImpl implements ItsNatFreeList,ItsNatListInternal,JoystickModeComponent
{
    protected boolean enabled = true;
    protected ItsNatListCellRenderer renderer;
    protected ItsNatListSharedImpl delegate = createItsNatListShared();
    protected ItsNatListStructure structure;

    /**
     * Creates a new instance of ItsNatFreeListImpl
     */
    public ItsNatFreeListImpl(Element element,ItsNatListStructure structure,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        this.structure = structure != null ? structure : (ItsNatListStructure)getDeclaredStructure(ItsNatListStructure.class);

        setItsNatListCellRenderer(componentMgr.createDefaultItsNatListCellRenderer());
    }

    public ItsNatCompNormalEventListenersByDocImpl createItsNatCompNormalEventListenersByDoc()
    {
        return new ItsNatCompNormalEventListenersByDocJoystickImpl(this);
    }

    public ItsNatCompNormalEventListenersByClientImpl createItsNatCompNormalEventListenersByClient(ClientDocumentImpl clientDoc)
    {
        return new ItsNatCompNormalEventListenersByClientJoystickImpl(this,clientDoc);
    }

    public boolean isJoystickMode()
    {
        return getItsNatCompNormalEventListenersByDocJoystick().isJoystickEnabled();
    }

    public void setJoystickMode(boolean value)
    {
        getItsNatCompNormalEventListenersByDocJoystick().setJoystickEnabled(value);
    }

    /* Esté método se hará público en el futuro */
    public boolean isJoystickMode(ClientDocument clientDoc)
    {
        return getItsNatCompNormalEventListenersByClientJoystick((ClientDocumentImpl)clientDoc).isJoystickEnabled();
    }

    /* Esté método se hará público en el futuro */
    public void setJoystickMode(ClientDocument clientDoc,boolean value)
    {
        getItsNatCompNormalEventListenersByClientJoystick((ClientDocumentImpl)clientDoc).setJoystickEnabled(value);
    }

    public ItsNatCompNormalEventListenersByDocJoystickImpl getItsNatCompNormalEventListenersByDocJoystick()
    {
        return (ItsNatCompNormalEventListenersByDocJoystickImpl)normalEventListenersByDoc;
    }

    public ItsNatCompNormalEventListenersByClientJoystickImpl getItsNatCompNormalEventListenersByClientJoystick(ClientDocumentImpl clientDoc)
    {
        return (ItsNatCompNormalEventListenersByClientJoystickImpl)getItsNatCompNormalEventListenersByClient(clientDoc);
    }

    public ItsNatListStructure getItsNatListStructure()
    {
        return structure;
    }

    public Object createDefaultStructure()
    {
        return getItsNatComponentManager().createDefaultItsNatListStructure();
    }

    @Override
    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        enableEventListener("click");
    }

    public void bindDataModel()
    {
        delegate.bindDataModel();
    }

    public void unbindDataModel()
    {
        delegate.unbindDataModel();
    }

    public void initialSyncUIWithDataModel()
    {
        delegate.initialSyncUIWithDataModel();
    }

    public ItsNatListUI getItsNatListUI()
    {
        return (ItsNatListUI)compUI;
    }

    public ItsNatListUIInternal getItsNatListUIInternal()
    {
        return (ItsNatListUIInternal)compUI;
    }

    public ItsNatFreeListUIImpl getItsNatFreeListUIImpl()
    {
        return (ItsNatFreeListUIImpl)compUI;
    }
    
    public int indexOf(Object obj)
    {
        return ItsNatListSharedImpl.indexOf(obj,getListModel());
    }

    public ItsNatListCellRenderer getItsNatListCellRenderer()
    {
        return renderer;
    }

    public void setItsNatListCellRenderer(ItsNatListCellRenderer renderer)
    {
        this.renderer = renderer;
    }

    public void intervalAdded(ListDataEvent e)
    {
        delegate.intervalAdded(e);
    }

    public void intervalRemoved(ListDataEvent e)
    {
        delegate.intervalRemoved(e);
    }

    public void contentsChanged(ListDataEvent e)
    {
        delegate.contentsChanged(e);
    }

    public void insertElementAtInternal(int i,Object item)
    {
        ItsNatFreeListUIImpl compUI = getItsNatFreeListUIImpl();
        Element elem = compUI.insertElementAt(i,item);

        addInternalEventListenerJoystickMode(i,elem);
    }

    public void removeElementRangeInternal(int fromIndex,int toIndex)
    {
        removeInternalEventListenerJoystickMode(fromIndex,toIndex);

        ItsNatFreeListUIImpl compUI = getItsNatFreeListUIImpl();
        compUI.removeElementRange(fromIndex,toIndex);
    }

    public Element[] getContentElementList()
    {
        ItsNatListUI compUI = getItsNatListUI();
        int size = compUI.getLength();
        Element[] elemList = new Element[size];
        for(int i = 0; i < size; i++)
            elemList[i] = compUI.getContentElementAt(i);
        return elemList;
    }

    public void addInternalEventListenerJoystickMode(int index,Element elem)
    {
        ArrayList<ItsNatCompNormalEventListenersJoystick> domListeners = ItsNatCompNormalEventListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        Element contentElem = getItsNatListStructure().getContentElement(this,index,elem);

        ItsNatCompNormalEventListenersJoystickSharedImpl.addEventListenerJoystick(domListeners, contentElem);
    }

    public void removeInternalEventListenerJoystickMode(int index)
    {
        ArrayList<ItsNatCompNormalEventListenersJoystick> domListeners = ItsNatCompNormalEventListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        removeInternalEventListenerJoystickMode(domListeners,index);
    }

    public void removeInternalEventListenerJoystickMode(ArrayList<ItsNatCompNormalEventListenersJoystick> domListeners,int index)
    {
        ItsNatListUI compUI = getItsNatListUI();
        Element contentElem = compUI.getContentElementAt(index);

        ItsNatCompNormalEventListenersJoystickSharedImpl.removeEventListenerJoystick(domListeners, contentElem);
    }

    public void removeInternalEventListenerJoystickMode(int fromIndex,int toIndex)
    {
        ArrayList<ItsNatCompNormalEventListenersJoystick> domListeners = ItsNatCompNormalEventListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        for(int i = fromIndex; i <= toIndex; i++)
            removeInternalEventListenerJoystickMode(domListeners,i);
    }

    public void processNormalEvent(Event evt)
    {
        String type = evt.getType();
        if (type.equals("click") || type.equals("mouseup"))
        {
            Node nodeClicked = (Node)evt.getTarget(); // Puede ser un nodo interior del elemento pulsado

            ItsNatListUI compUI = getItsNatListUI();
            ItsNatListCellUI option = compUI.getItsNatListCellUIFromNode(nodeClicked);
            if (option != null) // por si acaso
                processMouseClick((MouseEvent)evt,option);
        }

        super.processNormalEvent(evt);
    }

    public abstract void processMouseClick(MouseEvent mouseEvt,ItsNatListCellUI option);

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean b)
    {
        this.enabled = b;
    }
}
