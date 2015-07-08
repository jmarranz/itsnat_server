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

package org.itsnat.comp.list;

import org.itsnat.comp.ItsNatHTMLFormComponent;
import org.w3c.dom.html.HTMLSelectElement;

/**
 * Is the base interface of list components attached to HTML &lt;select&gt; elements.
 *
 * <p>HTML select based components can be combo boxes and multiple selection lists</p>
 *
 * <p>The HTML select element may be empty initially, no pattern is needed (because only
 * &lt;option&gt; elements are allowed, current implementations do not support &lt;optgroup&gt;).</p>
 *
 * <p>HTML &lt;select&gt; based components have no in place editor, because &lt;option&gt; elements
 * only support text nodes.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatHTMLSelect extends ItsNatHTMLFormComponent, ItsNatList
{

    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatHTMLSelectUI getItsNatHTMLSelectUI();

    /**
     * Returns the associated DOM element to this component.
     *
     * @return the associated DOM element.
     */
    public HTMLSelectElement getHTMLSelectElement();

    /**
     *  Removes focus from this element.
     *
     * <p>It sends the appropriated JavaScript to the client to call again
     * using the symmetric DOM element at the client. This method does the same
     * as <code>org.w3c.dom.html.HTMLSelectElement.blur()</code> in non-internal (remote) event mode.
     * </p>
     */
    public void blur();

    /**
     *  Gives focus to this element.
     *
     * <p>It sends the appropriated JavaScript to the client to call again
     * using the symmetric DOM element at the client. This method does the same
     * as <code>org.w3c.dom.html.HTMLSelectElement.focus()</code> in non-internal (remote) event mode.
     * </p>
     */
    public void focus();

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
