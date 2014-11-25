package org.itsnat.droid.impl.browser;

import android.content.res.AssetManager;

import org.apache.http.params.HttpParams;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnHttpRequestErrorListener;
import org.itsnat.droid.OnHttpRequestListener;
import org.itsnat.droid.impl.dom.DOMAttrRemote;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpDownloadResourcesAsyncTask extends ProcessingAsyncTask<List<HttpRequestResultImpl>>
{
    protected List<DOMAttrRemote> attrRemoteList;
    protected DownloadResourcesHttpClient parent;
    protected String method;
    protected String pageURLBase;
    protected HttpConfig httpConfig;
    protected OnHttpRequestListener listener;
    protected OnHttpRequestErrorListener errorListener;
    protected int errorMode;
    protected XMLInflateRegistry xmlInflateRegistry;
    protected AssetManager assetManager;

    public HttpDownloadResourcesAsyncTask(List<DOMAttrRemote> attrRemoteList,DownloadResourcesHttpClient parent, String method, String pageURLBase, HttpParams httpParamsRequest, OnHttpRequestListener listener, OnHttpRequestErrorListener errorListener, int errorMode,AssetManager assetManager)
    {
        PageImpl page = parent.getPageImpl();

        this.attrRemoteList = attrRemoteList;
        this.parent = parent;
        this.method = method;
        this.pageURLBase = pageURLBase;
        this.httpConfig = new HttpConfig(page);
        this.listener = listener;
        this.errorListener = errorListener;
        this.errorMode = errorMode;
        this.xmlInflateRegistry = page.getItsNatDroidBrowserImpl().getItsNatDroidImpl().getXMLInflateRegistry();
        this.assetManager = assetManager;
    }

    protected List<HttpRequestResultImpl> executeInBackground() throws Exception
    {
        HttpResourceDownloader resDownloader =
                new HttpResourceDownloader(pageURLBase,httpConfig.httpContext,httpConfig.httpParamsRequest,httpConfig.httpParamsDefault,httpConfig.httpHeaders,httpConfig.sslSelfSignedAllowed,xmlInflateRegistry,assetManager);
        List<HttpRequestResultImpl> resultList = new LinkedList<HttpRequestResultImpl>();
        resDownloader.downloadResources(attrRemoteList,resultList);
        return resultList;
    }

    @Override
    protected void onFinishOk(List<HttpRequestResultImpl> resultList)
    {
        for (HttpRequestResultImpl result : resultList)
        {
            try
            {
                parent.processResult(result, listener, errorMode);
            }
            catch (Exception ex)
            {
                if (errorListener != null)
                {
                    errorListener.onError(parent.getPageImpl(), ex, result);
                    return;
                }
                else
                {
                    if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException) ex;
                    else throw new ItsNatDroidException(ex);
                }
            }
        }
    }

    @Override
    protected void onFinishError(Exception ex)
    {
        ItsNatDroidException exFinal = parent.convertException(ex);

        if (errorListener != null)
        {
            HttpRequestResult result = (exFinal instanceof ItsNatDroidServerResponseException) ?
                    ((ItsNatDroidServerResponseException)exFinal).getHttpRequestResult() : null;
            errorListener.onError(parent.getPageImpl(),exFinal, result);
            return;
        }
        else
        {
            if (exFinal instanceof ItsNatDroidException) throw (ItsNatDroidException)exFinal;
            else throw new ItsNatDroidException(exFinal);
        }
    }
}

