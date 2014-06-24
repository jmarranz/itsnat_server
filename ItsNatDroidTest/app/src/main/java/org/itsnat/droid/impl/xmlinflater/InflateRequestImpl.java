package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.InflateRequest;
import org.itsnat.droid.InflatedLayout;

import java.io.InputStream;

/**
 * Created by jmarranz on 5/06/14.
 */
public class InflateRequestImpl implements InflateRequest
{
    protected Context ctx;
    protected AttrCustomInflaterListener inflateListener;

    public InflateRequestImpl()
    {
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
    public InflatedLayout inflate(InputStream input)
    {
        return inflateInternal(input,new String[1]);
    }

    public InflatedLayoutImpl inflateInternal(InputStream input,String[] code)
    {
        InflatedLayoutImpl inflated = new InflatedLayoutImpl(inflateListener,ctx);
        XMLLayoutInflater.inflate(input,code,inflated);
        return inflated;
    }
}
