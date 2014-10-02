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
import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDesc
{
    private static Class class_R_styleable;

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

    protected static Class getClass_R_styleable()
    {
        if (class_R_styleable == null)
            class_R_styleable = MiscUtil.resolveClass("com.android.internal.R$styleable");
        return class_R_styleable;
    }

    private static boolean isResource(String attrValue)
    {
        // No hace falta hacer un trim, un espacio al ppio invalida el atributo
        return attrValue.startsWith("@") || attrValue.startsWith("?");
    }

    public int getIdentifier(String attrValue, Context ctx)
    {
        return getIdentifier(attrValue,ctx,true);
    }

    public int getIdentifier(String attrValue, Context ctx,boolean throwErr)
    {
        return getIdentifier(attrValue,ctx,parent.getClassDescViewMgr().getXMLLayoutInflateService(),throwErr);
    }

    public static int getIdentifier(String attrValue, Context ctx,XMLLayoutInflateService layoutService,boolean throwErr)
    {
        if ("0".equals(attrValue) || "-1".equals(attrValue) || "@null".equals(attrValue)) return 0;

        int id;
        char first = attrValue.charAt(0);
        if (first == '?')
        {
            id = getIdentifierTheme(attrValue, ctx);
        }
        else if (first == '@')
        {
            // En este caso es posible que se haya registrado dinámicamente el id via "@+id/..." Tiene prioridad el registro de Android que el de ItsNat, para qué generar un id si ya existe como recurso
            id = getIdentifierResource(attrValue, ctx);
            if (id > 0)
                return id;
            id = getIdentifierDynamicallyAdded(attrValue,ctx,layoutService);
        }
        else
            throw new ItsNatDroidException("INTERNAL ERROR");

        if (throwErr && id <= 0) throw new ItsNatDroidException("Not found resource with id \"" + attrValue + "\"");
        return id;
    }

    private static int getIdentifierTheme(String attrValue, Context ctx)
    {
        // http://stackoverflow.com/questions/12781501/android-setting-linearlayout-background-programmatically
        // Ej. android:textAppearance="?android:attr/textAppearanceMedium"
        TypedValue outValue = new TypedValue();
        ctx.getTheme().resolveAttribute(getIdentifierResource(attrValue, ctx), outValue, true);
        return outValue.resourceId;
    }

    private static int getIdentifierDynamicallyAdded(String attrValue, Context ctx,XMLLayoutInflateService layoutService)
    {
        Resources res = ctx.getResources();

        if (attrValue.indexOf(':') != -1) // Tiene package, ej "@+android:id/", no se encontrará un id registrado como "@+id/..." y los posibles casos con package NO los hemos contemplado
            return 0; // No encontrado

        attrValue = attrValue.substring(1); // Quitamos el @ o #
        int pos = attrValue.indexOf('/');
        String idName = attrValue.substring(pos + 1);

        return layoutService.findViewId(idName);
    }

    private static int getIdentifierResource(String attrValue, Context ctx)
    {
        Resources res = ctx.getResources();

        attrValue = attrValue.substring(1); // Quitamos el @ o #
        if (attrValue.startsWith("+id/"))
            attrValue = attrValue.substring(1); // Quitamos el +
        String packageName;
        if (attrValue.indexOf(':') != -1) // Tiene package, ej "android:" delegamos en Resources.getIdentifier() que lo resuelva
        {
            packageName = null;
        }
        else
        {
            packageName = ctx.getPackageName(); // El package es necesario como parámetro sólo cuando no está en la string (recursos locales)
        }

        return res.getIdentifier(attrValue, null, packageName);
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

    protected int getDimensionWithNameInt(View view, String value)
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

    public Drawable getDrawable(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue,ctx);
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

    public abstract void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending);

    public abstract void removeAttribute(View view);
}


