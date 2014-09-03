package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_ViewGroup_animateLayoutChanges;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_ViewGroup_descendantFocusability;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_ViewGroup_layoutAnimation;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_ViewGroup_persistentDrawingCache;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ViewGroup extends ClassDescViewBased
{
    public ClassDesc_widget_ViewGroup(ClassDesc_view_View parentClass)
    {
        super("android.view.ViewGroup",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecBoolean(this,"addStatesFromChildren",false));
        addAttrDesc(new AttrDescReflecBoolean(this,"alwaysDrawnWithCache","setAlwaysDrawnWithCacheEnabled",true));
        addAttrDesc(new AttrDesc_view_ViewGroup_animateLayoutChanges(this)); // animateLayoutChanges
        addAttrDesc(new AttrDescReflecBoolean(this,"animationCache","setAnimationCacheEnabled",true));
        addAttrDesc(new AttrDescReflecBoolean(this,"clipChildren",true));
        addAttrDesc(new AttrDescReflecBoolean(this,"clipToPadding",true));
        addAttrDesc(new AttrDesc_view_ViewGroup_descendantFocusability(this)); // descendantFocusability
        addAttrDesc(new AttrDesc_view_ViewGroup_layoutAnimation(this)); // layoutAnimation
        // android:layoutMode es Level 18
        addAttrDesc(new AttrDesc_view_ViewGroup_persistentDrawingCache(this)); // persistentDrawingCache
        addAttrDesc(new AttrDescReflecBoolean(this,"splitMotionEvents","setMotionEventSplittingEnabled",false));

    }
}

