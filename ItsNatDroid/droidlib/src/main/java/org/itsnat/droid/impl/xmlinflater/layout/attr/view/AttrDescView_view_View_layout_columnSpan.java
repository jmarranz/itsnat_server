package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcessChildGridLayout;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GridLayout_columnSpec;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_layout_columnSpan extends AttrDescView
{
    public AttrDescView_view_View_layout_columnSpan(ClassDescViewBased parent)
    {
        super(parent,"layout_columnSpan");
    }

    public void setAttribute(View view, String value,final OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        // Default: 1

        final int columnSpan = getInteger(value,view.getContext());

        Runnable task = new Runnable(){
            @Override
            public void run()
            {
                OneTimeAttrProcessChildGridLayout oneTimeAttrProcessGrid = (OneTimeAttrProcessChildGridLayout) oneTimeAttrProcess;
                if (oneTimeAttrProcessGrid.gridLayout_columnSpec == null)
                    oneTimeAttrProcessGrid.gridLayout_columnSpec = new GridLayout_columnSpec();

                oneTimeAttrProcessGrid.gridLayout_columnSpec.layout_columnSpan = columnSpan;
            }};

        if (oneTimeAttrProcess != null)
            oneTimeAttrProcess.addLayoutParamsTask(task);
        else
            throw new ItsNatDroidException("Attribute " + getName() + " cannot be changed post creation");
    }

    public void removeAttribute(View view)
    {
        // Cannot be changed post creation
    }

}
