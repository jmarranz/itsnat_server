package org.itsnat.droid.impl.util;

import java.util.HashMap;
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
public class MapLightList<Key,Value> implements MapList<Key,Value>
{
    protected List<Map.Entry<Key,List<Value>>> map = new LinkedList<Map.Entry<Key,List<Value>>>();

    public MapLightList()
    {
    }

    public List<Value> get(Key key)
    {
        for(Iterator<Map.Entry<Key,List<Value>>> it = map.iterator(); it.hasNext(); )
        {
            Map.Entry<Key,List<Value>> entry = it.next();
            if (entry.getKey().equals(key))
                return entry.getValue();
        }

        return null;
    }

    private List<Value> remove(Key key)
    {
        for(Iterator<Map.Entry<Key,List<Value>>> it = map.iterator(); it.hasNext(); )
        {
            Map.Entry<Key,List<Value>> entry = it.next();
            if (entry.getKey().equals(key))
            {
                it.remove();
                return entry.getValue();
            }
        }

        return null;
    }

    public void add(Key key,Value value)
    {
        List<Value> list = get(key);
        if (list == null)
        {
            list = new LinkedList<Value>();
            MapEntryImpl<Key,List<Value>> entry = new MapEntryImpl<Key,List<Value>>(key,list);
            map.add(entry);
        }
        list.add(value);
    }

    public boolean remove(Key key,Value value)
    {
        List<Value> valueList = get(key);
        if (valueList == null) return false;
        for(Iterator<Value> it = valueList.iterator(); it.hasNext(); )
        {
            Value currValue = it.next();
            if (value.equals(currValue))
            {
                it.remove();
                if (valueList.isEmpty()) remove(key);
                return true;
            }
        }
        return false;
    }

    public static class MapEntryImpl<Key,Value> implements Map.Entry<Key,Value>
    {
        protected Key key;
        protected Value value;

        public MapEntryImpl(Key key, Value value)
        {
            this.key = key;
            this.value = value;
        }

        @Override
        public Key getKey()
        {
            return key;
        }

        @Override
        public Value getValue()
        {
            return value;
        }

        @Override
        public Value setValue(Value value)
        {
            return value;
        }
    }
}
