package org.itsnat.droid.impl.browser;

import android.content.res.AssetManager;

import org.apache.http.params.HttpParams;
import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpGetPageAsyncTask extends ProcessingAsyncTask<PageRequestResult>
{
    protected final ItsNatDroidImpl itsNatDroid; // No hay problemas de hilos, únicamente se pasa a un objeto resultado y dicho objeto no hace nada con él durante la ejecución del hilo
    protected final PageRequestImpl pageRequest;
    protected final String url;
    protected final String pageURLBase;
    protected final HttpConfig httpConfig;
    protected final XMLInflateRegistry xmlInflateRegistry;
    protected final AttrDrawableInflaterListener attrDrawableInflaterListener;
    protected final AssetManager assetManager;

    public HttpGetPageAsyncTask(PageRequestImpl pageRequest, String url, HttpParams httpParamsRequest)
    {
        ItsNatDroidBrowserImpl browser = pageRequest.getItsNatDroidBrowserImpl();
        ItsNatDroidImpl itsNatDroid = browser.getItsNatDroidImpl();

        this.itsNatDroid = itsNatDroid;
        this.pageRequest = pageRequest;
        this.url = url;
        this.pageURLBase = pageRequest.getURLBase();
        this.xmlInflateRegistry = itsNatDroid.getXMLInflateRegistry();
        this.attrDrawableInflaterListener = pageRequest.getAttrDrawableInflaterListener();
        this.assetManager = pageRequest.getContext().getAssets();

        // Hay que tener en cuenta que estos objetos se acceden en multihilo
        this.httpConfig = new HttpConfig(pageRequest);
    }

    protected PageRequestResult executeInBackground() throws Exception
    {
        HttpRequestResultOKImpl result = HttpUtil.httpGet(url,httpConfig.httpFileCache, httpConfig.httpContext, httpConfig.httpParamsRequest, httpConfig.httpParamsDefault, httpConfig.httpHeaders, httpConfig.sslSelfSignedAllowed, null, null);

        PageRequestResult pageReqResult = PageRequestImpl.processHttpRequestResult(result,pageURLBase,httpConfig,xmlInflateRegistry,assetManager);
        return pageReqResult;
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
                errorListener.onError(ex, pageRequest,result.getHttpRequestResultOKImpl()); // Para poder recogerla desde fuera
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
