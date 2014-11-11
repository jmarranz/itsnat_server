package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_TextView_ellipsize extends AttrDescView
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

    public AttrDescView_widget_TextView_ellipsize(ClassDescViewBased parent)
    {
        super(parent,"ellipsize");
    }

    public void setAttribute(View view, AttrParsed attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        TextUtils.TruncateAt convValue = AttrDescView.<TextUtils.TruncateAt>parseSingleName(attr.getValue(), valueMap);

        ((TextView)view).setEllipsize(convValue);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setAttribute(view,"@null",xmlInflaterLayout,ctx,null,null);
    }
}
