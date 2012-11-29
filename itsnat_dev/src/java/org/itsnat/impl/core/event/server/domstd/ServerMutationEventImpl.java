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

package org.itsnat.impl.core.event.server.domstd;

import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public class ServerMutationEventImpl extends ServerItsNatDOMStdEventImpl implements MutationEvent
{
    protected Node relatedNode;
    protected String prevValue;
    protected String newValue;
    protected String attrName;
    protected short attrChange;

    /** Creates a new instance of ServerMutationEventImpl */
    public ServerMutationEventImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public Node getRelatedNode()
    {
        return relatedNode;
    }

    public String getPrevValue()
    {
        return prevValue;
    }

    public String getNewValue()
    {
        return newValue;
    }

    public String getAttrName()
    {
        return attrName;
    }

    public short getAttrChange()
    {
        return attrChange;
    }

    public void initMutationEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, Node relatedNodeArg, String prevValueArg, String newValueArg, String attrNameArg, short attrChangeArg)
    {
        super.initEvent(typeArg,canBubbleArg,cancelableArg);

        this.relatedNode = relatedNodeArg;
        this.prevValue = prevValueArg;
        this.newValue = newValueArg;
        this.attrName = attrNameArg;
        this.attrChange = attrChangeArg;
    }

}
