package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;

/**
 * Created by jmarranz on 4/11/14.
 */
public abstract class XMLInflater
{
    protected Context ctx;

    protected XMLInflater(Context ctx)
    {
        this.ctx = ctx;
    }

    public Context getContext()
    {
        return ctx;
    }
}
