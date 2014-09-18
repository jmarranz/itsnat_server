package org.itsnat.itsnatdroidtest.testact.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.itsnat.droid.ItsNatDroidException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 3/07/14.
 */
public class TestUtil
{
    private final static Map<String,Field> fieldCache = new HashMap<String,Field>();

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

    public static Class resolveClass(String viewName)
    {
        try { return Class.forName(viewName); }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
    }
}
