/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.util;

import java.util.Map;

/**
 *
 * @author jmarranz
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
    
    public Key getKey()
    {
        return key;
    }

    public Value getValue()
    {
        return value;
    }

    public Value setValue(Value value)
    {
        Value old = this.value;
        this.value = value;
        return old;
    }  
    
}
