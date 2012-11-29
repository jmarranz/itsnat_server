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

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.servlet.PrintWriterAttachedServerImpl;
import org.itsnat.impl.core.servlet.ServletOutputStreamAttachedServerImpl;
import org.itsnat.impl.core.servlet.ServletResponseAttachedServer;

/**
 *
 * @author jmarranz
 */
public class HttpServletResponseAttachedServerImpl extends HttpServletResponseWrapper implements ServletResponseAttachedServer
{
    protected ServletOutputStreamAttachedServerImpl output;
    protected PrintWriterAttachedServerImpl writer;

    public HttpServletResponseAttachedServerImpl(HttpServletResponse response)
    {
        super(response);
    }

    public void setContentType(String value)
    {
        // Redefinimos para ignor la llamada pues se llamará para tratar de poner el MIME del tipo de markup
        // resultante cuando el verdadero MIME de la respuesta es el de JavaScript.
    }

    public ServletOutputStream getOutputStream() throws IOException
    {
        if (writer != null) throw new ItsNatException("A PrintWriter is already being used");
        if (output == null) this.output = new ServletOutputStreamAttachedServerImpl();
        return output;
    }

    public PrintWriter getWriter() throws IOException
    {
        if (output != null) throw new ItsNatException("A ServletOutputStream is already being used");
        if (writer == null) this.writer = new PrintWriterAttachedServerImpl();
        return writer;
    }

    public String getString(String encoding)
    {
        if (output != null) return output.getString(encoding);
        if (writer != null) return writer.getString(encoding);
        return ""; // Raro
    }
}
