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

package org.itsnat.impl.core.scriptren.bsren.dom.node;

import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.InsertAsMarkupInfoImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class BSRenderCommentImpl extends BSRenderNotAttrOrAbstractViewNodeImpl
{
    private static final BSRenderCommentImpl SINGLETON = new BSRenderCommentImpl();
    
    public static BSRenderCommentImpl getBSRenderComment()
    {
        return SINGLETON;
    }

    @Override
    protected String createNodeCode(Node node, ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return null;
    }

    @Override
    public Object getAppendNewNodeCode(Node parent, Node newNode, String parentVarName, InsertAsMarkupInfoImpl insertMarkupInfo, ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return null;
    }

    @Override
    public Object getInsertNewNodeCode(Node newNode, ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return null;
    }
    
}
