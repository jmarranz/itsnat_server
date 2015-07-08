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
package org.itsnat.impl.comp.iframe;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class KnownSizeInputStreamImpl extends FilterInputStream
{
    protected long sizeMax;
    protected long count;

    public KnownSizeInputStreamImpl(InputStream input, long sizeMax)
    {
        super(input);
        this.sizeMax = sizeMax;
    }

    public long getSizeMax()
    {
        return sizeMax;
    }

    @Override
    public int read() throws IOException
    {
        int res = super.read();
        if (res == -1) return -1;
        if (count + 1 > sizeMax) return -1; // Ya se ha terminado de leer el archivo, será el delimitador final de la request
        count++;
        return res;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        int res = super.read(b, off, len);
        if (res == -1) return -1;
        if (count == sizeMax) return -1;
        res = (int)updateCount(res);
        return res;
    }

    @Override
    public long skip(long n) throws IOException
    {
        long res = super.skip(n);
        if (res == 0) return 0;
        if (count == sizeMax) return 0;
        res = updateCount(res);
        return res;
    }

    @Override
    public int available() throws IOException
    {
	int res = super.available();
        if (res == 0) return 0;
        if (count == sizeMax) return 0;
        return (int)correctReadedBytes(res);
    }

    protected long correctReadedBytes(long readedBytes)
    {
        if (count + readedBytes > sizeMax)
        {
            // Se ha leído un bloque del archivo que también contiene el delimitador final de la request
            // sólo considerar la parte del archivo
            readedBytes = sizeMax - count;
        }
        return readedBytes;
    }

    protected long updateCount(long readedBytes)
    {
        readedBytes = correctReadedBytes(readedBytes);
        this.count += readedBytes;
        return readedBytes;
    }
}
