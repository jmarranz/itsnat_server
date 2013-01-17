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

import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatServletRequest;

/**
 * Is the ItsNat wrapper of the <code>javax.servlet.http.HttpServletRequest</code> object.
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatHttpServletRequest extends ItsNatServletRequest
{
    /**
     * Returns the wrapped <code>javax.servlet.http.HttpServletRequest</code> object.
     *
     * @return the wrapped servlet request object. Can not be null.
     */
    public HttpServletRequest getHttpServletRequest();

    /**
     * Returns the ItsNat HTTP servlet associated to this request.
     *
     * @return the ItsNat HTTP servlet of this request. Can not be null.
     */
    public ItsNatHttpServlet getItsNatHttpServlet();

    /**
     * Returns the ItsNat HTTP session associated to this request.
     *
     * @return the ItsNat HTTP session of this request. Can not be null.
     */
    public ItsNatHttpSession getItsNatHttpSession();
}
