package org.itsnat.droid.impl.browser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by jmarranz on 23/05/14.
 */
public class HttpUtil
{
    private static HttpParams getHttpParams(HttpParams httpParamsRequest, HttpParams httpParamsDefault)
    {
        return httpParamsRequest != null ? new DefaultedHttpParams(httpParamsRequest,httpParamsDefault) : httpParamsDefault;
    }

    public static HttpClient createHttpClient(URI uri,boolean sslSelfSignedAllowed,HttpParams httpParams)
    {
        if (sslSelfSignedAllowed && uri.getScheme().equals("https"))
            return getHttpClientSSLSelfSignedAllowed(httpParams);
        return new DefaultHttpClient(httpParams);
    }

    public static byte[] httpGet(String url, HttpContext httpContext, HttpParams httpParamsRequest, HttpParams httpParamsDefault,boolean sslSelfSignedAllowed,StatusLine[] status) throws SocketTimeoutException
    {
        URI uri;
        try { uri = new URI(url); }
        catch (URISyntaxException ex) { throw new ItsNatDroidException(ex); }

        HttpParams httpParams = getHttpParams(httpParamsRequest, httpParamsDefault);

        HttpClient httpClient = createHttpClient(uri,sslSelfSignedAllowed,httpParams);

                // Prepare a request object
        HttpGet httpGet = new HttpGet(uri);

        return execute(httpClient,httpGet,httpContext,status);
    }

    public static byte[] httpPost(String url, HttpContext httpContext, HttpParams httpParamsRequest, HttpParams httpParamsDefault,boolean sslSelfSignedAllowed,List<NameValuePair> nameValuePairs,StatusLine[] status) throws SocketTimeoutException
    {
        URI uri;
        try { uri = new URI(url); }
        catch (URISyntaxException ex) { throw new ItsNatDroidException(ex); }

        HttpParams httpParams = getHttpParams(httpParamsRequest, httpParamsDefault);

        HttpClient httpClient = createHttpClient(uri,sslSelfSignedAllowed,httpParams);

        HttpPost httpPost = new HttpPost(url);

        //httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        try
        {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }
        catch (UnsupportedEncodingException ex) { throw new ItsNatDroidException(ex); }

        return execute(httpClient,httpPost,httpContext,status);
    }

    private static byte[] execute(HttpClient httpClient,HttpUriRequest httpUriRequest,HttpContext httpContext,StatusLine[] status) throws SocketTimeoutException
    {
        try
        {
            // Para evitar cacheados (en el caso de GET) por si acaso
            // http://stackoverflow.com/questions/49547/making-sure-a-web-page-is-not-cached-across-all-browsers
            httpUriRequest.setHeader("If-Modified-Since","Wed, 15 Nov 1995 00:00:00 GMT");
            httpUriRequest.setHeader("Cache-Control","no-store,no-cache,must-revalidate");
            httpUriRequest.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            httpUriRequest.setHeader("Expires","0"); // Proxies.

            HttpResponse response = httpClient.execute(httpUriRequest, httpContext);

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release

            status[0] = response.getStatusLine();

            if (entity == null) return null;

            InputStream input = entity.getContent(); // Interesa incluso cuando hay error (statusCode != 200)
            return read(input);
        }
        catch(SocketTimeoutException ex)
        {
            throw ex; // Nos interesa tratarlo explícitamente y por tanto NO envolverlo en un ItsNatDroidException
        }
        catch(ClientProtocolException ex)
        {
            throw new ItsNatDroidException(ex);
        }
        catch(IOException ex)
        {
            throw new ItsNatDroidException(ex);
        }
    }


    public static byte[] read(InputStream input)
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream(20*1024);
        byte[] buffer = new byte[10*1024];
        int read = 0;
        try
        {
            read = input.read(buffer);
            while (read != -1)
            {
                output.write(buffer, 0, read);
                read = input.read(buffer);
            }
        }
        catch (IOException ex)
        {
            throw new ItsNatDroidException(ex);
        }
        finally
        {
            try { input.close(); }
            catch (IOException ex2) { throw new ItsNatDroidException(ex2); }
        }
        return output.toByteArray();
    }

    private static class SSLSocketFactoryForSelfSigned extends SSLSocketFactory
    {
        private SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryForSelfSigned(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
        {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
                public X509Certificate[] getAcceptedIssuers() { return null; }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException
        {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    public static HttpClient getHttpClientSSLSelfSignedAllowed(HttpParams params)
    {
        // URLs para probar: "https://www.pcwebshop.co.uk/" "https://mms.nw.ru/"
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryForSelfSigned(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            //HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            //HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        }
        catch (KeyStoreException ex) { throw new ItsNatDroidException(ex); }
        catch (NoSuchAlgorithmException ex) { throw new ItsNatDroidException(ex); }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }
        catch (CertificateException ex) { throw new ItsNatDroidException(ex); }
        catch (KeyManagementException ex) { throw new ItsNatDroidException(ex); }
        catch (UnrecoverableKeyException ex) { throw new ItsNatDroidException(ex); }
    }


    private static String convertStreamToString_NO_SE_USA(InputStream is)
    {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        }
        catch (IOException ex)
        {
            throw new ItsNatDroidException(ex);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException ex)
            {
                throw new ItsNatDroidException(ex);
            }
        }
        return sb.toString();
    }

}