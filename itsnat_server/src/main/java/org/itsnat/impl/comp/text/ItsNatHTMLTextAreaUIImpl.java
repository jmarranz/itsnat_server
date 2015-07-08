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

import org.itsnat.comp.text.ItsNatTextArea;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.comp.text.ItsNatTextAreaUI;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLTextAreaUIImpl extends ItsNatHTMLFormTextCompUIImpl implements ItsNatTextAreaUI
{
    /**
     * Creates a new instance of ItsNatHTMLTextAreaUIImpl
     */
    public ItsNatHTMLTextAreaUIImpl(ItsNatHTMLTextAreaImpl parentComp)
    {
        super(parentComp);
    }

    public ItsNatHTMLTextArea getItsNatHTMLTextArea()
    {
        return (ItsNatHTMLTextArea)parentComp;
    }

    public ItsNatTextArea getItsNatTextArea()
    {
        return (ItsNatTextArea)parentComp;
    }

    public HTMLTextAreaElement getHTMLTextAreaElement()
    {
        return getItsNatHTMLTextArea().getHTMLTextAreaElement();
    }

    public String getDOMValueProperty()
    {
        HTMLTextAreaElement elem = getHTMLTextAreaElement();
        return elem.getValue();
    }

    public void setDOMValueProperty(String str)
    {
        // No es necesario considerar la desactivación de los mutation
        // events porque este método es llamado por código de la clase
        // base que ya controla eso.
        HTMLTextAreaElement elem = getHTMLTextAreaElement();
        elem.setValue(str);
    }

    public boolean isEditable()
    {
        HTMLTextAreaElement element = getHTMLTextAreaElement();
        return element.getReadOnly();
    }

    public void setEditable(boolean b)
    {
        if (b == isEditable()) return; // No hacer nada

        HTMLTextAreaElement element = getHTMLTextAreaElement();
        element.setReadOnly( ! b );
    }

    public int getColumns()
    {
        HTMLTextAreaElement element = getHTMLTextAreaElement();
        return element.getCols();
    }

    public void setColumns(int cols)
    {
        HTMLTextAreaElement element = getHTMLTextAreaElement();
        element.setCols(cols);
    }

    public int getRows()
    {
        HTMLTextAreaElement element = getHTMLTextAreaElement();
        return element.getRows();
    }

    public void setRows(int rows)
    {
        HTMLTextAreaElement element = getHTMLTextAreaElement();
        element.setRows(rows);
    }

    public boolean isLineWrap()
    {
        // El atributo wrap no es W3C pero está definido en MSIE y FireFox
        // http://www.htmlcodetutorial.com/forms/_TEXTAREA_WRAP.html
        HTMLTextAreaElement element = getHTMLTextAreaElement();
        String res = element.getAttribute("wrap");
        res = res.toLowerCase();
        return !res.equals("off");
    }

    public void setLineWrap(boolean wrap)
    {
        HTMLTextAreaElement element = getHTMLTextAreaElement();
        if (wrap) element.removeAttribute("wrap"); // Por defecto tanto MSIE como FireFox hace wrap
        else DOMUtilInternal.setAttribute(element,"wrap","off");
    }

    public boolean isEnabled()
    {
        HTMLTextAreaElement element = getHTMLTextAreaElement();
        return !element.getDisabled();
    }

    public void setEnabled(boolean b)
    {
        HTMLTextAreaElement element = getHTMLTextAreaElement();
        element.setDisabled( ! b );
    }
}
