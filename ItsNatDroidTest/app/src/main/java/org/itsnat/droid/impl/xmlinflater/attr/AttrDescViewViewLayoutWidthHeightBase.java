package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;


/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescViewViewLayoutWidthHeightBase extends AttrDesc
{
    public AttrDescViewViewLayoutWidthHeightBase(ClassDescViewBase parent,String name)
    {
        super(parent,name);
    }

    protected int getDimension(View view, String value)
    {
        int dimension;

        // No hace falta hacer trim en caso de "match_parent" etc un espacio fastidia el attr
        if      ("fill_parent".equals(value))  dimension = ViewGroup.LayoutParams.MATCH_PARENT;
        else if ("match_parent".equals(value)) dimension = ViewGroup.LayoutParams.MATCH_PARENT;
        else if ("wrap_content".equals(value)) dimension = ViewGroup.LayoutParams.WRAP_CONTENT;
        else
        {
            dimension = (int) getDimPixel(value,view.getContext());
        }
        return dimension;
    }

}
