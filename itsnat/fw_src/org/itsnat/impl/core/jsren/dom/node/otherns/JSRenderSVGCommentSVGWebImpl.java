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
package org.itsnat.impl.core.jsren.dom.node.otherns;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.dom.node.JSRenderCommentImpl;
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;

/**
 * En SVGWeb por ahora los comentarios son tolerados en carga pero no se reflejan
 * en el DOM y no soporta la inserción y borrado de comentarios.
 *
 * @author jmarranz
 */
public class JSRenderSVGCommentSVGWebImpl extends JSRenderCommentImpl
{
    public static final JSRenderSVGCommentSVGWebImpl SINGLETON = new JSRenderSVGCommentSVGWebImpl();

    /** Creates a new instance of JSRenderHTMLCommentMSIEPocketImpl */
    public JSRenderSVGCommentSVGWebImpl()
    {
    }

    protected String getAppendCompleteChildNode(Node parent,Node newNode,String parentVarName,ClientDocumentStfulImpl clientDoc)
    {
        return "";
    }

    protected String getInsertCompleteNodeCode(Node newNode,ClientDocumentStfulImpl clientDoc)
    {
        return "";
    }

    public String getRemoveNodeCode(Node removedNode,ClientDocumentStfulImpl clientDoc)
    {
        return "";
    }

    public String getCharacterDataModifiedCode(CharacterData node,ClientDocumentStfulImpl clientDoc)
    {
        return "";
    }

}
