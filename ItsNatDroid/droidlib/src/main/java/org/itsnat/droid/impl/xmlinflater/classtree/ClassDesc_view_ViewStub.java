package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodId;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_view_ViewStub extends ClassDescViewBased
{
    public ClassDesc_view_ViewStub(ClassDescViewMgr classMgr, ClassDesc_view_View parentClass)
    {
        super(classMgr,"android.view.ViewStub",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodId(this,"inflatedId","setInflatedId",-1));
        addAttrDesc(new AttrDescReflecMethodId(this,"layout","setLayoutResource",0));


        /*
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"addStatesFromChildren",false));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"alwaysDrawnWithCache","setAlwaysDrawnWithCacheEnabled",true));
        addAttrDesc(new AttrDesc_view_ViewGroup_animateLayoutChanges(this)); // animateLayoutChanges
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"animationCache","setAnimationCacheEnabled",true));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"clipChildren",true));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"clipToPadding",true));
        addAttrDesc(new AttrDesc_view_ViewGroup_descendantFocusability(this)); // descendantFocusability
        addAttrDesc(new AttrDesc_view_ViewGroup_layoutAnimation(this)); // layoutAnimation
        // android:layoutMode es Level 18
        addAttrDesc(new AttrDesc_view_ViewGroup_persistentDrawingCache(this)); // persistentDrawingCache
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"splitMotionEvents","setMotionEventSplittingEnabled",false));
*/
    }
}

