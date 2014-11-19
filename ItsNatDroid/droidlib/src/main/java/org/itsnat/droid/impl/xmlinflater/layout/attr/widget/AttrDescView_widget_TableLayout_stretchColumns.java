package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;

import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TableLayout_stretchColumns extends AttrDescView
{
    public AttrDescView_widget_TableLayout_stretchColumns(ClassDescViewBased parent)
    {
        super(parent,"stretchColumns");
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final TableLayout tableView = (TableLayout)view;

        if (oneTimeAttrProcess == null) // Si es no nulo es que estamos creando el TableLayout y no hace falta ésto
        {
            tableView.setStretchAllColumns(false);
        }

        String value = attr.getValue();
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

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view,"",xmlInflaterLayout,ctx,null,null);
    }


}
