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


/**
 * Is the interface of text based components which text is rendered
 * as a single line using an HTML input element with type "hidden".
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * <p>An HTML input hidden element can not be edited by the user, but the
 * value may be set in any time calling {@link #setText(String)} or writing to the data model.
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatHTMLComponentManager#createItsNatHTMLInputHidden(org.w3c.dom.html.HTMLInputElement,org.itsnat.core.NameValue[])
 */
public interface ItsNatHTMLInputHidden extends ItsNatHTMLInputTextBased
{

}
