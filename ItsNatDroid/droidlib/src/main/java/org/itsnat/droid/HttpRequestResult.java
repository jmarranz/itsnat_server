package org.itsnat.droid;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.json.JSONObject;

/**
 * Created by jmarranz on 10/10/14.
 */
public interface HttpRequestResult
{
    public StatusLine getStatusLine();
    public String getMimeType();
    public String getEncoding();
    public Header[] getResponseHeaders(String name);
    public byte[] getResponseByteArray();
    public String getResponseText();
}
