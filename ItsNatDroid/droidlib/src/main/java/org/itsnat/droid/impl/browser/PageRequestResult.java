package org.itsnat.droid.impl.browser;

import org.itsnat.droid.impl.parser.LayoutParser;
import org.itsnat.droid.impl.parser.LayoutParserPage;
import org.itsnat.droid.impl.parser.TreeViewParsed;

/**
 * Created by jmarranz on 27/10/14.
 */
public class PageRequestResult
{
    protected HttpRequestResultImpl httpReqResult;
    protected TreeViewParsed treeViewParsed;

    public PageRequestResult(HttpRequestResultImpl httpReqResult)
    {
        this.httpReqResult = httpReqResult;

        String markup = httpReqResult.getResponseText();
        String itsNatServerVersion = httpReqResult.getItsNatServerVersion();
        boolean loadingPage = true;
        LayoutParser layoutParser = new LayoutParserPage(itsNatServerVersion, loadingPage);
        this.treeViewParsed = layoutParser.inflate(markup);
    }

    public HttpRequestResultImpl getHttpRequestResult()
    {
        return httpReqResult;
    }

    public TreeViewParsed getTreeViewParsed()
    {
        return treeViewParsed;
    }
}
