package org.itsnat.droid.impl.xmlinflater.classtree;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescDrawable;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewId;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewLayoutAlignParentTop;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewLayoutBelow;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewLayoutHeight;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewLayoutWidth;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDescViewViewPadding;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescUnknown extends ClassDescViewBase
{
    public ClassDescUnknown(String className,ClassDescViewBase parent)
    {
        super(className,parent);
    }

}
