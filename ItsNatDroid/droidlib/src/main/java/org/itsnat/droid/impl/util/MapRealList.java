package org.itsnat.droid.impl.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 4/07/14.
 */
public class MapRealList<Key,Value> implements MapList<Key,Value>
{
    protected Map<Key,List<Value>> map = new HashMap<Key,List<Value>>();

    public MapRealList()
    {
    }

    public List<Value> get(Key key)
    {
        return map.get(key);
    }

    public void add(Key key,Value value)
    {
        List<Value> list = map.get(key);
        if (list == null)
        {
            list = new LinkedList<Value>();
            map.put(key,list);
        }
        list.add(value);
    }

    public boolean remove(Key key,Value value)
    {
        List<Value> valueList = map.get(key);
        if (valueList == null) return false;
        for(Iterator<Value> it = valueList.iterator(); it.hasNext(); )
        {
            Value currValue = it.next();
            if (ValueUtil.equalsNullAllowed(value, currValue))
            {
                it.remove();
                if (valueList.isEmpty()) map.remove(key);
                return true;
            }
        }
        return false;
    }
}
