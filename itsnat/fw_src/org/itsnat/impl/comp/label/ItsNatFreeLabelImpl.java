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

package org.itsnat.impl.comp.label;

import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.comp.label.ItsNatFreeLabel;
import org.itsnat.core.NameValue;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.comp.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ItsNatFreeLabelImpl extends ItsNatLabelImpl implements ItsNatFreeLabel
{
    /**
     * Creates a new instance of ItsNatFreeLabelImpl
     */
    public ItsNatFreeLabelImpl(Element element,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        setItsNatLabelEditor(componentMgr.createDefaultItsNatLabelEditor(null));

        init();
    }

    public Node createDefaultNode()
    {
        throw new ItsNatException("There is no default Element and later attachment is not allowed",this);
    }
}
