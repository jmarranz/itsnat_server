package org.itsnat.droid.impl.util;

import java.util.Map;

/**
 * Created by jmarranz on 14/08/14.
 */

public class MapEntryImpl<Key,Value> implements Map.Entry<Key,Value>
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