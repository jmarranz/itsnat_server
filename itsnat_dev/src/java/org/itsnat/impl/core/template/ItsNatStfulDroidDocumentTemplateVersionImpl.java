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

package org.itsnat.impl.core.template;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.doc.droid.ItsNatDroidDocumentImpl;
import org.itsnat.impl.core.markup.parse.XercesDOMParserWrapperImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDroidDocumentTemplateVersionImpl extends ItsNatStfulDocumentTemplateVersionImpl
{
    public ItsNatStfulDroidDocumentTemplateVersionImpl(ItsNatStfulDocumentTemplateImpl docTemplate, InputSource source, long timeStamp, ItsNatServletRequest request, ItsNatServletResponse response)
    {
        super(docTemplate, source, timeStamp, request, response);
    }

    @Override
    public String wrapBodyAsDocument(String source)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override    
    public Element getBodyParentElement(Document doc)
    {
        return doc.getDocumentElement(); 
    }
    
    @Override
    protected ItsNatDocumentImpl createItsNatDocument(Document doc, Browser browser, String requestURL, ItsNatSessionImpl session, boolean stateless)
    {
        return new ItsNatDroidDocumentImpl(doc,this,browser,requestURL,session,stateless);
    }

    @Override
    protected MarkupTemplateVersionDelegateImpl createMarkupTemplateVersionDelegate()
    {
        return new StfulDroidTemplateVersionDelegateImpl(this);        
    }
    
    @Override
    public Document parseDocument(InputSource input,XercesDOMParserWrapperImpl parser)
    {
        Document doc = super.parseDocument(input,parser);
        // Filtramos los comentarios, son incordio y total no se manifiestan en el arbol de View, este método también se usa para los fragments 
        
        Node child = ItsNatTreeWalker.getNextNode(doc);
        while(child != null)
        {
            if (child.getNodeType() == Node.COMMENT_NODE)
            {
                Node newChild = child.getPreviousSibling();
                if (newChild == null) newChild = child.getParentNode();
                child.getParentNode().removeChild(child);
                child = newChild;
            }
            child = ItsNatTreeWalker.getNextNode(child);
        }        
        
        return doc;
    }    
    
}
