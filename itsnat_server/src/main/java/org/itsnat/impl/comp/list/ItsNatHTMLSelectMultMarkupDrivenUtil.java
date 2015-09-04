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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.domimpl.ItsNatNodeInternal;
import org.w3c.dom.CharacterData;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLSelectMultMarkupDrivenUtil extends ItsNatHTMLSelectMarkupDrivenUtil
{
    public ItsNatHTMLSelectMultMarkupDrivenUtil(ItsNatHTMLSelectMultImpl comp)
    {
        super(comp);
    }

    public ItsNatHTMLSelectMultImpl getItsNatHTMLSelectMult()
    {
        return (ItsNatHTMLSelectMultImpl)comp;
    }

    @Override
    public void addDataModelItem(String item,ListModel dataModel)
    {
        ((DefaultListModel)dataModel).addElement(item);
    }

    @Override
    public void addDataModelItem(int index,String item,ListModel dataModel)
    {
        ((DefaultListModel)dataModel).add(index,item);
    }

    @Override
    public void removeDataModelItem(int index,ListModel dataModel)
    {
        ((DefaultListModel)dataModel).remove(index);
    }

    @Override
    public void selectItem(int index,boolean selected)
    {
        ItsNatHTMLSelectMultImpl comp = getItsNatHTMLSelectMult();
        ListSelectionModelMgrImpl selModelMgr = comp.getListSelectionModelMgr();
        if (selected) selModelMgr.addSelectionInterval(index, index);
        else selModelMgr.removeSelectionInterval(index, index);
    }

    public void postSetDefaultListSelectionModel()
    {
        ItsNatHTMLSelectMultImpl comp = getItsNatHTMLSelectMult();
        HTMLSelectElement select = comp.getHTMLSelectElement();

        // El uso de HTMLSelectElement.getOptions() es terriblemente ineficiente.
        // No toleramos <optgroup>
        int i = 0;
        HTMLOptionElement option = (HTMLOptionElement)ItsNatTreeWalker.getFirstChildElement(select);
        while(option != null)
        {
            boolean selected = option.getSelected();
            selectItem(i,selected);
            i++;
            option = (HTMLOptionElement)ItsNatTreeWalker.getNextSiblingElement(option);
        }
    }

    @Override
    public void initialSyncUIWithDataModel()
    {
        // Esto es necesario por ejemplo porque al inicializarse el UI se eliminan todos
        // los nodos, volverán a crearse al sincronizarse con el data model,
        // pero ahora sin el atributo selected que había originalmente en los nodos
        // seleccionados

        ItsNatHTMLSelectMultImpl comp = getItsNatHTMLSelectMult();
        ListSelectionModel selModel = comp.getListSelectionModel();

        int len = comp.getListModel().getSize();
        ItsNatListMultSelUIInternal compUI = comp.getItsNatListMultSelUIInternal();
        for(int i = 0; i <= len; i++)
        {
            boolean selected = selModel.isSelectedIndex(i);
            compUI.setSelectedIndex(i,selected);
        }

        HTMLSelectElement elem = getItsNatHTMLSelectMult().getHTMLSelectElement();
        ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMCharacterDataModified",this, false); // Para cuando cambie el contenido de una opción
        ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMAttrModified",this, false); // Para el atributo selected

        super.initialSyncUIWithDataModel();
    }

    @Override
    public void dispose()
    {
        super.dispose();

        HTMLSelectElement elem = getItsNatHTMLSelect().getHTMLSelectElement();
        ((ItsNatNodeInternal)elem).removeEventListenerInternal("DOMCharacterDataModified",this, false);
        ((ItsNatNodeInternal)elem).removeEventListenerInternal("DOMAttrModified",this, false);
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        // Hay que tener en cuenta que los event listeners internos no se serializan
        if (!comp.isDisposed())
        {
            HTMLSelectElement elem = getItsNatHTMLSelectMult().getHTMLSelectElement();
            ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMCharacterDataModified",this, false); // Para cuando cambie el contenido de una opción
            ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMAttrModified",this, false); // Para el atributo selected
        }
    }

    @Override
    public void handleEvent(Event evt)
    {
        super.handleEvent(evt);

        ItsNatHTMLSelectMultImpl comp = getItsNatHTMLSelectMult();
//        if (comp.isServerUpdatingFromClient())
//            return;

        String type = evt.getType();
        if (type.equals("DOMCharacterDataModified"))
        {
            MutationEvent mutEvent = (MutationEvent)evt;
            CharacterData charDataNode = (CharacterData)mutEvent.getTarget();
            HTMLOptionElement option = (HTMLOptionElement)charDataNode.getParentNode();
            int index = option.getIndex();
            String value = charDataNode.getData();
            DefaultListModel dataModel = (DefaultListModel)comp.getListModel();
            if (!value.equals(dataModel.get(index)))
                dataModel.set(index, value);
        }
    }
}
