package org.itsnat.itsnatdroidtest.testact.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.CalendarView;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.attr.FieldContainer;
import org.itsnat.droid.impl.xmlinflater.attr.MethodContainer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jmarranz on 3/07/14.
 */
public class TestUtil
{
    private final static Map<String,Field> fieldCache = new HashMap<String,Field>();
    private final static Map<String,Method> methodCache = new HashMap<String,Method>();

    public static void alertDialog(Context ctx,String content)
    {
        alertDialog(ctx,"Alert",content);
    }

    public static void alertDialog(Context ctx,String title,String content)
    {
        new AlertDialog.Builder(ctx).setTitle(title).setMessage(content)
        .setCancelable(false)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
            }
        }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    public static Object getField(Object obj,String fieldName)
    {
        return getField(obj,obj.getClass(),fieldName);
    }

    public static Object getField(Object obj,Class clasz,String fieldName)
    {
        try
        {
            Field field = getField(clasz,fieldName);
            return field.get(obj);
        }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }

    public static Object getField(Object obj,Class[] classes,String[] fieldName)
    {
        try
        {
            for (int i = 0; i < classes.length; i++)
            {
                Field field = getField(classes[i], fieldName[i]);
                obj = field.get(obj);
            }
        }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }

        return obj;
    }

    public static Field getField(Class clasz,String fieldName)
    {
        try
        {
            String key = clasz.getName() + ":" + fieldName;
            Field field = fieldCache.get(key);
            if (field == null)
            {
                field = clasz.getDeclaredField(fieldName);
                field.setAccessible(true);
                fieldCache.put(key,field);
            }

            return field;
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
    }

    public static Object callMethod(Object obj,Object[] params,String methodName,Class[] paramClasses)
    {
        return callMethod(obj,params,obj.getClass(),methodName,paramClasses);
    }

    public static Object callMethod(Object obj,Object[] params,Class clasz,String methodName,Class[] paramClasses)
    {
        try
        {
            Method method = getMethod(clasz,methodName,paramClasses);
            return method.invoke(obj,params);
        }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
    }

    public static Method getMethod(Class clasz,String methodName,Class[] paramClasses)
    {
        try
        {
            StringBuilder paramsKey = new StringBuilder();
            if (paramClasses != null)
            {
                for(int i = 0; i < paramClasses.length; i++)
                {
                    paramsKey.append(":" + paramClasses[i].getName());
                }
            }
            String key = clasz.getName() + ":" + methodName + paramsKey.toString();
            Method method = methodCache.get(key);
            if (method == null)
            {
                method = clasz.getDeclaredMethod(methodName, paramClasses);
                method.setAccessible(true);
                methodCache.put(key,method);
            }

            return method;
        }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
    }

    public static Class resolveClass(String viewName)
    {
        try { return Class.forName(viewName); }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
    }


}
