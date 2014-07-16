package org.itsnat.droid.impl.browser;

import android.content.Context;

import org.apache.http.params.HttpParams;
import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.OnPageListener;
import org.itsnat.droid.PageRequest;
import org.itsnat.droid.impl.xmlinflater.InflateRequestImpl;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

/**
 * Created by jmarranz on 5/06/14.
 */
public class PageRequestImpl implements PageRequest
{
    protected XMLLayoutInflateService inflateService;
    protected ItsNatDroidBrowserImpl browser;
    protected Context ctx;
    protected HttpParams httpParams;
    protected OnPageListener pageListener;
    protected OnPageLoadErrorListener errorListener;
    protected AttrCustomInflaterListener inflateListener;

    public PageRequestImpl(ItsNatDroidBrowserImpl browser,XMLLayoutInflateService inflateService)
    {
        this.browser = browser;
        this.inflateService = inflateService;
    }

    /*
    public XMLLayoutInflateService getXMLLayoutInflateService()
    {
        return inflateService;
    }
*/

    @Override
    public PageRequest setContext(Context ctx)
    {
        this.ctx = ctx;
        return this;
    }

    @Override
    public PageRequest setOnPageListener(OnPageListener pageListener)
    {
        this.pageListener = pageListener;
        return this;
    }

    @Override
    public PageRequest setOnPageLoadErrorListener(OnPageLoadErrorListener errorListener)
    {
        this.errorListener = errorListener;
        return this;
    }

    @Override
    public PageRequest setAttrCustomInflaterListener(AttrCustomInflaterListener inflateListener)
    {
        this.inflateListener = inflateListener;
        return this;
    }

    @Override
    public PageRequest setHttpParams(HttpParams httpParams)
    {
        this.httpParams = httpParams;
        return this;
    }

    @Override
    public void execute(String url)
    {
        // Pasamos una copia y no usamos los atributos porque nos interesa que el PageRequestImpl sea reutilizado si quiere el usuario
        execute(this,browser,url,httpParams,pageListener,errorListener,inflateListener,ctx);
    }

    public static void execute(final PageRequestImpl pageRequest,final ItsNatDroidBrowserImpl browser, String url,HttpParams httpParamsRequest,final OnPageListener pageListener,final OnPageLoadErrorListener errorListener, final AttrCustomInflaterListener inflateListener,final Context ctx)
    {
        HttpGetAsyncTask task = new HttpGetAsyncTask(url,browser.getHttpContext(), httpParamsRequest, browser.getHttpParams(),browser.isSSLSelfSignedAllowed())
        {
            @Override
            protected void onFinishOk(String result)
            {
                try
                {
                    StringReader input = new StringReader(result);

                    InflateRequestImpl inflateRequest = new InflateRequestImpl(browser.getItsNatDroidImpl());
                    inflateRequest.setContext(ctx);
                    if (inflateListener != null) inflateRequest.setAttrCustomInflaterListener(inflateListener);

                    String[] scriptArr = new String[1];
                    InflatedLayoutImpl inflated = inflateRequest.inflateInternal(input, scriptArr);

                    String loadScript = scriptArr[0];
                    PageImpl page = new PageImpl(browser, url,httpParamsRequest, inflated, result, loadScript);
                    pageListener.onPage(page);
                }
                catch(Exception ex)
                {
                    if (errorListener != null)
                    {
                        errorListener.onError(ex, pageRequest); // Para poder recogerla desde fuera
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
                if (errorListener != null)
                {
                    errorListener.onError(ex, pageRequest); // Para poder recogerla desde fuera
                    return;
                }
                else
                {
                    if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException)ex;
                    else throw new ItsNatDroidException(ex);
                }
            }
        };
        task.execute();
    }
}
