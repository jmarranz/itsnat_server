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
 * Used by {@link ElementList} objects to render the values associated
 * to child elements.
 *
 * @author Jose Maria Arranz Santamaria
 * @see ElementList#setElementListRenderer(ElementListRenderer)
 * @see ElementGroupManager#createDefaultElementListRenderer()
 */
public interface ElementListRenderer
{
    /**
     * Renders as markup the specified value into the specified child element of the list.
     *
     * <p>The content element must be used to render below the value, usually
     * as a text node.</p>
     *
     * <p>Default implementation renders the specified value inside the first text node found below the
     * specified content element.</p>
     *
     * @param list the target list.
     * @param index the child element position.
     * @param value the value to render.
     * @param contentElem the content element. This element
     *    is a hint, if provided, should be obtained by calling {@link ElementListStructure#getContentElement(ElementList,int,Element)}.
     * @param isNew true if this is the first time the markup is rendered. Default implementation ignores this parameter.
     */
    public void renderList(ElementList list,int index,Object value,Element contentElem,boolean isNew);

    /**
     * Unrenders the markup of the specified list item. This method is called <i>before</i> the markup
     * is removed.
     *
     * <p>Default implementation does nothing.</p>
     *
     * @param list the target list.
     * @param index the child element position.
     * @param contentElem the content element. This element
     *    is a hint, if provided, should be obtained by calling {@link ElementListStructure#getContentElement(ElementList,int,Element)}.
     */
    public void unrenderList(ElementList list,int index,Element contentElem);

}
