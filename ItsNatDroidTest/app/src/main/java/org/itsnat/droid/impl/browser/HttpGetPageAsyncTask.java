package org.itsnat.droid.impl.browser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.OnPageLoadListener;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.InflateRequestImpl;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpGetPageAsyncTask extends ProcessingAsyncTask<String>
{
    protected PageRequestImpl pageRequest;
    protected String url;
    protected HttpContext httpContext;
    protected HttpParams httpParamsRequest;
    protected HttpParams httpParamsDefault;
    protected boolean sslSelfSignedAllowed;

    public HttpGetPageAsyncTask(PageRequestImpl pageRequest,String url, HttpContext httpContext, HttpParams httpParamsRequest, HttpParams httpParamsDefault, boolean sslSelfSignedAllowed)
    {
        this.pageRequest = pageRequest;
        this.url = url;
        this.httpContext = httpContext;
        this.httpParamsRequest = httpParamsRequest;
        this.httpParamsDefault = httpParamsDefault;
        this.sslSelfSignedAllowed = sslSelfSignedAllowed;
    }

    protected String executeInBackground() throws Exception
    {
        StatusLine[] status = new StatusLine[1];
        String[] encoding = new String[1];
        byte[] resultArr = HttpUtil.httpGet(url, httpContext, httpParamsRequest,httpParamsDefault,sslSelfSignedAllowed,status,encoding);
        String result = ValueUtil.toString(resultArr,encoding[0]);
        if (status[0].getStatusCode() != 200)
            throw new ItsNatDroidServerResponseException(status[0].getStatusCode(),status[0].getReasonPhrase(), result);

        return result;
    }

    @Override
    protected void onFinishOk(String result)
    {
        try
        {
            pageRequest.processResponse(url,result);
        }
        catch(Exception ex)
        {
            OnPageLoadErrorListener errorListener = pageRequest.getOnPageLoadErrorListener();
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
        OnPageLoadErrorListener errorListener = pageRequest.getOnPageLoadErrorListener();
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
