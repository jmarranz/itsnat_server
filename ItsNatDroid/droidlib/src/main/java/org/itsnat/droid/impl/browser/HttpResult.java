package org.itsnat.droid.impl.browser;

import org.apache.http.StatusLine;

/**
 * Created by jmarranz on 16/07/14.
 */
public class HttpResult
{
    public byte[] contentArr;
    public String contentStr;
    public StatusLine status;
    public String contentType;
    public String encoding;

    public HttpResult(byte[] contentArr,String contentStr, StatusLine status,String contentType,String encoding)
    {
        this.contentArr = contentArr;
        this.contentStr = contentStr;
        this.status = status;
        this.contentType = contentType;
        this.encoding = encoding;
    }

}