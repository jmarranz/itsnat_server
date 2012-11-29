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

package org.itsnat.impl.core.jsren.dom.node;

/**
 *
 * @author jmarranz
 */
public class PropertyImpl
{
    public final static int BOOLEAN = 1;
    public final static int STRING = 2;
    public final static int INTEGER = 3;
    public final static int FUNCTION = 4;

    protected JSRenderPropertyImpl render;
    protected String propName;
    protected String attrName;
    protected String attrNameLower;
    protected int type;
    protected String nullValue;

    public PropertyImpl(JSRenderPropertyImpl render,String propName,String attrName,int type,String nullValue)
    {
        this.render = render;
        this.propName = propName;
        this.attrName = attrName;
        this.attrNameLower = attrName.toLowerCase();
        this.type = type;
        this.nullValue = nullValue;
    }

    public JSRenderPropertyImpl getJSRenderProperty()
    {
        return render;
    }

    public String getPropertyName()
    {
        return propName;
    }

    public String getAttributeName()
    {
        return attrName;
    }

    public String getAttributeNameLower()
    {
        return attrNameLower;
    }

    public int getType()
    {
        return type;
    }

    public String getNullValue()
    {
        if (nullValue == null)
        {
            switch(type)
            {
                case BOOLEAN:
                     return "false";
                case STRING:
                     return "\"\"";
                case INTEGER:
                     return "-1";
                case FUNCTION:
                     return "null";
            }
        }

        return nullValue;
    }

}
