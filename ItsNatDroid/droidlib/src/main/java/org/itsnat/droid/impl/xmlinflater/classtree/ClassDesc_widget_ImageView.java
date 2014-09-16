package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ImageView extends ClassDescViewBased
{
    public ClassDesc_widget_ImageView(ClassDesc_view_View parentClass)
    {
        super("android.widget.ImageView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecDrawable(this,"src","setImageDrawable"));


    }
}

