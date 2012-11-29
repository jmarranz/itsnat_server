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

package org.itsnat.core;

import java.io.Serializable;

/**
 * This class is provided to specify a generic pair name-value object, this value
 * may be used by the framework with several objectives.
 *
 * <p>They provide a generic mechanism to configure a component with no specific API.</p>
 *
 * <p>For instance to specify custom structures used in lists,
 * tables or trees ("artifacts").</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponent#getArtifact(String)
 * @see org.itsnat.core.ItsNatDocument#getArtifact(String)
 * @see org.itsnat.core.tmpl.ItsNatDocumentTemplate#getArtifact(String)
 * @see org.itsnat.core.ItsNatServletConfig#getArtifact(String)
 * @see org.itsnat.comp.ItsNatComponentManager#createItsNatComponent(Node,String,NameValue[])
 */
public class NameValue implements Serializable
{
    private String name;
    private Object value;

    /**
     * Creates a new instance with the specified name and value.
     *
     * @param name the name of the pair name/value.
     * @param value the value of the pair name/value.
     */
    public NameValue(String name,Object value)
    {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of the pair name/value.
     *
     * @return the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the value of the pair name/value.
     *
     * @return the value.
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * Changes the value of the pair name/value.
     *
     * @param value the new value.
     */
    public void setValue(Object value)
    {
        this.value = value;
    }

    /**
     * Returns the name of this object as string representation of the object.
     *
     * @return  a string representation of the object.
     */
    public String toString()
    {
        return getName();
    }
}
