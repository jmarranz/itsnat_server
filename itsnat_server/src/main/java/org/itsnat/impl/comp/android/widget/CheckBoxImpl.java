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

import org.itsnat.comp.android.widget.CheckBox;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.droid.ItsNatStfulDroidDocComponentManagerImpl;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;

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
    public String getClassName()
    {
        return "CheckBox";
    }
    
    @Override
    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        enableEventListener("click"); // Por defecto se procesa, pues es lo importante
    }    

    @Override
    public void processNormalEvent(Event evt)
    {
        String type = evt.getType();
        if (type.equals("click"))
        {
            toggle();
        }
    }



}
