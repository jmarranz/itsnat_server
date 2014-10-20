package org.itsnat.droid.impl.browser;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.itsnat.droid.HttpRequestResult;
import org.json.JSONObject;

/**
 * Created by jmarranz on 16/07/14.
 */
public class HttpRequestResultImpl implements HttpRequestResult
{
    public HttpResponse response;
    public byte[] responseByteArray;
    public StatusLine status;
    public String mimeType;
    public String encoding;
    public String responseText;
    public JSONObject responseJSONObject;

    public HttpRequestResultImpl(HttpResponse response,byte[] responseByteArray,StatusLine status, String mimeType, String encoding)
    {
        this.response = response;
        this.responseByteArray = responseByteArray;
        this.status = status;
        this.mimeType = mimeType;
        this.encoding = encoding;
    }

    @Override
    public StatusLine getStatusLine()
    {
        return response.getStatusLine();
    }

    @Override
    public String getMimeType()
    {
        return mimeType;
    }

    @Override
    public String getEncoding()
    {
        return encoding;
    }

    @Override
    public String getResponseHeader(String header)
    {
        Header[] headerList = response.getHeaders(header);
        if (headerList == null || headerList.length == 0) return null;
        return headerList[0].getName();
    }

    @Override
    public byte[] getResponseByteArray()
    {
        return responseByteArray;
    }

    @Override
    public String getResponseText()
    {
        return responseText;
    }

    @Override
    public JSONObject getResponseJSONObject()
    {
        return responseJSONObject;
    }
}