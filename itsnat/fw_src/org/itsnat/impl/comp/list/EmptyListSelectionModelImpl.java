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

package org.itsnat.impl.comp.list;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author jmarranz
 */
public class EmptyListSelectionModelImpl extends DefaultListSelectionModel
{
    public static final ListSelectionModel SINGLETON = new EmptyListSelectionModelImpl();

    /** Creates a new instance of EmptyListSelectionModelImpl */
    public EmptyListSelectionModelImpl()
    {
    }

    public void setSelectionInterval(int index0, int index1)
    {
    }

    public void addSelectionInterval(int index0, int index1)
    {
    }

    public void removeSelectionInterval(int index0, int index1)
    {
    }

    public void setAnchorSelectionIndex(int index)
    {
    }

    public void setLeadSelectionIndex(int index)
    {
    }

    public void clearSelection()
    {
    }

    public void insertIndexInterval(int index, int length, boolean before)
    {
    }

    public void removeIndexInterval(int index0, int index1)
    {
    }

    public void setValueIsAdjusting(boolean valueIsAdjusting)
    {
    }

    public void setSelectionMode(int selectionMode)
    {
    }

    public void addListSelectionListener(ListSelectionListener x)
    {
    }

    public void removeListSelectionListener(ListSelectionListener x)
    {
    }

    public void setLeadAnchorNotificationEnabled(boolean flag)
    {
    }

}
