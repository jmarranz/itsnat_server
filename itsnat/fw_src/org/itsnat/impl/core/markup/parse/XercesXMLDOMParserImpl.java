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

package org.itsnat.impl.core.markup.parse;

import org.apache.batik.dom.GenericDocumentType;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XNIException;
import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.w3c.dom.Attr;

/**
 *
 * @author jmarranz
 */
public class XercesXMLDOMParserImpl extends org.apache.xerces.parsers.DOMParser        
{
    public XercesXMLDOMParserImpl()
    {
    }

    @Override
    public void xmlDecl(String version, String encoding, String standalone,
                        Augmentations augs) throws XNIException
    {
        DocumentImpl batikDoc = (DocumentImpl)getDocument();
        batikDoc.setXMLDec(version,encoding,standalone);
    }

    @Override
    public void doctypeDecl(String root,String publicId,String systemId,Augmentations augs) throws XNIException
    {
        DocumentImpl batikDoc = (DocumentImpl)getDocument();
        GenericDocumentType docType = (GenericDocumentType)batikDoc.getImplementation().createDocumentType(root, publicId, systemId);
        docType.setOwnerDocument(batikDoc);
        batikDoc.setDoctype(docType);
    }
       
}
