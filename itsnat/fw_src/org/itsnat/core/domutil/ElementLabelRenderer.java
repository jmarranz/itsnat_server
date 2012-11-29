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

package org.itsnat.core.domutil;

import org.w3c.dom.Element;

/**
 * Used by {@link ElementLabel} objects to render the associated value.
 *
 * @author Jose Maria Arranz Santamaria
 * @see ElementLabel#setElementLabelRenderer(ElementLabelRenderer)
 * @see ElementGroupManager#createDefaultElementLabelRenderer()
 */
public interface ElementLabelRenderer
{
    /**
     * Renders as markup the specified value into the specified element of the label.
     *
     * <p>The specified element must be used to render the value below, usually
     * as a text node.</p>
     *
     * <p>Default implementation renders the specified value inside the first text node found below the
     * specified content element.</p>
     *
     * @param label the target label.
     * @param value the value to render.
     * @param elem the DOM element to render the value inside. This element
     *    is a hint, if provided, should be obtained by calling {@link ElementLabel#getParentElement()}.
     * @param isNew true if this is the first time the markup is rendered. Default implementation ignores this parameter.
     */
    public void renderLabel(ElementLabel label,Object value,Element elem,boolean isNew);

    /**
     * Unrenders the label markup. This method is called <i>before</i> the markup
     * is removed.
     *
     * <p>Default implementation does nothing.</p>
     *
     * @param label the target label.
     * @param elem the DOM element used to render the label. This element
     *    is a hint, if provided, should be obtained by calling {@link ElementLabel#getParentElement()}>.
     */
    public void unrenderLabel(ElementLabel label,Element elem);
}
