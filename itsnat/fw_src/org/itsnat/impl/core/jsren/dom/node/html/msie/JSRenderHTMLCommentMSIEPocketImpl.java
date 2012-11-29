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
package org.itsnat.impl.core.jsren.dom.node.html.msie;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLCommentMSIEPocketImpl extends JSRenderHTMLCommentMSIEOldImpl
{
    public static final JSRenderHTMLCommentMSIEPocketImpl SINGLETON = new JSRenderHTMLCommentMSIEPocketImpl();

    /** Creates a new instance of JSRenderHTMLCommentMSIEPocketImpl */
    public JSRenderHTMLCommentMSIEPocketImpl()
    {
    }

/*
    public String dataTextToJS(CharacterData node,ClientDocumentStfulImpl clientDoc)
    {
        String code = super.dataTextToJS(node,clientDoc);
        // Metemos los <!-- --> dentro de las "" en las que dataTextToJS mete el texto.
        code = "\"<!--" + code.substring(1,code.length() - 1) + "-->\"";
        return code;
    }
*/
    protected String getAppendCompleteChildNode(Node parent,Node newNode,String parentVarName,ClientDocumentStfulImpl clientDoc)
    {
        return "";
        //String value = dataTextToJS((Comment)newNode,clientDoc);
        //return JSRenderHTMLFilteredNodeMSIEPocketImpl.getAppendFilteredNodeCode(parentVarName,value);
    }

    protected String getInsertCompleteNodeCode(Node newNode,ClientDocumentStfulImpl clientDoc)
    {
        return "";
        //Comment newComNode = (Comment)newNode;
        //String value = dataTextToJS(newComNode,clientDoc);
        //return JSRenderHTMLFilteredNodeMSIEPocketImpl.getInsertFilteredNodeCode(newComNode,value,clientDoc);
    }

    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulImpl clientDoc)
    {
        return "";
        //Comment commentNode = (Comment)removedNode;
        //String jsValue = dataTextToJS("",clientDoc);
        //return JSRenderHTMLFilteredNodeMSIEPocketImpl.getModifiedFilteredNodeCode(commentNode,jsValue,clientDoc);
    }

    public String getCharacterDataModifiedCode(CharacterData node,ClientDocumentStfulImpl clientDoc)
    {
        return "";
        //String jsNewValue = dataTextToJS(node.getData(),clientDoc);
        //return JSRenderHTMLFilteredNodeMSIEPocketImpl.getModifiedFilteredNodeCode(node,jsNewValue,clientDoc);
    }

}
