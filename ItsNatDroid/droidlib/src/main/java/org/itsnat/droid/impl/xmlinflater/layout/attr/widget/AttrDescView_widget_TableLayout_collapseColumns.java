package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TableLayout_collapseColumns extends AttrDescView
{
    public AttrDescView_widget_TableLayout_collapseColumns(ClassDescViewBased parent)
    {
        super(parent,"collapseColumns");
    }

    public void setAttribute(final View view, final AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final TableLayout tableView = (TableLayout)view;

        if (oneTimeAttrProcess == null) // Si es no nulo es que estamos creando el TableLayout y no hace falta ésto
        {
            int maxColumns = getMaxColumns((TableLayout) view);
            for (int i = 0; i < maxColumns; i++)
            {
                if (tableView.isColumnCollapsed(i))
                    tableView.setColumnCollapsed(i, false);
            }
        }

        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                String value = attr.getValue();
                if ("".equals(value))
                {
                    int maxColumns = getMaxColumns((TableLayout) view);
                    for (int i = 0; i < maxColumns; i++)
                    {
                        if (tableView.isColumnCollapsed(i))
                            tableView.setColumnCollapsed(i, false);
                    }
                }
                else
                {
                    String[] columns = value.split(",");
                    for (int i = 0; i < columns.length; i++)
                    {
                        String columnStr = columns[i];
                        int column = Integer.parseInt(columnStr);
                        tableView.setColumnCollapsed(column, true);
                    }
                }
            }
        };
        if (pending != null)
            pending.addTask(task);
        else
            task.run();

    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view,"",xmlInflaterLayout,ctx,null,null);
    }

    private static int getMaxColumns(TableLayout view)
    {
        int maxColumns = 0;
        int childCount = view.getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            View child = view.getChildAt(i);
            if (child instanceof TableRow)
            {
                int columns = ((TableRow)child).getChildCount();
                if (columns > maxColumns) maxColumns = columns;
            }
        }
        return maxColumns;
    }

}
