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

package org.itsnat.core.http;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.itsnat.core.*;

/**
 * Simplifies the development of new ItsNat based servlets.
 *
 * <p>New servlets may inherit from this class and overload {@link #init(javax.servlet.ServletConfig config)}
 * to setup the ItsNat infrastructure (default properties, register templates etc).</p>
 *
 * <p>This class creates an <code>ItsNatHttpServlet</code> wrapping this servlet and forwards
 * request/response calls to the <code>ItsNatHttpServlet</code>. Standard request and response objects
 * are wrapped in <code>HttpServletRequest</code> and <code>HttpServletResponse</code></p>.
 *
 * @author Jose Maria Arranz Santamaria
 */
public abstract class HttpServletWrapper extends HttpServlet
{
    protected ItsNatHttpServlet itsNatServlet;

    /**
     * Creates a new instance of HttpServletWrapper
     */
    public HttpServletWrapper()
    {
    }

    /**
     * Returns the ItsNat servlet wrapping this servlet.
     *
     * @return the ItsNat servlet.
     * @see ItsNatHttpServlet#getHttpServlet()
     */
    public ItsNatHttpServlet getItsNatHttpServlet()
    {
        return itsNatServlet;
    }

    /**
     * Initializes the ItsNat servlet wrapping this servlet. Overload this method
     * to initialize the ItsNat servlet (setup properties, register templates etc).
     */
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        this.itsNatServlet = (ItsNatHttpServlet)ItsNatBoot.get().createItsNatServlet(this);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods
     * and forwards them to the {@link ItsNatHttpServlet}.
     *
     * @param request servlet request object.
     * @param response servlet response object.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        getItsNatHttpServlet().processRequest(request,response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "ItsNat Servlet";
    }
    // </editor-fold>
}
