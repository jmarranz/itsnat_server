package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodColor;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodFloat;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodLong;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.OrientationUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.gesture.AttrDescView_gesture_GestureOverlayView_gestureStrokeType;
import org.itsnat.droid.impl.xmlinflater.layout.attr.gesture.AttrDescView_gesture_GestureOverlayView_gestureColor;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_gesture_GestureOverlayView extends ClassDescViewBased
{
    public ClassDescView_gesture_GestureOverlayView(ClassDescViewMgr classMgr,ClassDescView_widget_FrameLayout parentClass)
    {
        super(classMgr,"android.gesture.GestureOverlayView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"eventsInterceptionEnabled",true));
        addAttrDesc(new AttrDescViewReflecFieldSetInt(this,"fadeDuration","mFadeDuration",150)); // Curiosamente mFadeDuration es long pero t_odo se procesa como int
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"fadeEnabled",true));
        addAttrDesc(new AttrDescViewReflecMethodLong(this,"fadeOffset",420L));
        addAttrDesc(new AttrDescView_gesture_GestureOverlayView_gestureColor(this));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"gestureStrokeAngleThreshold",40.0f));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"gestureStrokeLengthThreshold",50.0f));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"gestureStrokeSquarenessThreshold","setGestureStrokeSquarenessTreshold",0.275f));  // Es necesario el nombre del método por un gazapo, falta la h de Threshold
        addAttrDesc(new AttrDescView_gesture_GestureOverlayView_gestureStrokeType(this));
        addAttrDesc(new AttrDescViewReflecMethodFloat(this,"gestureStrokeWidth",12.0f)); // Sorprendetemente NO se admite dimensión (dp etc)
        addAttrDesc(new AttrDescViewReflecMethodSingleName(this,"orientation",int.class, OrientationUtil.valueMap,"vertical"));
        addAttrDesc(new AttrDescViewReflecMethodColor(this, "uncertainGestureColor", "#48FFFF00"));


    }
}

