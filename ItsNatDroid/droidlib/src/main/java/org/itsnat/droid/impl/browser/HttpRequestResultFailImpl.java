package org.itsnat.droid.impl.browser;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.itsnat.droid.impl.util.IOUtil;
import org.itsnat.droid.impl.util.ValueUtil;

import java.io.InputStream;

/**
 * Created by Jose on 18/06/2015.
 */
public class HttpRequestResultFailImpl extends HttpRequestResultImpl
{
    public HttpRequestResultFailImpl(String url,Header[] headerList,InputStream input, StatusLine status, String mimeType, String encoding)
    {
        super(url,headerList, input, status, mimeType, encoding);

        // Normalmente sera el texto del error que envia el servidor, por ejemplo el stacktrace
        this.responseByteArray = IOUtil.read(input);
        this.responseText = ValueUtil.toString(responseByteArray, getEncoding());
    }
}
