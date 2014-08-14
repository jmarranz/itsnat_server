package org.itsnat.droid.impl.util;

import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public interface MapList<Key,Value>
{
    public List<Value> get(Key key);

    public void add(Key key,Value value);

    public boolean remove(Key key,Value value);

}
