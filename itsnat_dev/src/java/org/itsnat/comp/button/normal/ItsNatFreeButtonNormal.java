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

package org.itsnat.comp.button.normal;

import org.itsnat.comp.*;

/**
 * Is the interface of the free normal button components.
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * <p>ItsNat does not define a default decoration, button model listeners may be
 * used to decorate any button state. The mouse "click" decoration in normal buttons
 * is not very useful because is server based, only works well with very low net delays,
 * use instead JavaScript or CSS.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createItsNatFreeButtonNormal(org.w3c.dom.Element element,org.itsnat.core.NameValue[] artifacts)
 */
public interface ItsNatFreeButtonNormal extends ItsNatButtonNormal,ItsNatFreeComponent
{

}
