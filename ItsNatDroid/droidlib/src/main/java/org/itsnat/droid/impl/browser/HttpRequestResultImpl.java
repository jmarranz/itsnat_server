package org.itsnat.droid.impl.browser;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.impl.util.ValueUtil;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jmarranz on 16/07/14.
 */
public class HttpRequestResultImpl implements HttpRequestResult
{
    private byte[] responseByteArray;
    private StatusLine status;
    private String mimeType;
    private String encoding;
    private Header[] headerList;
    public String responseText;
    public JSONObject responseJSONObject;

    public HttpRequestResultImpl(Header[] headerList,byte[] responseByteArray,StatusLine status, String mimeType, String encoding)
    {
        this.headerList = headerList;
        this.responseByteArray = responseByteArray;
        this.status = status;
        this.mimeType = mimeType;
        this.encoding = encoding;
    }

    @Override
    public StatusLine getStatusLine()
    {
        return status;
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
    public Header[] getResponseHeaders(String name)
    {
        // http://grepcode.com/file/repo1.maven.org/maven2/org.apache.httpcomponents/httpcore/4.0/org/apache/http/message/HeaderGroup.java#HeaderGroup.getHeaders%28java.lang.String%29
        ArrayList headersFound = new ArrayList();
        for (int i = 0; i < headerList.length; i++)
        {
            Header header = (Header) headerList[i];
            if (header.getName().equalsIgnoreCase(name))
                    headersFound.add(header);
        }
        if (headersFound.size() == 0) return null;
        return (Header[]) headersFound.toArray(new Header[headersFound.size()]);
    }

    @Override
    public byte[] getResponseByteArray()
    {
        return responseByteArray;
    }

    @Override
    public String getResponseText()
    {
        if (responseText == null)
        {
            // Esto es porque el MIME está mal, si está bien la conversión a texto se hace en el hilo de la request que es lo mejor
            this.responseText = ValueUtil.toString(responseByteArray,getEncoding());
        }
        return responseText;
    }

    @Override
    public JSONObject getResponseJSONObject()
    {
        return responseJSONObject;
    }
}