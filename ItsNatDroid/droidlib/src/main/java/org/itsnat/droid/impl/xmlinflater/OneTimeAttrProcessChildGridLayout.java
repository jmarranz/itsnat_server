package org.itsnat.droid.impl.xmlinflater;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.attr.GridLayout_columnSpec;
import org.itsnat.droid.impl.xmlinflater.attr.GridLayout_rowSpec;

/**
 * Created by jmarranz on 8/09/14.
 */
public class OneTimeAttrProcessChildGridLayout extends OneTimeAttrProcess
{
    public GridLayout_columnSpec gridLayout_columnSpec;
    public GridLayout_rowSpec gridLayout_rowSpec;

    public OneTimeAttrProcessChildGridLayout(View view)
    {
        super(view);
    }

    public void finish()
    {
        if (gridLayout_columnSpec != null) gridLayout_columnSpec.setAttributes(view);
        if (gridLayout_rowSpec != null) gridLayout_rowSpec.setAttributes(view);

        super.finish();
    }
}
