package org.itsnat.itsnatdroidtest.testact.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by jmarranz on 20/06/14.
 */
public abstract class CustomTextViewBase extends TextView
{
    public CustomTextViewBase(Context context)
    {
        super(context);
    }

    public CustomTextViewBase(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomTextViewBase(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
}
