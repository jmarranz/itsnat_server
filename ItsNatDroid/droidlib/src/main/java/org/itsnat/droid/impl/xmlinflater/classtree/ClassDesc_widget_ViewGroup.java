package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_ViewGroup_animateLayoutChanges;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_ViewGroup_descendantFocusability;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_ViewGroup_layoutAnimation;
import org.itsnat.droid.impl.xmlinflater.attr.view.AttrDesc_view_ViewGroup_persistentDrawingCache;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ViewGroup extends ClassDescViewBased
{
    public ClassDesc_widget_ViewGroup(ClassDescViewMgr classMgr,ClassDesc_view_View parentClass)
    {
        super(classMgr,"android.view.ViewGroup",parentClass);
    }

    protected void init()
    {
        super.init();

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

    }
}

