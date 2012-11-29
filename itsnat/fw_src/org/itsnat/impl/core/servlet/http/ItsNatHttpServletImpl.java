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

import java.util.Map;
import org.itsnat.impl.core.ItsNatImpl;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatHttpServletImpl extends ItsNatServletImpl implements ItsNatHttpServlet
{
    public static final String ACTION_SERVLET_WEAK_UP = "servlet_weak_up";

    /**
     * Creates a new instance of ItsNatHttpServletImpl
     */
    public ItsNatHttpServletImpl(ItsNatImpl parent,HttpServlet servlet)
    {
        super(parent,servlet);
    }

    public HttpServlet getHttpServlet()
    {
        return (HttpServlet)servlet;
    }

    public ItsNatServletRequestImpl createItsNatServletRequest(ServletRequest request,ServletResponse response,ItsNatSessionImpl itsNatSession)
    {
        return (ItsNatHttpServletRequestImpl)createItsNatHttpServletRequest((HttpServletRequest)request,(HttpServletResponse)response,(ItsNatHttpSessionImpl)itsNatSession);
    }
    
    public ItsNatHttpServletRequestImpl createItsNatHttpServletRequest(HttpServletRequest request,HttpServletResponse response,ItsNatHttpSessionImpl itsnatSession)
    {
        if (itsnatSession != null)
            return new ItsNatHttpServletRequestImpl(this,request,response,itsnatSession);
        else
            return new ItsNatHttpServletRequestImpl(this,request,response); // La cargará de-serializándola o creará una nueva etc
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
    {
        String action = (String)ItsNatServletRequestImpl.getAttrOrParam(request,"itsnat_action");
        if ((action != null)&& action.equals(ACTION_SERVLET_WEAK_UP))
            return; // NO HACEMOS ABSOLUTAMENTE NADA, es para que se inicie en servlet y se registren
                    // los templates, útil en de-serialización en GAE,
                    // el mero hecho de crear un ItsNatHttpServletRequestImpl ya carga la sesión, lo evitamos

        ItsNatHttpServletRequestImpl itsNatReq = createItsNatHttpServletRequest(request,response,null);

        itsNatReq.process(action);
    }

    public void processRequest(ServletRequest request, ServletResponse response)
    {
        processRequest((HttpServletRequest)request,(HttpServletResponse)response);
    }

    public ServletRequest createServletRequest(ServletRequest request,Map params)
    {
        return new HttpServletRequestNewParamsImpl((HttpServletRequest)request,params);
    }
}
