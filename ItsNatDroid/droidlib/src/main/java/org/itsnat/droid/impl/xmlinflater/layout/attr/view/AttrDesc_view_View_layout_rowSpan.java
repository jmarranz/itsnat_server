package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcessChildGridLayout;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GridLayout_rowSpec;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_rowSpan extends AttrDesc
{
    public AttrDesc_view_View_layout_rowSpan(ClassDescViewBased parent)
    {
        super(parent,"layout_rowSpan");
    }

    public void setAttribute(View view, String value,final OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        // Default: 1

        final int rowSpan = getInteger(value,view.getContext());

        Runnable task = new Runnable(){
            @Override
            public void run()
            {
                OneTimeAttrProcessChildGridLayout oneTimeAttrProcessGrid = (OneTimeAttrProcessChildGridLayout) oneTimeAttrProcess;
                if (oneTimeAttrProcessGrid.gridLayout_rowSpec == null)
                    oneTimeAttrProcessGrid.gridLayout_rowSpec = new GridLayout_rowSpec();

                oneTimeAttrProcessGrid.gridLayout_rowSpec.layout_rowSpan = rowSpan;
            }};

        if (oneTimeAttrProcess != null)
        {
            oneTimeAttrProcess.addLayoutParamsTask(task);
        }
        else
        {
            throw new ItsNatDroidException("Attribute " + getName() + " cannot be changed post creation");
        }
    }

    public void removeAttribute(View view)
    {
        // cannot be changed post creation
    }
}
