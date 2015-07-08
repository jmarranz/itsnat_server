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

import org.itsnat.comp.text.ItsNatTextField;
import org.itsnat.comp.ItsNatHTMLInput;
import org.itsnat.comp.text.ItsNatHTMLInputTextBased;
import org.itsnat.comp.text.ItsNatTextFieldUI;
import org.itsnat.core.ItsNatDOMException;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLInputTextBasedUIImpl extends ItsNatHTMLFormTextCompUIImpl implements ItsNatTextFieldUI
{

    /**
     * Creates a new instance of ItsNatHTMLInputTextBasedUIImpl
     */
    public ItsNatHTMLInputTextBasedUIImpl(ItsNatHTMLInputTextBasedImpl parentComp)
    {
        super(parentComp);

        HTMLInputElement element = getHTMLInputElement();

        String type = element.getAttribute("type");
        type = type.toLowerCase();
        if (!type.equals("text") && !type.equals("password") && !type.equals("file") && !type.equals("hidden"))
            throw new ItsNatDOMException("HTMLInputElement type property must be text, password, file of hidden: " + type,element);
    }

    public ItsNatHTMLInputTextBased getItsNatHTMLInputTextBased()
    {
        return (ItsNatHTMLInputTextBased)parentComp;
    }

    public ItsNatHTMLInput getItsNatHTMLInput()
    {
        return (ItsNatHTMLInput)parentComp;
    }

    public ItsNatTextField getItsNatTextField()
    {
        return (ItsNatTextField)parentComp;
    }

    public HTMLInputElement getHTMLInputElement()
    {
        return getItsNatHTMLInputTextBased().getHTMLInputElement();
    }

    public String getDOMValueProperty()
    {
        HTMLInputElement elem = getHTMLInputElement();
        return elem.getValue();
    }

    public void setDOMValueProperty(String str)
    {
        // No es necesario considerar la desactivación de los mutation
        // events porque este método es llamado por código de la clase
        // base que ya controla eso.
        HTMLInputElement elem = getHTMLInputElement();
        elem.setValue(str);
    }

    public boolean isEditable()
    {
        HTMLInputElement element = getHTMLInputElement();
        return element.getReadOnly();
    }

    public void setEditable(boolean b)
    {
        if (b == isEditable()) return; // No hacer nada

        HTMLInputElement element = getHTMLInputElement();
        element.setReadOnly( ! b );
    }

    public int getColumns()
    {
        HTMLInputElement element = getHTMLInputElement();
        String value = element.getSize();
        if (value.equals(""))
            return -1; // Desconocido
        else
            return Integer.parseInt(value);
    }

    public void setColumns(int cols)
    {
        HTMLInputElement element = getHTMLInputElement();
        element.setSize(Integer.toString(cols));
    }
}
