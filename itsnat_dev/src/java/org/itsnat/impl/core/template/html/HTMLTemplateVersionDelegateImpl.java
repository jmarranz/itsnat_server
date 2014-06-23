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

package org.itsnat.impl.core.template.html;

import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.template.StfulWebTemplateVersionDelegateImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLMetaElement;
import org.w3c.dom.html.HTMLStyleElement;

/**
 *
 * @author jmarranz
 */
public class HTMLTemplateVersionDelegateImpl extends StfulWebTemplateVersionDelegateImpl
{
    /**
     * Creates a new instance of HTMLTemplateVersionDelegateImpl
     */
    public HTMLTemplateVersionDelegateImpl(MarkupTemplateVersionImpl parent)
    {
        super(parent);
    }

    public void normalizeDocument(Document doc)
    {
        super.normalizeDocument(doc);

        normalizeHTMLDocument((HTMLDocument)doc);
    }

    public static void normalizeHTMLDocument(HTMLDocument doc)
    {
        // Este problema sólo se da en documentos HTML

        LinkedList<Node> styles = DOMUtilInternal.getChildElementListWithTagNameNS(doc,NamespaceUtil.XHTML_NAMESPACE,"style",true);
        if (styles != null)
        {
            /* Este chequeo es muy conveniente porque el parser de FireFox (v2 al menos)
             * y Safari (v3) automáticamente mueve un elemento <style> bajo <body> a <head>
             * (el último) lo cual perturba el layout esperado.
             * Por ello lo hacemos nosotros antes.
             */
            HTMLHeadElement head = DOMUtilHTML.getHTMLHead(doc);
            for(Iterator<Node> it = styles.iterator(); it.hasNext(); )
            {
                HTMLStyleElement style = (HTMLStyleElement)it.next();
                if (style.getParentNode() != head)
                    head.appendChild(style);
            }
        }
    }

    public boolean isSVGWebMetaDeclaration(Element elem)
    {
        return isSVGWebMetaDeclarationStatic(elem);
    }

    public static boolean isSVGWebMetaDeclarationStatic(Element elem)
    {
        return (elem instanceof HTMLMetaElement) &&
                ((HTMLMetaElement)elem).getName().equals("svg.render.forceflash");
    }
}
