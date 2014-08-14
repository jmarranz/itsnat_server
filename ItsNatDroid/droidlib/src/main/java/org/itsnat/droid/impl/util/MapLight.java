package org.itsnat.droid.impl.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * El motivo de esta clase es evitar un Map cuando tenemos la previsión de que habrá apenas dos o tres elementos de clave, un LinkedList
 * es mucho más ligero.
 * http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/util/HashMap.java
 *
 * Created by jmarranz on 4/07/14.
 */
public class MapLight<Key,Value>
{
    protected List<Map.Entry<Key,Value>> list = new LinkedList<Map.Entry<Key,Value>>();

    public MapLight()
    {
    }

    public Value get(Key key)
    {
        for(Iterator<Map.Entry<Key,Value>> it = list.iterator(); it.hasNext(); )
        {
            Map.Entry<Key,Value> entry = it.next();
            if (entry.getKey().equals(key))
                return entry.getValue();
        }

        return null;
    }

    public Value remove(Key key)
    {
        for(Iterator<Map.Entry<Key,Value>> it = list.iterator(); it.hasNext(); )
        {
            Map.Entry<Key,Value> entry = it.next();
            if (entry.getKey().equals(key))
            {
                it.remove();
                return entry.getValue();
            }
        }

        return null;
    }

    public Value put(Key key,Value value)
    {
        for(Iterator<Map.Entry<Key,Value>> it = list.iterator(); it.hasNext(); )
        {
            Map.Entry<Key,Value> entry = it.next();
            if (entry.getKey().equals(key))
            {
                Value old = entry.getValue();
                entry.setValue(value);
                return old;
            }
        }
        // Es nueva la key
        MapEntryImpl<Key,Value> entry = new MapEntryImpl<Key,Value>(key,value);
        list.add(entry);
        return null;
    }

    public List<Map.Entry<Key,Value>> getEntryList()
    {
        return list;
    }

}
