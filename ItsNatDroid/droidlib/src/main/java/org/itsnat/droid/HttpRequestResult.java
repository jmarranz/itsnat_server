package org.itsnat.droid;

import org.json.JSONObject;

/**
 * Created by jmarranz on 10/10/14.
 */
public interface HttpRequestResult
{
    public String getMimeType();
    public String getEncoding();
    public String getResponseHeader(String header);
    public byte[] getResponseByteArray();
    public String getResponseText();
    public JSONObject getResponseJSONObject();
}
