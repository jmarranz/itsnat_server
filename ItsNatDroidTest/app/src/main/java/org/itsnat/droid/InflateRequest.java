package org.itsnat.droid;

import android.content.Context;
import android.view.View;

import java.io.InputStream;

/**
 * Created by jmarranz on 15/06/14.
 */
public interface InflateRequest
{
    public InflateRequest setContext(Context ctx);
    public InflateRequest setAttrCustomInflaterListener(AttrCustomInflaterListener listener);
    public InflatedLayout inflate(InputStream input);
}
