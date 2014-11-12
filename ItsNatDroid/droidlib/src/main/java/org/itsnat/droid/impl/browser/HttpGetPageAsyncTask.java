package org.itsnat.droid.impl.browser;

import android.content.Context;

import org.apache.http.params.HttpParams;
import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.AttrParsedRemote;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.model.layout.ScriptParsed;
import org.itsnat.droid.impl.model.layout.ScriptRemoteParsed;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;

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
    protected final AttrDrawableInflaterListener inflateDrawableListener;
    protected final Context ctx; // No hay problemas de hilos, únicamente se pasa a un objeto resultado y dicho objeto no hace nada con él durante la ejecución del hilo

    public HttpGetPageAsyncTask(PageRequestImpl pageRequest,String url,
                    HttpParams httpParamsRequest)
    {
        ItsNatDroidImpl itsNatDroid = pageRequest.getItsNatDroidBrowserImpl().getItsNatDroidImpl();
        this.itsNatDroid = itsNatDroid;
        this.pageRequest = pageRequest;
        this.url = url;
        this.pageURLBase = pageRequest.getURLBase();
        this.xmlInflateRegistry = itsNatDroid.getXMLInflateRegistry();
        this.inflateDrawableListener = pageRequest.getAttrDrawableInflaterListener();
        this.ctx = pageRequest.getContext();

        // Hay que tener en cuenta que estos objetos se acceden en multihilo
        this.httpConfig = new HttpConfig(pageRequest);
    }

    protected PageRequestResult executeInBackground() throws Exception
    {
        HttpRequestResultImpl result = HttpUtil.httpGet(url,httpConfig.httpContext,httpConfig.httpParamsRequest,httpConfig.httpParamsDefault,httpConfig.httpHeaders,httpConfig.sslSelfSignedAllowed,null,null);

        LayoutParsed layoutParsed = null;
        if (result.isStatusOK())
        {
            String markup = result.getResponseText();
            String itsNatServerVersion = result.getItsNatServerVersion();
            layoutParsed = xmlInflateRegistry.getLayoutParsedCache(markup,itsNatServerVersion);
        }

        PageRequestResult pageReqResult = new PageRequestResult(result, layoutParsed);

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
        {
            HttpResourceDownloader resDownloader =
                    new HttpResourceDownloader(pageURLBase,httpConfig.httpContext,httpConfig.httpParamsRequest,httpConfig.httpParamsDefault,httpConfig.httpHeaders,httpConfig.sslSelfSignedAllowed,xmlInflateRegistry);
            resDownloader.downloadResources(attrRemoteList);
        }

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
        HttpRequestResultImpl result = HttpUtil.httpGet(src,httpConfig.httpContext,httpConfig.httpParamsRequest,httpConfig.httpParamsDefault,httpConfig.httpHeaders,httpConfig.sslSelfSignedAllowed, null,HttpUtil.MIME_BEANSHELL);
        return result.getResponseText();
    }

}
