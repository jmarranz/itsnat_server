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

import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.InnerMarkupCodeImpl;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderHTMLElementSVGPluginImpl extends JSRenderHTMLElementW3CImpl
{
    /** Creates a new instance of JSRenderHTMLElementSVGPluginImpl */
    public JSRenderHTMLElementSVGPluginImpl()
    {
        // No nos preocupa el soporte de innerHTML pues realmente
        // NO usamos innerHTML sino que "mentimos" usando
        // setInnerXML en su lugar, por lo que admitimos todos los
        // elementos XHTML
        // Esto es realmente "por cumplir" para que no de error, pues el soporte de <foreignObject>
        // es deficiente (son elementos inútiles SVG pero con namespace XHTML)
    }

    public String getAppendChildrenCodeAsMarkupSentence(InnerMarkupCodeImpl innerMarkupRender,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // Redefinimos setInnerHTML antes de que sea usada (también indirectamente al llamar a setInnerHTML2)
        StringBuilder code = new StringBuilder();

        final String methodName = "setInnerHTML";
        if (!clientDoc.isClientMethodBounded(methodName))
        {
            code.append( "var func = function (parentNode,value)" );
            code.append( "{" );
            code.append( "  this.setInnerXML(parentNode,\"<html xmlns='http://www.w3.org/1999/xhtml'>\" + value + \"</html>\"); ");
            code.append( "};\n" );
            code.append( "itsNatDoc." + methodName + " = func;\n" );

            clientDoc.bindClientMethod(methodName);
        }

        code.append( super.getAppendChildrenCodeAsMarkupSentence(innerMarkupRender, clientDoc) );

        return code.toString();
    }
}

