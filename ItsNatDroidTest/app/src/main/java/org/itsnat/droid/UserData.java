package org.itsnat.droid;

/**
 * Created by jmarranz on 6/07/14.
 */
public interface UserData
{
    public String[] getNames();
    public boolean containsName(String name);
    public Object get(String name);
    public Object set(String name, Object value);
    public Object remove(String name);
}
