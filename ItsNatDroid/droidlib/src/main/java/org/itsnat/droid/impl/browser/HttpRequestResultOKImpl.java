package org.itsnat.droid.impl.browser;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.impl.util.IOUtil;
import org.itsnat.droid.impl.util.MimeUtil;
import org.itsnat.droid.impl.util.ValueUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jose on 18/06/2015.
 */
public class HttpRequestResultOKImpl extends HttpRequestResultImpl
{
    private HttpFileCache httpFileCache;
    private String itsNatServerVersion;
    private Integer bitmapDensityReference;
    private JSONObject responseJSONObject;

    public HttpRequestResultOKImpl(String url,HttpFileCache httpFileCache,Header[] headerList,InputStream input, StatusLine status, String mimeType, String encoding)
    {
        super(url,headerList, input, status, mimeType, encoding);

        // Tomcat v6 devuelve por ejemplo:
        // Last-Modified: Wed, 10 Jun 2015 19:48:47 GMT
        Header[] lastModifiedHeader = getResponseHeaders("Last-Modified"); // Es devuelto cuando se accede a un archivo
        if (lastModifiedHeader != null)
        {
            long lastModified;
            try {
                lastModified = DateUtils.parseDate(lastModifiedHeader[0].getValue()).getTime();
            } catch (DateParseException ex) {
                throw new ItsNatDroidException(ex);
            }

            HttpFileCache.FileCached fileCached = httpFileCache.get(url);

            if (fileCached == null)
            {
                // No esta cacheado, pero como hay Last-Modified significa que es un archivo "cacheable"
                this.responseByteArray = IOUtil.read(input);

                fileCached = new HttpFileCache.FileCached(url,lastModified,responseByteArray);
                httpFileCache.put(fileCached);
            }
            else
            {
                // Esta cacheado, vemos si ha cambiado en el servidor respecto a lo que tenemos
                long lastModifiedCached = fileCached.getLastModified();
                if (lastModified > lastModifiedCached)
                {
                    // Ha cambiado, tenemos que descargarlo de nuevo y recachearlo,
                    // lo mas sencillo es eliminarlo de la httpFileCache y anadirlo de nuevo, ten en cuenta que esto
                    // solo ocurre raramente cuando cambiemos el archivo en el servidor

                    this.responseByteArray = IOUtil.read(input);

                    httpFileCache.remove(fileCached);

                    fileCached = new HttpFileCache.FileCached(url,lastModified,responseByteArray);
                    httpFileCache.put(fileCached);
                }
                else
                {
                    // NO ha cambiado (si ha cambiado dos veces en el mismo segundo SE SIENTE pues Last-Modified tiene 1 seg de granularidad)
                    // usamos el archivo de la httpFileCache

                    // de acuerdo con
                    // https://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/org/apache/http/util/EntityUtils.html#consume(org.apache.http.HttpEntity)
                    // http://grepcode.com/file/repo1.maven.org/maven2/org.apache.httpcomponents/httpcore/4.1/org/apache/http/util/EntityUtils.java
                    // basta con cerrar el InputStream para evitar descargar y a la vez cerrar la request

                    this.responseByteArray = fileCached.getContentByteArray(); // Evitamos asi descargar el archivo via HTTP constantemente

                    try {
                        input.close();
                    } catch (IOException ex) {
                        throw new ItsNatDroidException(ex);
                    }
                }
            }
        }
        else
        {
            this.responseByteArray = IOUtil.read(input);
        }


        Header[] itsNatServerVersionArr = getResponseHeaders("ItsNat-version");
        this.itsNatServerVersion = itsNatServerVersionArr != null ? itsNatServerVersionArr[0].getValue() : null;
        Header[] bitmapDensityReferenceArr = getResponseHeaders("ItsNat-bitmapDensityReference");
        this.bitmapDensityReference = bitmapDensityReferenceArr != null ? new Integer(bitmapDensityReferenceArr[0].getValue()) : null;

        // Intentamos hacer procesos de conversion/parsing aqui para aprovechar el multinucleo y evitar usar el hilo UI
        if (MimeUtil.MIME_ANDROID_LAYOUT.equals(mimeType) ||
                MimeUtil.MIME_BEANSHELL.equals(mimeType) ||
                MimeUtil.MIME_JSON.equals(mimeType) ||
                mimeType.startsWith("text/"))
        {
            this.responseText = ValueUtil.toString(responseByteArray, getEncoding());

            if (MimeUtil.MIME_JSON.equals(mimeType))
            {
                try { this.responseJSONObject = new JSONObject(this.responseText) ; }
                catch (JSONException ex) { throw new ItsNatDroidServerResponseException(ex, this); }
            }
        }
    }

    public HttpFileCache getHttpFileCache()
    {
        return httpFileCache;
    }

    public String getItsNatServerVersion()
    {
        return itsNatServerVersion; // Si no es servida por ItsNat devuelve null
    }

    public Integer getBitmapDensityReference()
    {
        return bitmapDensityReference;
    }

    public JSONObject getResponseJSONObject()
    {
        return responseJSONObject;
    }

}
