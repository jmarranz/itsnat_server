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

package org.itsnat.comp.text;

import org.itsnat.comp.ItsNatElementComponentUI;

/**
 * Is the base interface of the User Interface of a text based header.
 *
 * <p>Current {@link ItsNatTextComponent} implementations are based on
 * &lt;input type="text"&gt; and &lt;textarea&gt; elements, in both cases the
 * <code>value</code> attribute/property is used to save the text as markup
 * (the textarea version is a bit more complicated because the first value
 * is set as the element text content too).</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatTextComponent#getItsNatTextComponentUI()
 */
public interface ItsNatTextComponentUI extends ItsNatElementComponentUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatTextComponent getItsNatTextComponent();

    /**
     * Returns the current text saved in markup.
     *
     * @return the current text saved in markup.
     */
    public String getText();

    /**
     * Returns whether the component user interface is editable or read-only.
     *
     * @return true if the component UI is editable.
     */
    public boolean isEditable();

    /**
     * Sets the element as editable or read-only.
     *
     * <p>Current implementation calls the methods <code>HTMLInputElement.setReadOnly(boolean)</code>
     * and <code>HTMLTextAreaElement.setReadOnly(boolean)</code> accordingly.</p>
     */
    public void setEditable(boolean b);
}
