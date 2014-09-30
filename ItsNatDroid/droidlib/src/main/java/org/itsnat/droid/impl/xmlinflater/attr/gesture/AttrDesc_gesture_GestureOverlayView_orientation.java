package org.itsnat.droid.impl.xmlinflater.attr.gesture;

import android.gesture.GestureOverlayView;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_gesture_GestureOverlayView_orientation extends AttrDescReflecMethodSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 2 );
    static
    {
        valueMap.put("horizontal", GestureOverlayView.ORIENTATION_HORIZONTAL);
        valueMap.put("vertical",GestureOverlayView.ORIENTATION_VERTICAL);
    }

    public AttrDesc_gesture_GestureOverlayView_orientation(ClassDescViewBased parent)
    {
        super(parent,"orientation",int.class,valueMap,"vertical");
    }

}
