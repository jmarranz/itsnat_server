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

package org.itsnat.impl.comp.android.widget;

import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.comp.android.widget.CheckBox;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocImpl;
import org.itsnat.impl.comp.mgr.droid.ItsNatStfulDroidDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class CheckBoxImpl extends CompoundButtonImpl implements CheckBox
{
    /** Creates a new instance of CheckBoxImpl */
    public CheckBoxImpl(Element element,NameValue[] artifacts,ItsNatStfulDroidDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        init();
    }


    @Override
    public Node createDefaultNode()
    {
        return getDocument().createElement("CheckBox");
    }

}
