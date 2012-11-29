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

import java.beans.PropertyVetoException;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class DelegateHTMLInputTextEditorImpl extends DelegateHTMLElementComponentEditorImpl
{
    public DelegateHTMLInputTextEditorImpl(ItsNatHTMLInputText compEditor)
    {
        super(compEditor);
    }

    public ItsNatHTMLInputText getItsNatHTMLInputText()
    {
        return (ItsNatHTMLInputText)compEditor;
    }

    public Object getCellEditorValue()
    {
        ItsNatHTMLInputText compEditor = getItsNatHTMLInputText();
        if (compEditor instanceof ItsNatHTMLInputTextFormatted)
            return ((ItsNatHTMLInputTextFormatted)compEditor).getValue();
        else
            return compEditor.getText();
    }

    public void preSetValue(Object value)
    {
        String text = value.toString();
        ItsNatHTMLInputText compEditor = getItsNatHTMLInputText();
        HTMLInputElement inputElem = compEditor.getHTMLInputElement();
        DOMUtilInternal.setAttribute(inputElem,"size",String.valueOf(text.length() + 1)); // El + 1 es para evitar el 0 en caso de cadena nula
    }

    public void setValue(Object value)
    {
        String text = value.toString();
        ItsNatHTMLInputText compEditor = getItsNatHTMLInputText();

        if (compEditor instanceof ItsNatHTMLInputTextFormatted)
        {
            try
            {
             ((ItsNatHTMLInputTextFormatted)compEditor).setValue(value);
            }
            catch(PropertyVetoException ex)
            {
                throw new ItsNatException(ex,compEditor);
            }
        }
        else compEditor.setText(text);
    }

}
