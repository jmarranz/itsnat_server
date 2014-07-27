package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_gravity extends AttrDesc
{
    private final Map<String,Integer> values = new HashMap<String,Integer>();

    {
        // Merece la pena un Map, de otra manera son muchos if anidados y poco rendimiento

        http://developer.android.com/reference/android/widget/LinearLayout.LayoutParams.html#attr_android:layout_gravity

        values.put("top",0x30);
        values.put("bottom",0x50);
        values.put("left",0x03);
        values.put("right",0x05);
        values.put("center_vertical",0x10);
        values.put("fill_vertical",0x70);
        values.put("center_horizontal",0x01);
        values.put("fill_horizontal",0x07);
        values.put("center",0x11);
        values.put("fill",0x77);
        values.put("clip_vertical",0x80);
        values.put("clip_horizontal",0x08);
        values.put("start",0x00800003);
        values.put("end",0x00800005);
    }

    public AttrDesc_view_View_layout_gravity(ClassDescViewBased parent)
    {
        super(parent,"layout_gravity");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess)
    {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();



        Integer valueInt = values.get(value);
        if (valueInt == null) throw new ItsNatDroidException("Unknown value " + value + " for attribute layout_gravity");

        params.gravity = valueInt;

        if (oneTimeAttrProcess != null) oneTimeAttrProcess.neededSetLayoutParams = true;
        else view.setLayoutParams(view.getLayoutParams());
    }

    public void removeAttribute(View view)
    {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();

        params.gravity = 0x03; // left

        view.setLayoutParams(params);
    }
}
