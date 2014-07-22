package org.itsnat.droid.impl.browser;

import org.apache.http.StatusLine;

/**
 * Created by jmarranz on 16/07/14.
 */
class HttpPostResult
{
    public HttpPostResult(String result, StatusLine status)
    {
        this.result = result;
        this.status = status;
    }

    public String result;
    public StatusLine status;
}