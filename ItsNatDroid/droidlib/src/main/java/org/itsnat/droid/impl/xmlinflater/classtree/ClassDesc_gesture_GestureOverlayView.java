package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodLong;
import org.itsnat.droid.impl.xmlinflater.attr.gesture.AttrDesc_gesture_GestureOverlayView_gestureColor;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_gesture_GestureOverlayView extends ClassDescViewBased
{
    public ClassDesc_gesture_GestureOverlayView(ClassDesc_widget_FrameLayout parentClass)
    {
        super("android.gesture.GestureOverlayView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodBoolean(this,"eventsInterceptionEnabled",true));
        addAttrDesc(new AttrDescReflecFieldSetInt(this,"fadeDuration","mFadeDuration",150)); // Curiosamente mFadeDuration es long pero t_odo se procesa como int
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"fadeEnabled",true));
        addAttrDesc(new AttrDescReflecMethodLong(this,"fadeOffset",420L));
        addAttrDesc(new AttrDesc_gesture_GestureOverlayView_gestureColor(this));
        addAttrDesc(new AttrDescReflecMethodFloat(this,"gestureStrokeAngleThreshold",40.0f));
        addAttrDesc(new AttrDescReflecMethodFloat(this,"gestureStrokeLengthThreshold",50.0f));

    }
}

