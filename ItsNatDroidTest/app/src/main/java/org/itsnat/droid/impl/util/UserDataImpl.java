package org.itsnat.droid.impl.util;

import org.itsnat.droid.UserData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 6/07/14.
 */
public class UserDataImpl implements UserData
{
    protected Map<String,Object> userData;

    /**
     * Creates a new instance of UserDataMonoThreadImpl
     */
    public UserDataImpl()
    {
    }

    public Map<String,Object> getInternalMap()
    {
        if (userData == null) userData = new HashMap<String,Object>();
        return userData;
    }

    public String[] getNames()
    {
        Map<String,Object> userData = getInternalMap();
        String[] names = new String[userData.size()];
        return userData.keySet().toArray(names);
    }

    public boolean containsName(String name)
    {
        Map<String,Object> userData = getInternalMap();
        return userData.containsKey(name);
    }

    public Object get(String name)
    {
        Map<String,Object> userData = getInternalMap();
        return userData.get(name);
    }

    public Object set(String name, Object value)
    {
        Map<String,Object> userData = getInternalMap();
        return userData.put(name,value);
    }

    public Object remove(String name)
    {
        Map<String,Object> userData = getInternalMap();
        return userData.remove(name);
    }

}
