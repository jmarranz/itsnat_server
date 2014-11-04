package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodColor;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_AbsListView_choiceMode;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_AbsListView_transcriptMode;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_AbsListView extends ClassDescViewBased
{
    public ClassDescView_widget_AbsListView(ClassDescViewMgr classMgr,ClassDescViewBased parentClass)
    {
        super(classMgr,"android.widget.AbsListView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodColor(this,"cacheColorHint","#000000"));
        addAttrDesc(new AttrDescView_widget_AbsListView_choiceMode(this));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"drawSelectorOnTop",false));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"fastScrollEnabled",false));
        addAttrDesc(new AttrDescViewReflecMethodDrawable(this,"listSelector","setSelector",null)); // Hay un drawable por defecto de Android
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"scrollingCache","setScrollingCacheEnabled",false));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"smoothScrollbar","setSmoothScrollbarEnabled",true));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"stackFromBottom",false));
        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"textFilterEnabled",false));
        addAttrDesc(new AttrDescView_widget_AbsListView_transcriptMode(this));


    }
}

