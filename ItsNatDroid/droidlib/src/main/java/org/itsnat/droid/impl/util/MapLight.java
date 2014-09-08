package org.itsnat.droid.impl.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * El motivo de esta clase es evitar un Map cuando tenemos la previsión de que habrá apenas dos o tres elementos de clave,
 * un LinkedList o un ArrayList, es mucho más ligero, ahora bien NO MÁS RÁPIDO
 * Tiene mucho mejor rendimiento un ArrayList al recorrerlo por índice (método get(Key)) que un LinkedList en donde hay que crear un Iterator
 * http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/util/HashMap.java
 *
 * Created by jmarranz on 4/07/14.
 */
public class MapLight<Key,Value>
{
    protected ArrayList<Map.Entry<Key,Value>> list = new ArrayList<Map.Entry<Key,Value>>(5);

    public MapLight()
    {
    }

    public Value get(Key key)
    {
        for(int i = 0; i < list.size(); i++)
        {
            Map.Entry<Key, Value> entry = list.get(i);
            if (entry.getKey().equals(key)) return entry.getValue();
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
        for(int i = 0; i < list.size(); i++)
        {
            Map.Entry<Key, Value> entry = list.get(i);
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
