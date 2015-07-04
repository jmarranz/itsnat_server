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

package org.itsnat.impl.core.template.web.otherns;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.doc.web.ItsNatXULDocumentImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateImpl;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatXULDocumentTemplateVersionImpl extends ItsNatOtherNSDocumentTemplateVersionImpl
{
    /**
     * Creates a new instance of ItsNatXULDocumentTemplateVersionImpl
     */
    public ItsNatXULDocumentTemplateVersionImpl(ItsNatStfulDocumentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        super(docTemplate,source,timeStamp,request,response);
    }

    @Override
    protected ItsNatDocumentImpl createItsNatDocument(Document doc,Browser browser,String requestURL,ItsNatSessionImpl session,boolean stateless)
    {
        return new ItsNatXULDocumentImpl(doc,this,browser,requestURL,session,stateless);
    }

    public String wrapBodyAsDocument(String source)
    {
        // En un futuro podría plantearse el cacheado

        return wrapBodyAsDocument(source,getEncoding(),null,null);
    }

    public static String wrapBodyAsDocument(String source,String encoding,String prefix,String defaultNS)
    {
        if ((prefix == null)&&(defaultNS != null)) throw new ItsNatException("INTERNAL ERROR");

        StringBuilder code = new StringBuilder();
        if (encoding != null) // Si no espeficica es que no es necesaria la cabecera xml
            code.append( "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>" );
        code.append( "<?xml-stylesheet href=\"chrome://global/skin/\" type=\"text/css\"?>" );
        code.append( "<!DOCTYPE window>" ); // Aunque no funciona en MSIE y loadXML nos da igual, esto es Gecko-only
        code.append( "<" );
        if (prefix != null) code.append( prefix + ":" );
        code.append( "window xmlns" );
        if (prefix != null) code.append( ":" + prefix );
        code.append( "=\"" + NamespaceUtil.XUL_NAMESPACE + "\"" );
        if (defaultNS != null)
            code.append( " xmlns=\"" + defaultNS + "\"" );
        code.append( ">" );

        code.append( source );

        code.append( "</" );
        if (prefix != null) code.append( prefix + ":" );
        code.append( "window>" );

        return code.toString();
    }
}
