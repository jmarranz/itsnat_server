package org.itsnat.droid.impl.xmlinflater.attr;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcessChildGridLayout;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_view_View_layout_rowSpan extends AttrDesc
{
    public AttrDesc_view_View_layout_rowSpan(ClassDescViewBased parent)
    {
        super(parent,"layout_rowSpan");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        // Default: 1

        int rowSpan = getInteger(value,view.getContext());

        if (oneTimeAttrProcess != null)
        {
            OneTimeAttrProcessChildGridLayout oneTimeAttrProcessGrid = (OneTimeAttrProcessChildGridLayout) oneTimeAttrProcess;
            if (oneTimeAttrProcessGrid.gridLayout_rowSpec == null)
                oneTimeAttrProcessGrid.gridLayout_rowSpec = new GridLayout_rowSpec();

            oneTimeAttrProcessGrid.gridLayout_rowSpec.layout_rowSpan = rowSpan;

            oneTimeAttrProcess.setNeededSetLayoutParams();
        }
        else throw new ItsNatDroidException("Attribute " + getName() + " cannot be changed post creation");
    }

    public void removeAttribute(View view)
    {
        // No se que hacer
    }
}
