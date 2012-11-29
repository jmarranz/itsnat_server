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

import org.w3c.dom.Element;

/**
 * Is used to render label values as markup into DOM elements.
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createDefaultItsNatLabelRenderer()
 * @see ItsNatLabel#getItsNatLabelRenderer()
 * @see ItsNatLabel#setItsNatLabelRenderer(ItsNatLabelRenderer)
 */
public interface ItsNatLabelRenderer
{
    /**
     * Renders as markup the specified value into the specified element, usually
     * as a text node.
     *
     * <p>Default implementation delegates to the default {@link org.itsnat.core.domutil.ElementRenderer}.</p>
     *
     * @param label the label component, may be used to provide contextual information. Default implementation ignores it.
     * @param value the value to render.
     * @param labelElem the label element to render the value into. Is a hint, if provided should be obtained by calling {@link ItsNatLabel#getElement()}.
     * @param isNew true if this is the first time the markup is rendered. Default implementation ignores this parameter.
     */
    public void renderLabel(
                ItsNatLabel label,
                Object value,
                Element labelElem,
                boolean isNew);

    /**
     * Unrenders the label markup. This method is called <i>before</i> the markup
     * is removed.
     *
     * <p>Default implementation does nothing.</p>
     *
     * @param label the label component, may be used to provide contextual information. Default implementation ignores it.
     * @param labelElem the label element to render the value into. Is a hint, if provided should be obtained by calling {@link ItsNatLabel#getElement()}.
     */
    public void unrenderLabel(ItsNatLabel label,Element labelElem);
}
