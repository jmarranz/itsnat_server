package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescView extends AttrDesc
{
    private static Class class_R_styleable;

    protected ClassDescViewBased parent;

    public AttrDescView(ClassDescViewBased parent, String name)
    {
        super(name);
        this.parent = parent;

        /* Para ver si nos hemos equivocado y name no se corresponde con el nombre de la clase específica
        String className = getClass().getName();
        if (!className.contains(name) &&
            !className.contains("AttrDescReflec") &&
            !className.contains("AttrDesc_view_View_layout_rellayout_byId") &&
            !className.contains("AttrDesc_view_View_layout_rellayout_byBoolean")  )
            System.out.println("ERROR: " + className);
        */
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
        return getIdentifier(attrValue,ctx,parent.getClassDescViewMgr().getXMLInflateRegistry(),throwErr);
    }

    public static int getIdentifier(String attrValue, Context ctx,XMLInflateRegistry layoutService)
    {
        return getIdentifier(attrValue, ctx,layoutService,true);
    }

    public static int getIdentifier(String value, Context ctx,XMLInflateRegistry layoutService,boolean throwErr)
    {
        if ("0".equals(value) || "-1".equals(value) || "@null".equals(value)) return 0;

        int id;
        char first = value.charAt(0);
        if (first == '?')
        {
            id = getIdentifierTheme(value, ctx);
        }
        else if (first == '@')
        {
            // En este caso es posible que se haya registrado dinámicamente el id via "@+id/..." Tiene prioridad el registro de Android que el de ItsNat, para qué generar un id si ya existe como recurso
            id = getIdentifierResource(value, ctx);
            if (id > 0)
                return id;
            id = getIdentifierDynamicallyAdded(value,ctx,layoutService);
        }
        else
            throw new ItsNatDroidException("INTERNAL ERROR");

        if (throwErr && id <= 0) throw new ItsNatDroidException("Not found resource with id \"" + value + "\"");
        return id;
    }

    private static int getIdentifierTheme(String value, Context ctx)
    {
        // http://stackoverflow.com/questions/12781501/android-setting-linearlayout-background-programmatically
        // Ej. android:textAppearance="?android:attr/textAppearanceMedium"
        TypedValue outValue = new TypedValue();
        ctx.getTheme().resolveAttribute(getIdentifierResource(value, ctx), outValue, true);
        return outValue.resourceId;
    }

    public static int getIdentifierDynamicallyAdded(String value, Context ctx,XMLInflateRegistry layoutService)
    {
        if (value.indexOf(':') != -1) // Tiene package, ej "@+android:id/", no se encontrará un id registrado como "@+id/..." y los posibles casos con package NO los hemos contemplado
            return 0; // No encontrado

        value = value.substring(1); // Quitamos el @ o #
        int pos = value.indexOf('/');
        String idName = value.substring(pos + 1);

        return layoutService.findId(idName);
    }

    private static int getIdentifierResource(String value, Context ctx)
    {
        Resources res = ctx.getResources();

        value = value.substring(1); // Quitamos el @ o #
        if (value.startsWith("+id/"))
            value = value.substring(1); // Quitamos el +
        String packageName;
        if (value.indexOf(':') != -1) // Tiene package el value, ej "android:" delegamos en Resources.getIdentifier() que lo resuelva
        {
            packageName = null;
        }
        else
        {
            packageName = ctx.getPackageName(); // El package es necesario como parámetro sólo cuando no está en la string (recursos locales)
        }

        return res.getIdentifier(value, null, packageName);
    }

    public int getIdentifierAddIfNecessary(String value, Context ctx)
    {
        // Procesamos aquí los casos de "@+id/...", la razón es que cualquier atributo que referencie un id (más allá
        // de android:id) puede registrar un nuevo atributo lo cual es útil si el android:id como tal está después,
        // después en android:id ya no hace falta que sea "@+id/...".
        // http://stackoverflow.com/questions/11029635/android-radiogroup-checkedbutton-property
        int id = 0;
        if (value.startsWith("@+id/") || value.startsWith("@id/")) // Si fuera el caso de "@+mypackage:id/name" ese caso no lo soportamos, no lo he visto nunca aunque en teoría está sintácticamente permitido
        {
            id = getIdentifier(value, ctx, false); // Tiene prioridad el recurso de Android, pues para qué generar un id nuevo si ya existe o bien ya fue registrado dinámicamente
            if (id <= 0)
            {
                int pos = value.indexOf('/');
                String idName = value.substring(pos + 1);
                XMLInflateRegistry inflateService = parent.getClassDescViewMgr().getXMLInflateRegistry();
                if (value.startsWith("@+id/")) id = inflateService.findIdAddIfNecessary(idName);
                else id = inflateService.findId(idName);
                if (id <= 0) throw new ItsNatDroidException("Not found resource with id \"" + value + "\"");
            }
        }
        else id = getIdentifier(value, ctx);
        return id;
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

    public void setAttribute(View view, AttrParsed attr, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        setAttribute(view,attr.getValue(), oneTimeAttrProcess,pending);
    }

    protected abstract void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending);

    public abstract void removeAttribute(View view);
}


