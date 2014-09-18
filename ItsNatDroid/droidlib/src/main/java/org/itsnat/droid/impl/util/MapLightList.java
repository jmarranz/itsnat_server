package org.itsnat.droid.impl.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * El motivo de esta clase es evitar un Map cuando tenemos la previsión de que habrá apenas dos o tres elementos de clave, un LinkedList
 * es mucho más ligero.
 * http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/util/HashMap.java
 *
 * Created by jmarranz on 4/07/14.
 */
public class MapLightList<Key,Value> implements MapList<Key,Value>
{
    protected MapLight<Key,List<Value>> map = new MapLight<Key,List<Value>>();

    public MapLightList()
    {
    }

    public List<Value> get(Key key)
    {
        return map.get(key);
    }

    private List<Value> remove(Key key)
    {
        return map.remove(key);
    }

    public void add(Key key,Value value)
    {
        List<Value> list = get(key);
        if (list == null)
        {
            list = new LinkedList<Value>();
            map.put(key,list);
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
            if (ValueUtil.equalsNullAllowed(value, currValue))
            {
                it.remove(); // Comprobado que el size() de la lista cambia tras la llamada
                if (valueList.isEmpty()) remove(key);
                return true;
            }
        }
        return false;
    }

}
