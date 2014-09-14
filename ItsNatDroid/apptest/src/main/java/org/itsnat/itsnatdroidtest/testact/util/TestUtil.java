package org.itsnat.itsnatdroidtest.testact.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.itsnat.droid.ItsNatDroidException;

import java.lang.reflect.Field;

/**
 * Created by jmarranz on 3/07/14.
 */
public class TestUtil
{
    public static void alertDialog(Context ctx,String content)
    {
        alertDialog(ctx,"Alert",content);
    }

    public static void alertDialog(Context ctx,String title,String content)
    {
        new AlertDialog.Builder(ctx).setTitle("XML").setMessage(content)
        .setCancelable(false)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
            }
        }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    public static <T> T getField(Object obj,String fieldName)
    {
        return getField(obj,obj.getClass(),fieldName);
    }

    public static <T> T getField(Object obj,Class clasz,String fieldName)
    {
        try
        {
            Field field = clasz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T)field.get(obj);
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }

    public static Object getField(Object obj,Class[] classes,String[] fieldName)
    {
        try
        {
            for (int i = 0; i < classes.length; i++)
            {
                Field field = classes[i].getDeclaredField(fieldName[i]);
                field.setAccessible(true);
                obj = field.get(obj);
            }
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }

        return obj;
    }

    public static Class resolveClass(String viewName)
    {
        try { return Class.forName(viewName); }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
    }
}
