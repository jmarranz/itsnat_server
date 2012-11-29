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

package org.itsnat.comp.label;

import org.itsnat.comp.*;
import javax.swing.CellEditor;
import org.w3c.dom.Element;

/**
 * Used to specify how a label value is edited in place.
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatComponentManager#createDefaultItsNatLabelEditor(ItsNatComponent)
 * @see ItsNatLabel#getItsNatLabelEditor()
 */
public interface ItsNatLabelEditor extends CellEditor
{
    /**
     * Returns the component used to edit in place the label value.
     *
     * <p>Default implementation uses a {@link org.itsnat.comp.text.ItsNatHTMLInputText} (text not formatted version) to edit
     * the label value.</p>
     *
     * @param label the label component, may be used to provide contextual information. Default implementation ignores it.
     * @param value the value to edit (initial value).
     * @param labelElem the label element to render the value into. Is a hint, if provided should be obtained by calling {@link ItsNatLabel#getElement()}.
     * @return the component used to edit in place the label value. Current implementation of labels does nothing with this component and may be null (is not mandatory to use a single component as an editor).
     * @see ItsNatLabel#setItsNatLabelEditor(ItsNatLabelEditor)
     */
    public ItsNatComponent getLabelEditorComponent(
                ItsNatLabel label,
                Object value,
                Element labelElem);
}
