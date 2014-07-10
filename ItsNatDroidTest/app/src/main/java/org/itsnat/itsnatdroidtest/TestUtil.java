package org.itsnat.itsnatdroidtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

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
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // continue with delete
            }
        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // do nothing
            }
        }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}
