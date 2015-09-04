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

import java.io.File;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class MarkupSourceLocalFileImpl extends MarkupSourceFromFileImpl
{
    protected final File file;
    protected String url;

    public MarkupSourceLocalFileImpl(String path)
    {
        this.file = new File(path);
        if (!file.exists())
            throw new ItsNatException("File " + file.getAbsoluteFile() + " does not exist");
        int index = path.indexOf("file:");
        if (index != -1) this.url = path;  // Ya tiene formato URL
        else this.url = "file:" + path;
    }

    @Override
    public long getCurrentTimestamp(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        // Es multihilo
        if (!file.exists()) // Ha sido borrado (existió)
            throw new ItsNatException("File " + file.getAbsoluteFile() + " does not exist",this);
        return file.lastModified();
    }

    @Override
    public boolean isMustReload(long oldTimestamp,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        long currentTimeStamp = getCurrentTimestamp(request,response);
        return (currentTimeStamp > oldTimestamp);
    }

    @Override
    public InputSource createInputSource(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        return new InputSource(url);
    }

    @Override
    public Object getSource()
    {
        return url;
    }
}
