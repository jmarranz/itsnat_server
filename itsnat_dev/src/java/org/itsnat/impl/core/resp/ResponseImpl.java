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

package org.itsnat.impl.core.resp;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletResponse;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.UseGZip;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.req.RequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletResponseImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseImpl
{
    protected RequestImpl request;
    protected ItsNatServletResponseImpl itsNatResponse;
    protected Writer writer;
    protected LinkedList<Object> codeToSend = new LinkedList<Object>();    
    
    /** Creates a new instance of ResponseImpl */
    public ResponseImpl(RequestImpl request)
    {
        this.request = request;
        this.itsNatResponse = request.getItsNatServletRequest().getItsNatServletResponseImpl();

        request.setResponse(this);
        this.itsNatResponse.setResponse(this);
    }

    public RequestImpl getRequest()
    {
        return request;
    }

    public ItsNatServletResponseImpl getItsNatServletResponse()
    {
        return itsNatResponse;
    }

    public ItsNatDocumentImpl getItsNatDocument()
    {
        return request.getItsNatDocument(); // Puede ser null
    }

    public ClientDocumentImpl getClientDocument()
    {
        return request.getClientDocument();
    }

    private void addCodeFromClientDocAndReset()
    {
        ClientDocumentImpl listener = getClientDocument();
        if (listener == null) return;
        String code = listener.getCodeToSendAndReset();
        if ((code == null) || code.equals("")) return;

        codeToSend.add( code );
    }    
    
    public String getCodeToSendAndReset()
    {
        itsNatResponse.getItsNatSessionImpl().endOfRequestBeforeSendCode();

        addCodeFromClientDocAndReset();

        StringBuilder code = new StringBuilder();
        
        if (!codeToSend.isEmpty())
        {
            for(Iterator<Object> it = codeToSend.iterator(); it.hasNext(); )
            {
                Object codeFragment = it.next();
                it.remove(); // Para ir liberando memoria
                code.append( codeFragment.toString() );
            }
            codeToSend.clear();  // por si acaso
        }
        return code.toString();
    }    
    
    public void addCodeToSend(Object newCode)
    {
        // El código nuevo únicamente se puede devolver en esta request.
        addCodeFromClientDocAndReset();
   
        codeToSend.add( newCode );
    }    
    
    public void writeResponse(String text)
    {
        try
        {
            if (writer != null) 
                writer.write(text);
        }
        catch(IOException ex)
        {
            throw new ItsNatException(ex);
        }
    }

    public void process()
    {
        preProcess();

        try
        {
            this.writer = initWriter();
            processResponse();
            if (writer != null) writer.close();
            this.writer = null;
        }
        catch(IOException ex)
        {
            throw new ItsNatException(ex,itsNatResponse);
        }

        postProcess();
    }

    public Browser getBrowser()
    {
        ClientDocumentImpl clientDoc = getClientDocument(); // Es muy raro que sea null, yo creo que todos los casos excepto el RequestCustomImpl (que no tiene Response) definen un ClientDocumentImpl
        if (clientDoc != null) return clientDoc.getBrowser();
        return getItsNatServletResponse().getItsNatSessionImpl().getBrowser();
    }

    public void preProcess()
    {
        itsNatResponse.preProcess();

        String mime;
        if (this instanceof ResponseJavaScript)
        {
             mime = "text/javascript";
        }
        else
        {
            ItsNatDocumentImpl itsNatDoc = getItsNatDocument();
            if (itsNatDoc != null)
                mime = itsNatDoc.getMIME();
            else
                mime = "text/html";
        }

        String encoding = getEncoding();

        ServletResponse response = itsNatResponse.getServletResponse();
        response.setContentType(mime + ";charset=" + encoding);

    }

    public String getEncoding()
    {
        ItsNatDocumentImpl itsNatDoc = getItsNatDocument();
        if (itsNatDoc != null)
            return itsNatDoc.getEncoding();        
        else        
            return getItsNatServletResponse().getItsNatServletImpl().getItsNatServletConfigImpl().getDefaultEncoding();        
    }

    protected Writer initWriter() throws IOException
    {
        boolean useGZip = false;

        ItsNatDocumentImpl itsNatDoc = getItsNatDocument();
        if (itsNatDoc != null)
        {
            int useGZipConfig = itsNatDoc.getUseGZip();
            if (useGZipConfig != UseGZip.NONE)
            {
                if (this instanceof ResponseJavaScript)
                {
                    if (UseGZip.isScriptUsingGZip(useGZipConfig))
                    {
                        useGZip = true;
                        if (((ResponseJavaScript)this).isLoadByScriptElement() &&
                             (getClientDocument().getBrowser() instanceof BrowserMSIEOld))
                            useGZip = false; // Porque en el MSIE (v6) da problemas, por ejemplo si se comprime no se carga el script externo inmediatamente (por lo menos antes de ejecutar el handler onload de la página el cual puede necesitar registrar un monitor de eventos) y fallará en la primera carga por ejemplo el registro de un monitor de eventos en el onload  (luego el script se cachea y no hay problema), y falla en los modos SCRIPT y SCRIPT_HOLD
                    }
                }
                else
                {
                    useGZip = UseGZip.isMarkupUsingGZip(useGZipConfig);
                }
            }
        }

        if (useGZip)
            return itsNatResponse.getWriterGZip();
        else
            return itsNatResponse.getWriter();
    }

    public void postProcess()
    {
        itsNatResponse.postProcess();
    }

    protected abstract void processResponse();

}
