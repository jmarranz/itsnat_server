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

package org.itsnat.impl.core.dompath;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class DOMPathResolverDroid extends DOMPathResolver
{
    /** Creates a new instance of DOMPathResolverOtherNSDoc */
    public DOMPathResolverDroid(ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        super(clientDoc);
    }

    public boolean isFilteredInClient(Node node)
    {
        return false;
    }
    
    @Override
    protected Node getChildNodeFromPos(Node parentNode,int pos,boolean isTextNode)
    {
        if (isTextNode) return null;
        
        if (!parentNode.hasChildNodes()) return null;

        int currPos = 0;
        Node currNode = parentNode.getFirstChild();
        while(currNode != null)
        {
            int type = currNode.getNodeType();
            
            if (type == Node.ELEMENT_NODE && !currNode.getNodeName().equals("script")) // Sólo contamos nodos elemento, ni comentarios ni nodos de texto, los <script> son temporales, no cuentan, en cuanto se puede se eliminan
            {
                if (currPos == pos) 
                    return currNode;
                else 
                    currPos++;
            }

            currNode = currNode.getNextSibling();
        }

        return null;
    }    
    
    @Override
    protected Node getChildNodeFromStrPos(Node parentNode,String posStr)
    {
        // Vemos si se especifica un atributo o nodo de texto
        if (posStr.equals("de")) throw new ItsNatException("INTERNAL ERROR");

        int posBracket = posStr.indexOf('[');
        if (posBracket == -1)
        {
            int pos = Integer.parseInt(posStr);
            return getChildNodeFromPos(parentNode,pos,false);
        }
        else
        {
            return null; // Ni atributos ni nodos de texto soportados
        }
    }    
}
