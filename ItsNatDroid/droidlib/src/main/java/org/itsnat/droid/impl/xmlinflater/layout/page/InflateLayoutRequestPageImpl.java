package org.itsnat.droid.impl.xmlinflater.layout.page;

import android.content.Context;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.PageRequestImpl;
import org.itsnat.droid.impl.xmlinflater.layout.InflateLayoutRequestImpl;

/**
 * Created by jmarranz on 5/06/14.
 */
public class InflateLayoutRequestPageImpl extends InflateLayoutRequestImpl
{
    protected PageImpl page;

    public InflateLayoutRequestPageImpl(PageImpl page)
    {
        super(getItsNatDroidImpl(page));
        this.page = page;
    }

    public static ItsNatDroidImpl getItsNatDroidImpl(PageImpl page)
    {
        PageRequestImpl pageRequest = page.getPageRequestImpl();
        ItsNatDroidBrowserImpl browser = pageRequest.getItsNatDroidBrowserImpl();
        ItsNatDroidImpl itsNatDroid = browser.getItsNatDroidImpl();
        return itsNatDroid;
    }

    public String getEncoding()
    {
        return page.getHttpRequestResultImpl().getEncoding();
    }

    public int getBitmapDensityReference()
    {
        return page.getBitmapDensityReference();
    }

    public AttrLayoutInflaterListener getAttrLayoutInflaterListener()
    {
        return page.getPageRequestImpl().getAttrLayoutInflaterListener();
    }

    public AttrDrawableInflaterListener getAttrDrawableInflaterListener()
    {
        return page.getPageRequestImpl().getAttrDrawableInflaterListener();
    }

    public Context getContext()
    {
        return page.getPageRequestImpl().getContext();
    }

}
