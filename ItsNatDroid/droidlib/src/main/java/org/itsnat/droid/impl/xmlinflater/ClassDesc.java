package org.itsnat.droid.impl.xmlinflater;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.util.MiscUtil;

import java.util.HashMap;

/**
 * Created by jmarranz on 4/11/14.
 */
public abstract class ClassDesc<T>
{
    protected String className;
    protected Class<T> clasz;
    protected ClassDesc parentClass;
    protected boolean initiated;
    protected HashMap<String,AttrDesc> attrDescMap;

    public ClassDesc(String className,ClassDesc parentClass)
    {
        this.className = className;
        this.parentClass = parentClass;
    }

    public ClassDesc getParentClassDesc()
    {
        return parentClass;
    }

    public String getClassName()
    {
        return className;
    }

    public Class<T> getDeclaredClass()
    {
        return clasz;
    }

    protected Class<T> initClass()
    {
        if (clasz == null) this.clasz = (Class<T>) MiscUtil.resolveClass(className);
        return clasz;
    }

    protected boolean isInit()
    {
        return initiated;
    }

    protected void init()
    {
        initClass();

        this.attrDescMap = new HashMap<String,AttrDesc>();
    }

    protected void addAttrDesc(AttrDesc attrDesc)
    {
        AttrDesc old = attrDescMap.put(attrDesc.getName(),attrDesc);
        if (old != null) throw new ItsNatDroidException("Internal Error, duplicated attribute in this class: " + attrDesc.getName());
    }

    protected AttrDesc getAttrDesc(String name)
    {
        return attrDescMap.get(name);
    }
}
