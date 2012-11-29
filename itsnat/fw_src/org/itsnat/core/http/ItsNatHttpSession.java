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

import org.itsnat.core.ItsNatSession;
import javax.servlet.http.HttpSession;

/**
 * Is the ItsNat wrapper of a <code>javax.servlet.http.HttpSession</code> object.
 *
 * <p>The ItsNat the session concept is borrowed from the HTTP Java Servlet
 * session concept.</p>
 *
 * <p>The live of an ItsNat session is the same as the wrapped <code>HttpSession</code>.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatHttpServletRequest#getItsNatHttpSession()
 */
public interface ItsNatHttpSession extends ItsNatSession
{
    /**
     * Returns the wrapped <code>javax.servlet.http.HttpSession</code> object.
     *
     * @return the wrapped servlet session object.
     */
    public HttpSession getHttpSession();

    /**
     * Returns the user agent that started this session.
     *
     * <p>This value is obtained from the first request that started this session
     * calling <code>HttpServletRequest.getHeader(String)</code> with "User-Agent" as parameter. </p>
     *
     * <p>In a typical HTTP environment the user agent (a browser) does not change alongside the session.</p>
     *
     * @return the user agent of this session.
     */
    public String getUserAgent();
}
