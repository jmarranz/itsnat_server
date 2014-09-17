package org.itsnat.droid.impl.xmlinflater.attr;

/**
 * Created by jmarranz on 17/09/14.
 */
public class Dimension
{
    private int complexUnit;
    private float value;

    public Dimension(int complexUnit,float value)
    {
        this.complexUnit = complexUnit;
        this.value = value;
    }

    public int getComplexUnit()
    {
        return complexUnit;
    }

    public float getValue()
    {
        return value;
    }
}