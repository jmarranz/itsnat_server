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
 * Is the interface of text based components which text is rendered/edited
 * as a single line using an HTML input element with type "file".
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * <p>This component is "passive", do not try to set a file path explicitly
 * calling {@link #setText(String)} or using the data model because for security reasons browsers reject or ignore
 * a initial value of an &lt;input&gt; file element or any attempt to set this value
 * using JavaScript (to avoid an unadvertised file upload), only the user
 * can set a file path. Anyway the file path introduced by the user is automatically
 * sent to the server and ready to read calling {@link #getText()} or reading the
 * data model.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatHTMLComponentManager#createItsNatHTMLInputFile(org.w3c.dom.html.HTMLInputElement,org.itsnat.core.NameValue[])
 */
public interface ItsNatHTMLInputFile extends ItsNatHTMLInputTextBased
{
}
