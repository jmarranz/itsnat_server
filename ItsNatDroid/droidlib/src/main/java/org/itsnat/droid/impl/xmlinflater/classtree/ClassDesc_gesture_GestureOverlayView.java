package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecFieldSetInt;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodColor;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodLong;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.attr.OrientationUtil;
import org.itsnat.droid.impl.xmlinflater.attr.gesture.AttrDesc_gesture_GestureOverlayView_gestureColor;
import org.itsnat.droid.impl.xmlinflater.attr.gesture.AttrDesc_gesture_GestureOverlayView_gestureStrokeType;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_gesture_GestureOverlayView extends ClassDescViewBased
{
    public ClassDesc_gesture_GestureOverlayView(ClassDescViewMgr classMgr,ClassDesc_widget_FrameLayout parentClass)
    {
        super(classMgr,"android.gesture.GestureOverlayView",parentClass);
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
        addAttrDesc(new AttrDescReflecMethodFloat(this,"gestureStrokeSquarenessThreshold","setGestureStrokeSquarenessTreshold",0.275f));  // Es necesario el nombre del método por un gazapo, falta la h de Threshold
        addAttrDesc(new AttrDesc_gesture_GestureOverlayView_gestureStrokeType(this));
        addAttrDesc(new AttrDescReflecMethodFloat(this,"gestureStrokeWidth",12.0f)); // Sorprendetemente NO se admite dimensión (dp etc)
        addAttrDesc(new AttrDescReflecMethodSingleName(this,"orientation",int.class, OrientationUtil.valueMap,"vertical"));
        addAttrDesc(new AttrDescReflecMethodColor(this, "uncertainGestureColor", "#48FFFF00"));


    }
}

