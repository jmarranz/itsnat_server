package org.itsnat.droid.impl.browser.clientdoc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import org.itsnat.itsnatdroidtest.R;

/**
 * Created by jmarranz on 30/06/14.
 */
public class SimpleAlert
{
    public static void show(String title,String text,Context ctx)
    {
        new AlertDialog.Builder(ctx).setTitle(title).setMessage(text)
        .setCancelable(false)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
            }
        }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}

