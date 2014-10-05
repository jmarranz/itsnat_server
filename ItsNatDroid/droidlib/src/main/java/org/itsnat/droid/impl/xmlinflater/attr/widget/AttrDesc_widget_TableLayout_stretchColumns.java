package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;
import android.widget.TableLayout;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TableLayout_stretchColumns extends AttrDesc
{
    public AttrDesc_widget_TableLayout_stretchColumns(ClassDescViewBased parent)
    {
        super(parent,"stretchColumns");
    }

    public void setAttribute(View view,final String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final TableLayout tableView = (TableLayout)view;

        if (oneTimeAttrProcess == null) // Si es no nulo es que estamos creando el TableLayout y no hace falta ésto
        {
            tableView.setStretchAllColumns(false);
        }

        if ("".equals(value))
            tableView.setStretchAllColumns(false);
        else if ("*".equals(value))
            tableView.setStretchAllColumns(true);
        else
        {
            String[] columns = value.split(",");
            for (int i = 0; i < columns.length; i++)
            {
                String columnStr = columns[i];
                int column = Integer.parseInt(columnStr);
                tableView.setColumnStretchable(column, true);
            }
        }
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"",null,null);
    }


}
