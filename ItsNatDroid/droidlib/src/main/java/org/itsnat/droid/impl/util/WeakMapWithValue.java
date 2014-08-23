package org.itsnat.droid.impl.util;

import org.itsnat.droid.ItsNatDroidException;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by jmarranz on 16/06/14.
 */
public class WeakMapWithValue<Key,Value>
{
    protected WeakHashMap<Value,Key> mapByValue = new WeakHashMap<Value,Key>();
    protected HashMap<Key,WeakReference<Value>> mapByKey = new HashMap<Key,WeakReference<Value>>(); // Esta colección nos sirve para obtener el objeto valor y hacer algún tipo de cleaning

    /** Creates a new instance of NodeCache */
    public WeakMapWithValue()
    {
    }

    public Value removeByKey(Key key)
    {
        cleanUnused();

        WeakReference<Value> weakValue = mapByKey.remove(key);
        if (weakValue == null)
            return null;
        Value value = weakValue.get();
        if (value == null)
            return null;
        mapByValue.remove(value);
        return value;
    }

    public Key removeByValue(Value value)
    {
        cleanUnused();

        Key key = mapByValue.remove(value);
        mapByKey.remove(key);
        return key;
    }

    public Value getValueByKey(Key key)
    {
        cleanUnused();

        WeakReference<Value> weakValue = mapByKey.get(key);
        if (weakValue == null)
            return null;

        return weakValue.get(); // Puede ser null, significa que se ha perdido el Value, en el siguiente cleanUnused se normalizará
    }

    public Key getKeyByValue(Value value)
    {
        cleanUnused();

        return mapByValue.get(value);
    }

    public Value put(Key key,Value value)
    {
        if (value == null)
            throw new ItsNatDroidException("null value is not allowed"); // Usar removeByKey
        cleanUnused();

        mapByValue.put(value,key);
        WeakReference<Value> oldWeakValue = mapByKey.put(key,new WeakReference<Value>(value));
        if (oldWeakValue == null)
            return null;
        return oldWeakValue.get();
    }

    public boolean isEmpty()
    {
        cleanUnused();

        boolean res = mapByKey.isEmpty();
        if (res != mapByValue.isEmpty())
            throw new ItsNatDroidException("INTERNAL ERROR");
        return res;
    }

    public void cleanUnused()
    {
        // No es muy eficiente pero jamás llegaremos ni a 10 ids por cada Layout.
        // mapByValue dismuinirá en elementos cuando se pierda un View, mapByKey no se enterará si no lo eliminamos explícitamente
        if (!mapByValue.isEmpty() && mapByValue.size() < mapByKey.size()) // mapByValue y mapByKey
        {
            for (Iterator<Map.Entry<Key, WeakReference<Value>>> it = mapByKey.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<Key, WeakReference<Value>> entry = it.next();
                WeakReference<Value> weakRef = entry.getValue();
                Value value = weakRef.get();
                if (!mapByValue.containsKey(value))
                    it.remove();
            }
        }
        // Despues aun así no hay 100% garantía de que los tamaños sean iguales, una colección puede disminuir en cualquier momento
        // a capricho del GC
        //if (mapByKey.size() != mapByValue.size()) throw new ItsNatDroidException("Internal Error");
    }

    public static void test(String[] args) //throws Exception
    {
        WeakMapWithValue map = new WeakMapWithValue();
        for(int i = 0; i < 1000; i++)
        {
            map.put(new Integer(i),new String(Integer.toString(i)));
        }

        System.gc();
        map.cleanUnused();

    }

}
