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

package org.itsnat.impl.core.mut.doc.droid;

import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.droid.ItsNatStfulDroidDocumentImpl;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerStfulImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public class DocMutationEventListenerStfulDroidImpl extends DocMutationEventListenerStfulImpl
{
    public DocMutationEventListenerStfulDroidImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    protected void checkOperation(MutationEvent mutEvent)
    {
    }

    public ItsNatStfulDroidDocumentImpl getItsNatStfulDroidDocument()
    {
        return (ItsNatStfulDroidDocumentImpl)itsNatDoc;
    }
    
    @Override
    protected void beforeAfterRenderAndSendMutationCode(boolean before,MutationEvent mutEvent,ClientDocumentImpl[] allClients)
    {
        super.beforeAfterRenderAndSendMutationCode(before, mutEvent, allClients);    
     
        if (itsNatDoc.isLoadingPhaseAndFastLoadMode())
        {
            if (before)
            {
                String type = mutEvent.getType();                
                if (type.equals("DOMNodeInserted"))
                {
                    Node insertedNode = (Node)mutEvent.getTarget();
                    if (insertedNode instanceof Element && "script".equals(((Element)insertedNode).getTagName()))
                    {
                        Element scriptElem = (Element)insertedNode;                        
                        scriptElem.getParentNode().removeChild(scriptElem);
                        
                        getItsNatStfulDroidDocument().addScript(scriptElem);
                    }    
                }
            }
        }        
    }
}
