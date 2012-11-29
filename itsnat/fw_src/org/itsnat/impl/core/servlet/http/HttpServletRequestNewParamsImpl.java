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

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Utilizamos javax.servlet.http.HttpServletRequestWrapper
 * para ser tolerantes a los cambios de las sucesivas especificaciones.
 *
 * @author jmarranz
 */
public class HttpServletRequestNewParamsImpl extends HttpServletRequestWrapper implements HttpServletRequest
{
    protected Map paramMap;

    public HttpServletRequestNewParamsImpl(HttpServletRequest parent, Map paramMap)
    {
        super(parent);
        this.paramMap = Collections.unmodifiableMap(paramMap);
    }

    public HttpServletRequest getHttpServletRequest()
    {
        return (HttpServletRequest) getRequest();
    }

    public String getParameter(String arg0)
    {
        String[] values = getParameterValues(arg0);
        if (values == null)
            return null;
        if (values.length == 0)
            return null; // Yo creo que esto no debería ocurrir nunca pero por si acaso
        return (String) values[0];
    }

    public Enumeration getParameterNames()
    {
        return Collections.enumeration(paramMap.keySet());
    }

    public String[] getParameterValues(String arg0)
    {
        return (String[])paramMap.get(arg0);
    }

    public Map getParameterMap()
    {
        return paramMap;
    }
}
