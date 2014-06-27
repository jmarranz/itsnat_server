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

package org.itsnat.impl.core.mut.client;

import java.util.LinkedList;
import org.itsnat.impl.core.scriptren.bsren.dom.node.BSRenderAttributeImpl;
import org.itsnat.impl.core.scriptren.bsren.dom.node.BSRenderNodeImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.bsren.dom.node.BSRenderNotAttrOrAbstractViewNodeImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderNodeImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderNotAttrOrAbstractViewNodeImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public class ClientMutationEventListenerStfulDroidImpl extends ClientMutationEventListenerStfulImpl
{
    public ClientMutationEventListenerStfulDroidImpl(ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        super(clientDoc);
    }

    public ClientDocumentStfulDelegateDroidImpl getClientDocumentStfulDelegateDroid()
    {
        return (ClientDocumentStfulDelegateDroidImpl)clientDoc;
    }
    
    @Override    
    public void preRenderAndSendMutationCode(MutationEvent mutEvent)
    {
    }


    @Override
    public void postRenderAndSendMutationCode(MutationEvent mutEvent)
    {
        // Nada que hacer (ver la clase Web para verlo claro)
    }


    @Override
    public Object getTreeDOMNodeInsertedCode(Node newNode)
    {
        ClientDocumentStfulDelegateDroidImpl clientDoc = getClientDocumentStfulDelegateDroid();        
        BSRenderNotAttrOrAbstractViewNodeImpl render = (BSRenderNotAttrOrAbstractViewNodeImpl)BSRenderNodeImpl.getBSRenderNode(newNode,clientDoc);
        Object code = render.getInsertNewNodeCode(newNode,clientDoc); // Puede ser null
        return code;
    }

    @Override
    public Object getTreeDOMNodeRemovedCode(Node removedNode)
    {
        ClientDocumentStfulDelegateDroidImpl clientDoc = getClientDocumentStfulDelegateDroid();        
        BSRenderNotAttrOrAbstractViewNodeImpl render = (BSRenderNotAttrOrAbstractViewNodeImpl)BSRenderNodeImpl.getBSRenderNode(removedNode,clientDoc);
        String code = render.getRemoveNodeCode(removedNode,clientDoc);
        return code;
    }

    @Override
    protected String getDOMAttrModifiedCode(Attr attr, Element elem, int changeType)
    {
        ClientDocumentStfulDelegateDroidImpl clientDoc = getClientDocumentStfulDelegateDroid();
        String code = null;
        BSRenderAttributeImpl render = BSRenderAttributeImpl.getBSRenderAttribute();
        switch(changeType)
        {
            case MutationEvent.ADDITION:
            case MutationEvent.MODIFICATION:
                code = render.setAttributeCode(attr,elem,false,clientDoc);
                break;
            case MutationEvent.REMOVAL:
                code = render.removeAttributeCode(attr,elem,clientDoc);
                break;
            // No hay más casos
        }        
        
        return code;
    }

    @Override
    protected String getCharacterDataModifiedCode(CharacterData charDataNode)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRemoveNodeFromCacheCode(LinkedList<String> idList)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRemoveAllChildCode(Node node)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
