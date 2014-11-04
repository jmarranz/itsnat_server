package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_ViewGroup_animateLayoutChanges;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_ViewGroup_layoutAnimation;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_ViewGroup_descendantFocusability;
import org.itsnat.droid.impl.xmlinflater.layout.attr.view.AttrDescView_view_ViewGroup_persistentDrawingCache;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_view_ViewGroup extends ClassDescViewBased
{
    public ClassDescView_view_ViewGroup(ClassDescViewMgr classMgr, ClassDescView_view_View parentClass)
    {
        super(classMgr,"android.view.ViewGroup",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"addStatesFromChildren",false));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"alwaysDrawnWithCache","setAlwaysDrawnWithCacheEnabled",true));
        addAttrDesc(new AttrDescView_view_ViewGroup_animateLayoutChanges(this)); // animateLayoutChanges
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"animationCache","setAnimationCacheEnabled",true));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"clipChildren",true));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"clipToPadding",true));
        addAttrDesc(new AttrDescView_view_ViewGroup_descendantFocusability(this)); // descendantFocusability
        addAttrDesc(new AttrDescView_view_ViewGroup_layoutAnimation(this)); // layoutAnimation
        // android:layoutMode es Level 18
        addAttrDesc(new AttrDescView_view_ViewGroup_persistentDrawingCache(this)); // persistentDrawingCache
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"splitMotionEvents","setMotionEventSplittingEnabled",false));

    }
}

