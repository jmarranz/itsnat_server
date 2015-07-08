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

package org.itsnat.impl.comp.tree;

import java.beans.PropertyChangeListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.RowMapper;
import javax.swing.tree.TreePath;

/**
 *
 * @author jmarranz
 */
public class EmptyTreeSelectionModelImpl extends DefaultTreeSelectionModel
{
    public static final EmptyTreeSelectionModelImpl SINGLETON = new EmptyTreeSelectionModelImpl();

    /**
     * Creates a new instance of EmptyTreeSelectionModelImpl
     */
    public EmptyTreeSelectionModelImpl()
    {
    }

    public void setSelectionPaths(TreePath[] pPaths)
    {
    }

    public void addSelectionPaths(TreePath[] paths)
    {
    }

    public void removeSelectionPaths(TreePath[] paths)
    {
    }

    public void addSelectionPath(TreePath path)
    {
    }

    public void removeSelectionPath(TreePath path)
    {
    }

    public void setSelectionPath(TreePath path)
    {
    }

    public void setSelectionMode(int mode)
    {
    }

    public void setRowMapper(RowMapper newMapper)
    {
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
    }

    public void addTreeSelectionListener(TreeSelectionListener x)
    {
    }

    public void removeTreeSelectionListener(TreeSelectionListener x)
    {
    }

    public void clearSelection()
    {
    }

    public void resetRowSelection()
    {
    }

}
