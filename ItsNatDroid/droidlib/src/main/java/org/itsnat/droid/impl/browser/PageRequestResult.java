package org.itsnat.droid.impl.browser;

import org.itsnat.droid.impl.dom.layout.LayoutParsed;

/**
 * Created by jmarranz on 27/10/14.
 */
public class PageRequestResult
{
    protected HttpRequestResultImpl httpReqResult;
    protected LayoutParsed layoutParsed;

    public PageRequestResult(HttpRequestResultImpl httpReqResult,LayoutParsed layoutParsed)
    {
        this.httpReqResult = httpReqResult;
        this.layoutParsed = layoutParsed;
    }

    public HttpRequestResultImpl getHttpRequestResult()
    {
        return httpReqResult;
    }

    public LayoutParsed getLayoutParsed()
    {
        return layoutParsed;
    }
}
