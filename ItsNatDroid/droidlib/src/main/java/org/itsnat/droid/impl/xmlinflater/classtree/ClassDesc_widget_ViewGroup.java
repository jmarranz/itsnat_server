package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_ViewGroup_animateLayoutChanges;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc_view_View_descendantFocusability;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ViewGroup extends ClassDescViewBased
{
    public ClassDesc_widget_ViewGroup(ClassDescViewBased parentClass)
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
        addAttrDesc(new AttrDesc_view_View_descendantFocusability(this));

    }
}

