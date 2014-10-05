package org.itsnat.droid.impl.xmlinflater.attr.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TableRow;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcessChildGridLayout;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.attr.GridLayout_columnSpec;
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

    public void setAttribute(final View view, String value,final OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final int column = getInteger(value,view.getContext());

        Runnable task = new Runnable(){
            @Override
            public void run()
            {
                ViewGroup.LayoutParams params = view.getLayoutParams();

                if (params instanceof GridLayout.LayoutParams)
                {
                    OneTimeAttrProcessChildGridLayout oneTimeAttrProcessGrid = (OneTimeAttrProcessChildGridLayout) oneTimeAttrProcess;
                    if (oneTimeAttrProcessGrid.gridLayout_columnSpec == null) oneTimeAttrProcessGrid.gridLayout_columnSpec = new GridLayout_columnSpec();

                    oneTimeAttrProcessGrid.gridLayout_columnSpec.layout_column = column;
                }
                else if (params instanceof TableRow.LayoutParams)
                {
                    ((TableRow.LayoutParams) params).column = column;
                }
            }};

        if (oneTimeAttrProcess != null)
            oneTimeAttrProcess.addLayoutParamsTask(task);
        else
        {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof GridLayout.LayoutParams)
            {
                throw new ItsNatDroidException("Attribute " + getName() + " cannot be changed post creation");
            }
            else if (params instanceof TableRow.LayoutParams)
            {
                task.run();
                view.setLayoutParams(view.getLayoutParams());
            }
        }
    }

    public void removeAttribute(View view)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params instanceof GridLayout.LayoutParams)
        {
            // No hacemos nada, no puede ser cambiado "post-creación"
        }
        else if (params instanceof TableRow.LayoutParams)
        {
            ((TableRow.LayoutParams)params).column = -1;

            view.setLayoutParams(view.getLayoutParams());
        }

    }
}
