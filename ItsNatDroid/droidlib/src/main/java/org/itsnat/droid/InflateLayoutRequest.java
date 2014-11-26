package org.itsnat.droid;

import android.content.Context;

import java.io.Reader;

/**
 * Created by jmarranz on 15/06/14.
 */
public interface InflateLayoutRequest
{
    public InflateLayoutRequest setContext(Context ctx);
    public InflateLayoutRequest setEncoding(String encoding);
    public InflateLayoutRequest setAttrLayoutInflaterListener(AttrLayoutInflaterListener listener);
    public InflateLayoutRequest setAttrDrawableInflaterListener(AttrDrawableInflaterListener inflateDrawableListener);
    public InflatedLayout inflate(Reader input);
}
