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

import javax.servlet.ServletResponse;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.http.ItsNatHttpSession;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletResponseImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatHttpServletRequestImpl extends ItsNatServletRequestImpl implements ItsNatHttpServletRequest
{

    public ItsNatHttpServletRequestImpl(ItsNatServletImpl servlet,HttpServletRequest request,HttpServletResponse response,ItsNatHttpSessionImpl itsnatSession)
    {
        super(servlet,request,response);
        
        // Si la sesión es null la cargará de-serializándola o creará una nueva etc
        this.itsnatSession = itsnatSession == null ? ItsNatHttpSessionImpl.getItsNatHttpSession(this) : itsnatSession; 
    }
    
    public ItsNatHttpSession getItsNatHttpSession()
    {
        return getItsNatHttpSessionImpl();
    }

    public ItsNatHttpSessionImpl getItsNatHttpSessionImpl()
    {
        return (ItsNatHttpSessionImpl)itsnatSession;
    }

    public HttpServletRequest getHttpServletRequest()
    {
        return (HttpServletRequest)request;
    }

    public ItsNatHttpServlet getItsNatHttpServlet()
    {
        return getItsNatHttpServletImpl();
    }

    public ItsNatHttpServletImpl getItsNatHttpServletImpl()
    {
        return (ItsNatHttpServletImpl)itsNatServlet;
    }

    public ItsNatServletResponseImpl createItsNatServletResponse(ServletResponse response)
    {
        return new ItsNatHttpServletResponseImpl(this,(HttpServletResponse)response);
    }

    public String getServletPathInternal()
    {
        HttpServletRequest request = getHttpServletRequest();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath(); // Así permitimos mappings en web.xml que cambien de nombre el servlet "desde fuera". Incluye ya el /
        // La alternativa no admite cambiar el nombre público del servlet via mapping: getItsNatHttpServletImpl().getServlet().getServletConfig().getServletName()
        return contextPath + servletPath;
    }

    public StringBuffer getRequestURLInternal()
    {
        HttpServletRequest request = getHttpServletRequest();
        return request.getRequestURL();
    }

    public String getQueryStringInternal()
    {
        HttpServletRequest request = getHttpServletRequest();
        return request.getQueryString();
    }

    public String getHeader(String name)
    {
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader(name);
    }

    public boolean isValidClientStandardSessionId()
    {
         // El método isRequestedSessionIdValid() compara los valores
         // obtenidos llamando a HttpServletRequest.getRequestedSessionId()
         // y a HttpSession.getId() por ejemplo para detectar si la sesión
         // ha expirado.
         // Sin embargo NO es del todo fiable (para expiración sí), por ejemplo he comprobado que el Tomcat 5.5
         // reutiliza el ID de una sessión anterior cuando se reinicia la aplicación web
         // en caliente (es decir, no se reinicia el Tomcat) por lo que nos da
         // un falso válido, esto no ocurre si se reinicia el Tomcat.
         return getHttpServletRequest().isRequestedSessionIdValid();
    }
}
