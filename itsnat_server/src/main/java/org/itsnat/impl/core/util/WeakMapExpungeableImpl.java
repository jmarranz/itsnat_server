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
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.itsnat.core.ItsNatException;

/**
 * http://www.javaworld.com/javaworld/jw-01-2002/jw-0104-java101.html?page=3
 *
 * @author jmarranz
 */
public class WeakMapExpungeableImpl implements Serializable
{
    protected transient WeakHashMap<Object,KeyValuePair> weakMap = new WeakHashMap<Object,KeyValuePair>();
    protected transient HashMap<WeakReference<Object>,KeyValuePair> weakRefMap = new HashMap<WeakReference<Object>,KeyValuePair>(); // Esta colección nos sirve para obtener el objeto valor y hacer algún tipo de cleaning
    protected transient ReferenceQueue<Object> queue = new ReferenceQueue<Object>();
    protected ExpungeListener listener;

    /** Creates a new instance of NodeCache */
    public WeakMapExpungeableImpl(ExpungeListener listener)
    {
        this.listener = listener;
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        Map<Object,Object> mapTmp = new HashMap<Object,Object>();
        for(Map.Entry<WeakReference<Object>,KeyValuePair> entry : weakRefMap.entrySet())
        {
            WeakReference<Object> weakRef = entry.getKey();
            KeyValuePair pair = entry.getValue();
            Object key = weakRef.get();
            Object value = pair.getValue();
            mapTmp.put(key, value);
        }
        out.writeObject(mapTmp);

        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        this.queue = new ReferenceQueue<Object>();
        this.weakMap = new WeakHashMap<Object,KeyValuePair>();
        this.weakRefMap = new HashMap<WeakReference<Object>,KeyValuePair>();

        @SuppressWarnings("unchecked")
        Map<Object,Object> mapTmp = (Map<Object,Object>)in.readObject();
        for(Map.Entry<Object,Object> entry : mapTmp.entrySet())
        {
            Object key = entry.getKey();
            Object value = entry.getValue();

            WeakReference<Object> weakRef = new WeakReference<Object>(key,queue);
            KeyValuePair pair = new KeyValuePair(weakRef,value);

            weakMap.put(key,pair);
            weakRefMap.put(weakRef,pair);
        }

        in.defaultReadObject();
    }

    public Object remove(Object key)
    {
        cleanUnused();

        KeyValuePair pair = weakMap.remove(key);
        if (pair == null)
            return null;
        WeakReference<Object> weakRef = pair.getKey();
        weakRefMap.remove(weakRef);
        return pair.getValue();
    }

    public Object get(Object key)
    {
        cleanUnused();

        KeyValuePair pair = weakMap.get(key);
        if (pair == null)
            return null;

        return pair.getValue();
    }

    public Object put(Object key,Object value)
    {
        cleanUnused();

        WeakReference<Object> weakKeyRef = new WeakReference<Object>(key,queue);
        KeyValuePair pair = new KeyValuePair(weakKeyRef,value); // Notar que value se sujeta con una referencia fuerte

        KeyValuePair oldPair = weakMap.put(key,pair);
        weakRefMap.put(weakKeyRef,pair);

        if (oldPair == null)
            return null;

        return oldPair.getValue();
    }

    public void cleanUnused()
    {
        // System.gc(); // ralentiza muchísimo y no es imprescindible

        Reference weakRef = queue.poll();
        while(weakRef != null)
        {
            KeyValuePair pair = weakRefMap.remove(weakRef);
            if ((pair != null) && (listener != null))
                listener.processExpunged(pair.getValue());

            weakRef = queue.poll();
        }
    }

    public boolean isEmpty()
    {
        cleanUnused();

        boolean res = weakMap.isEmpty();
        if (res != weakRefMap.isEmpty())
            throw new ItsNatException("INTERNAL ERROR");
        return res;
    }

    public Set<Map.Entry<Object,KeyValuePair>> entrySet()
    {
        return weakMap.entrySet();
    }

    public static class KeyValuePair implements Serializable
    {
        protected transient WeakReference<Object> key;
        protected Object value;

        public KeyValuePair(WeakReference<Object> key,Object value)
        {
            this.key = key;
            this.value = value;
        }

        private void writeObject(ObjectOutputStream out) throws IOException
        {
            Object obj = null;
            if (key != null) obj = key.get();

            out.writeObject(obj);            

            out.defaultWriteObject();
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
        {
            Object obj = in.readObject();
            if (obj != null) this.key = new WeakReference<Object>(obj);

            in.defaultReadObject();
        }

        public WeakReference<Object> getKey()
        {
            return key;
        }

        public Object getValue()
        {
            return value;
        }
    }

    public interface ExpungeListener
    {
        public void processExpunged(Object value);
    }

/*
    public static void main(String[] args) throws Exception
    {
        ExpungeListener listener = new ExpungeListener()
        {
            public int counter = 0;
            public void processExpunged(Object value)
            {
                counter++;
                System.out.println(value + " " + counter);
            }
        };

        WeakMapExpungeableImpl map = new WeakMapExpungeableImpl(listener);
        for(int i = 0; i < 60000; i++)
        {
            map.put(new Integer(i),new String(Integer.toString(i)));
        }

        map.cleanUnused();
    }
*/
}
