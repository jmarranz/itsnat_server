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
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.ItsNatSession;
import org.itsnat.core.ItsNatVariableResolver;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.req.RequestImpl;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.itsnat.core.ClientDocument;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatServletRequestImpl extends ItsNatUserDataImpl implements ItsNatServletRequest
{
    protected ItsNatServletImpl itsNatServlet;
    protected ServletRequest request;
    protected ItsNatServletResponseImpl itsNatResponse;
    protected ItsNatSessionImpl itsnatSession;
    protected RequestImpl delegRequest; // Se puede decir que nunca es nulo pues se define enseguida

    /**
     * Creates a new instance of ItsNatServletRequestImpl
     */
    public ItsNatServletRequestImpl(ItsNatServletImpl itsNatServlet,ServletRequest request,ServletResponse response)
    {
        super(false);

        this.itsNatServlet = itsNatServlet;
        this.request = request; 
        this.itsNatResponse = createItsNatServletResponse(response);
    }

    public abstract ItsNatServletResponseImpl createItsNatServletResponse(ServletResponse response);

    public ItsNatSessionImpl getItsNatSessionImpl()
    {
        return itsnatSession;
    }

    public ItsNatServletImpl getItsNatServletImpl()
    {
        return itsNatServlet;
    }

    public ItsNatServlet getItsNatServlet()
    {
        return itsNatServlet;
    }

    public ServletRequest getServletRequest()
    {
        return request;
    }

    public ItsNatServletResponse getItsNatServletResponse()
    {
        return getItsNatServletResponseImpl();
    }

    public ItsNatServletResponseImpl getItsNatServletResponseImpl()
    {
        return itsNatResponse;
    }

    public RequestImpl getRequest()
    {
        return delegRequest;
    }

    public void setRequest(RequestImpl delegRequest)
    {
        this.delegRequest = delegRequest;
    }

    public ItsNatDocumentImpl getItsNatDocumentImpl()
    {
        return delegRequest.getItsNatDocument();
    }

    public ItsNatDocument getItsNatDocument()
    {
        return getItsNatDocumentImpl();
    }

    public static String getAttrOrParam(ServletRequest request,String name)
    {
        String value = (String)request.getAttribute(name);
        if (value == null)
            value = request.getParameter(name);
        return value;
    }
    
    public String getAttrOrParam(String name)
    {
        return getAttrOrParam(request,name);
    }

    public String getAttrOrParamExist(String name)
    {
        return getAttrOrParam(name,true);
    }

    protected String getAttrOrParam(String name,boolean throwError)
    {
        String param = getAttrOrParam(name);
        if (param == null && throwError)
            throw new ItsNatException(name + " parameter is not specified");
        return param;
    }

    public void bindRequestToDocument(ItsNatDocumentImpl itsNatDoc)
    {
        if (itsNatDoc != null) // Por si acaso pues en algun caso es nulo (documento perdido etc)
            itsNatDoc.setCurrentItsNatServletRequest(this);
    }

    public void unbindRequestFromDocument()
    {
        ClientDocumentImpl clientDoc = getClientDocumentImpl();
        if (clientDoc != null) // por si acaso
        {
            // Para que el garbage collector pueda liberar el request y el response
            ItsNatDocumentImpl itsNatDoc = clientDoc.getItsNatDocumentImpl();
            if (itsNatDoc.getCurrentItsNatServletRequest() == this) // Esta comprobación no es necesaria porque lo normal es bloquear el documento pero por si acaso
                itsNatDoc.setCurrentItsNatServletRequest(null);

            // NO HACEMOS nulo el atributo clientDoc así podemos obtener
            // via ItsNatServletRequest.getItsNatDocument() y ItsNatServletRequest.getItsNatClient()
            // el documento y el cliente "como resultado" cuando por ejemplo llamamos manualmente
            // a ItsNatServlet.processRequest(request,response) pasando
            // via atributo el itsnat_doc_name.
        }
    }


    public ClientDocument getClientDocument()
    {
        return getClientDocumentImpl();
    }

    public ClientDocumentImpl getClientDocumentImpl()
    {
        return delegRequest.getClientDocument();
    }

    public ItsNatServletContextImpl getItsNatServletContext()
    {
        return getItsNatServletImpl().getItsNatServletConfigImpl().getItsNatServletContextImpl();
    }

    public ItsNatSession getItsNatSession()
    {
        return getItsNatSessionImpl();
    }

    public void process(String action,ClientDocumentStfulImpl clientDocStateless)
    {
        this.delegRequest = RequestImpl.createRequest(action,this);
        delegRequest.process(clientDocStateless);
    }

    public Object getVariable(String varName)
    {
        ServletRequest request = getServletRequest();
        Object value = request.getParameter(varName);
        if (value != null)
            return value;

        value = request.getAttribute(varName);
        if (value != null)
            return value;

        return getItsNatDocumentImpl().getVariable(varName);
    }

    public ItsNatVariableResolver createItsNatVariableResolver()
    {
        return new ItsNatVariableResolverImpl(null,this,null,null,null);
    }

    public ItsNatDocument getItsNatDocumentReferrer()
    {
        return getItsNatStfulDocumentReferrer();
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocumentReferrer()
    {
        return delegRequest.getItsNatStfulDocumentReferrer();
    }

    public String getServletPath(boolean absoluteURL,boolean addSessionIdWhenNeeded)
    {
        String servletPath;
        if (absoluteURL)
            servletPath = getRequestURLInternal().toString();
        else
            servletPath = getServletPathInternal();

        // Añade si es necesario al URL el  ";jsessionid=..." por ejemplo cuando
        // el navegador tiene las cookies desactivadas, para aplicaciones
        // Single Page Interface es suficiente pues los eventos
        // se enviarán con el ID nativo de la sesión si es necesario.
        // Para aplicaciones basadas en páginas no es suficiente
        // pues hay que reescribir las URLs por parte del usuario.
        if (addSessionIdWhenNeeded)
        {
            ItsNatServletResponseImpl itsNatResponse = getItsNatServletResponseImpl();
            servletPath = itsNatResponse.encodeURL(servletPath);
        }

        return servletPath;
    }


    public abstract String getServletPathInternal();
    public abstract StringBuffer getRequestURLInternal();
    public abstract String getQueryStringInternal();
    
    public abstract boolean isValidClientStandardSessionId();

    public abstract String getHeader(String name);
}
