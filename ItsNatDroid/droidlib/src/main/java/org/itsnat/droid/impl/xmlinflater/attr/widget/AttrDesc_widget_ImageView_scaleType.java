package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.widget.ImageView;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_ImageView_scaleType extends AttrDescReflecMethodSingleName<ImageView.ScaleType>
{
    static Map<String, ImageView.ScaleType> valueMap = new HashMap<String, ImageView.ScaleType>( 8 );
    static
    {
        valueMap.put("matrix", ImageView.ScaleType.MATRIX);
        valueMap.put("fitXY", ImageView.ScaleType.FIT_XY);
        valueMap.put("fitStart", ImageView.ScaleType.FIT_START);
        valueMap.put("fitCenter", ImageView.ScaleType.FIT_CENTER);
        valueMap.put("fitEnd", ImageView.ScaleType.FIT_END);
        valueMap.put("center", ImageView.ScaleType.CENTER);
        valueMap.put("centerCrop", ImageView.ScaleType.CENTER_CROP);
        valueMap.put("centerInside", ImageView.ScaleType.CENTER_INSIDE);
    }

    public AttrDesc_widget_ImageView_scaleType(ClassDescViewBased parent)
    {
        super(parent,"scaleType",ImageView.ScaleType.class,valueMap,"fitCenter");
    }

}
