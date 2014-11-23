package org.itsnat.droid.impl.xmlinflated.drawable;

import android.content.Context;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.dom.drawable.XMLDOMDrawable;

/**
 * Created by jmarranz on 10/11/14.
 */
public class InflatedDrawablePage extends InflatedDrawable
{
    protected PageImpl page;

    public InflatedDrawablePage(PageImpl page,XMLDOMDrawable xmlDOMDrawable,Context ctx)
    {
        // Este constructor puede llegar a ejecutarse en un hilo NO UI, no hacer nada más
        super(page.getItsNatDroidBrowserImpl().getItsNatDroidImpl(), xmlDOMDrawable, ctx);

        this.page = page;
    }

    public PageImpl getPageImpl()
    {
        return page;
    }

    public AttrDrawableInflaterListener getAttrDrawableInflaterListener()
    {
        return page.getPageRequestImpl().getAttrDrawableInflaterListener();
    }
}
