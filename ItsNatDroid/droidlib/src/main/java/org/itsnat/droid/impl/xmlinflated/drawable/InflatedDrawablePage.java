package org.itsnat.droid.impl.xmlinflated.drawable;

import android.content.Context;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.model.drawable.DrawableParsed;

/**
 * Created by jmarranz on 7/11/14.
 */
public class InflatedDrawablePage extends InflatedDrawable
{
    protected PageImpl page;

    public InflatedDrawablePage(PageImpl page, DrawableParsed drawableParsed, AttrDrawableInflaterListener inflateDrawableListener, Context ctx)
    {
        super(page.getItsNatDroidBrowserImpl().getItsNatDroidImpl(), drawableParsed, inflateDrawableListener, ctx);
        this.page = page;
    }

    public PageImpl getPageImpl()
    {
        return page;
    }
}
