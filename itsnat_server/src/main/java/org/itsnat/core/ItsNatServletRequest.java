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

import javax.servlet.ServletRequest;

/**
 * Is the ItsNat wrapper of the <code>javax.servlet.ServletRequest</code> object.
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatServletRequest extends ItsNatUserData
{
    /**
     * Returns the wrapped <code>javax.servlet.ServletRequest</code> object.
     *
     * @return the wrapped servlet request object. Can not be null.
     */
    public ServletRequest getServletRequest();

    /**
     * Returns the ItsNat servlet associated to this request.
     *
     * @return the ItsNat servlet. Can not be null.
     */
    public ItsNatServlet getItsNatServlet();

    /**
     * Returns the ItsNat session associated to this request.
     *
     * @return the ItsNat session. Can not be null.
     */
    public ItsNatSession getItsNatSession();

    /**
     * Returns the ItsNat document associated to this request.
     *
     * @return the ItsNat document. If null no document could be loaded/accessed.
     */
    public ItsNatDocument getItsNatDocument();

    /**
     * Returns the client document, this object mirrors the user (client) document/page
     * sending this request.
     *
     * @return the client document. Is null {@link #getItsNatDocument()} is null too.
     */
    public ClientDocument getClientDocument();

    /**
     * Creates a variable resolver bound to this request.
     *
     * @return a variable resolver bound to this request.
     */
    public ItsNatVariableResolver createItsNatVariableResolver();

    /**
     * Returns the document referrer. This referrer document was the previous page
     * and the document related to this request is the target using classic
     * navigation.
     *
     * <p>The referrer feature and events in the source document must be enabled
     * (see {@link org.itsnat.core.tmpl.ItsNatDocumentTemplate#setReferrerEnabled(boolean)} and
     *  {@link org.itsnat.core.tmpl.ItsNatDocumentTemplate#setEventsEnabled(boolean)}) otherwise returns null. </p>
     *
     * <p>Document referrer is not available if the target document was requested
     * using a direct URL (written on the browser URL bar) or, in the Internet Explorer
     * case, the target document is the result of a reload button click. </p>
     *
     * <p>Only the first request (the loading document phase) has access to the
     * document referrer, subsequent requests (events) return null.</p>
     *
     * <p>The document referrer is still live but is going to be unloaded.</p>
     *
     * @return the document referrer. May be null.
     */
    public ItsNatDocument getItsNatDocumentReferrer();
}
