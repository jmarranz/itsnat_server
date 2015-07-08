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
package org.itsnat.core.tmpl;

import java.io.InputStream;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;

/**
 * This interface represents a free-form user defined markup source to be registered as an ItsNat template.
 *
 * <p>When a document or fragment is going to be loaded based on the template which this
 * template source was registered, the first time the method
 * {@link #getInputStream(ItsNatServletRequest, ItsNatServletResponse)} is called,
 * the request and response parameters are the original request and response objects
 * of the document or fragment loading request.</p>
 *
 * <p>If a document or fragment was already loaded, then any new load request
 * asks first if the template must be reloaded calling {@link #isMustReload(ItsNatServletRequest,ItsNatServletResponse)}
 * if this method returns true the method {@link #getInputStream(ItsNatServletRequest, ItsNatServletResponse)}
 * is called after, if false the previous loaded template is also used for this load request.
 * </p>
 *
 * <p>In a very special and highly improbable case the method {@link #isMustReload(ItsNatServletRequest,ItsNatServletResponse)}
 * is called twice with the same request and response object, because this may happen
 * this function must be idempotent (same params same result).
 * </p>
 *
 * <p>This interface could be used to ever load a new template per document load request,
 * by this way ItsNat could be used as a front end (proxy or filter) of any web application.
 * </p>
 *
 * @see org.itsnat.core.ItsNatServlet#registerItsNatDocumentTemplate(String,String,Object)
 * @see org.itsnat.core.ItsNatServlet#registerItsNatDocFragmentTemplate(String,String,Object)
 * @author Jose Maria Arranz Santamaria
 */
public interface TemplateSource
{
    /**
     * Informs whether the current template should be discarded and a new one loaded.
     *
     * <p>If this method returns true the method {@link #getInputStream(ItsNatServletRequest, ItsNatServletResponse)}
     * is called with the same request and response objects.
     * </p>
     *
     * @param request the request object of the document/fragment loading web request.
     * @param response the response object of the document/fragment loading web request.
     * @return true whether the template must be reloaded.
     */
    public boolean isMustReload(ItsNatServletRequest request, ItsNatServletResponse response);

    /**
     * Returns the input stream to read the markup going to be used as template to create documents
     * or fragments.
     *
     * @param request the request object of the document/fragment loading web request.
     * @param response the response object of the document/fragment loading web request.
     * @return the input stream of the template markup.
     * @see #isMustReload(ItsNatServletRequest,ItsNatServletResponse)
     */
    public InputStream getInputStream(ItsNatServletRequest request, ItsNatServletResponse response);
}
