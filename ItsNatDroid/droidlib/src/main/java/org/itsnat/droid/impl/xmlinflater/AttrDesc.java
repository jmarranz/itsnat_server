package org.itsnat.droid.impl.xmlinflater;

/**
 * Created by jmarranz on 4/11/14.
 */
public abstract class AttrDesc
{
    protected String name;

    public AttrDesc(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
