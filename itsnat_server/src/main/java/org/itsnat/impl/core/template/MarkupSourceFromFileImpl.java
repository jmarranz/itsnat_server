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

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author jmarranz
 */
public abstract class MarkupSourceFromFileImpl extends MarkupSourceImpl
{
    public MarkupSourceFromFileImpl()
    {
    }

    public static MarkupSourceFromFileImpl createMarkupSourceFromFile(String urlStr)
    {
        urlStr = urlStr.trim(); // Los espacios al final y al principio hacen fallar a java.io.File en Linux por ejemplo
        if ((urlStr.indexOf(':') == -1) || // Tenemos la seguridad de que no tiene formato de URL
             urlStr.startsWith("file:"))   // URL tipo archivo local
                return new MarkupSourceLocalFileImpl(urlStr);
        else
        {
            URL url;
            try
            {
                url = new URL(urlStr);
            }
            catch(MalformedURLException ex)
            {
                // Por ejemplo puede ser un path de Windows "C:\\..." (tiene dos puntos que nos hizo creer que era un URL)
                return new MarkupSourceLocalFileImpl(urlStr);
            }
            return new MarkupSourceOtherFileImpl(url);
        }
    }

}
