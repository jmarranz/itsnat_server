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

package org.itsnat.impl.core.resp.shared.otherns;

import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.itsnat.impl.core.resp.shared.bybrow.web.ResponseDelegStfulLoadDocByBOperaOldImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateSVGLoadDocOperaOldImpl extends ResponseDelegateSVGLoadDocImpl
{

    /**
     * Creates a new instance of ResponseDelegateOtherNSLoadDocImpl
     */
    public ResponseDelegateSVGLoadDocOperaOldImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public ResponseDelegStfulLoadDocByBOperaOldImpl getResponseDelegStfulLoadDocByBOperaOld()
    {
        return (ResponseDelegStfulLoadDocByBOperaOldImpl)delegByBrowser;
    }

    protected void rewriteClientUIControlProperties(Element elem,boolean revertJSChanges,StringBuilder code)
    {
        // Que yo sepa SVG no tiene controles propios, pero admite XHTML embebido
        // dentro de <foreignObject>
        rewriteClientHTMLUIControlProperties(elem,revertJSChanges,code);
    }

    @Override
    public void dispatchRequestListeners()
    {
        super.dispatchRequestListeners();

        getResponseDelegStfulLoadDocByBOperaOld().afterLoadRewriteClientUIControlProperties();
    }

}
