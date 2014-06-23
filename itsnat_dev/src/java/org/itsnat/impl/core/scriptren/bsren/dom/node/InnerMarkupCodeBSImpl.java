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

package org.itsnat.impl.core.scriptren.bsren.dom.node;

import org.itsnat.impl.core.scriptren.shared.dom.node.*;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class InnerMarkupCodeBSImpl extends InnerMarkupCodeImpl
{
    protected BSRenderElementImpl render;

    public InnerMarkupCodeBSImpl(BSRenderElementImpl render,Element parentNode,String parentNodeLocator,boolean useNodeLocation,String firstInnerMarkup)
    {
        super(parentNode,parentNodeLocator,useNodeLocation,firstInnerMarkup);
        this.render = render;
    }
   
    public String render(ClientDocumentImpl clientDoc)
    {
        ClientDocumentStfulDelegateDroidImpl clientDocDeleg = (ClientDocumentStfulDelegateDroidImpl)((ClientDocumentStfulImpl)clientDoc).getClientDocumentStfulDelegate();
        return render.getAppendChildrenCodeAsMarkupSentence(this,clientDocDeleg);
    }
}
