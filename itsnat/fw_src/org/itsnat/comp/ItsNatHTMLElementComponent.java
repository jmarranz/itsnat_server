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

import org.w3c.dom.html.HTMLElement;

/**
 * Is the base interface which every HTML DOM Element based component implements.
 *
 * <p>Some HTML elements has a built-in ItsNat components as &lt;input&gt;,
 * &lt;a&gt;,&lt;select&gt; etc. These components inherit from this interface.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatHTMLElementComponent extends ItsNatElementComponent
{
    /**
     * Returns the associated DOM element to this component.
     *
     * @return the associated DOM element.
     */
    public HTMLElement getHTMLElement();

    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatHTMLElementComponentUI getItsNatHTMLElementComponentUI();

    /**
     * Returns the ItsNat HTML component manager parent of this component.
     *
     * @return the parent ItsNat HTML component manager.
     */
    public ItsNatHTMLComponentManager getItsNatHTMLComponentManager();
}
