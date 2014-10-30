/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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
package org.itsnat.impl.core.template.droid;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContext;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.util.IOUtil;

/**
 *
 * @author jmarranz
 */
public class ScriptLocalFile extends ScriptCode
{
    protected File file;
    protected long timeStamp;
    
    public ScriptLocalFile(String src,ServletContext servContext)
    {
        String basePath = servContext.getRealPath("/");
        String filePath = basePath + src;

        File fileBasePath = new File(basePath);            
        File file = new File(filePath);
        boolean unexpected = false;
        try
        {
            unexpected = !file.getCanonicalPath().startsWith(fileBasePath.getCanonicalPath()); // Debemos evitar un intento de leer archivos fuera de la app web
        }
        catch (IOException ex) { throw new ItsNatException(ex); }

        if (unexpected) throw new ItsNatException("Unexpected security break attempt"); // Inexperado pues se supone que el path de <script src=""> lo pone el programador

        this.file = file;
        
        reload();
    }
    
    private void reload()
    {
        long timeStamp = file.lastModified();
        if (this.timeStamp == timeStamp) return;        
        
        synchronized(this)
        {
            this.timeStamp = timeStamp;
            this.code = IOUtil.readTextFile(file,"UTF-8");         
        }
    }
    
    @Override
    public String getCode()
    {
        reload();
        return super.getCode();
    }    
}
