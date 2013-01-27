/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.core.servlet.http;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.impl.core.servlet.ItsNatServletResponseImpl;
import javax.servlet.http.HttpServletResponse;
import org.itsnat.core.http.ItsNatHttpSession;
import org.itsnat.impl.core.servlet.ServletResponseAttachedServer;

/**
 *
 * @author jmarranz
 */
public class ItsNatHttpServletResponseImpl extends ItsNatServletResponseImpl implements ItsNatHttpServletResponse
{
    /**
     * Creates a new instance of ItsNatHttpServletResponseImpl
     */
    public ItsNatHttpServletResponseImpl(ItsNatHttpServletRequestImpl request,HttpServletResponse response)
    {
        super(request,response);
    }

    public ItsNatHttpServletImpl getItsNatHttpServletImpl()
    {
        return (ItsNatHttpServletImpl)getItsNatServletImpl();
    }

    public ItsNatHttpServletRequestImpl getItsNatHttpServletRequestImpl()
    {
        return (ItsNatHttpServletRequestImpl)request;
    }

    public HttpServletResponse getHttpServletResponse()
    {
        return (HttpServletResponse)response;
    }

    public ItsNatHttpServlet getItsNatHttpServlet()
    {
        return getItsNatHttpServletRequestImpl().getItsNatHttpServletImpl();
    }

    public ItsNatHttpSession getItsNatHttpSession()
    {
        return getItsNatHttpSessionImpl();
    }

    public ItsNatHttpSessionImpl getItsNatHttpSessionImpl()
    {
        return getItsNatHttpServletRequestImpl().getItsNatHttpSessionImpl();
    }

    public void preProcess()
    {
        HttpServletResponse response = getHttpServletResponse();
        response.addHeader("Cache-Control","no-store,no-cache,must-revalidate"); //HTTP 1.1
        response.addHeader("Pragma","no-cache"); //HTTP 1.0
        response.addDateHeader("Expires", 0); // Quizás también -1
        // response.addHeader("Expires", "Thu, 01 Jan 1970 01:00:00 GMT");
        // response.setHeader("Last-Modified", "FRI, JUN 26 3020 23:59:59 GMT"); // En el futuro

        // Es absolutamente vital no cachear, sobre todo en FireFox porque soluciona el problema del back/forward
        // que al volver ejecuta el evento load pero sin recargar la página en el servidor
        // y el problema de que el evento unload no
        // se ejecuta (por lo menos con listener asociado via addEventListener) y por tanto
        // no podemos detectar cuando se deja la página.
        // http://developer.mozilla.org/en/docs/Using_Firefox_1.5_caching
        // http://www.jguru.com/faq/view.jsp?EID=377&page=2
        // http://support.microsoft.com/kb/234067/EN-US/
        // http://www.mnot.net/cache_docs/
        // Problemas:
        // http://support.microsoft.com/kb/q222064/

    }

    public ServletResponseAttachedServer createServletResponseAttachedServer()
    {
        return new HttpServletResponseAttachedServerImpl(getHttpServletResponse());
    }

    public Writer getWriterGZip() throws IOException
    {
        // http://www.javaworld.com/javaworld/jw-06-2004/jw-0628-performance.html?page=2

        // Si el navegador no soporta gzip se devuelve el Writer normal

        OutputStream out = null;

        ItsNatHttpServletRequestImpl itsNatRequest = getItsNatHttpServletRequestImpl();

        // MSIE 6 tiene problemas con gzip aunque en teoría lo soporta
        // http://support.microsoft.com/kb/321722/EN-US/
        // http://support.microsoft.com/default.aspx?scid=kb;en-us;Q312496
        // http://support.microsoft.com/default.aspx?scid=kb;en-us;823386&Product=ie600
        // http://blog.joshuaeichorn.com/archives/2007/01/10/compressing-javascript-and-css/#comment-125483
        // FireFox no ha dado ningún problema

        HttpServletResponse response = getHttpServletResponse();

        if (!(response instanceof HttpServletResponseAttachedServerImpl)) // En el caso del wrapper de attached server el Writer y OutputStream no son los reales son meros lectores de String
        {
            HttpServletRequest request = itsNatRequest.getHttpServletRequest();
            String acceptEncoding = request.getHeader("Accept-Encoding");

            if (acceptEncoding != null)
            {
                if (acceptEncoding.indexOf("gzip") != -1)
                {
                   response.addHeader("Content-Encoding","gzip");
                   out = new GZIPOutputStream(response.getOutputStream());
                }
                /* // Ni FireFox ni MSIE por defecto aceptan "compress", no lo soportamos
                else if (acceptEncoding.indexOf("compress") != -1)
                {
                   response.addHeader("Content-Encoding","compress");
                   out = new ZipOutputStream(response.getOutputStream());
                }
                 */
            }

            if (out != null)
            {
                // Se supone que ya se definió antes en el response el acceptEncoding
                // pues es lo normal antes de acceder al Writer
                String encoding = response.getCharacterEncoding();
                return new BufferedWriter(new OutputStreamWriter(out,encoding));
            }
        }

        return super.getWriter(); // No se ha podido como GZip
     }

    public String encodeURL(String url)
    {
        HttpServletResponse response = getHttpServletResponse();
        return response.encodeURL(url);
    }
}
