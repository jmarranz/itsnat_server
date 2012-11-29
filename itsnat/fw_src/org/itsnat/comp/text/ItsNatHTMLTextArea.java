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

import org.itsnat.comp.ItsNatHTMLFormComponent;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 * Is the interface of text based components which text is rendered/edited
 * with multiple lines using an HTML &lt;textarea&gt; element.
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatHTMLComponentManager#createItsNatHTMLTextArea(org.w3c.dom.html.HTMLTextAreaElement,org.itsnat.core.NameValue[])
 */
public interface ItsNatHTMLTextArea extends ItsNatHTMLFormComponent,ItsNatTextArea
{
    /**
     * Returns the associated DOM element to this component.
     *
     * @return the associated DOM element.
     */
    public HTMLTextAreaElement getHTMLTextAreaElement();

    /**
     *  Removes focus from this element.
     *
     * <p>It sends the appropriated JavaScript to the client to call again
     * using the symmetric DOM element at the client. This method does the same
     * as <code>org.w3c.dom.html.HTMLTextAreaElement.blur()</code> in non-internal (remote) event mode.
     * </p>
     */
    public void blur();

    /**
     *  Gives focus to this element.
     *
     * <p>It sends the appropriated JavaScript to the client to call again
     * using the symmetric DOM element at the client. This method does the same
     * as <code>org.w3c.dom.html.HTMLTextAreaElement.focus()</code> in non-internal (remote) event mode.
     * </p>
     */
    public void focus();

    /**
     *  Select the contents of the <code>TEXTAREA</code> .
     *
     * <p>It sends the appropriated JavaScript to the client to call again
     * using the symmetric DOM element at the client. This method does the same
     * as <code>org.w3c.dom.html.HTMLTextAreaElement.select()</code> in non-internal (remote) event mode.
     * </p>
     */
    public void select();

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
