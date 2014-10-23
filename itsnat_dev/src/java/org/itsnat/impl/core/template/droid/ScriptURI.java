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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.util.IOUtil;

/**
 *
 * @author jmarranz
 */
public class ScriptURI extends ScriptCode
{
    public ScriptURI(URI uri)
    {
        URL url;
        try
        {
            url = uri.toURL();
        }
        catch (IOException ex) { throw new ItsNatException(ex); }        
        
        byte[] content = IOUtil.readURL(url);
        try        
        {
            this.code = new String(content,"UTF-8");
        }
        catch (UnsupportedEncodingException ex) { throw new ItsNatException(ex); }    
    }
}
