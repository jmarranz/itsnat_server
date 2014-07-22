package org.itsnat.droid;

import android.content.Context;
import android.view.View;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by jmarranz on 15/06/14.
 */
public interface InflateRequest
{
    public InflateRequest setContext(Context ctx);
    public InflateRequest setAttrCustomInflaterListener(AttrCustomInflaterListener listener);
    public InflatedLayout inflate(Reader input);
}
