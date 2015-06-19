package org.itsnat.droid.impl.browser;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.util.IOUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by jmarranz on 16/07/14.
 */
public abstract class HttpRequestResultImpl implements HttpRequestResult
{
    private String url;
    protected byte[] responseByteArray;
    private StatusLine status;
    private String mimeType;
    private String encoding;
    private Header[] headerList;
    protected String responseText;


    protected HttpRequestResultImpl(String url,Header[] headerList,InputStream input,StatusLine status, String mimeType, String encoding)
    {
        this.url = url;
        this.headerList = headerList;
        this.status = status;
        this.mimeType = mimeType;
        this.encoding = encoding;
    }

    public static HttpRequestResultImpl createHttpRequestResult(String url,HttpFileCache httpFileCache,Header[] headerList,InputStream input,StatusLine status, String mimeType, String encoding)
    {
        if (isStatusOK(status))
            return new HttpRequestResultOKImpl(url,httpFileCache,headerList,input,status,mimeType,encoding);
        else
            return new HttpRequestResultFailImpl(url,headerList,input,status,mimeType,encoding);
    }

    public String getUrl()
    {
        return url;
    }


    public boolean isStatusOK()
    {
        return isStatusOK(status);
    }

    public static boolean isStatusOK(StatusLine status)
    {
        return status.getStatusCode() == 200;
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
        ArrayList<Header> headersFound = new ArrayList<Header>();
        for (int i = 0; i < headerList.length; i++)
        {
            Header header = headerList[i];
            if (header.getName().equalsIgnoreCase(name))
                    headersFound.add(header);
        }
        if (headersFound.size() == 0) return null;
        // No me fio de ArrayList.toArray(Array) cuando apenas tenemos 1 elemento
        // http://stackoverflow.com/questions/8526907/is-javas-system-arraycopy-efficient-for-small-arrays
        Header[] arrayRes = new Header[headersFound.size()];
        for(int i = 0; i < headersFound.size(); i++)
            arrayRes[i] = headersFound.get(i);

        return arrayRes;
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

}