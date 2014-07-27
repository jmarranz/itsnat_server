package org.itsnat.droid.impl.xmlinflater.attr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;
import org.itsnat.droid.impl.util.ValueUtil;

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
        {
            // http://stackoverflow.com/questions/12781501/android-setting-linearlayout-background-programmatically
            // Ej. android:textAppearance="?android:attr/textAppearanceMedium"
            TypedValue outValue = new TypedValue();
            ctx.getTheme().resolveAttribute(getIdentifierSimple(attrValue, ctx), outValue, true);
            return outValue.resourceId;
        }
        else if (attrValue.startsWith("@"))
            return getIdentifierSimple(attrValue,ctx);
        else
            throw new ItsNatDroidException("INTERNAL ERROR");
    }

    private static int getIdentifierSimple(String attrValue, Context ctx)
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

    public static boolean getBoolean(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getBoolean(resId);
        }
        else return Boolean.parseBoolean(attrValue);
    }

    protected int getDimensionSuffixAsInt(String suffix)
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

    protected String getDimensionSuffix(String value)
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
        else throw new ItsNatDroidException("Internal error");
    }

    protected static float extractFloat(String value, String suffix)
    {
        int pos = value.lastIndexOf(suffix);
        value = value.substring(0,pos);
        return Float.parseFloat(value);
    }

    public static float getDimPixel(String attrValue, Context ctx)
    {
        Resources res = ctx.getResources();
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return res.getDimension(resId);
        }
        else
        {
            String valueTrim = attrValue.trim();
            if (valueTrim.endsWith("dp"))
            {
                float num = extractFloat(valueTrim, "dp");
                return ValueUtil.dpToPixel(num, res);
            }
            else if (valueTrim.endsWith("px"))
            {
                return extractFloat(valueTrim, "px");
            }
            else if (valueTrim.endsWith("sp"))
            {
                float num = extractFloat(valueTrim, "sp");
                return ValueUtil.spToPixel(num, res);
            }
            else if (valueTrim.endsWith("in"))
            {
                float num = extractFloat(valueTrim, "in");
                return ValueUtil.inToPixel(num, res);
            }
            else if (valueTrim.endsWith("mm"))
            {
                float num = extractFloat(valueTrim, "mm");
                return ValueUtil.mmToPixel(num, res);
            }

            throw new ItsNatDroidException("Cannot process " + attrValue); // POR AHORA hay que ver si faltan más casos
        }
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

    public abstract void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess);

    public abstract void removeAttribute(View view);
}
