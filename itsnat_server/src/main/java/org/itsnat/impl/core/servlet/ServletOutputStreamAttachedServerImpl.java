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

package org.itsnat.impl.core.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletOutputStream;
import org.itsnat.core.ItsNatException;

/**
 *
 * @author jmarranz
 */
public class ServletOutputStreamAttachedServerImpl extends ServletOutputStream
{
    protected ByteArrayOutputStream byteArray = new ByteArrayOutputStream(1024);

    public ServletOutputStreamAttachedServerImpl()
    {
    }

    public void write(int b) throws IOException
    {
        byteArray.write(b);
    }

    public String getString(String encoding)
    {
        try
        {
            flush();
            close();
        }
        catch(IOException ex)
        {
            throw new ItsNatException(ex);
        }

        String res;
        try
        {
            res = byteArray.toString(encoding);
        }
        catch(UnsupportedEncodingException ex)
        {
            throw new ItsNatException(ex);
        }
        this.byteArray = null;
        return res;
    }
}
