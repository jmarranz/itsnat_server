package org.itsnat.droid.impl.xmlinflater;

import org.itsnat.droid.ItsNatDroidException;

import java.util.HashMap;

/**
 * Created by jmarranz on 4/11/14.
 */
public abstract class ClassDesc<Tnative>
{
    protected ClassDescMgr classMgr;
    protected String className;
    protected ClassDesc parentClass;
    protected boolean initiated;
    protected HashMap<String,AttrDesc> attrDescMap;

    public ClassDesc(ClassDescMgr classMgr,String className,ClassDesc parentClass)
    {
        this.classMgr = classMgr;
        this.className = className;
        this.parentClass = parentClass;
    }

    public XMLInflateRegistry getXMLInflateRegistry()
    {
        return classMgr.getXMLInflateRegistry();
    }

    public ClassDescMgr getClassDescMgr()
    {
        return classMgr;
    }

    public ClassDesc getParentClassDesc()
    {
        return parentClass;
    }

    public String getClassName()
    {
        return className;
    }



    protected boolean isInit()
    {
        return initiated;
    }

    protected void init()
    {
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
