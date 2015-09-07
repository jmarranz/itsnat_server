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
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * @author jmarranz
 */
public class MapListImpl<K,V> implements Serializable
{
    public Map<K,LinkedList<V>> map = new HashMap<K,LinkedList<V>>();

    /**
     * Creates a new instance of MapListImpl
     */
    public MapListImpl()
    {
    }

    public Map<K,LinkedList<V>> getMap()
    {
        return map;
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public int add(K key,V value)
    {
        LinkedList<V> list = map.get(key);
        if (list == null)
        {
            list = new LinkedList<V>();
            map.put(key,list);
        }
        list.add(value);
        return list.size() - 1;
    }

    public LinkedList<V> get(K key)
    {
        return map.get(key);
    }

    public LinkedList<V> remove(K key)
    {
        return map.remove(key);
    }
    
    public LinkedList<V> getAllValuesCopy()
    {
        if (isEmpty()) return null;

        LinkedList<V> res = new LinkedList<V>();
        for(Map.Entry<K,LinkedList<V>> entry : map.entrySet())
        {
            LinkedList<V> list = entry.getValue(); // No puede ser null
            res.addAll(list);
        }
        return res;
    }

    public boolean contains(K key,V value)
    {
        LinkedList<V> list = map.get(key);
        if (list == null)
            return false;
        for(V currValue : list)
        {
            if ((currValue == value) ||
                ((value != null) && value.equals(currValue)))
                return true;
        }
        return false;
    }

    public V remove(K key,V value)
    {
        LinkedList<V> list = map.get(key);
        if (list == null)
            return null;
        for(ListIterator<V> it = list.listIterator(); it.hasNext(); )
        {
            V currValue = it.next();
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
