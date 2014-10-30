package org.itsnat.droid.impl.browser;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.itsnat.droid.HttpRequestResult;
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
    private String itsNatServerVersion;
    public String responseText;
    public JSONObject responseJSONObject;

    public HttpRequestResultImpl(Header[] headerList,byte[] responseByteArray,StatusLine status, String mimeType, String encoding)
    {
        this.headerList = headerList;
        this.responseByteArray = responseByteArray;
        this.status = status;
        this.mimeType = mimeType;
        this.encoding = encoding;

        Header[] itsNatServerVersionArr = getResponseHeaders("ItsNat-version");
        this.itsNatServerVersion = itsNatServerVersionArr != null ? itsNatServerVersionArr[0].getValue() : null;
    }

    public String getItsNatServerVersion()
    {
        return itsNatServerVersion; // Si no es servida por ItsNat devuelve null
    }

    public boolean isStatusOK()
    {
        return getStatusLine().getStatusCode() == 200;
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
        /*
        if (responseText == null)
        {
            // Esto es porque el MIME está mal, si está bien la conversión a texto se hace en el hilo de la request que es lo mejor
            this.responseText = ValueUtil.toString(responseByteArray,getEncoding());
        }
        */
        return responseText;
    }

    @Override
    public JSONObject getResponseJSONObject()
    {
        return responseJSONObject;
    }
}