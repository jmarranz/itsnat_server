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

import org.itsnat.comp.ItsNatElementComponentUI;

/**
 * Is the base interface of the User Interface of a label component.
 *
 * <p>The the current label renderer is used.</p>
 *
 * <p>Current implementation relays heavily on
 * {@link org.itsnat.core.domutil.ElementLabel}.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatLabel#getItsNatLabelUI()
 * @see ItsNatLabel#getItsNatLabelRenderer()
 */
public interface ItsNatLabelUI extends ItsNatElementComponentUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatLabel getItsNatLabel();

    /**
     * Informs whether this label contains markup.
     *
     * @return true if this label contains markup.
     */
    public boolean hasLabelMarkup();


    /**
     * Informs whether the original (saved as pattern) markup is used to render.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatDocument#isUsePatternMarkupToRender()}</p>
     *
     * @return true if the original markup is used.
     * @see #setUsePatternMarkupToRender(boolean)
     */
    public boolean isUsePatternMarkupToRender();

    /**
     * Sets whether the original (saved as pattern) markup is used to render.
     *
     * @param value true to enable the use of original markup to render.
     * @see #isUsePatternMarkupToRender()
     */
    public void setUsePatternMarkupToRender(boolean value);
}
