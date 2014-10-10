package org.itsnat.droid;

/**
 * Created by jmarranz on 10/10/14.
 */
public interface HttpRequestResult
{
    public String getContentType();
    public String getEncoding();
    public String getResponseHeader(String header);
    public byte[] getResponseByteArray();
    public String getResponseText();
}
