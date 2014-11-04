package org.itsnat.droid.impl.browser;

import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.impl.model.AttrParsedRemote;
import org.itsnat.droid.impl.model.XMLParsed;
import org.itsnat.droid.impl.model.XMLParsedCache;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.model.layout.ScriptParsed;
import org.itsnat.droid.impl.model.layout.ScriptRemoteParsed;
import org.itsnat.droid.impl.parser.drawable.DrawableParser;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpGetPageAsyncTask extends ProcessingAsyncTask<PageRequestResult>
{
    protected PageRequestImpl pageRequest;
    protected String url;
    protected String pageURLBase;
    protected HttpContext httpContext;
    protected HttpParams httpParamsRequest;
    protected HttpParams httpParamsDefault;
    protected Map<String,String> httpHeaders;
    protected boolean sslSelfSignedAllowed;
    protected XMLParsedCache<LayoutParsed> layoutParsedCache; // Es un singleton con métodos sincronizados

    public HttpGetPageAsyncTask(PageRequestImpl pageRequest,String url,
                    HttpParams httpParamsRequest)
    {
        this.pageRequest = pageRequest;
        this.url = url;
        this.pageURLBase = pageRequest.getURLBase();
        this.layoutParsedCache = pageRequest.getItsNatDroidBrowserImpl().getItsNatDroidImpl().getXMLLayoutInflateService().getLayoutParsedCache();

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
        PageRequestResult pageReqResult = new PageRequestResult(result, layoutParsedCache);

        LayoutParsed layoutParsed = pageReqResult.getLayoutParsed();
        if (result.getItsNatServerVersion() == null)
        {
            // Página NO servida por ItsNat, tenemos que descargar los <script src="..."> remótamente
            ArrayList<ScriptParsed> scriptList = layoutParsed.getScriptList();
            for(int i = 0; i < scriptList.size(); i++)
            {
                ScriptParsed script = scriptList.get(i);
                if (script instanceof ScriptRemoteParsed)
                {
                    ScriptRemoteParsed scriptRemote = (ScriptRemoteParsed)script;
                    String code = downloadScript(scriptRemote.getSrc());
                    scriptRemote.setCode(code);
                }
            }
        }

        LinkedList<AttrParsedRemote> attrRemoteList = layoutParsed.getAttributeRemoteList();
        if (attrRemoteList != null)
            downloadRemoteResources(attrRemoteList);

        return pageReqResult;
    }

    protected void downloadRemoteResources(LinkedList<AttrParsedRemote> attrRemoteList) throws Exception
    {
        final Thread[] threadArray = new Thread[attrRemoteList.size()];
        final Exception[] exList = new Exception[attrRemoteList.size()];

        {
            int i = 0;
            final boolean[] stop = new boolean[1];
            for (final AttrParsedRemote attr : attrRemoteList)
            {
                final String resourceMime = attr.getResourceMime();
                final String resourceType = attr.getResourceType();
                final int i2 = i;
                final String url = HttpUtil.composeAbsoluteURL(attr.getRemoteLocation(), pageURLBase);
                Thread thread = new Thread()
                {
                    public void run()
                    {
                        if (stop[0]) return;
                        HttpRequestResultImpl resultRes = null;
                        try
                        {
                            resultRes = HttpUtil.httpGet(url, httpContext, httpParamsRequest, httpParamsDefault, httpHeaders, sslSelfSignedAllowed, null, resourceMime);
                            if (HttpUtil.MIME_XML.equals(resourceMime))
                            {
                                String markup = resultRes.getResponseText();
                                XMLParsed parsed;
                                if ("drawable".equals(resourceType))
                                {
                                    parsed = DrawableParser.parse(markup);
                                }
                                else throw new ItsNatDroidException("Unsupported resource type as remote: " + resourceType);

                                attr.setRemoteResource(parsed);
                                LinkedList<AttrParsedRemote> attrRemoteList = parsed.getAttributeRemoteList();
                                if (attrRemoteList != null)
                                    downloadRemoteResources(attrRemoteList);
                            }
                            else if (HttpUtil.MIME_PNG.equals(resourceMime) ||
                                     HttpUtil.MIME_JPEG.equals(resourceMime) ||
                                     HttpUtil.MIME_GIF.equals(resourceMime))
                            {
                                attr.setRemoteResource(resultRes.getResponseByteArray());
                            }
                            else throw new ItsNatDroidException("Unsupported resource mime: " + resourceMime);
                        }
                        catch (Exception ex)
                        {
                            exList[i2] = ex;
                            stop[0] = true;
                        }
                    }
                };
                thread.start();
                threadArray[i] = thread;
                i++;
            }
        }

        {
            for (int i = 0; i < threadArray.length; i++)
            {
                threadArray[i].join();
                if (exList[i] != null)
                    throw exList[i];
            }
        }
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


    private String downloadScript(String src) throws SocketTimeoutException
    {
        src = HttpUtil.composeAbsoluteURL(src,pageURLBase);
        HttpRequestResultImpl result = HttpUtil.httpGet(src, httpContext, httpParamsRequest, httpParamsDefault, httpHeaders, sslSelfSignedAllowed, null,HttpUtil.MIME_BEANSHELL);
        return result.getResponseText();
    }

}
