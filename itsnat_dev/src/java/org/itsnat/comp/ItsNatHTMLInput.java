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

package org.itsnat.comp;

import org.w3c.dom.html.HTMLInputElement;

/**
 * Is the interface of &lt;input&gt; based components.
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatHTMLInput extends ItsNatHTMLFormComponent
{
    /**
     * Returns the associated DOM element to this component.
     *
     * @return the associated DOM element.
     */
    public HTMLInputElement getHTMLInputElement();

    /**
     *  Removes focus from this element.
     *
     * <p>It sends the appropriated JavaScript to the client to call again
     * using the symmetric DOM element at the client. This method does the same
     * as <code>org.w3c.dom.html.HTMLInputElement.blur()</code> in non-internal (remote) event mode.
     * </p>
     */
    public void blur();

    /**
     *  Gives focus to this element.
     *
     * <p>It sends the appropriated JavaScript to the client to call again
     * using the symmetric DOM element at the client. This method does the same
     * as <code>org.w3c.dom.html.HTMLInputElement.focus()</code> in non-internal (remote) event mode.
     * </p>
     */
    public void focus();

    /**
     *  Select the contents of the &lt;textarea&gt;.
     *
     * <p>It sends the appropriated JavaScript to the client to call again
     * using the symmetric DOM element at the client. This method does the same
     * as <code>org.w3c.dom.html.HTMLInputElement.select()</code> in non-internal (remote) event mode.
     * </p>
     */
    public void select();

    /**
     *  Simulate a mouse-click. For &lt;input&gt; elements whose
     * <code>type</code> attribute has one of the following values: "button",
     * "checkbox", "radio", "reset", or "submit".
     *
     * <p>It sends the appropriated JavaScript to the client to call again
     * using the symmetric DOM element at the client. This method does the same
     * as <code>org.w3c.dom.html.HTMLInputElement.click()</code> in non-internal (remote) event mode.
     * </p>
     */
    public void click();
}
