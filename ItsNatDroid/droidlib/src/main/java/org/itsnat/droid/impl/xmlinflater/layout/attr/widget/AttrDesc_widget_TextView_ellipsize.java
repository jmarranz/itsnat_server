package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_ellipsize extends AttrDesc
{
    private static Map<String,TextUtils.TruncateAt> valueMap = new HashMap<String,TextUtils.TruncateAt>( 5 );
    static
    {
        valueMap.put("none", null);
        valueMap.put("start", TextUtils.TruncateAt.START);
        valueMap.put("middle", TextUtils.TruncateAt.MIDDLE);
        valueMap.put("end", TextUtils.TruncateAt.END);
        valueMap.put("marquee", TextUtils.TruncateAt.MARQUEE);
    }

    public AttrDesc_widget_TextView_ellipsize(ClassDescViewBased parent)
    {
        super(parent,"ellipsize");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        TextUtils.TruncateAt convValue = AttrDesc.<TextUtils.TruncateAt>parseSingleName(value,valueMap);

        ((TextView)view).setEllipsize(convValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view,"@null",null,null);
    }
}
