package org.itsnat.droid.impl.browser;

import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.impl.parser.TreeViewParsedCache;

import java.util.Map;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpGetPageAsyncTask extends ProcessingAsyncTask<PageRequestResult>
{
    protected PageRequestImpl pageRequest;
    protected String url;
    protected HttpContext httpContext;
    protected HttpParams httpParamsRequest;
    protected HttpParams httpParamsDefault;
    protected Map<String,String> httpHeaders;
    protected boolean sslSelfSignedAllowed;
    protected TreeViewParsedCache treeViewParsedCache; // Es un singleton con métodos sincronizados

    public HttpGetPageAsyncTask(PageRequestImpl pageRequest,String url,
                    HttpParams httpParamsRequest)
    {
        this.pageRequest = pageRequest;
        this.url = url;
        this.treeViewParsedCache = pageRequest.getItsNatDroidBrowserImpl().getItsNatDroidImpl().getXMLLayoutInflateService().getTreeViewParsedCache();

        ItsNatDroidBrowserImpl browser = pageRequest.getItsNatDroidBrowserImpl();
        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsDefault = browser.getHttpParams();
        Map<String,String> httpHeaders = pageRequest.createHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();

        // Hay que tener en cuenta que estos objetos se acceden en multihilo
        this.httpContext = httpContext;
        this.httpParamsRequest = httpParamsRequest != null ? httpParamsRequest.copy() : null;
        this.httpParamsDefault = httpParamsDefault != null ? httpParamsDefault.copy() : null;
        this.httpHeaders = httpHeaders; // No hace falta clone porque createHttpHeaders() crea un Map
        this.sslSelfSignedAllowed = sslSelfSignedAllowed;
    }

    protected PageRequestResult executeInBackground() throws Exception
    {
        HttpRequestResultImpl result = HttpUtil.httpGet(url, httpContext, httpParamsRequest,httpParamsDefault, httpHeaders,sslSelfSignedAllowed,null,null);
        return new PageRequestResult(result,treeViewParsedCache);
    }

    @Override
    protected void onFinishOk(PageRequestResult result)
    {
        try
        {
            pageRequest.processResponse(result);
        }
        catch(Exception ex)
        {
            OnPageLoadErrorListener errorListener = pageRequest.getOnPageLoadErrorListener();
            if (errorListener != null)
            {
                errorListener.onError(ex, pageRequest,result.getHttpRequestResult()); // Para poder recogerla desde fuera
                return;
            }
            else
            {
                if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException)ex;
                else throw new ItsNatDroidException(ex);
            }
        }
    }

    @Override
    protected void onFinishError(Exception ex)
    {
        OnPageLoadErrorListener errorListener = pageRequest.getOnPageLoadErrorListener();
        if (errorListener != null)
        {
            HttpRequestResult result = (ex instanceof ItsNatDroidServerResponseException) ?
                    ((ItsNatDroidServerResponseException)ex).getHttpRequestResult() : null;

            if (errorListener != null) errorListener.onError(ex, pageRequest,result);
            return;
        }
        else
        {
            if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException)ex;
            else throw new ItsNatDroidException(ex);
        }
    }
}
