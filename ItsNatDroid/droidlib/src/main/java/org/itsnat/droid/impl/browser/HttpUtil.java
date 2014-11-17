package org.itsnat.droid.impl.browser;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
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
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.impl.util.IOUtil;
import org.itsnat.droid.impl.util.ValueUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by jmarranz on 23/05/14.
 */
public class HttpUtil
{
    // http://www.sitepoint.com/web-foundations/mime-types-complete-list/
    public static final String MIME_ANDROID_LAYOUT = "android/layout";
    public static final String MIME_BEANSHELL = "text/beanshell";   // Inventado obviamente
    public static final String MIME_JSON = "application/json";
    public static final String MIME_XML = "text/xml"; // No usamos application/xml aunque sea más correcto, pues en otro lugar al detectar "text/XXX" lo convertimos a texto
    public static final String MIME_PNG = "image/png";
    //public static final String MIME_PNG_9 = "image/png9"; // Inventada, si no se necesitara realmente eliminarla
    public static final String MIME_JPEG = "image/jpg"; // Válido en BitmapDrawable
    public static final String MIME_GIF = "image/gif"; // Válido en BitmapDrawable

    public static final Map<String,String> MIME_BY_EXT = new HashMap<String,String>(); // Como sólo se lee puede usarse en multihilo
    static
    {
        // http://www.sitepoint.com/web-foundations/mime-types-complete-list/
        MIME_BY_EXT.put("xml",HttpUtil.MIME_XML);
        MIME_BY_EXT.put("png",HttpUtil.MIME_PNG);
        //MIME_BY_EXT.put("9.png",HttpUtil.MIME_PNG);
        MIME_BY_EXT.put("jpg",HttpUtil.MIME_JPEG);
        MIME_BY_EXT.put("jpe",HttpUtil.MIME_JPEG);
        MIME_BY_EXT.put("jpeg",HttpUtil.MIME_JPEG);
        MIME_BY_EXT.put("gif",HttpUtil.MIME_GIF);

        // No es necesario "bs" ni "json" estos no se acceden remótamente, en el caso de .bs sí pero sabemos que es un script beanshell da igual la extensión
    }

    private static HttpParams getHttpParams(HttpParams httpParamsRequest, HttpParams httpParamsDefault)
    {
        return httpParamsRequest != null ? new DefaultedHttpParams(httpParamsRequest,httpParamsDefault) : httpParamsDefault;
    }

    public static HttpClient createHttpClient(String scheme,boolean sslSelfSignedAllowed,HttpParams httpParams)
    {
        if (sslSelfSignedAllowed && scheme.equals("https"))
            return getHttpClientSSLSelfSignedAllowed(httpParams);
        return new DefaultHttpClient(httpParams);
    }

    public static HttpRequestResultImpl httpGet(String url, HttpContext httpContext, HttpParams httpParamsRequest, HttpParams httpParamsDefault,Map<String,String> httpHeaders,boolean sslSelfSignedAllowed,List<NameValuePair> paramList,String overrideMime) throws SocketTimeoutException
    {
        return httpAction("GET",url,httpContext,httpParamsRequest,httpParamsDefault,httpHeaders,sslSelfSignedAllowed,paramList,overrideMime);
    }

    public static HttpRequestResultImpl httpPost(String url, HttpContext httpContext, HttpParams httpParamsRequest, HttpParams httpParamsDefault,Map<String,String> httpHeaders,boolean sslSelfSignedAllowed,List<NameValuePair> paramList,String overrideMime) throws SocketTimeoutException
    {
        return httpAction("POST",url,httpContext,httpParamsRequest,httpParamsDefault,httpHeaders,sslSelfSignedAllowed,paramList,overrideMime);
    }

    public static HttpRequestResultImpl httpAction(String method,String url, HttpContext httpContext, HttpParams httpParamsRequest, HttpParams httpParamsDefault,Map<String,String> httpHeaders,boolean sslSelfSignedAllowed,List<NameValuePair> paramList,String overrideMime) throws SocketTimeoutException
    {
        URI uri;
        try { uri = new URI(url); }
        catch (URISyntaxException ex) { throw new ItsNatDroidException(ex); }

        HttpParams httpParams = getHttpParams(httpParamsRequest, httpParamsDefault);

        HttpClient httpClient = createHttpClient(uri.getScheme(),sslSelfSignedAllowed,httpParams);

        HttpUriRequest httpUriRequest = null;

        method = method.toUpperCase(); // Se especifica que sea en mayúsculas pero por si acaso
        if ("POST".equals(method) || "PUT".equals(method)) // PATCH no está implementado (sería HttpPatch)
        {
            if ("POST".equals(method))
                httpUriRequest = new HttpPost(url);
            else // PUT
                httpUriRequest = new HttpPut(url); // http://stackoverflow.com/questions/3649814/android-httpput-example-code
            // httpUriRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            try
            {
                if (paramList == null) paramList = new LinkedList<NameValuePair>(); // Creo que no se admite que sea nulo
                ((HttpEntityEnclosingRequestBase)httpUriRequest).setEntity(new UrlEncodedFormEntity(paramList));
            }
            catch (UnsupportedEncodingException ex) { throw new ItsNatDroidException(ex); }
        }
        else
        {
            if (paramList != null && paramList.size() > 0)
            {
                // http://stackoverflow.com/questions/2959316/how-to-add-parameters-to-a-http-get-request-in-android
                String paramString = URLEncodedUtils.format(paramList, "utf-8");
                int pos = url.lastIndexOf('?');
                if (pos != -1) // Tiene ?
                {
                    if (!url.endsWith("?")) url += '&'; // Tiene parámetros
                }
                else // No tiene ?
                {
                    url += '?';
                }
                url += paramString;
            }

            if ("GET".equals(method)) { httpUriRequest = new HttpGet(url); }
            else if ("DELETE".equals(method)) { httpUriRequest = new HttpDelete(url); }
            else if ("HEAD".equals(method)) { httpUriRequest = new HttpHead(url); }
            else if ("OPTIONS".equals(method)) { httpUriRequest = new HttpOptions(url); }
            else if ("TRACE".equals(method)) { httpUriRequest = new HttpTrace(url); }
            else throw new ItsNatDroidException("Unsupported HTTP method: " + method);
        }

        HttpResponse response = execute(httpClient,httpUriRequest,httpContext,httpHeaders);
        return processResponse(response,overrideMime);
    }

    private static HttpResponse execute(HttpClient httpClient,HttpUriRequest httpUriRequest,HttpContext httpContext,Map<String,String> httpHeaders) throws SocketTimeoutException
    {
        try
        {
            // Para evitar cacheados (en el caso de GET) por si acaso
            // http://stackoverflow.com/questions/49547/making-sure-a-web-page-is-not-cached-across-all-browsers
            httpUriRequest.setHeader("If-Modified-Since","Wed, 15 Nov 1995 00:00:00 GMT");
            httpUriRequest.setHeader("Cache-Control","no-store,no-cache,must-revalidate");
            httpUriRequest.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            httpUriRequest.setHeader("Expires","0"); // Proxies.

            for(Map.Entry<String,String> header : httpHeaders.entrySet())
            {
                String name = header.getKey();
                String value = header.getValue();
                httpUriRequest.setHeader(name,value);
            }

            HttpResponse response = httpClient.execute(httpUriRequest, httpContext);
            return response;
        }
        catch(SocketTimeoutException ex)
        {
            throw ex; // Nos interesa tratarlo explícitamente y por tanto NO envolverlo en un ItsNatDroidException
        }
        catch(ClientProtocolException ex) { throw new ItsNatDroidException(ex); }
        catch(IOException ex) { throw new ItsNatDroidException(ex); }
    }

    private static HttpRequestResultImpl processResponse(HttpResponse response,String overrideMime)
    {
        // Get hold of the response entity
        HttpEntity entity = response.getEntity();
        // If the response does not enclose an entity, there is no need
        // to worry about connection release

        StatusLine status = response.getStatusLine();

        String[] mimeTypeRes = new String[1];
        String[] encodingRes = new String[1];

        getMimeTypeEncoding(response, mimeTypeRes, encodingRes);

        if (!ValueUtil.isEmpty(overrideMime)) mimeTypeRes[0] = overrideMime;

        byte[] contentArr = null;

        if (entity != null) // null es muy raro incluso con error
        {
            InputStream input = null;
            try { input = entity.getContent(); } // Interesa incluso cuando hay error (statusCode != 200)
            catch (IOException ex) { throw new ItsNatDroidException(ex); }
            contentArr = IOUtil.read(input);
        }

        HttpRequestResultImpl result = new HttpRequestResultImpl(response.getAllHeaders(),contentArr,status,mimeTypeRes[0],encodingRes[0]);

        if (result.isStatusOK())
        {
            // Intentamos hacer procesos de conversión/parsing aquí para aprovechar el multinúcleo y evitar usar el hilo UI
            String mimeType = result.getMimeType();
            if (MIME_ANDROID_LAYOUT.equals(mimeType) ||
                MIME_BEANSHELL.equals(mimeType) ||
                MIME_JSON.equals(mimeType) ||
                mimeType.startsWith("text/"))
            {
                result.setResponseText( ValueUtil.toString(result.getResponseByteArray(), result.getEncoding()) );

                if (MIME_JSON.equals(mimeType))
                {
                    try { result.setResponseJSONObject( new JSONObject(result.getResponseText() ) ); }
                    catch (JSONException ex) { throw new ItsNatDroidServerResponseException(ex, result); }
                }
            }
        }
        else
        {
            // Normalmente será el texto del error que envía el servidor, por ejemplo el stacktrace
            result.setResponseText( ValueUtil.toString(result.getResponseByteArray(), result.getEncoding()) );
            throw new ItsNatDroidServerResponseException(result);
        }

        return result;
    }

    private static void getMimeTypeEncoding(HttpResponse response, String[] mimeType, String[] encoding)
    {
        Header[] contentTypes = response.getHeaders("Content-Type"); // Internamente ignora mayúsculas y minúsculas, no hay que preocuparse
        if (contentTypes != null && contentTypes.length > 0)
        {
            // Ej: Content-Type: android/layout;charset=UTF-8
            HeaderElement[] elems = contentTypes[0].getElements(); // https://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/HeaderElement.html
            for (HeaderElement elem : elems)
            {
                mimeType[0] = elem.getName();
                NameValuePair[] params = elem.getParameters();
                for (NameValuePair param : params)
                {
                    String name = param.getName();
                    if (name.equalsIgnoreCase("charset"))
                    {
                        encoding[0] = param.getValue();
                        break;
                    }
                }
                if (encoding != null) break;
            }
        }

        if (mimeType[0] == null) mimeType[0] = "android/layout"; // Por si acaso
        if (encoding[0] == null) encoding[0] = "UTF-8"; // Por si acaso
    }


    public static String composeAbsoluteURL(String src,String pageURL)
    {
        URI uri = null;
        try { uri = new URI(src); } catch (URISyntaxException ex) { throw new ItsNatDroidException(ex); }

        String scheme = uri.getScheme();
        if (scheme == null)
        {
            int pos = pageURL.lastIndexOf('/');
            if (pos < pageURL.length() - 1) // El / no está en el final
                pageURL = pageURL.substring(0, pos + 1); // Quitamos así el servlet, el JSP etc que generó la página
            // pageURL termina en '/'
            // Si src empieza en '/' lo quitamos para que no haya dos "//" seguidos
            if (src.startsWith("/")) src = src.substring(1);
            src = pageURL.substring(0, pos + 1) + src;
        }
        else if (!scheme.equals("http") && !scheme.equals("https"))
            throw new ItsNatDroidException("Scheme not supported: " + scheme);

        return src;
    }

    public static String getBasePathOfURL(String urlStr)
    {
        URL u = null;
        try { u = new URL(urlStr); }
        catch (MalformedURLException ex) { throw new ItsNatDroidException(ex); }

        // Vale, sí, este código está basado en el código fuente de java.net.URLStreamHandler.toExternalForm()

        // pre-compute length of StringBuilder
        int len = u.getProtocol().length() + 1;
        if (u.getAuthority() != null && u.getAuthority().length() > 0)
            len += 2 + u.getAuthority().length();
        if (u.getPath() != null) {
            len += u.getPath().length();
        }
        /*
        if (u.getQuery() != null) {
            len += 1 + u.getQuery().length();
        }
        if (u.getRef() != null)
            len += 1 + u.getRef().length();
        */
        StringBuilder result = new StringBuilder(len);
        result.append(u.getProtocol());
        result.append(":");
        if (u.getAuthority() != null && u.getAuthority().length() > 0) {
            result.append("//");
            result.append(u.getAuthority());
        }
        if (u.getPath() != null) {
            result.append(u.getPath());
        }
        /*
        if (u.getQuery() != null) {
            result.append('?');
            result.append(u.getQuery());
        }
        if (u.getRef() != null) {
            result.append("#");
            result.append(u.getRef());
        }
        */
        return result.toString();
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
        catch (IOException ex) { throw new ItsNatDroidException(ex); }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException ex) { throw new ItsNatDroidException(ex); }
        }
        return sb.toString();
    }

}