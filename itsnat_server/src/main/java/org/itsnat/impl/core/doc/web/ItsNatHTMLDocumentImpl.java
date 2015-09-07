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

package org.itsnat.impl.core.doc.web;

import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerImpl;
import org.itsnat.impl.core.mut.doc.web.DocMutationEventListenerHTMLImpl;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.mgr.web.ItsNatHTMLDocComponentManagerImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.domutil.ElementGroupManagerImpl;
import org.itsnat.impl.core.domutil.HTMLElementGroupManagerImpl;
import org.itsnat.impl.core.template.web.html.ItsNatHTMLDocumentTemplateVersionImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLDocumentImpl extends ItsNatStfulWebDocumentImpl implements ItsNatHTMLDocument
{

    /** Creates a new instance of ItsNatHTMLDocumentImpl */
    public ItsNatHTMLDocumentImpl(HTMLDocument doc, ItsNatHTMLDocumentTemplateVersionImpl docLoader,Browser browser,String requestURL,ItsNatSessionImpl parentSession,boolean stateless)
    {
        super(doc,docLoader,browser,requestURL,parentSession,stateless);
    }

    @Override
    public DocMutationEventListenerImpl createInternalMutationEventListener()
    {
        return new DocMutationEventListenerHTMLImpl(this);
    }

    public ItsNatHTMLDocComponentManagerImpl getItsNatHTMLComponentManagerImpl()
    {
        return (ItsNatHTMLDocComponentManagerImpl) getItsNatComponentManagerImpl();
    }

    @Override
    public ItsNatHTMLComponentManager getItsNatHTMLComponentManager()
    {
        return getItsNatHTMLComponentManagerImpl();
    }

    @Override
    public ItsNatDocComponentManagerImpl createItsNatComponentManager()
    {
        return new ItsNatHTMLDocComponentManagerImpl(this);
    }

    public ItsNatHTMLDocumentTemplateVersionImpl getItsNatHTMLDocumentTemplateVersion()
    {
        return (ItsNatHTMLDocumentTemplateVersionImpl)docTemplateVersion;
    }

    @Override
    public HTMLDocument getHTMLDocument()
    {
        return (HTMLDocument)doc;
    }

    @Override
    public ElementGroupManagerImpl createElementGroupManager()
    {
        // Leer comentarios en HTMLElementGroupManagerImpl
        return new HTMLElementGroupManagerImpl(this);
    }

    @Override
    public Element getVisualRootElement()
    {
        return getHTMLDocument().getBody();
    }

    @Override
    public boolean isNewNodeDirectChildOfContentRoot(Node newNode)
    {
        Node parentNode = newNode.getParentNode();
        if (parentNode == null) return false; // No ocurre nunca pero por si acaso
        // Consideramos "roots" de contenido en HTML al <head> y el <body>
        HTMLDocument doc = getHTMLDocument();
        if (parentNode.getParentNode() == doc.getDocumentElement())
        {
            // Vemos si es el <body> o el <head>
            if (doc.getBody() == parentNode) return true;
            else if (DOMUtilHTML.getHTMLHead(doc) == parentNode) return true;
            // Podría ser un comentario en esa zona aunque en teoría los quitamos al cargar el DOM
            // es raro que lleguemos aquí
        }
        return false;
    }
}
