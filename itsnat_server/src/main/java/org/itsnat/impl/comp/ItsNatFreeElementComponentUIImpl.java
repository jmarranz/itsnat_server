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

package org.itsnat.impl.comp;

/**
 * NO hacer pública via interface pues no sirve para nada salvo para contener el "enable"
 *
 * @author jmarranz
 */
public class ItsNatFreeElementComponentUIImpl extends ItsNatElementComponentUIImpl
{
    /**
     * Creates a new instance of ItsNatFreeElementComponentUIImpl
     */
    public ItsNatFreeElementComponentUIImpl(ItsNatElementComponentImpl parentComp)
    {
        super(parentComp);
    }

}
