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

package org.itsnat.impl.core.scriptren.jsren.dom.node;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class JSRenderCDATASectionImpl extends JSRenderCharacterDataAliveImpl
{
    public static final JSRenderCDATASectionImpl SINGLETON = new JSRenderCDATASectionImpl();

    /** Creates a new instance of JSCDATASectionRender */
    public JSRenderCDATASectionImpl()
    {
    }

    public String createNodeCode(Node node,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // No válido en HTML
        // http://www.w3.org/2003/01/dom2-javadoc/org/w3c/dom/Document.html#createCDATASection_java.lang.String_
        // http://aptana.com/reference/api/Document.html#Document.createCDATASection

        CDATASection nodeCData = (CDATASection)node;
        return "itsNatDoc.doc.createCDATASection(" + dataTextToJS(nodeCData,clientDoc) + ")";
    }

    public String getCharacterDataModifiedCode(CharacterData node,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return getCharacterDataModifiedCodeDefault(node,clientDoc);
    }
}
