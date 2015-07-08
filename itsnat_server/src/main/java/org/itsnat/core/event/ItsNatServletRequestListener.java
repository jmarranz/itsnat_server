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

package org.itsnat.core.event;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;

/**
 * An object implementing this interface and registered is executed to receive and process ItsNat servlet
 * requests usually when a new page is being loaded.
 *
 * <p>This interface is the ItsNat version of the <code>Servlet.service(ServletRequest,ServletResponse)</code>
 * version. When a client request is received the ItsNat servlet converts and routes this request
 * to the registered ItsNatServletRequestListener objects invoking
 * {@link #processRequest(ItsNatServletRequest,ItsNatServletResponse)}, usually
 * this request is a page load request. This method is not called for events.
 * </p>
 *
 * <p>A request listener is registered usually calling {@link org.itsnat.core.tmpl.ItsNatDocumentTemplate#addItsNatServletRequestListener(ItsNatServletRequestListener)}
 * and optionally calling {@link org.itsnat.core.ItsNatServlet#addItsNatServletRequestListener(ItsNatServletRequestListener)}
 * (for uses like filtering etc).</p>
 *
 * <p>Another different use is as ItsNat referrer request listener if registered
 * with {@link org.itsnat.core.ItsNatDocument#addReferrerItsNatServletRequestListener(ItsNatServletRequestListener)}.
 * In this case the listener is called when the framework loads
 * a new target document and the referrer document (the document where the listener
 * was registered) is being unloaded and <i>before</i> the target request listeners are executed
 * (referrer push). Request and response objects are the same objects being sent to the normal
 * target request listeners, target document may be modified, new request attributes may be added
 * etc before ItsNat delegates to the normal target loading process.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatServletRequestListener
{
    /**
     * If this listener is registered this method is invoked when a client request is received
     * usually for loading a new page.
     *
     * <p>If a new document was requested the {@link org.itsnat.core.ItsNatDocument} object is already created using the required template,
     * this document may be obtained calling {@link org.itsnat.core.ItsNatServletRequest#getItsNatDocument()}
     * or {@link org.itsnat.core.ItsNatServletResponse#getItsNatDocument()}. In this scenario
     * there is no need to use the standard output stream ( <code>ServletResponse.getWriter()</code>
     * or <code>ServletResponse.getOutputStream()</code> ) because ItsNat already
     * uses it behind the scenes. Usual page load request processing involves DOM tree manipulation or custom JavaScript
     * is sent calling {@link org.itsnat.core.ItsNatDocument#addCodeToSend(Object)}
     * or {@link org.itsnat.core.ClientDocument#addCodeToSend(Object)}.
     *
     * <p>If no new or existing document is requested the request processing
     * is mostly the same as the typical <code>Servlet.service(ServletRequest,ServletResponse)</code>
     * processing including output to standard output stream. This scenario
     * is an opportunity to change the normal ItsNat document request
     * for instance to implement pretty URLs, because the request may be
     * forwarded again to ItsNat calling
     * {@link org.itsnat.core.ItsNatServlet#processRequest(ServletRequest,ServletResponse)}.
     * </p>
     *
     * @param request the ItsNat request object.
     * @param response the ItsNat response object.
     */
    public void processRequest(ItsNatServletRequest request,ItsNatServletResponse response);
}
