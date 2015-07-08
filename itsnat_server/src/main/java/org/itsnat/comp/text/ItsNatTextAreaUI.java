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
 * Is the base interface of a text area component User Interface.
 *
 * <p>Current implementation of this interface only applies to &lt;textarea&gt;
 * based components. The text is saved in the <code>value</code> attribute/property, when
 * loading the document is saved too as the &lt;textarea&gt; content.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatTextArea#getItsNatTextAreaUI()
 */
public interface ItsNatTextAreaUI extends ItsNatTextComponentUI
{
    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatTextArea getItsNatTextArea();

    /**
     * Returns the max number of visible columns of the text area.
     *
     * @return the max number of visible columns.
     * @see #setColumns(int)
     */
    public int getColumns();

    /**
     * Sets the max number of visible columns of the text area.
     *
     * @param cols the max number of visible columns.
     * @see #getColumns()
     */
    public void setColumns(int cols);

    /**
     * Returns the max number of visible rows of the text area.
     *
     * @return the max number of visible rows.
     * @see #setRows(int)
     */
    public int getRows();

    /**
     * Sets the max number of visible rows of the text area.
     *
     * @param rows the max number of visible rows.
     * @see #getRows()
     */
    public void setRows(int rows);

    /**
     * Informs whether the text area wraps lines.
     *
     * <p>Current implementation uses the <code>wrap</code> attribute
     * of the &lt;textarea&gt;.</p>
     *
     * @return true if line wrap is enabled.
     * @see #setLineWrap(boolean)
     */
    public boolean isLineWrap();

    /**
     * Enables or disables the line wrap.
     *
     * <p>Current implementation uses the <code>wrap</code> attribute
     * of the &lt;textarea&gt;.</p>
     *
     * @param wrap if true to enable the line wrap.
     * @see #isLineWrap()
     */
    public void setLineWrap(boolean wrap);
}
