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
public class AttrDesc_view_View_layout_columnSpan extends AttrDesc
{
    public AttrDesc_view_View_layout_columnSpan(ClassDescViewBased parent)
    {
        super(parent,"layout_columnSpan");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        // Default: 1

        int columnSpan = getInteger(value,view.getContext());

        if (oneTimeAttrProcess != null)
        {
            OneTimeAttrProcessChildGridLayout oneTimeAttrProcessGrid = (OneTimeAttrProcessChildGridLayout) oneTimeAttrProcess;
            if (oneTimeAttrProcessGrid.gridLayout_columnSpec == null)
                oneTimeAttrProcessGrid.gridLayout_columnSpec = new GridLayout_columnSpec();

            oneTimeAttrProcessGrid.gridLayout_columnSpec.layout_columnSpan = columnSpan;

            oneTimeAttrProcess.setNeededSetLayoutParams();
        }
        else throw new ItsNatDroidException("Attribute " + getName() + " cannot be changed post creation");
    }

    public void removeAttribute(View view)
    {
        // No se que hacer
    }

}
