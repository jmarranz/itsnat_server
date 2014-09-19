package org.itsnat.itsnatdroidtest.testact.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by jmarranz on 9/09/14.
 */
public class CustomProgressBar extends ProgressBar
{
    public CustomProgressBar(Context context)
    {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        for(int i = 0; i < attrs.getAttributeCount(); i++)
        {
            int style = attrs.getStyleAttribute();
            String name = attrs.getAttributeName(i);
            System.out.println("PARAR");
        }
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyle, int styleRes)
    {
        super(context, attrs, defStyle);
    }
}