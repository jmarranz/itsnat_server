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

import org.itsnat.comp.android.widget.TextView;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.android.view.ViewImpl;
import org.itsnat.impl.comp.mgr.droid.ItsNatStfulDroidDocComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class TextViewImpl extends ViewImpl implements TextView
{
    protected boolean enabled = true; // Por defecto
    
    /** Creates a new instance of CheckBoxImpl */
    public TextViewImpl(Element element,NameValue[] artifacts,ItsNatStfulDroidDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean b)
    {
        getItsNatDocumentImpl().addCodeToSend(getNodeReference() + ".setEnabled(" + b + ");");
        this.enabled = b;
    }    
}
