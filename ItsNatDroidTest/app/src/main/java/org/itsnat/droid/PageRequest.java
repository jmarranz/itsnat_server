package org.itsnat.droid;

import android.content.Context;

import org.apache.http.params.HttpParams;

/**
 * Created by jmarranz on 5/06/14.
 */
public interface PageRequest
{
    public PageRequest setContext(Context ctx);
    public PageRequest setOnPageListener(OnPageListener listener);
    public PageRequest setOnPageLoadErrorListener(OnPageLoadErrorListener listener);
    public PageRequest setAttrCustomInflaterListener(AttrCustomInflaterListener listener);
    public PageRequest setHttpParams(HttpParams httpParams);
    public void execute(String url);
}
