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

package org.itsnat.impl.comp.list;

import javax.swing.ListModel;

/**
 *
 * @author jmarranz
 */
public class ListDataModelUtil
{

    /** Creates a new instance of ListDataModelUtil */
    public ListDataModelUtil()
    {
    }

    public static boolean contains(Object obj,ListModel model)
    {
        return (indexOf(obj,model) >= 0);
    }

    public static int indexOf(Object obj,ListModel model)
    {
        return indexOf(obj,0,model);
    }

    public static int indexOf(Object obj,int index,ListModel model)
    {
        if (obj == null) return -1;

        int size = model.getSize();
        for(int i = index; i < size; i++)
        {
            Object currObj = model.getElementAt(i);
            if (currObj.equals(obj))
                return i;
        }
        return -1;
    }

    public static int lastIndexOf(Object obj,ListModel model)
    {
        return lastIndexOf(obj,model.getSize() - 1,model);
    }

    public static int lastIndexOf(Object obj,int index,ListModel model)
    {
        if (obj == null) return -1;

        for(int i = index; i >= 0; i--)
        {
            Object currObj = model.getElementAt(i);
            if (currObj.equals(obj))
                return i;
        }
        return -1;
    }
}
