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

package org.itsnat.core;

import com.innowhere.relproxy.jproxy.JProxyScriptEngine;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

/**
 * Is the root interface of the ItsNat infrastructure. It works as a root factory
 * because almost every ItsNat object is created directed or indirectly using this
 * class.
 *
 * <p>The feature registry, ({@link #getFeature(String)} and {@link #setFeature(String,Object)})
 * may be used to setup ItsNat. Current implementation
 * does not define any default configurable feature.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNat extends ItsNatUserData
{

    /**
     * Returns the ItsNat version with format: X.X.X.X (this format may vary)
     *
     * @return the ItsNat version.
     */
    public String getVersion();

    /**
     * Returns the ItsNat application context wrapper of the provided ServletContext.
     *
     * @param context the ServletContext of the web application
     * @return the context object.
     * @see ItsNatServlet#getItsNatServletContext()
     */
    public ItsNatServletContext getItsNatServletContext(ServletContext context);

    /**
     * Returns the feature value of given name.
     *
     * @param name the feature name to search.
     * @return the feature value, null if not found.
     */
    public Object getFeature(String name);

    /**
     * Sets the feature value of given name.
     *
     * @param name the feature name.
     * @param value the feature value.
     * @return the previous feature value if defined or null.
     */
    public Object setFeature(String name,Object value);

    /**
     * Creates a new ItsNat servlet wrapping the specified servlet object.
     *
     * <p>Current implementation only supports <code>javax.servlet.http.HttpServlet</code> servlets,
     * and returns {@link org.itsnat.core.http.ItsNatHttpServlet} objects.</p>
     *
     * @param servlet the servlet to wrap.
     * @return a new ItsNat servlet wrapping the standard servlet.
     * @see org.itsnat.core.http.HttpServletWrapper#init(javax.servlet.ServletConfig)
     */
    public ItsNatServlet createItsNatServlet(Servlet servlet);

    /**
     * Gets a not configured <code>JProxyScriptEngine</code> object to be used for hot class reloading in development or production time.
     *
     * <p>The returned object is ever the same.</p>
     * <p>See <a href="https://github.com/jmarranz/relproxy/">RelProxy project</a> for more info.</p>
     *
     * @return returns a not configured <code>JProxyScriptEngine</code> object.
     */
    public JProxyScriptEngine getJProxyScriptEngine();
}
