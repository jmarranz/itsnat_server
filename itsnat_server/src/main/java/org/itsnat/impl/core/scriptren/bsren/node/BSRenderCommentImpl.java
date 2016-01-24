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

package org.itsnat.impl.core.scriptren.bsren.node;

import org.itsnat.impl.core.scriptren.jsren.node.*;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.clientdoc.web.SVGWebInfoImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.scriptren.jsren.node.html.msie.JSRenderHTMLCommentMSIEOldImpl;
import org.itsnat.impl.core.scriptren.jsren.node.otherns.JSRenderSVGCommentSVGWebImpl;
import org.itsnat.impl.core.scriptren.shared.node.InsertAsMarkupInfoImpl;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class BSRenderCommentImpl extends BSRenderNotAttrOrAbstractViewNodeImpl
{
    public static final BSRenderCommentImpl SINGLETON = new BSRenderCommentImpl();
    
    /** Creates a new instance of BSRenderCommentImpl */
    public BSRenderCommentImpl()
    {
    }

    public static BSRenderCommentImpl getBSRenderComment()
    {
        return SINGLETON;
    }
    
    @Override
    public Object getAppendNewNodeCode(Node parent, Node newNode, String parentVarName, InsertAsMarkupInfoImpl insertMarkupInfo, ClientDocumentStfulDelegateImpl clientDoc)
    {
        return "";
    }

    @Override
    public Object getInsertNewNodeCode(Node newNode, ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        return "";
    }

    @Override
    public String createNodeCode(Node node, ClientDocumentStfulDelegateImpl clientDoc)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
