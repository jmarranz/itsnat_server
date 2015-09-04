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
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListModel;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.domimpl.ItsNatNodeInternal;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLSelectComboBoxMarkupDrivenUtil extends ItsNatHTMLSelectMarkupDrivenUtil
{
    public ItsNatHTMLSelectComboBoxMarkupDrivenUtil(ItsNatHTMLSelectComboBoxImpl comp)
    {
        super(comp);
    }

    public ItsNatHTMLSelectComboBoxImpl getItsNatHTMLSelectComboBox()
    {
        return (ItsNatHTMLSelectComboBoxImpl)comp;
    }

    @Override
    public void addDataModelItem(String item,ListModel dataModel)
    {
        ((DefaultComboBoxModel)dataModel).addElement(item);
    }

    @Override
    public void addDataModelItem(int index,String item,ListModel dataModel)
    {
        ((DefaultComboBoxModel)dataModel).insertElementAt(item, index);
    }

    @Override
    public void removeDataModelItem(int index,ListModel dataModel)
    {
        ((DefaultComboBoxModel)dataModel).removeElementAt(index);
    }

    @Override
    public void selectItem(int index,boolean selected)
    {
        ItsNatHTMLSelectComboBoxImpl comp = getItsNatHTMLSelectComboBox();
        if (selected)
            comp.setSelectedIndex(index);
        else if (comp.getSelectedIndex() == index)
            comp.setSelectedIndex(-1);
    }

    @Override
    public void preSetDefaultDataModel(Object dataModel)
    {
        super.preSetDefaultDataModel(dataModel);

        ItsNatHTMLSelectImpl comp = getItsNatHTMLSelect();
        HTMLSelectElement select = comp.getHTMLSelectElement();

        // El uso de HTMLSelectElement.getOptions() es terriblemente ineficiente.
        // No toleramos <optgroup>
        int selectedOption = -1;
        int i = 0;
        HTMLOptionElement option = (HTMLOptionElement)ItsNatTreeWalker.getFirstChildElement(select);
        while(option != null)
        {
            if (option.getSelected())
            {
                selectedOption = i;
                break;
            }
            i++;
            option = (HTMLOptionElement)ItsNatTreeWalker.getNextSiblingElement(option);
        }

        // Recordar que todavía el data model no está asociado al componente
        ItsNatComboBoxSharedImpl.setSelectedIndex((ComboBoxModel)dataModel,selectedOption);
    }

    @Override
    public void initialSyncUIWithDataModel()
    {
        // Esto es necesario porque por ejemplo al inicializarse el UI se eliminan todos
        // los nodos, volverán a crearse al sincronizarse con el data model,
        // pero ahora sin el atributo selected que había originalmente en los nodos
        // seleccionados
        ItsNatHTMLSelectComboBoxImpl comp = getItsNatHTMLSelectComboBox();
        int index = comp.getSelectedIndex();
        if (index != -1)
            comp.getItsNatHTMLSelectComboBoxUIImpl().setSelectedIndex(index);

        HTMLSelectElement elem = comp.getHTMLSelectElement();
        ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMAttrModified",this, false); // Para el atributo selected

        super.initialSyncUIWithDataModel();
    }


    @Override
    public void dispose()
    {
        super.dispose();
        HTMLSelectElement elem = getItsNatHTMLSelect().getHTMLSelectElement();
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
            HTMLSelectElement elem = getItsNatHTMLSelect().getHTMLSelectElement();
            ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMAttrModified",this, false); // Para el atributo selected
        }
    }
}
