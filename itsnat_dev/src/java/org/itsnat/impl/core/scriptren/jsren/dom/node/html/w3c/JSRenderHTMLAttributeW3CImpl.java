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

package org.itsnat.impl.core.scriptren.jsren.dom.node.html.w3c;

import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.browser.web.opera.BrowserOpera;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.html.JSRenderHTMLAttributeImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.NodeScriptRefImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.w3c.dom.Attr;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderHTMLAttributeW3CImpl extends JSRenderHTMLAttributeImpl
{
    /** Creates a new instance of JSRenderHTMLAttributeW3CImpl */
    public JSRenderHTMLAttributeW3CImpl()
    {
    }

    public static JSRenderHTMLAttributeW3CImpl getJSRenderHTMLAttributeW3C(BrowserW3C browser)
    {
        if (browser instanceof BrowserOpera)
        {
            if (browser instanceof BrowserOpera)
                return JSRenderHTMLAttributeOperaImpl.SINGLETON;
            else return null; // No hay más
        }
        else
            return JSRenderHTMLAttributeW3CDefaultImpl.SINGLETON;
    }

    @Override
    public String setAttributeOnlyCode(Attr attr,String attrName,String jsValue,NodeScriptRefImpl nodeRef,boolean newElem,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // Es raro que tenga namespace no nulo, pero por si acaso.
        String namespaceURI = attr.getNamespaceURI();
        if (namespaceURI != null)
        {
            if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            {
                NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                return "itsNatDoc.setAttributeNS2(" + nodeLoc.toScriptNodeLocation(true) + ",\"" + namespaceURI + "\",\"" + attrName + "\"," + jsValue + ");\n";
            }
            else
            {
                String elemVarName = (String)nodeRef.getNodeRef();
                return "itsNatDoc.setAttributeNS(" + elemVarName + ",\"" + namespaceURI + "\",\"" + attrName + "\"," + jsValue + ");\n";
            }
        }
        else
            return super.setAttributeOnlyCode(attr,attrName,jsValue,nodeRef,newElem,clientDoc);
    }
}
