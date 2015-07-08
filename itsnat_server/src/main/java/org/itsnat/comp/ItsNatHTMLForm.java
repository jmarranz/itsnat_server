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

import org.w3c.dom.html.HTMLFormElement;

/**
 * Is the interface of HTML &lt;form&gt; components.
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatHTMLComponentManager#createItsNatHTMLForm(org.w3c.dom.html.HTMLFormElement,org.itsnat.core.NameValue[])
 */
public interface ItsNatHTMLForm extends ItsNatHTMLElementComponent
{
    /**
     * Returns the associated DOM element to this component.
     *
     * @return the associated DOM element.
     */
    public HTMLFormElement getHTMLFormElement();

    /**
     *  Calls the <code>submit()</code> method on the client element.
     *
     * <p>It sends the appropriated JavaScript to the client to call again
     * using the symmetric DOM element at the client. This method does the same
     * as <code>org.w3c.dom.html.HTMLFormElement.submit()</code> in non-internal (remote) event mode.
     * </p>
     */
    public void submit();

    /**
     *  Calls the <code>reset()</code> method on the client element.
     *
     * <p>It sends the appropriated JavaScript to the client to call again
     * using the symmetric DOM element at the client. This method does the same
     * as <code>org.w3c.dom.html.HTMLFormElement.reset()</code> in non-internal (remote) event mode.
     * </p>
     */
    public void reset();
}
