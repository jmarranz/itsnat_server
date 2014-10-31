package org.itsnat.droid.impl.browser;

import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.parser.layout.LayoutParsedCache;
import org.itsnat.droid.impl.parser.layout.LayoutParser;
import org.itsnat.droid.impl.parser.layout.LayoutParserPage;

/**
 * Created by jmarranz on 27/10/14.
 */
public class PageRequestResult
{
    protected HttpRequestResultImpl httpReqResult;
    protected LayoutParsed layoutParsed;

    public PageRequestResult(HttpRequestResultImpl httpReqResult,LayoutParsedCache layoutParsedCache)
    {
        this.httpReqResult = httpReqResult;

        if (httpReqResult.isStatusOK())
        {
            String markup = httpReqResult.getResponseText();

            LayoutParsed cachedLayout = layoutParsedCache.get(markup);
            if (cachedLayout != null) this.layoutParsed = cachedLayout;
            else
            {
                String itsNatServerVersion = httpReqResult.getItsNatServerVersion();
                boolean loadingPage = true;
                LayoutParser layoutParser = new LayoutParserPage(itsNatServerVersion, loadingPage);
                this.layoutParsed = layoutParser.inflate(markup);
                layoutParsedCache.put(markup, layoutParsed);
            }
        }
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
