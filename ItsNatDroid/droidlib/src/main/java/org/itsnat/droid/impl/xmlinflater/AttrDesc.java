package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMAttrRemote;
import org.itsnat.droid.impl.xmlinflater.layout.attr.Dimension;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 4/11/14.
 */
public abstract class AttrDesc<TclassDesc extends ClassDesc>
{
    protected String name;
    protected TclassDesc classDesc;

    public AttrDesc(TclassDesc classDesc, String name)
    {
        this.classDesc = classDesc;
        this.name = name;
    }

    public TclassDesc getClassDesc()
    {
        return classDesc;
    }

    public String getName()
    {
        return name;
    }



    protected XMLInflateRegistry getXMLInflateRegistry()
    {
        return classDesc.getXMLInflateRegistry();
    }

    protected void processDownloadTask(DOMAttrRemote attr, Runnable task, XMLInflater xmlInflater)
    {
        // Es el caso de inserción dinámica post page load via ItsNat de nuevos View con atributos que especifican recursos remotos
        // Hay que cargar primero los recursos y luego ejecutar la task que definirá el drawable
        downloadResources(attr, task, xmlInflater);
    }

    private static void downloadResources(DOMAttrRemote attr, Runnable task, XMLInflater xmlInflater)
    {
        PageImpl page = ClassDescViewBased.getPageImpl(xmlInflater); // NO puede ser nulo

        page.getItsNatDocImpl().downloadResources(attr, task);
    }

    public int getIdentifierAddIfNecessary(String value, Context ctx)
    {
        return getXMLInflateRegistry().getIdentifierAddIfNecessary(value, ctx);
    }

    public int getIdentifier(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getIdentifier(attrValue, ctx);
    }

    public int getInteger(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getInteger(attrValue, ctx);
    }

    public float getFloat(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getFloat(attrValue,ctx);
    }

    public String getString(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getString(attrValue,ctx);
    }

    public CharSequence getText(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getText(attrValue,ctx);
    }

    public CharSequence[] getTextArray(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getTextArray(attrValue,ctx);
    }

    public boolean getBoolean(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getBoolean(attrValue,ctx);
    }

    public Dimension getDimensionObject(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getDimensionObject(attrValue,ctx);
    }

    public int getDimensionInt(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getDimensionInt(attrValue, ctx);
    }

    public float getDimensionFloat(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getDimensionFloat(attrValue,ctx);
    }

    protected int getDimensionWithNameInt(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getDimensionWithNameInt(attrValue, ctx);
    }

    public Drawable getDrawable(DOMAttr attr, Context ctx,XMLInflater xmlInflater)
    {
        return getXMLInflateRegistry().getDrawable(attr,ctx,xmlInflater);
    }

    public int getColor(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getColor(attrValue,ctx);
    }

    public static <T> T parseSingleName(String value, Map<String, T> valueMap)
    {
        T valueRes = valueMap.get(value);
        if (valueRes == null)
            throw new ItsNatDroidException("Unrecognized value name " + value + " for attribute");
        return valueRes;
    }

    public static int parseMultipleName(String value, Map<String, Integer> valueMap)
    {
        String[] names = value.split("\\|");
        int res = 0;
        for(int i = 0; i < names.length; i++)
        {
            // No hace falta hacer trim, los espacios dan error
            String name = names[i];
            Integer valueInt = valueMap.get(name);
            if (valueInt == null)
                throw new ItsNatDroidException("Unrecognized value name " + name + " for attribute");

            res |= valueInt;
        }

        return res;
    }

}
