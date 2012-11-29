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

import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.tmpl.TemplateSource;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public abstract class MarkupSourceImpl
{
    public MarkupSourceImpl()
    {
    }

    public static MarkupSourceImpl createMarkupSource(Object source)
    {
        if (source instanceof String)
            return MarkupSourceFromFileImpl.createMarkupSourceFromFile((String)source);
        else if (source instanceof TemplateSource)
            return new TemplateSourceWrapperImpl((TemplateSource)source);
        else
             throw new ItsNatException("Up to now sources of templates only can be String or TemplateSource objects");
    }

    public abstract boolean isMustReload(long currentTimestamp,ItsNatServletRequest request, ItsNatServletResponse response);
    public abstract long getCurrentTimestamp(ItsNatServletRequest request, ItsNatServletResponse response);
    public abstract InputSource createInputSource(ItsNatServletRequest request, ItsNatServletResponse response);
    public abstract Object getSource(); // Devolver por ahora como String y con formato URL
}
