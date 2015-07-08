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

import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author jmarranz
 */
public class DelegateHTMLTextAreaEditorImpl extends DelegateHTMLElementComponentEditorImpl
{
    public DelegateHTMLTextAreaEditorImpl(ItsNatHTMLTextArea compEditor)
    {
        super(compEditor);
    }

    public ItsNatHTMLTextArea getItsNatHTMLTextArea()
    {
        return (ItsNatHTMLTextArea)compEditor;
    }

    public Object getCellEditorValue()
    {
        ItsNatHTMLTextArea compEditor = getItsNatHTMLTextArea();
        return compEditor.getText();
    }

    public void preSetValue(Object value)
    {
        String text = value.toString();
        ItsNatHTMLTextArea compEditor = getItsNatHTMLTextArea();
        HTMLTextAreaElement elem = compEditor.getHTMLTextAreaElement();

        int maxNumCols = 1;
        int rows = 0;
        int cols = 0;
        for(int i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) == '\n')
            {
                rows++;
                if (cols > maxNumCols) maxNumCols = cols;
                cols = 0;
            }
            else cols++;
        }
        if (cols > maxNumCols) maxNumCols = cols;
        maxNumCols += 4; // Para que sobre algo más (o al menos una pues el cero no vale)
        rows += 2; // Idem
        elem.setCols(maxNumCols);
        elem.setRows(rows);
    }

    public void setValue(Object value)
    {
        String text = value.toString();
        ItsNatHTMLTextArea compEditor = getItsNatHTMLTextArea();
        compEditor.setText(text);
    }
}
