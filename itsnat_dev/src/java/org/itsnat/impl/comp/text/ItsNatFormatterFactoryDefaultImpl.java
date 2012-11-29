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

package org.itsnat.impl.comp.text;

import java.io.Serializable;
import org.itsnat.comp.text.ItsNatFormattedTextField;
import org.itsnat.comp.text.ItsNatFormattedTextField.ItsNatFormatter;
import org.itsnat.comp.text.ItsNatFormatterFactoryDefault;

/**
 *
 * @author jmarranz
 */
public class ItsNatFormatterFactoryDefaultImpl implements ItsNatFormatterFactoryDefault,Serializable
{
    protected ItsNatFormatter defaultFormat;
    protected ItsNatFormatter displayFormat;
    protected ItsNatFormatter editFormat;
    protected ItsNatFormatter nullFormat;

    /**
     * Creates a new instance of ItsNatFormatterFactoryDefaultImpl
     */
    public ItsNatFormatterFactoryDefaultImpl()
    {
    }

    public void setDefaultFormatter(ItsNatFormatter atf)
    {
        defaultFormat = atf;
    }

    public ItsNatFormatter getDefaultFormatter()
    {
        return defaultFormat;
    }

    public void setDisplayFormatter(ItsNatFormatter atf)
    {
        displayFormat = atf;
    }

    public ItsNatFormatter getDisplayFormatter()
    {
        return displayFormat;
    }

    public void setEditFormatter(ItsNatFormatter atf)
    {
        editFormat = atf;
    }

    public ItsNatFormatter getEditFormatter()
    {
        return editFormat;
    }

    public void setNullFormatter(ItsNatFormatter atf)
    {
        nullFormat = atf;
    }

    public ItsNatFormatter getNullFormatter()
    {
        return nullFormat;
    }

    public ItsNatFormatter getItsNatFormatter(ItsNatFormattedTextField comp)
    {
        ItsNatFormatter formatter = null;

        if (comp == null)
            return null;

        Object value = comp.getValue();

        if (value == null)
            formatter = getNullFormatter();

        if (formatter == null)
        {
            if (comp.hasFocus())
                formatter = getEditFormatter();
            else
                formatter = getDisplayFormatter();

            if (formatter == null)
            {
                formatter = getDefaultFormatter();
            }
        }

        return formatter; // Puede ser null
    }

}
