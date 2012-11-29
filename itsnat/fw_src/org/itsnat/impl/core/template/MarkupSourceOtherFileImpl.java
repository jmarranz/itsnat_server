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

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class MarkupSourceOtherFileImpl extends MarkupSourceFromFileImpl
{
    protected URL url;
    protected long timeStamp;

    public MarkupSourceOtherFileImpl(URL url)
    {
        this.url = url;
        this.timeStamp = System.currentTimeMillis();
    }

    public long getCurrentTimestamp(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        return timeStamp;
    }

    public boolean isMustReload(long currentTimestamp,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        // Decidimos no recargar para cada request de carga de template,
        // si se necesita una política diferente usar un MarkupSource a medida
        return false;
    }

    public InputSource createInputSource(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        try
        {
            URLConnection conn = url.openConnection();
            return new InputSource(conn.getInputStream());
        }
        catch(IOException ex)
        {
            throw new ItsNatException(ex);
        }
    }

    public Object getSource()
    {
        return url.toExternalForm(); // Devuelve la URL al menos en el caso HTTP, comprobado
    }
}
