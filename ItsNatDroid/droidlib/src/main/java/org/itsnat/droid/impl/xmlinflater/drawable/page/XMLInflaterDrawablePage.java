package org.itsnat.droid.impl.xmlinflater.drawable.page;

import android.content.Context;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawablePage;
import org.itsnat.droid.impl.xmlinflater.XMLInflaterPage;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

/**
 * Created by jmarranz on 24/11/14.
 */
public class XMLInflaterDrawablePage extends XMLInflaterDrawable implements XMLInflaterPage
{
    protected PageImpl page;

    public XMLInflaterDrawablePage(InflatedDrawablePage inflatedXML,int bitmapDensityReference,AttrLayoutInflaterListener attrLayoutInflaterListener,AttrDrawableInflaterListener attrDrawableInflaterListener, Context ctx,PageImpl page)
    {
        super(inflatedXML,bitmapDensityReference,attrLayoutInflaterListener,attrDrawableInflaterListener, ctx);
        this.page = page;
    }

    public PageImpl getPageImpl()
    {
        return page;
    }
}
