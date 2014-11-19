package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.ViewGroup;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.dom.AttrParsedRemote;
import org.itsnat.droid.impl.dom.drawable.DrawableParsed;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawablePage;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.Dimension;

import java.util.Map;

/**
 * Created by jmarranz on 4/11/14.
 */
public abstract class AttrDesc<TclassDesc extends ClassDesc>
{
    protected String name;
    protected TclassDesc classDesc;

    public AttrDesc(TclassDesc classDesc,String name)
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

    protected static boolean isResource(String attrValue)
    {
        // No hace falta hacer un trim, un espacio al ppio invalida el atributo
        return attrValue.startsWith("@") || attrValue.startsWith("?");
    }

    protected XMLInflateRegistry getXMLInflateRegistry()
    {
        return classDesc.getXMLInflateRegistry();
    }

    public int getIdentifierAddIfNecessary(String value, Context ctx)
    {
        return getXMLInflateRegistry().getIdentifierAddIfNecessary(value,ctx);
    }
    
    public int getIdentifier(String attrValue, Context ctx)
    {
        return getXMLInflateRegistry().getIdentifier(attrValue,ctx);
    }

    public int getInteger(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getInteger(resId);
        }
        else
        {
            if (attrValue.startsWith("0x"))
            {
                attrValue = attrValue.substring(2);
                return Integer.parseInt(attrValue, 16);
            }
            return Integer.parseInt(attrValue);
        }
    }

    public float getFloat(String attrValue, Context ctx)
    {
        // Ojo, para valores sin sufijo de dimensión (por ej layout_weight o alpha)
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getDimension(resId); // No hay getFloat
        }
        else return Float.parseFloat(attrValue);
    }

    public String getString(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getString(resId);
        }
        else return attrValue;
    }

    public CharSequence getText(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getText(resId);
        }
        else return attrValue;
    }

    public CharSequence[] getTextArray(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getTextArray(resId);
        }
        else return null;
    }

    public boolean getBoolean(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getBoolean(resId);
        }
        else return Boolean.parseBoolean(attrValue);
    }

    private static int getDimensionSuffixAsInt(String suffix)
    {
        if (suffix.equals("dp") || suffix.equals("dip"))
            return TypedValue.COMPLEX_UNIT_DIP;
        else if (suffix.equals("px"))
            return TypedValue.COMPLEX_UNIT_PX;
        else if (suffix.equals("sp"))
            return TypedValue.COMPLEX_UNIT_SP;
        else if (suffix.equals("in"))
            return TypedValue.COMPLEX_UNIT_IN;
        else if (suffix.equals("mm"))
            return TypedValue.COMPLEX_UNIT_MM;
        else
            throw new ItsNatDroidException("Internal error");
    }

    private static String getDimensionSuffix(String value)
    {
        String valueTrim = value.trim();

        if (valueTrim.endsWith("dp"))
            return "dp";
        if (valueTrim.endsWith("dip")) // Concesión al pasado
            return "dip";
        else if (valueTrim.endsWith("px"))
            return "px";
        else if (valueTrim.endsWith("sp"))
            return "sp";
        else if (valueTrim.endsWith("in"))
            return "in";
        else if (valueTrim.endsWith("mm"))
            return "mm";
        else throw new ItsNatDroidException("ERROR unrecognized dimension: " + valueTrim);
    }

    private static float extractFloat(String value, String suffix)
    {
        int pos = value.lastIndexOf(suffix);
        value = value.substring(0,pos);
        return Float.parseFloat(value);
    }

    public Dimension getDimensionObject(String attrValue, Context ctx)
    {
        // El retorno es en px
        Resources res = ctx.getResources();
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            float num = res.getDimension(resId);
            return new Dimension(TypedValue.COMPLEX_UNIT_PX,num);
        }
        else
        {
            String valueTrim = attrValue.trim();
            String suffix = getDimensionSuffix(valueTrim);
            int complexUnit = getDimensionSuffixAsInt(suffix);
            float num = extractFloat(valueTrim, suffix);
            return new Dimension(complexUnit,num);
        }
    }

    public int getDimensionInt(String attrValue, Context ctx)
    {
        //return (int)getDimensionFloat(attrValue,ctx);
        return Math.round(getDimensionFloat(attrValue,ctx));
    }

    public float getDimensionFloat(String attrValue, Context ctx)
    {
        // El retorno es en px
        Resources res = ctx.getResources();

        Dimension dimen = getDimensionObject(attrValue,ctx);
        int unit = dimen.getComplexUnit();
        float num = dimen.getValue();
        switch(unit)
        {
            case TypedValue.COMPLEX_UNIT_DIP:
                return ValueUtil.dpToPixel(num, res);
            case TypedValue.COMPLEX_UNIT_PX:
                return num;
            case TypedValue.COMPLEX_UNIT_SP:
                return ValueUtil.spToPixel(num, res);
            case TypedValue.COMPLEX_UNIT_IN:
                return ValueUtil.inToPixel(num, res);
            case TypedValue.COMPLEX_UNIT_MM:
                return ValueUtil.mmToPixel(num, res);
        }

        throw new ItsNatDroidException("Cannot process " + attrValue); // POR AHORA hay que ver si faltan más casos
    }

    protected int getDimensionWithNameInt(String value,Context ctx)
    {
        int dimension;

        // No hace falta hacer trim en caso de "match_parent" etc un espacio fastidia el attr
        if      ("fill_parent".equals(value))  dimension = ViewGroup.LayoutParams.MATCH_PARENT;
        else if ("match_parent".equals(value)) dimension = ViewGroup.LayoutParams.MATCH_PARENT;
        else if ("wrap_content".equals(value)) dimension = ViewGroup.LayoutParams.WRAP_CONTENT;
        else
        {
            dimension = getDimensionInt(value, ctx);
        }
        return dimension;
    }

    public Drawable getDrawable(AttrParsed attr, Context ctx,PageImpl page)
    {
        if (attr instanceof AttrParsedRemote)
        {
            AttrParsedRemote attrRem = (AttrParsedRemote)attr;
            DrawableParsed drawableParsed = (DrawableParsed)attrRem.getRemoteResource();
            InflatedDrawable inflatedDrawable = new InflatedDrawablePage(page,(DrawableParsed)drawableParsed,ctx);
            XMLInflaterDrawable xmlInflater = XMLInflaterDrawable.createXMLInflaterDrawable(inflatedDrawable,ctx);
            return xmlInflater.inflateDrawable();
        }
        else
        {
            String attrValue = attr.getValue();
            if (isResource(attrValue))
            {
                int resId = getIdentifier(attrValue, ctx);
                if (resId <= 0) return null;
                return ctx.getResources().getDrawable(resId);
            }
            else if (attrValue.startsWith("#")) // Color literal. No hace falta hacer trim
            {
                int color = Color.parseColor(attrValue);
                return new ColorDrawable(color);
            }

            throw new ItsNatDroidException("Cannot process " + attrValue);
        }
    }

    public int getColor(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue,ctx);
            return ctx.getResources().getColor(resId);
        }
        else if (attrValue.startsWith("#")) // Color literal. No hace falta hacer trim
        {
            return Color.parseColor(attrValue);
        }

        throw new ItsNatDroidException("Cannot process " + attrValue);
    }

    protected static <T> T parseSingleName(String value, Map<String, T> valueMap)
    {
        T valueRes = valueMap.get(value);
        if (valueRes == null)
            throw new ItsNatDroidException("Unrecognized value name " + value + " for attribute");
        return valueRes;
    }

    protected static int parseMultipleName(String value, Map<String, Integer> valueMap)
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
