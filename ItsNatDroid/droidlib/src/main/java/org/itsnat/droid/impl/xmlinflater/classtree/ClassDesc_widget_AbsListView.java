package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodColor;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_AbsListView_choiceMode;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_AbsListView_transcriptMode;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_AbsListView extends ClassDescViewBased
{
    public ClassDesc_widget_AbsListView(ClassDescViewMgr classMgr,ClassDescViewBased parentClass)
    {
        super(classMgr,"android.widget.AbsListView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodColor(this,"cacheColorHint","#000000"));
        addAttrDesc(new AttrDesc_widget_AbsListView_choiceMode(this));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"drawSelectorOnTop",false));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"fastScrollEnabled",false));
        addAttrDesc(new AttrDescReflecMethodDrawable(this,"listSelector","setSelector",null)); // Hay un drawable por defecto de Android
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"scrollingCache","setScrollingCacheEnabled",false));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"smoothScrollbar","setSmoothScrollbarEnabled",true));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"stackFromBottom",false));
        addAttrDesc(new AttrDescReflecMethodBoolean(this,"textFilterEnabled",false));
        addAttrDesc(new AttrDesc_widget_AbsListView_transcriptMode(this));


    }
}

