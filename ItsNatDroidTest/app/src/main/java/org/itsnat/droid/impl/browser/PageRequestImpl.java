package org.itsnat.droid.impl.browser;

import android.content.Context;

import org.apache.http.params.HttpParams;
import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.OnErrorListener;
import org.itsnat.droid.OnPageListener;
import org.itsnat.droid.PageRequest;
import org.itsnat.droid.impl.xmlinflater.InflateRequestImpl;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;

import java.io.ByteArrayInputStream;

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
    protected OnErrorListener errorListener;
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
    public PageRequest setOnErrorListener(OnErrorListener errorListener)
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
        execute(browser,url,httpParams,pageListener,errorListener,inflateListener,ctx);
    }

    public static void execute(ItsNatDroidBrowserImpl browser, String url,HttpParams httpParamsRequest,final OnPageListener pageListener,final OnErrorListener errorListener, final AttrCustomInflaterListener inflateListener,final Context ctx)
    {
        DownloadTask task = new DownloadTask(browser,httpParamsRequest,url)
        {
            @Override
            protected void onFinishOk(byte[] result)
            {
                try
                {
                    ByteArrayInputStream input = new ByteArrayInputStream(result);

                    InflateRequestImpl inflateRequest = new InflateRequestImpl(browser.getItsNatDroidImpl());
                    inflateRequest.setContext(ctx);
                    if (inflateListener != null) inflateRequest.setAttrCustomInflaterListener(inflateListener);

                    String[] scriptArr = new String[1];
                    InflatedLayoutImpl inflated = inflateRequest.inflateInternal(input, scriptArr);
                    String loadScript = scriptArr[0];
                    PageImpl page = new PageImpl(browser, url, inflated, result, loadScript);
                    pageListener.onPage(page);
                }
                catch(Exception ex)
                {
                    if (errorListener != null) errorListener.onError(ex); // Para poder recogerla desde fuera
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
                if (errorListener != null) errorListener.onError(ex);
            }
        };
        task.execute();
    }
}
