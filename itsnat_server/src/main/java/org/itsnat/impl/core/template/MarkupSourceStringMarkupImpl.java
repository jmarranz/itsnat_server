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
package org.itsnat.impl.core.template;

import java.io.Serializable;
import java.io.StringReader;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class MarkupSourceStringMarkupImpl extends MarkupSourceImpl implements Serializable
{
    protected String markupCode;

    public MarkupSourceStringMarkupImpl(String markupCode)
    {
        this.markupCode = markupCode;
    }

    @Override
    public long getCurrentTimestamp(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        return System.currentTimeMillis(); // Sólo se llama una sola vez
    }

    @Override
    public boolean isMustReload(long currentTimestamp,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        return true; // Yo creo que valdría lanzar una excepción (estudiar)
    }

    @Override
    public InputSource createInputSource(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        return new InputSource(new StringReader(markupCode));
    }

    @Override
    public Object getSource()
    {
        return null;
    }

}
