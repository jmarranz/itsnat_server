package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.attr.Dimension;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TextView_textSize extends AttrDescView
{
    public AttrDescView_widget_TextView_textSize(ClassDescViewBased parent)
    {
        super(parent,"textSize");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        // Existe un setTextSize(int) pero vale la pena, así es perfecto
        TextView textView = (TextView)view;

        Dimension dim = getDimensionObject(value,view.getContext());

        textView.setTextSize(dim.getComplexUnit(),dim.getValue());
    }

    public void removeAttribute(View view)
    {
        // No se que hacer, poner a cero el texto no tiene sentido, se tendría que extraer del Theme actual, un follón y total será muy raro
    }
}
