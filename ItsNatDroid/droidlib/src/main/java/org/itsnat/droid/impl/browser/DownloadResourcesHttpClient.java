package org.itsnat.droid.impl.browser;

import android.os.AsyncTask;

import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatDocImpl;
import org.itsnat.droid.impl.dom.AttrParsedRemote;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 9/10/14.
 */
public class DownloadResourcesHttpClient extends GenericHttpClientBaseImpl
{
    public DownloadResourcesHttpClient(ItsNatDocImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public List<HttpRequestResultImpl> request(AttrParsedRemote attrRemote,boolean async)
    {
        List<AttrParsedRemote> attrRemoteList = new LinkedList<AttrParsedRemote>();
        attrRemoteList.add(attrRemote);

        return request(attrRemoteList,async);
    }

    public List<HttpRequestResultImpl> request(List<AttrParsedRemote> attrRemoteList,boolean async)
    {
        if (async)
        {
            requestAsync(attrRemoteList);
            return null;
        }
        else return requestSync(attrRemoteList);
    }

    public List<HttpRequestResultImpl> requestSync(List<AttrParsedRemote> attrRemoteList)
    {
        PageImpl page = getPageImpl();
        ItsNatDroidBrowserImpl browser = page.getItsNatDroidBrowserImpl();

        // No hace falta clonar porque es síncrona la llamada
        String url = getFinalURL();
        HttpContext httpContext = browser.getHttpContext();
        HttpParams httpParamsDefault = browser.getHttpParams();
        HttpParams httpParamsRequest = this.httpParamsRequest;
        Map<String,String> httpHeaders = page.getPageRequestImpl().createHttpHeaders();
        boolean sslSelfSignedAllowed = browser.isSSLSelfSignedAllowed();
        //List<NameValuePair> params = this.paramList;

        XMLInflateRegistry xmlInflateRegistry = browser.getItsNatDroidImpl().getXMLInflateRegistry();

        List<HttpRequestResultImpl> resultList = new LinkedList<HttpRequestResultImpl>();

        try
        {
            HttpResourceDownloader resDownloader =
                    new HttpResourceDownloader(url,httpContext,httpParamsRequest,httpParamsDefault,httpHeaders,sslSelfSignedAllowed,xmlInflateRegistry);
            resDownloader.downloadResources(attrRemoteList,resultList);
        }
        catch (Exception ex)
        {
            ItsNatDroidException exFinal = convertException(ex);
            throw exFinal;

            // No usamos aquí el OnEventErrorListener porque la excepción es capturada por un catch anterior que sí lo hace
        }

        for(HttpRequestResultImpl result : resultList)
            processResult(result,listener,errorMode);

        return resultList;
    }

    public void requestAsync(List<AttrParsedRemote> attrRemoteList)
    {
        String url = getFinalURL();
        HttpDownloadResourcesAsyncTask task = new HttpDownloadResourcesAsyncTask(attrRemoteList,this,method,url,httpParamsRequest,listener,errorListener,errorMode);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // Con execute() a secas se ejecuta en un "pool" de un sólo hilo sin verdadero paralelismo
    }

}
