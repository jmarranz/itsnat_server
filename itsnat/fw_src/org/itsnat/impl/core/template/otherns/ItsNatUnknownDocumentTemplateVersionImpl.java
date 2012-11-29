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

package org.itsnat.impl.core.template.otherns;

import org.itsnat.core.ItsNatException;

/**
 *
 * @author jmarranz
 */
public class ItsNatUnknownDocumentTemplateVersionImpl
{
    /**
     * Creates a new instance of ItsNatUnknownDocumentTemplateVersionImpl
     */
    public ItsNatUnknownDocumentTemplateVersionImpl()
    {
    }

    public static String wrapBodyAsDocument(String namespace,String source,String encoding,String prefix,String defaultNS)
    {
        if ((prefix == null)&&(defaultNS != null)) throw new ItsNatException("INTERNAL ERROR");

        // No tenemos ni idea de cual es el elemento root, no se si esto funciona

        StringBuffer code = new StringBuffer();
        if (encoding != null) // Si no espeficica es que no es necesaria la cabecera xml
            code.append( "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>" );
// No funciona en MSIE y loadXML: code.append( "<!DOCTYPE root>" );
        code.append( "<" );
        if (prefix != null) code.append( prefix + ":" );
        code.append( "root xmlns" );
        if (prefix != null) code.append( ":" + prefix );
        code.append( "=\"" + namespace + "\"" );
        if (defaultNS != null)
            code.append( " xmlns=\"" + defaultNS + "\"" );
        code.append( ">" );

        code.append( source );

        code.append( "</" );
        if (prefix != null) code.append( prefix + ":" );
        code.append( "root>" );

        return code.toString();
    }


}
