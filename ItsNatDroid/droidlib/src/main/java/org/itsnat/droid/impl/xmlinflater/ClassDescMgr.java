package org.itsnat.droid.impl.xmlinflater;

import org.itsnat.droid.ItsNatDroidException;

import java.util.HashMap;

/**
 * Created by jmarranz on 6/11/14.
 */
public abstract class ClassDescMgr<TclassDesc extends ClassDesc>
{
    protected XMLInflateRegistry parent;
    protected final HashMap<String, TclassDesc> classes = new HashMap<String, TclassDesc>();

    public ClassDescMgr(XMLInflateRegistry parent)
    {
        this.parent = parent;
    }

    public XMLInflateRegistry getXMLInflateRegistry()
    {
        return parent;
    }

    public void addClassDesc(TclassDesc classDesc)
    {
        ClassDesc old = classes.put(classDesc.getClassName(), classDesc);
        if (old != null) throw new ItsNatDroidException("Internal Error, duplicated: " + classDesc.getClassName());
    }

    protected abstract void initClassDesc();
}
