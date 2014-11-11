package org.itsnat.droid.impl.xmlinflater;

import org.itsnat.droid.ItsNatDroidException;

import java.util.HashMap;

/**
 * Created by jmarranz on 6/11/14.
 */
public abstract class ClassDescMgr<TclassDesc extends ClassDesc,Tnative>
{
    protected XMLInflateRegistry parent;
    private final HashMap<String, TclassDesc> classes = new HashMap<String, TclassDesc>();

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

    public Class<Tnative> resolveClass(String className) throws ClassNotFoundException
    {
        return (Class<Tnative>)Class.forName(className);
    }

    public TclassDesc get(String viewName)
    {
        Class<Tnative> nativeClass = null;
        try { nativeClass = resolveClass(viewName); }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
        TclassDesc classDesc = get(nativeClass);
        return classDesc;
    }

    public TclassDesc get(Class<Tnative> nativeClass)
    {
        TclassDesc classDesc = classes.get(nativeClass.getName());
        if (classDesc == null) classDesc = registerUnknown(nativeClass);
        return classDesc; // Nunca es nulo
    }

    public TclassDesc get(Tnative nativeObj)
    {
        Class<Tnative> nativeClass = (Class<Tnative>)nativeObj.getClass();
        return get(nativeClass);
    }

    public TclassDesc registerUnknown(Class<Tnative> nativeClass)
    {
        String className = nativeClass.getName();
        // Tenemos que obtener los ClassDescViewBase de las clases base para que podamos saber lo más posible
        Class<?> superClass = (Class<?>)nativeClass.getSuperclass();
        TclassDesc parentClassDesc = get((Class<Tnative>)superClass); // Si fuera también unknown se llamará recursivamente de nuevo a este método
        TclassDesc classDesc = createClassDescUnknown(className, parentClassDesc);

        classes.put(nativeClass.getName(), classDesc);

        return classDesc;
    }

    protected abstract void initClassDesc();
    protected abstract TclassDesc createClassDescUnknown(String className, TclassDesc parentClass);
}
