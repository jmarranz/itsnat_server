package org.itsnat.droid.impl.xmlinflater.attr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDesc
{
    protected String name;
    protected ClassDescViewBased parent;

    public AttrDesc(ClassDescViewBased parent,String name)
    {
        this.parent = parent;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    private static boolean isResource(String attrValue)
    {
        // No hace falta hacer un trim, un espacio al ppio invalida el atributo
        return attrValue.startsWith("@") || attrValue.startsWith("?");
    }

    public static int getIdentifier(String attrValue, Context ctx)
    {
        if (attrValue.startsWith("?"))
            return getIdentifierTheme(attrValue, ctx);
        else if (attrValue.startsWith("@"))
            return getIdentifierResource(attrValue, ctx);
        else
            throw new ItsNatDroidException("INTERNAL ERROR");
    }

    private static int getIdentifierTheme(String attrValue, Context ctx)
    {
        // http://stackoverflow.com/questions/12781501/android-setting-linearlayout-background-programmatically
        // Ej. android:textAppearance="?android:attr/textAppearanceMedium"
        TypedValue outValue = new TypedValue();
        ctx.getTheme().resolveAttribute(getIdentifierResource(attrValue, ctx), outValue, true);
        return outValue.resourceId;
    }

    private static int getIdentifierResource(String attrValue, Context ctx)
    {
        Resources res = ctx.getResources();

        attrValue = attrValue.substring(1); // Quitamos el @ o la ?
        int pos = attrValue.indexOf(':');
        String packageName;
        if (pos != -1) // Tiene package, ej android:
        {
            packageName = attrValue.substring(0, pos);
            attrValue = attrValue.substring(pos + 1);
        }
        else
            packageName = ctx.getPackageName();

        int id = res.getIdentifier(attrValue, null, packageName);
        if (id == 0) throw new ItsNatDroidException("Not found resource with id \"" + attrValue + "\" Package: \"" + packageName );
        return id;
    }

    public static int getInteger(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getInteger(resId);
        }
        else return Integer.parseInt(attrValue);
    }

    public static float getFloat(String attrValue, Context ctx)
    {
        // Ojo, para valores sin sufijo de dimensión (por ej layout_weight o alpha)
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getDimension(resId); // No hay getFloat
        }
        else return Float.parseFloat(attrValue);
    }

    public static String getString(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getString(resId);
        }
        else return attrValue;
    }

    public static CharSequence[] getTextArray(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getTextArray(resId);
        }
        else return null;
    }

    public static boolean getBoolean(String attrValue, Context ctx)
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
        if (suffix.equals("dp"))
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

    public static Dimension getDimensionObject(String attrValue, Context ctx)
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

    public static int getDimensionInt(String attrValue, Context ctx)
    {
        return (int)getDimensionFloat(attrValue,ctx);
    }

    public static float getDimensionFloat(String attrValue, Context ctx)
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

    protected int getDimensionWithName(View view, String value)
    {
        int dimension;

        // No hace falta hacer trim en caso de "match_parent" etc un espacio fastidia el attr
        if      ("fill_parent".equals(value))  dimension = ViewGroup.LayoutParams.MATCH_PARENT;
        else if ("match_parent".equals(value)) dimension = ViewGroup.LayoutParams.MATCH_PARENT;
        else if ("wrap_content".equals(value)) dimension = ViewGroup.LayoutParams.WRAP_CONTENT;
        else
        {
            dimension = getDimensionInt(value, view.getContext());
        }
        return dimension;
    }

    public static Drawable getDrawable(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue,ctx);
            return ctx.getResources().getDrawable(resId);
        }
        else if (attrValue.startsWith("#")) // Color literal. No hace falta hacer trim
        {
            int color = Color.parseColor(attrValue);
            return new ColorDrawable(color);
        }

        throw new ItsNatDroidException("Cannot process " + attrValue);
    }

    public static int getColor(String attrValue, Context ctx)
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

    protected static int parseSingleName(String value, Map<String, Integer> valueMap)
    {
        Integer valueInt = valueMap.get(value);
        if (valueInt == null)
            throw new ItsNatDroidException("Unrecognized value name " + value + " for attribute");
        return valueInt;
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

    public abstract void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending);

    public abstract void removeAttribute(View view);
}

class Dimension
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
