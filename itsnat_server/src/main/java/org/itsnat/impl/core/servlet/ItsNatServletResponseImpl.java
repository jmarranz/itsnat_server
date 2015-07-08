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

package org.itsnat.impl.core.servlet;

import org.itsnat.impl.core.*;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.ItsNatServlet;
import javax.servlet.ServletResponse;
import org.itsnat.core.ItsNatSession;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.resp.ResponseImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatServletResponseImpl extends ItsNatUserDataImpl implements ItsNatServletResponse
{
    protected ItsNatServletRequestImpl request;
    protected ServletResponse response;
    protected ResponseImpl delegResponse;


    /**
     * Creates a new instance of ItsNatServletResponseImpl
     */
    public ItsNatServletResponseImpl(ItsNatServletRequestImpl request,ServletResponse response)
    {
        super(false);

        this.request = request;
        this.response = response;
    }

    public void setResponse(ResponseImpl delegResponse)
    {
        this.delegResponse = delegResponse;
    }

    public ItsNatServletRequestImpl getItsNatServletRequestImpl()
    {
        return request;
    }

    public ServletResponse getServletResponse()
    {
        return response;
    }

    public void setServletResponse(ServletResponse response)
    {
        // Se usa en un caso especial
        this.response = response;
    }
    
    public Writer getWriter() throws IOException
    {
        // Se supone que el encoding del response ya fue definido antes.
        return response.getWriter();
    }

    public abstract ServletResponseAttachedServer createServletResponseAttachedServer();
    
    public abstract Writer getWriterGZip() throws IOException;

    public ItsNatDocumentImpl getItsNatDocumentImpl()
    {
        return getItsNatServletRequestImpl().getItsNatDocumentImpl();
    }

    public ItsNatDocument getItsNatDocument()
    {
        return getItsNatDocumentImpl();
    }

    public ItsNatServlet getItsNatServlet()
    {
        return getItsNatServletImpl();
    }

    public ItsNatServletImpl getItsNatServletImpl()
    {
        return getItsNatServletRequestImpl().getItsNatServletImpl();
    }

    public ItsNatSession getItsNatSession()
    {
        return getItsNatSessionImpl();
    }

    public ItsNatSessionImpl getItsNatSessionImpl()
    {
        return getItsNatServletRequestImpl().getItsNatSessionImpl();
    }


    public void addCodeToSend(Object newCode)
    {
        // El código nuevo únicamente se puede devolver en esta request.
        delegResponse.addCodeToSend(newCode);
    }

    public abstract void prepareResponse();

    public void dispatchRequestListener(ItsNatServletRequestListener listener)
    {
        listener.processRequest(getItsNatServletRequestImpl(),this);
    }

    public void dispatchItsNatServletRequestListeners(Iterator<ItsNatServletRequestListener> iterator)
    {
        if (iterator != null)
        {
            while(iterator.hasNext())
            {
                ItsNatServletRequestListener listener = iterator.next();
                dispatchRequestListener(listener);
            }
        }
    }

    public abstract String encodeURL(String url);
}
