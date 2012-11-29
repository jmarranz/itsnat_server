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

package org.itsnat.impl.comp.inplace;

import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;

/**
 *
 * @author jmarranz
 */
public class DelegateHTMLSelectComboBoxEditorImpl extends DelegateHTMLElementComponentEditorImpl
{
    public DelegateHTMLSelectComboBoxEditorImpl(ItsNatHTMLSelectComboBox compEditor)
    {
        super(compEditor);
    }

    public ItsNatHTMLSelectComboBox getItsNatHTMLSelectComboBox()
    {
        return (ItsNatHTMLSelectComboBox)compEditor;
    }

    public Object getCellEditorValue()
    {
        ItsNatHTMLSelectComboBox compEditor = getItsNatHTMLSelectComboBox();
        return compEditor.getSelectedItem();
    }

    public void preSetValue(Object value)
    {
    }

    public void setValue(Object value)
    {
       ItsNatHTMLSelectComboBox compEditor = getItsNatHTMLSelectComboBox();
       compEditor.setSelectedItem(value);
    }
}
