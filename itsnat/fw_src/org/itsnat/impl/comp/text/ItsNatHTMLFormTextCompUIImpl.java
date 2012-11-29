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

import org.itsnat.impl.comp.ItsNatHTMLElementComponentUIImpl;
import org.itsnat.comp.text.ItsNatTextComponent;
import org.itsnat.comp.ItsNatHTMLFormComponent;
import org.itsnat.comp.text.ItsNatTextComponentUI;
import org.itsnat.impl.comp.ItsNatHTMLFormCompValueBasedImpl;
import org.itsnat.impl.comp.ItsNatHTMLFormComponentImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLFormTextCompUIImpl extends ItsNatHTMLElementComponentUIImpl implements ItsNatTextComponentUI
{
    /**
     * Creates a new instance of ItsNatHTMLFormTextCompUIImpl
     */
    public ItsNatHTMLFormTextCompUIImpl(ItsNatHTMLFormCompValueBasedImpl parentComp)
    {
        super(parentComp);

        setEditable(true);
    }

    public ItsNatHTMLFormComponent getItsNatHTMLFormComponent()
    {
        return (ItsNatHTMLFormComponent)parentComp;
    }

    public ItsNatHTMLFormComponentImpl getItsNatHTMLFormComponentImpl()
    {
        return (ItsNatHTMLFormComponentImpl)parentComp;
    }

    public ItsNatTextComponent getItsNatTextComponent()
    {
        return (ItsNatTextComponent)parentComp;
    }

    public String getText()
    {
        // NO publicar, es interna
        return getDOMValueProperty();
    }

    /**
     * ESTE METODO ERA PUBLICO ANTES
     *
     * Renders the specified text to the markup.
     *
     * @param text the new text.
     */
    public void setText(String str)
    {
        setDOMValueProperty(str); // Sólo nos interesa modificar en el servidor no en ningún cliente porque la modificación en el cliente se hace después
    }

    /**
     * ESTE METODO ERA PUBLICO
     *
     * Inserts the specified string into the markup.
     *
     * @param where index of the position to insert (0 based).
     * @param str the new string to insert.
     * @see #removeString(int,int)
     */
    public void insertString(int where, String str)
    {
        String text = getText();
        text = text.substring(0,where) + str + text.substring(where);
        setText(text);
    }

    /**
     * ESTE METODO ERA PUBLICO
     * 
     * Removes the specified text fragment from the markup.
     *
     * @param where position start of the fragment to remove (0 based).
     * @param len the number of characters to remove.
     * @see #insertString(int,String)
     */
    public void removeString(int where, int len)
    {
        String text = getText();
        text = text.substring(0,where) + text.substring(where + len);
        setText(text);
    }

    protected abstract String getDOMValueProperty();
    protected abstract void setDOMValueProperty(String str);
}
