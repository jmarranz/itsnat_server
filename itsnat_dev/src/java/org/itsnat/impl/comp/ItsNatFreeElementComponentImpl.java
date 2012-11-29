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

package org.itsnat.impl.comp;

import org.itsnat.comp.ItsNatElementComponent;
import org.itsnat.core.NameValue;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.ItsNatElementComponentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatFreeElementComponentImpl extends ItsNatElementComponentImpl implements ItsNatElementComponent
{

    /** Creates a new instance of ItsNatFreeElementComponentImpl */
    public ItsNatFreeElementComponentImpl(Element node,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(node,artifacts,componentMgr);
    }

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        return null;
    }

    public Node createDefaultNode()
    {
        throw new ItsNatException("There is no default Element and later attachment is not allowed",this);
    }
}
