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

package org.itsnat.impl.core.jsren.dom.node;

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.SVGWebInfoImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.jsren.dom.node.html.msie.JSRenderHTMLCommentMSIEOldImpl;
import org.itsnat.impl.core.jsren.dom.node.otherns.JSRenderSVGCommentSVGWebImpl;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderCommentImpl extends JSRenderCharacterDataImpl
{

    /** Creates a new instance of JSCommentRender */
    public JSRenderCommentImpl()
    {
    }

    public static JSRenderCommentImpl getJSRenderComment(Comment node,ClientDocumentStfulImpl clientDoc)
    {
        if (DOMUtilHTML.isHTMLCharacterData(node))
        {
            Browser browser = clientDoc.getBrowser();
            if (browser instanceof BrowserMSIEOld)
                return JSRenderHTMLCommentMSIEOldImpl.getJSRenderHTMLCommentMSIEOld((BrowserMSIEOld)browser);
            else
                return JSRenderCommentDefaultImpl.SINGLETON;
        }
        else if (SVGWebInfoImpl.isSVGNodeProcessedBySVGWebFlash(node,clientDoc))
            return JSRenderSVGCommentSVGWebImpl.SINGLETON;
        else
            return JSRenderCommentDefaultImpl.SINGLETON;
    }

    protected String createNodeCode(Node node,ClientDocumentStfulImpl clientDoc)
    {
        Comment nodeComm = (Comment)node;
        return "itsNatDoc.doc.createComment(" + dataTextToJS(nodeComm,clientDoc) + ")";
    }
    
    public String getCharacterDataModifiedCode(CharacterData node,ClientDocumentStfulImpl clientDoc)
    {
        return getCharacterDataModifiedCodeDefault(node,clientDoc);
    }
}
