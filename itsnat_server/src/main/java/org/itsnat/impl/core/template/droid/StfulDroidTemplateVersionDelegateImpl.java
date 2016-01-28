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

package org.itsnat.impl.core.template.droid;

import java.util.LinkedList;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NodeConstraints;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.itsnat.impl.core.template.StfulTemplateVersionDelegateImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 *
 * @author jmarranz
 */
public class StfulDroidTemplateVersionDelegateImpl extends StfulTemplateVersionDelegateImpl
{

    public StfulDroidTemplateVersionDelegateImpl(MarkupTemplateVersionImpl parent)
    {
        super(parent);
    }
    
    @Override
    public void normalizeDocument(Document doc)
    {
        super.normalizeDocument(doc);

        // Filtramos los comentarios, son incordio y total no se manifiestan en el arbol de View, este método también se usa para los fragments
        // y se llama en el Document antes de guardarlo como template
        NodeConstraints rule = new NodeConstraints()
        {
            @Override
            public boolean match(Node node, Object context)
            {
                return node.getNodeType() == Node.COMMENT_NODE;
            }
        };
        LinkedList<Node> commentList = DOMUtilInternal.getChildNodeListMatching(doc,rule,true,null);
        if (commentList != null)
        {
            for(Node comment : commentList)
                comment.getParentNode().removeChild(comment);
        }     
    }    
}
