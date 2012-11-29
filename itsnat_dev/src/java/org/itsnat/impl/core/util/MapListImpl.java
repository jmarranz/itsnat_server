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

package org.itsnat.impl.core.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * @author jmarranz
 */
public class MapListImpl implements Serializable
{
    public Map map = new HashMap();

    /**
     * Creates a new instance of EventTargetListenerList
     */
    public MapListImpl()
    {
    }

    public Map getMap()
    {
        return map;
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public int add(Object key,Object value)
    {
        LinkedList list = (LinkedList)map.get(key);
        if (list == null)
        {
            list = new LinkedList();
            map.put(key,list);
        }
        list.add(value);
        return list.size() - 1;
    }

    public LinkedList get(Object key)
    {
        return (LinkedList)map.get(key);
    }

    public LinkedList remove(Object key)
    {
        return (LinkedList)map.remove(key);
    }
    
    public LinkedList getAllValuesCopy()
    {
        if (isEmpty()) return null;

        LinkedList res = new LinkedList();
        for(Iterator it = map.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)it.next();
            LinkedList list = (LinkedList)entry.getValue(); // No puede ser null
            res.addAll(list);
        }
        return res;
    }

    public boolean contains(Object key,Object value)
    {
        LinkedList list = (LinkedList)map.get(key);
        if (list == null)
            return false;
        for(Iterator it = list.iterator(); it.hasNext(); )
        {
            Object currValue = it.next();
            if ((currValue == value) ||
                ((value != null) && value.equals(currValue)))
                return true;
        }
        return false;
    }

    public Object remove(Object key,Object value)
    {
        LinkedList list = (LinkedList)map.get(key);
        if (list == null)
            return null;
        for(ListIterator it = list.listIterator(); it.hasNext(); )
        {
            Object currValue = it.next();
            if ((currValue == value) ||
                ((value != null) && value.equals(currValue)))
            {
                it.remove();
                if (list.isEmpty())
                    map.remove(key);
                return currValue;
            }
        }
        return null;
    }

    public void clear()
    {
        map.clear();
    }
}
