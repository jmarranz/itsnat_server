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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.itsnat.core.ItsNatException;

/**
 *
 * @author jmarranz
 */
public class WeakSetImpl<K> implements Set<K>,Serializable
{
    protected transient WeakHashMap<K,Object> map;

    public WeakSetImpl()
    {
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        Map<K,Object> mapTmp = null;
        if (map != null)
            mapTmp = new HashMap<K,Object>(map);
        out.writeObject(mapTmp);

        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        @SuppressWarnings("unchecked")
        Map<K,Object> mapTmp = (Map<K,Object>)in.readObject();
        if (mapTmp != null)
            getWeakHashMap().putAll(mapTmp);

        in.defaultReadObject();
    }

    public WeakHashMap<K,Object> getWeakHashMap()
    {
        // Obviamente NO es multihilo
        if (map == null) this.map = new WeakHashMap<K,Object>();
        return map;
    }

    public int size()
    {
        if (map == null) return 0;
        return map.size();
    }

    public void clear()
    {
        if (map == null) return;
        map.clear();
    }

    public boolean isEmpty()
    {
        if (map == null) return true;
        return map.isEmpty();
    }

    public boolean add(K obj)
    {
        Object res = getWeakHashMap().put(obj,null);
        // Cambiamos el comportamiento, nos interesa detectar dobles inserciones
        if (res != null) throw new ItsNatException("INTERNAL ERROR");
        return true;
    }

    public boolean contains(Object obj)
    {
        if (map == null) return false;
        return map.containsKey(obj);
    }

    public boolean remove(Object obj)
    {
         if (map == null) return false;
         Object res = map.remove(obj);
         return res != obj; // Si res es null es que no estaba
    }

    public boolean addAll(Collection<? extends K> col)
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public boolean containsAll(Collection<?> col)
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public boolean removeAll(Collection<?> col)
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public boolean retainAll(Collection<?> col)
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public Iterator<K> iterator()
    {
        return getWeakHashMap().keySet().iterator();
    }

    public Object[] toArray()
    {
        return getWeakHashMap().keySet().toArray();
    }

    public <T> T[] toArray(T[] a)
    {
        return getWeakHashMap().keySet().toArray(a);
    }

}
