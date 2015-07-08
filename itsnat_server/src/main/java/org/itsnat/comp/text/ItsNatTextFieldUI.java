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

package org.itsnat.comp.text;

/**
 * Is the base interface of a text field component User Interface.
 *
 * <p>Current implementation of this interface only applies to &lt;input type="text"&gt;
 * based components. The text is saved in the <code>value</code> attribute/property.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatTextField#getItsNatTextFieldUI()
 */
public interface ItsNatTextFieldUI extends ItsNatTextComponentUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatTextField getItsNatTextField();

    /**
     * Returns the max number of visible columns of the field.
     *
     * @return the max number of visible columns.
     * @see #setColumns(int)
     */
    public int getColumns();

    /**
     * Sets the max number of visible columns of the field.
     *
     * @param cols the max number of visible columns.
     * @see #getColumns()
     */
    public void setColumns(int cols);
}
