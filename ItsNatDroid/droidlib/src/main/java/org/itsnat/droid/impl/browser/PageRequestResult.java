package org.itsnat.droid.impl.browser;

import org.itsnat.droid.impl.dom.layout.XMLDOMLayout;

/**
 * Created by jmarranz on 27/10/14.
 */
public class PageRequestResult
{
    protected HttpRequestResultOKImpl httpReqResult;
    protected XMLDOMLayout domLayout;

    public PageRequestResult(HttpRequestResultOKImpl httpReqResult,XMLDOMLayout domLayout)
    {
        this.httpReqResult = httpReqResult;
        this.domLayout = domLayout;
    }

    public HttpRequestResultOKImpl getHttpRequestResultOKImpl()
    {
        return httpReqResult;
    }

    public XMLDOMLayout getXMLDOMLayout()
    {
        return domLayout;
    }
}
