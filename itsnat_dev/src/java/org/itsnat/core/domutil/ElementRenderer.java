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
 * Is used to render values as markup into DOM elements.
 *
 * @see ElementGroupManager#createDefaultElementRenderer()
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementRenderer
{
    /**
     * Renders as markup the specified value into the specified element, usually
     * as a text node.
     *
     * <p>Default implementation renders the specified value inside the first text node with some text 
     * (not only spaces, tabs or line feeds) found below the specified element.</p>
     *
     * @param userObj a context object used to provide contextual information. Default implementation ignores it.
     * @param value the value to render.
     * @param elem the element to render the value into.
     * @param isNew true if this is the first time the markup is rendered. Default implementation ignores this parameter.
     */
    public void render(Object userObj,Object value,Element elem,boolean isNew);

    /**
     * Unrenders the markup used to render previously. This method is called <i>before</i> the markup
     * is removed.
     *
     * <p>Default implementation does nothing.</p>
     *
     * @param userObj a context object used to provide contextual information. Default implementation ignores it.
     * @param elem the element to render the value into.
     */
    public void unrender(Object userObj,Element elem);
}
