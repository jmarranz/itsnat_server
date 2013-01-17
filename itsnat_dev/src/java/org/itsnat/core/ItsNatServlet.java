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

import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.core.event.ItsNatAttachedClientEventListener;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.w3c.dom.events.EventListener;

/**
 * Is the ItsNat wrapper of the <code>javax.servlet.Servlet</code> object.
 * Bridges the normal servlet infrastructure with ItsNat.
 *
 * <p>To bridge a standard Servlet with ItsNat call <code>ItsNat.get().createItsNatServlet(this)</code>
 * into the overloaded <code>Servlet.init(ServletConfig)</code> method.
 * This call creates a new ItsNatServlet bound to the standard Servlet.
 * </p>
 *
 * <p>Use this <code>init</code> method to setup the ItsNatServlet
 * (using {@link ItsNatServletConfig}), to register page and fragment templates
 * (calling {@link #registerItsNatDocumentTemplate(String,String,Object)} and
 * {@link #registerItsNatDocFragmentTemplate(String,String,Object)}) and
 * to register ItsNat request listeners ({@link ItsNatServletRequestListener}).
 * </p>
 *
 * <p>To redirect normal requests to ItsNat call the method
 * {@link #processRequest(javax.servlet.ServletRequest,javax.servlet.ServletResponse)}.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.core.http.HttpServletWrapper
 */
public interface ItsNatServlet
{
    /**
     * Returns the wrapped <code>javax.servlet.Servlet</code> object.
     *
     * @return the wrapped servlet object.
     */
    public Servlet getServlet();

    /**
     * Returns the ItsNat "root" object used to create this servlet.
     *
     * @return the parent ItsNat object.
     * @see ItsNat#createItsNatServlet(javax.servlet.Servlet)
     */
    public ItsNat getItsNat();

    /**
     * Returns the utility object used to setup the ItsNat servlet.
     *
     * @return the configuration object.
     */
    public ItsNatServletConfig getItsNatServletConfig();

    /**
     * Returns the ItsNat application context this ItsNat servlet belongs to.
     *
     * @return the context object.
     */
    public ItsNatServletContext getItsNatServletContext();

    /**
     * Registers a markup source to be used as a document template with the specified name and MIME type.
     *
     * <p>The specified MIME type may be different to the "intrinsic" MIME of the specified
     * file. For instance the loaded file may be XHTML (MIME application/xhtml+xml) but
     * it can be registered with HTML MIME (text/html) to achieve the maximum compatibility.</p>
     *
     * <p>A markup template is a normal (X)HTML, SVG, XUL or XML local or remote file.</p>
     *
     * <p>Markup source can be a file path, a remote URL (specified as a String) or a {@link org.itsnat.core.tmpl.TemplateSource} object.</p>
     *
     * @param name the name used to identify the template.
     * @param mime the MIME type.
     * @param source the markup source.
     * @return the document template descriptor.
     * @see #registerItsNatDocFragmentTemplate(String,String,Object)
     * @see #getItsNatDocumentTemplate(String)
     */
    public ItsNatDocumentTemplate registerItsNatDocumentTemplate(String name,String mime,Object source);

    /**
     * Registers an attached server template with the specified name and MIME type.
     *
     * <p>An attached server template does not need a template file because the client page
     * is sent to the server as the initial template.
     * </p>
     * 
     * @param name the name used to identify the template.
     * @param mime the MIME type.
     * @return the document template descriptor.
     * @see #registerItsNatDocumentTemplate(String,String,Object)
     */
    public ItsNatDocumentTemplate registerItsNatDocumentTemplateAttachedServer(String name,String mime);

    /**
     * Returns the document template registered with the specified name.
     *
     * @param name the name used to look for.
     * @return the document template with this name or null if not found.
     */
    public ItsNatDocumentTemplate getItsNatDocumentTemplate(String name);

    /**
     * Registers a markup source to be used as a document fragment template with the specified name and MIME type.
     *
     * <p>The specified MIME type may be different to the "intrinsic" MIME of the specified
     * file. For instance the loaded file may be XHTML (application/xhtml+xml) but
     * it can be registered as HTML (text/html) to achieve the maximum compatibility.</p>
     *
     * <p>Because the main purpose of document fragments is to be included into documents,
     * you should be use the same HTML (text/html) or XHTML (application/xhtml+xml) MIME as the document
     * target.
     * </p>
     * 
     * <p>A markup template fragment is a normal (X)HTML, SVG, XUL or XML local or remote file.
     * The fragment part is:</p>
     *
     * <ol>
     *   <li>(X)HTML: the node group below the &lt;head&gt; and &lt;body&gt;. They are two fragments,
     *   an &lt;itsnat:include&gt; tag can select the appropriated fragment or using
     *   the concrete {@link org.itsnat.core.tmpl.ItsNatHTMLDocFragmentTemplate} method.
     *   </li>
     *   <li>SVG: the node group below the &lt;svg&gt; element.</li>
     *   <li>XUL: the node group below the &lt;window&gt; element.</li>
     *   <li>XML: the node group below the document root element. The root element tag can have any name.</li>
     * </ol>
     *
     * <p>Markup source can be a file path, a remote URL (specified as a String) or a {@link org.itsnat.core.tmpl.TemplateSource} object.</p>
     *
     * @param name the name used to identify the template.
     * @param mime the MIME type.
     * @param source the markup source.
     * @return the fragment template descriptor.
     * @see #registerItsNatDocumentTemplate(String,String,Object)
     * @see #getItsNatDocFragmentTemplate(String)
     */
    public ItsNatDocFragmentTemplate registerItsNatDocFragmentTemplate(String name,String mime,Object source);

    /**
     * Returns the document fragment template registered with the specified name.
     *
     * @param name the name used to look for.
     * @return the document fragment template with this name or null if not found.
     */
    public ItsNatDocFragmentTemplate getItsNatDocFragmentTemplate(String name);

    /**
     * Registers a new ItsNat request listener. This listener is called when this ItsNat servlet receives
     * a new client request.
     *
     * <p>If an ItsNatDocument is involved, this listener is called before listeners registered in the template.
     * Typical use is for logging, preprocessing, filtering etc.</p>
     *
     * @param listener the listener to register.
     * @see #removeItsNatServletRequestListener(ItsNatServletRequestListener)
     * @see ItsNatDocumentTemplate#addItsNatServletRequestListener(ItsNatServletRequestListener)
     * @see #processRequest(javax.servlet.ServletRequest,javax.servlet.ServletResponse)
     */
    public void addItsNatServletRequestListener(ItsNatServletRequestListener listener);

    /**
     * Unregisters the specified request listener.
     *
     * @param listener the request listener to remove.
     * @see #addItsNatServletRequestListener(ItsNatServletRequestListener)
     */
    public void removeItsNatServletRequestListener(ItsNatServletRequestListener listener);


    /**
     * Adds a remote control listener to this servlet. This listener is called when a remote view/control
     * is requested to control a document loaded using this servlet.
     *
     * <p>The listener is called <i>before</i> calling the template and document listener counterparts (if defined).</p>
     *
     * @param listener the listener to add.
     * @see #removeItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener)
     * @see ItsNatDocumentTemplate#addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener)
     * @see ItsNatDocument#addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener)
     */
    public void addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener);


    /**
     * Removes the specified remote control listener.
     *
     * @param listener the listener to remove.
     * @see #addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener)
     */
    public void removeItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener);

    /**
     * Adds a global event listener to this servlet. This listener is called when any DOM event
     * (standard or extended) is received by this servlet.
     *
     * <p>The listener is called <i>before</i> calling any DOM event listener registered in
     * templates and documents. This listener registry is <i>passive</i>, in no way the client
     * is modified (no listener is registered on the client) and usually used
     * for monitoring.</p>
     *
     * @param listener the listener to add.
     * @see #removeEventListener(EventListener)
     * @see ItsNatDocumentTemplate#addEventListener(EventListener)
     * @see ItsNatDocument#addEventListener(EventListener)
     */
    public void addEventListener(EventListener listener);

    /**
     * Removes the specified global event listener.
     *
     * @param listener the listener to remove.
     * @see #addEventListener(EventListener)
     */
    public void removeEventListener(EventListener listener);


    /**
     * Adds a new user defined component factory. This listener is called when the framework needs
     * to create a component instance.
     *
     * <p>The listener is called <i>before</i> calling the template listener counterparts (if defined).</p>
     *
     * @param listener the listener factory to register.
     * @see #removeCreateItsNatComponentListener(CreateItsNatComponentListener)
     * @see ItsNatDocumentTemplate#addCreateItsNatComponentListener(CreateItsNatComponentListener)
     */
    public void addCreateItsNatComponentListener(CreateItsNatComponentListener listener);

    /**
     * Removes the specified user defined component factory.
     *
     * @param listener the listener factory to remove.
     * @see #addCreateItsNatComponentListener(CreateItsNatComponentListener)
     */
    public void removeCreateItsNatComponentListener(CreateItsNatComponentListener listener);

    /**
     * Called to redirect a normal servlet request to the ItsNat servlet.
     *
     * <p>ItsNat requests are processed by the registered {@link ItsNatServletRequestListener} objects.</p>
     *
     * @param request the standard servlet request.
     * @param response the standard servlet response.
     */
    public void processRequest(ServletRequest request, ServletResponse response);

    /**
     * Creates a new request object wrapping the provided request object replacing 
     * parameters with the provided collection.
     *
     * <p>This method can be used to avoid the problem of read-only parameters
     * of the standard  <code>ServletRequest</code> object.
     * </p>
     *
     * <p>With exception of methods returning parameters, every method call is forwarded to
     * the wrapped <code>ServletRequest</code> object.
     * </p>
     *
     * <p>In a HTTP environment (the request object to wrap implements <code>HttpServletRequest</code>),
     * the resulting object implements <code>HttpServletRequest</code>.
     *
     * @param request the servlet request object to wrap.
     * @param params a collection with the new parameters.
     * @return a new request object.
     */
    public ServletRequest createServletRequest(ServletRequest request,Map params);
}
