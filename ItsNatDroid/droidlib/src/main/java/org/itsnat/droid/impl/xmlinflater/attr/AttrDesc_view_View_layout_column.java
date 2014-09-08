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
public class AttrDesc_view_View_layout_column extends AttrDesc
{
    public AttrDesc_view_View_layout_column(ClassDescViewBased parent)
    {
        super(parent,"layout_column");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        // Default: Integer.MIN_VALUE

        int column = getInteger(value,view.getContext());

        if (oneTimeAttrProcess != null)
        {
            OneTimeAttrProcessChildGridLayout oneTimeAttrProcessGrid = (OneTimeAttrProcessChildGridLayout) oneTimeAttrProcess;
            if (oneTimeAttrProcessGrid.gridLayout_columnSpec == null)
                oneTimeAttrProcessGrid.gridLayout_columnSpec = new GridLayout_columnSpec();

            oneTimeAttrProcessGrid.gridLayout_columnSpec.layout_column = column;

            oneTimeAttrProcess.setNeededSetLayoutParams();
        }
        else throw new ItsNatDroidException("Attribute " + getName() + " cannot be changed post creation");
    }

    public void removeAttribute(View view)
    {
        // No se que hacer
    }





}
