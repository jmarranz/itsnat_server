package org.itsnat.droid.impl.browser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroidException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmarranz on 23/05/14.
 */
public class HttpUtil
{
    private static HttpParams getHttpParams(HttpParams httpParamsRequest, HttpParams httpParamsDefault)
    {
        return httpParamsRequest != null ? new DefaultedHttpParams(httpParamsRequest,httpParamsDefault) : httpParamsDefault;
    }

    public static byte[] httpGet(String url, HttpContext httpContext, HttpParams httpParamsRequest, HttpParams httpParamsDefault,StatusLine[] status)
    {
        HttpParams httpParams = getHttpParams(httpParamsRequest, httpParamsDefault);

        HttpClient httpClient = new DefaultHttpClient(httpParams);

                // Prepare a request object
        HttpGet httpGet = new HttpGet(url);

        return execute(httpClient,httpGet,httpContext,status);
    }

    public static byte[] httpPost(String url, HttpContext httpContext, HttpParams httpParamsRequest, HttpParams httpParamsDefault,List<NameValuePair> nameValuePairs,StatusLine[] status)
    {
        HttpParams httpParams = getHttpParams(httpParamsRequest, httpParamsDefault);

        HttpClient httpClient = new DefaultHttpClient(httpParams);

        HttpPost httpPost = new HttpPost(url);

        //httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        try
        {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }
        catch (UnsupportedEncodingException ex) { throw new ItsNatDroidException(ex); }

        return execute(httpClient,httpPost,httpContext,status);
    }

    private static byte[] execute(HttpClient httpClient,HttpUriRequest httpUriRequest,HttpContext httpContext,StatusLine[] status)
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
        catch (Exception ex)
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
            throw new RuntimeException(ex);
        }
        finally
        {
            try { input.close(); }
            catch (IOException ex2) { throw new RuntimeException(ex2); }
        }
        return output.toByteArray();
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
            throw new RuntimeException(ex);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        }
        return sb.toString();
    }

}