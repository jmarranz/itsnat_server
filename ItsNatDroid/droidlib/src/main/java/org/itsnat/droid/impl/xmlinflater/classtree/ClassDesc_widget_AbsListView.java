package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecBoolean;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecColor;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_AbsListView_choiceMode;
import org.itsnat.droid.impl.xmlinflater.attr.widget.AttrDesc_widget_AbsListView_transcriptMode;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_AbsListView extends ClassDescViewBased
{
    public ClassDesc_widget_AbsListView(ClassDescViewBased parentClass)
    {
        super("android.widget.AbsListView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecColor(this,"cacheColorHint","#000000"));
        addAttrDesc(new AttrDesc_widget_AbsListView_choiceMode(this));
        addAttrDesc(new AttrDescReflecBoolean(this,"drawSelectorOnTop",false));
        addAttrDesc(new AttrDescReflecBoolean(this,"fastScrollEnabled",false));
        addAttrDesc(new AttrDescReflecDrawable(this,"listSelector","setSelector"));
        addAttrDesc(new AttrDescReflecBoolean(this,"scrollingCache","setScrollingCacheEnabled",false));
        addAttrDesc(new AttrDescReflecBoolean(this,"smoothScrollbar","setSmoothScrollbarEnabled",true));
        addAttrDesc(new AttrDescReflecBoolean(this,"stackFromBottom",false));
        addAttrDesc(new AttrDescReflecBoolean(this,"textFilterEnabled",false));
        addAttrDesc(new AttrDesc_widget_AbsListView_transcriptMode(this));


    }
}

