package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.InflateRequest;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.browser.PageImpl;

import java.io.Reader;
import java.util.List;

/**
 * Created by jmarranz on 5/06/14.
 */
public class InflateRequestImpl implements InflateRequest
{
    protected ItsNatDroidImpl parent;
    protected Context ctx;
    protected AttrCustomInflaterListener inflateListener;

    public InflateRequestImpl(ItsNatDroidImpl parent)
    {
        this.parent = parent;
    }

    @Override
    public InflateRequest setContext(Context ctx)
    {
        this.ctx = ctx;
        return this;
    }

    @Override
    public InflateRequest setAttrCustomInflaterListener(AttrCustomInflaterListener inflateListener)
    {
        this.inflateListener = inflateListener;
        return this;
    }

    public AttrCustomInflaterListener getAttrCustomInflaterListener()
    {
        return inflateListener;
    }

    public Context getContext()
    {
        return ctx;
    }

    @Override
    public InflatedLayout inflate(Reader input)
    {
        return inflateInternal(input,null,null,null);
    }

    public InflatedLayoutImpl inflateInternal(Reader input,String[] loadScript,List<String> scriptList,PageImpl page)
    {
        InflatedLayoutImpl inflated = page != null ? new InflatedLayoutPageImpl(page,inflateListener,ctx) :
                                                     new InflatedLayoutStandaloneImpl(parent,inflateListener,ctx);
        parent.getXMLLayoutInflateService().inflate(input, loadScript,scriptList,inflated);
        return inflated;
    }

}
