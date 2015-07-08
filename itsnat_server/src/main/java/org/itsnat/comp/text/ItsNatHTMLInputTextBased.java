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

import org.itsnat.comp.ItsNatHTMLInput;

/**
 * Is the base interface of text based components which text is rendered/edited
 * as a single line using an HTML input element.
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatHTMLInputTextBased extends ItsNatHTMLInput,ItsNatTextField
{
    /**
     * Informs whether this component is markup driven.
     *
     * <p>The default value is defined by the artifact name "markupDriven"
     * if defined or "markupDriven" attribute (ItsNat namespace) if defined
     * else by {@link org.itsnat.comp.ItsNatComponentManager#isMarkupDrivenComponents()}</p>
     *
     * @return true if this component is markup driven.
     * @see #setMarkupDriven(boolean)
     */
    public boolean isMarkupDriven();

    /**
     * Sets whether this component is markup driven.
     *
     * @param value true to enable markup driven.
     * @see #isMarkupDriven()
     */
    public void setMarkupDriven(boolean value);
}
