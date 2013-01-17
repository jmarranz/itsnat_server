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

import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.ItsNatHTMLFormComponent;
import org.w3c.dom.html.HTMLLabelElement;

/**
 * Is the interface of HTML &lt;label&gt; based components.
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * <p>This component may be useful to forms with non-fixed labels (e.g. internationalized forms).</p>
 *
 * <p>By default the component has the default renderer but no editor.</p>
 *
 * <p>In place edition is not recommended with this kind of component because when a &lt;label&gt;
 * is clicked the default behavior (specified by HTML) is to click the associated form control.</p>
 * 
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatHTMLComponentManager#createItsNatHTMLLabel(org.w3c.dom.html.HTMLLabelElement,org.itsnat.core.NameValue[])
 */
public interface ItsNatHTMLLabel extends ItsNatHTMLElementComponent,ItsNatLabel
{
    /**
     * Returns the associated DOM element to this component.
     *
     * @return the associated DOM element.
     */
    public HTMLLabelElement getHTMLLabelElement();

    /**
     * Returns the form based component pointed to by this label with the
     * "for" attribute.
     *
     * @return the form based component. Null if no "for" attribute is defined,
     *          the element id is invalid or no component is registered associated to that element.
     */
    public ItsNatHTMLFormComponent getForComponent();
}
