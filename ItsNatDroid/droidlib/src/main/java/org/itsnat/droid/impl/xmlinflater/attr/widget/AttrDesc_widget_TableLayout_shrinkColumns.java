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
public class AttrDesc_widget_TableLayout_shrinkColumns extends AttrDesc
{
    public AttrDesc_widget_TableLayout_shrinkColumns(ClassDescViewBased parent)
    {
        super(parent,"shrinkColumns");
    }

    public void setAttribute(View view,String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final TableLayout tableView = (TableLayout)view;

        if (oneTimeAttrProcess == null) // Si es no nulo es que estamos creando el TableLayout y no hace falta ésto
        {
            tableView.setShrinkAllColumns(false);
        }

        if ("".equals(value))
            tableView.setShrinkAllColumns(false);
        else if ("*".equals(value))
            tableView.setShrinkAllColumns(true);
        else
        {
            String[] columns = value.split(",");
            for (int i = 0; i < columns.length; i++)
            {
                String columnStr = columns[i];
                int column = Integer.parseInt(columnStr);
                tableView.setColumnShrinkable(column, true);
            }
        }

    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"",null,null);
    }

}
