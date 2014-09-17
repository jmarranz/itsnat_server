package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.attr.Dimension;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_textSize extends AttrDesc
{
    public AttrDesc_widget_TextView_textSize(ClassDescViewBased parent)
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
