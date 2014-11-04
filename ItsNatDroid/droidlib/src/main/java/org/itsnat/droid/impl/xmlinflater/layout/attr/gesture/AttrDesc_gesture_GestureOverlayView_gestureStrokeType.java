package org.itsnat.droid.impl.xmlinflater.layout.attr.gesture;

import android.gesture.GestureOverlayView;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_gesture_GestureOverlayView_gestureStrokeType extends AttrDescReflecMethodSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 2 );
    static
    {
        valueMap.put("single", GestureOverlayView.GESTURE_STROKE_TYPE_SINGLE);
        valueMap.put("multiple",GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
    }

    public AttrDesc_gesture_GestureOverlayView_gestureStrokeType(ClassDescViewBased parent)
    {
        super(parent,"gestureStrokeType",int.class,valueMap,"single");
    }

}
