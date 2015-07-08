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

import org.itsnat.comp.text.ItsNatFormattedTextField.ItsNatFormatter;
import org.itsnat.comp.text.ItsNatFormattedTextField.ItsNatFormatterFactory;

/**
 * Defines a factory with several specialized formatters.
 *
 * @author Jose Maria Arranz Santamaria
 * @see #getItsNatFormatter(ItsNatFormattedTextField)
 * @see ItsNatFormattedTextField#createDefaultItsNatFormatterFactory()
 */
public interface ItsNatFormatterFactoryDefault extends ItsNatFormatterFactory
{
    /**
     * Returns the ItsNat formatter to use if the component value
     * ({@link ItsNatFormattedTextField#getValue()}) is null.
     *
     * @return the formatter to use when the component value is null.
     * @see #setNullFormatter(ItsNatFormattedTextField.ItsNatFormatter)
     */
    public ItsNatFormatter getNullFormatter();

    /**
     * Sets the ItsNat formatter to use if the component value
     * ({@link ItsNatFormattedTextField#getValue()}) is null.
     *
     * @param atf the ItsNat formatter.
     * @see #getNullFormatter()
     */
    public void setNullFormatter(ItsNatFormatter atf);

    /**
     * Returns the ItsNat formatter to use if the {@link ItsNatFormattedTextField}
     * is not being edited ({@link ItsNatFormattedTextField#hasFocus()} returns false)
     * and either the value is not-null, or the value is null and a null formatter has not been specified.
     *
     * @return the formatter to use in display mode.
     * @see #setDisplayFormatter(ItsNatFormattedTextField.ItsNatFormatter)
     * @see #getNullFormatter()
     */
    public ItsNatFormatter getDisplayFormatter();

    /**
     * Sets the ItsNat formatter to use if the component
     * is not being edited and either the value is not null,
     * or the value is null and a null formatter has has not been specified.
     *
     * @param atf the formatter to use in display mode.
     * @see #getDisplayFormatter()
     */
    public void setDisplayFormatter(ItsNatFormatter atf);

    /**
     * Returns the ItsNat formatter to use if the {@link ItsNatFormattedTextField}
     * is being edited ({@link ItsNatFormattedTextField#hasFocus()} returns true)
     * and either the value is not-null, or the value is null and a null formatter has has not been specified.
     *
     * @return the formatter to use in edition mode.
     * @see #setEditFormatter(ItsNatFormattedTextField.ItsNatFormatter)
     * @see #getNullFormatter()
     */
    public ItsNatFormatter getEditFormatter();

    /**
     * Sets the ItsNat formatter to use if the component
     * is being edited and either the value is not-null,
     * or the value is null and a null formatter has has not been specified.
     *
     * @param atf the formatter to use in edition mode.
     * @see #getEditFormatter()
     */
    public void setEditFormatter(ItsNatFormatter atf);

    /**
     * Returns the ItsNat formatter to use
     * as a last resort, eg in case a display, edit or null
     * formatter has not been specified.
     *
     * @return the formatter to use if a more specific one is not specified.
     * @see #setDefaultFormatter(ItsNatFormattedTextField.ItsNatFormatter)
     * @see #getNullFormatter()
     * @see #getDisplayFormatter()
     * @see #getEditFormatter()
     */
    public ItsNatFormatter getDefaultFormatter();

    /**
     * Sets the ItsNat formatter to use
     * as a last resort, eg in case a display, edit or null
     * formatter has not been specified.
     *
     * @param atf the formatter to use if a more specific one is not specified.
     * @see #getDefaultFormatter()
     */
    public void setDefaultFormatter(ItsNatFormatter atf);

}
