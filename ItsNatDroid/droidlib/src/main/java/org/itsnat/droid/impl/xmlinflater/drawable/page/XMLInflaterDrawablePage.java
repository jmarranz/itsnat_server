package org.itsnat.droid.impl.xmlinflater.drawable.page;

import android.content.Context;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawablePage;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

/**
 * Created by jmarranz on 24/11/14.
 */
public class XMLInflaterDrawablePage extends XMLInflaterDrawable
{
    protected PageImpl page;

    public XMLInflaterDrawablePage(InflatedDrawablePage inflatedDrawable,AttrDrawableInflaterListener inflateDrawableListener, Context ctx,PageImpl page)
    {
        super(inflatedDrawable,inflateDrawableListener, ctx);
        this.page = page;
    }

    public PageImpl getPageImpl()
    {
        return page;
    }
}
