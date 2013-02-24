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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.itsnat.core.ItsNatException;

/**
 *
 * @author jmarranz
 */
public class MapUniqueId<T extends HasUniqueId> implements Serializable
{
    protected Map<String,T> map = new HashMap<String,T>();
    protected UniqueIdGenerator generator;

    /** Creates a new instance of MapUniqueId */
    public MapUniqueId(UniqueIdGenerator generator)
    {
        this.generator = generator;
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public int size()
    {
        return map.size();
    }

    public void clear()
    {
        map.clear();
    }

    public boolean containsKey(T obj)
    {
        check(obj);
        UniqueId idObj = obj.getUniqueId();
        String id = idObj.getId();
        return map.containsKey(id);
    }

    public void putAll(MapUniqueId<T> otherMap)
    {
        check(otherMap.generator);
        map.putAll(otherMap.map);
    }

    public Set<Map.Entry<String,T>> entrySet()
    {
        return map.entrySet();
    }

    public Collection<T> values()
    {
        return map.values();
    }

    public T get(String id)
    {
        return map.get(id);
    }

    public T put(T obj)
    {
        check(obj);
        UniqueId idObj = obj.getUniqueId();
        String id = idObj.getId();
        return map.put(id,obj);
    }

    public T removeById(String id)
    {
        return map.remove(id);
    }

    public T remove(T obj)
    {
        check(obj);
        UniqueId idObj = obj.getUniqueId();
        String id = idObj.getId();
        return map.remove(id);
    }

    public T[] toArray(T[] array)
    {
        if (array.length != size()) throw new ItsNatException("INTERNAL ERROR");
        int i = 0;
        for(Iterator<Map.Entry<String,T>> it = map.entrySet().iterator(); it.hasNext(); i++)
        {
            Map.Entry<String,T> entry = it.next();
            T value = entry.getValue();
            array[i] = value;
        }
        return array;
    }

    public void check(T idObj)
    {
        check(generator,idObj);
    }

    public void check(UniqueIdGenerator generator)
    {
        // La unicidad sólo está asegurada dentro de los ids generados
        // por el generador dado.
        // Evitamos que una colección tipo Map o Set metamos como clave
        // ids generados por diferentes generadores.
        if (this.generator != generator)
            throw new ItsNatException("INTERNAL ERROR");
    }

    public static void check(UniqueIdGenerator generator,HasUniqueId obj)
    {
        if (generator != obj.getUniqueId().getUniqueIdGenerator())
            throw new ItsNatException("INTERNAL ERROR");
    }
}
