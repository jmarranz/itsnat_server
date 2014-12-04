package org.itsnat.droid.impl.browser;

import org.itsnat.droid.impl.dom.layout.XMLDOMLayout;

/**
 * Created by jmarranz on 27/10/14.
 */
public class PageRequestResult
{
    protected HttpRequestResultImpl httpReqResult;
    protected XMLDOMLayout domLayout;

    public PageRequestResult(HttpRequestResultImpl httpReqResult,XMLDOMLayout domLayout)
    {
        this.httpReqResult = httpReqResult;
        this.domLayout = domLayout;
    }

    public HttpRequestResultImpl getHttpRequestResultImpl()
    {
        return httpReqResult;
    }

    public XMLDOMLayout getXMLDOMLayout()
    {
        return domLayout;
    }
}
