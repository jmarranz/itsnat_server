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

import javax.servlet.ServletResponse;

/**
 * Is the ItsNat wrapper of the <code>javax.servlet.ServletResponse</code> object.
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatServletResponse extends ItsNatUserData
{
    /**
     * Returns the wrapped <code>javax.servlet.ServletResponse</code> object.
     *
     * @return the wrapped servlet response object. Can not be null.
     */
    public ServletResponse getServletResponse();

    /**
     * Returns the ItsNat servlet associated to this response.
     *
     * @return the ItsNat servlet. Can not be null.
     */
    public ItsNatServlet getItsNatServlet();

    /**
     * Returns the ItsNat session associated to this response.
     *
     * @return the ItsNat session. Can not be null.
     */
    public ItsNatSession getItsNatSession();

    /**
     * Returns the ItsNat document associated to this response.
     *
     * @return the ItsNat document. If null no document could be loaded/accessed.
     */
    public ItsNatDocument getItsNatDocument();

    /**
     * Add JavaScript code to send to the client as return of this request.
     *
     * <p>If this request is a document load request, the code is added to the JavaScript
     * initialization code, otherwise is an event based request and the code is added to the
     * JavaScript event response.</p>
     *
     * <p>Use this method exceptionally if you need to send custom code in this circumstance,
     * otherwise use {@link ItsNatDocument#addCodeToSend(Object)} or {@link ClientDocument#addCodeToSend(Object)}.</p>
     *
     * @param code the code to send, <code>Object.toString()</code> is called to convert to string.
     * @see org.itsnat.core.script.ScriptUtil
     */
    public void addCodeToSend(Object code);
}
