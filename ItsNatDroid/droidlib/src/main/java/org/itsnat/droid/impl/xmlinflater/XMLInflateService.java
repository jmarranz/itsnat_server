package org.itsnat.droid.impl.xmlinflater;

import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.XMLParsedCache;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 25/06/14.
 */
public class XMLInflateService
{
    protected ItsNatDroidImpl parent;
    private int sNextGeneratedId = 1; // No usamos AtomicInteger porque no lo usaremos en multihilo
    protected Map<String,Integer> newIdMap = new HashMap<String,Integer>();
    protected ClassDescViewMgr classDescViewMgr = new ClassDescViewMgr(this);
    protected XMLParsedCache<LayoutParsed> layoutParsedCache = new XMLParsedCache<LayoutParsed>();

    public XMLInflateService(ItsNatDroidImpl parent)
    {
        this.parent = parent;
    }

    public ClassDescViewMgr getClassDescViewMgr()
    {
        return classDescViewMgr;
    }

    public XMLParsedCache<LayoutParsed> getLayoutParsedCache()
    {
        return layoutParsedCache;
    }

    public int generateViewId()
    {
        // Inspirado en el código fuente de Android View.generateViewId()
        final int result = sNextGeneratedId;
        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
        int newValue = result + 1;
        if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
        // No usamos compareAndSet porque no se debe usar en multihilo
        this.sNextGeneratedId = newValue;
        return result;
    }

    public int findIdAddIfNecessary(String name)
    {
        int id = findId(name);
        if (id == 0)
            id = addNewId(name);
        return id;
    }

    public int findId(String name)
    {
        Integer res = newIdMap.get(name);
        if (res == null)
            return 0; // No existe
        return res;
    }

    private int addNewId(String name)
    {
        int newId = generateViewId();
        newIdMap.put(name,newId);
        return newId;
    }
}

