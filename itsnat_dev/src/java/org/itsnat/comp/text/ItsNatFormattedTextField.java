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

import org.itsnat.comp.text.ItsNatTextField;
import java.beans.PropertyVetoException;
import java.text.Format;
import java.text.ParseException;

/**
 * Is the base interface of text based components which text is rendered/edited
 * as a single line and following a format.
 *
 * <p>This component type is inspired in <code>javax.swing.JFormattedTextField</code>.
 * Some documentation is borrowed from Swing.</p>
 *
 * <p>This component maintains two values, the value returned by {@link #getText()}
 * is the value in the visual control rendering the component, and the value returned by {@link #getValue()}.
 * The last one is restricted to the scope defined by {@link #getItsNatFormatter()}.</p>
 *
 * <p>When a new value on the visual control is set as the component value calling {@link #commitEdit()} first
 * is validated/converted calling {@link ItsNatFormatter#stringToValue(String,ItsNatFormattedTextField)}.</p>
 *
 * <p>Any change to the internal value is notified with a "value" property change event
 * and may be vetoed (see {@link #setValue(Object)}).</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatFormattedTextField extends ItsNatTextField
{
    /**
     * Constant identifying that when focus is lost,
     * <code>commitEdit</code> should be invoked. If in committing the
     * new value a <code>ParseException</code> or
     * <code>PropertyVetoException</code> is thrown, the invalid
     * value will remain.
     *
     * @see #setFocusLostBehavior(int)
     */
    public static final int COMMIT = 0;

    /**
     * Constant identifying that when focus is lost,
     * <code>commitEdit</code> should be invoked. If in commiting the new
     * value a <code>ParseException</code> or
     * <code>PropertyVetoException</code> is thrown, the value will be
     * reverted.
     *
     * @see #setFocusLostBehavior(int)
     */
    public static final int COMMIT_OR_REVERT = 1;

    /**
     * Constant identifying that when focus is lost, editing value should
     * be reverted to current value set on the component.
     *
     * @see #setFocusLostBehavior(int)
     */
    public static final int REVERT = 2;

    /**
     * Constant identifying that when focus is lost, the edited value
     * should be left.
     *
     * @see #setFocusLostBehavior(int)
     */
    public static final int PERSIST = 3;

    /**
     * Returns the behavior when focus is lost. This will be one of
     * {@link #COMMIT_OR_REVERT},
     * {@link #COMMIT},
     * {@link #REVERT} or
     * {@link #PERSIST}
     *
     * Note that some {@link ItsNatFormatter}s may push changes to the
     * control value.
     *
     * @return returns behavior when focus is lost. {@link #COMMIT_OR_REVERT} by default
     * @see #setFocusLostBehavior(int)
     */
    public int getFocusLostBehavior();

    /**
     * Sets the behavior when focus is lost. This will be one of
     * {@link #COMMIT_OR_REVERT},
     * {@link #COMMIT},
     * {@link #REVERT} or
     * {@link #PERSIST}
     *
     * @param behavior identifies behavior when focus is lost
     * @see #getFocusLostBehavior()
     */
    public void setFocusLostBehavior(int behavior);

    /**
     * Returns the last valid value. Based on the editing policy of
     * the {@link ItsNatFormatter} this may not return the current
     * edited value. The currently edited value can be obtained by invoking
     * {@link #commitEdit()} followed by {@link #getValue()}.
     *
     * @return last valid value. The default value is null.
     */
    public Object getValue();

    /**
     * Sets the value that will be formatted to the control by an
     * {@link ItsNatFormatter} obtained from the current
     * {@link ItsNatFormatterFactory}. If no
     * {@link ItsNatFormatterFactory} has been specified, this will
     * attempt to create one based on the type of <code>value</code>.
     *
     * <p>This new value is "voted" before is set firing a <code>java.beans.PropertyChangeEvent</code>
     *  event, with name "value", sent to the listeners registered with {@link org.itsnat.comp.ItsNatComponent#addVetoableChangeListener(VetoableChangeListener)}
     * if some listener does a "veto" (throws a <code>java.beans.PropertyVetoException</code>)
     * the new value is not set. If finally set the <code>PropertyChangeEvent</code> event
     * is sent to the listeners registered with
     * {@link org.itsnat.comp.ItsNatComponent#addPropertyChangeListener(java.beans.PropertyChangeListener)}
     * or {@link org.itsnat.comp.ItsNatComponent#addPropertyChangeListener(String,java.beans.PropertyChangeListener)}
     * with property name "value".
     * </p>
     *
     * @param value value to display (and may be edit).
     * @throws PropertyVetoException if the new value was vetoed.
     */
    public void setValue(Object value) throws PropertyVetoException;

    /**
     * Forces the current value to be taken from the
     * control and set as the current value converted first using the current
     * {@link ItsNatFormatter}.
     *
     * @throws ParseException if the {@link ItsNatFormatter} is not able
     *         to format the current control value
     * @throws PropertyVetoException if the new value was vetoed.
     */
    public void commitEdit() throws ParseException,PropertyVetoException;

    /**
     * Informs whether visual control content has changed and component value is
     * still not modified.
     *
     * @return if visual value has changed and component value not.
     */
    public boolean isEdited();

    /**
     * Returns true if the current value being edited is valid.
     *
     * @return true if the current value being edited is valid.
     */
    public boolean isEditValid();

    /**
     * Informs whether this component has the focus.
     *
     * <p>Returned value is only valid if this component processes "focus" and "blur" events.
     *
     * @return true if this component has the focus.
     */
    public boolean hasFocus();

    /**
     * Creates a new default ItsNat formatter factory.
     *
     * <p>Current implementation is an {@link ItsNatFormatterFactoryDefault}.</p>
     *
     * @return a default formatter factory instance.
     */
    public ItsNatFormatterFactory createDefaultItsNatFormatterFactory();

    /**
     * Returns the current ItsNat formatter factory.
     *
     * <p>By default ItsNat provides a default factory created with
     * {@link #createDefaultItsNatFormatterFactory()}.</p>
     *
     * @return the current factory. May be null.
     * @see #setItsNatFormatterFactory(ItsNatFormatterFactory)
     */
    public ItsNatFormatterFactory getItsNatFormatterFactory();

    /**
     * Sets the current ItsNat formatter factory.
     *
     * @param tf the new formatter factory. May be null.
     */
    public void setItsNatFormatterFactory(ItsNatFormatterFactory tf);

    /**
     * This method is called when the component needs to convert an Object value
     * from/to String.
     *
     * <p>If a formatter was defined explicitly with a call to {@link #setItsNatFormatter(ItsNatFormatter)}
     * this method returns this formatter, else returns the formatter of
     * the default formatter factory ({@link #getItsNatFormatterFactory()} calling
     * {@link ItsNatFormatterFactory#getItsNatFormatter(ItsNatFormattedTextField)},
     * if no factory is defined or the factory has not an appropriated formatter,
     * then the component tries to build an appropriated formatter to the current
     * value ({@link #getValue()}).</p>
     *
     * @return the selected formatter.
     */
    public ItsNatFormatter getItsNatFormatter();

    /**
     * Sets the default ItsNat formatter.
     *
     * @param formatter the default formatter. May be null (the factory will be used).
     */
    public void setItsNatFormatter(ItsNatFormatter formatter);

    /**
     * Defines a default ItsNat formatter based on the specified <code>java.text.Format</code>.
     *
     * <p>The method {@link #createItsNatFormatter(java.text.Format)} is called to create
     * an ItsNat formatter wrapping this format. The new ItsNat formatter is
     * set calling {@link #setItsNatFormatter(ItsNatFormatter)}.</p>
     *
     * @param format the <code>java.text.Format</code> object to set as the default formatter.
     */
    public void setFormat(Format format);

    /**
     * Creates an ItsNat formatter wrapping the specified <code>java.text.Format</code>.
     *
     * <p>Current implementation uses <code>Format.parseObject(String)</code> and
     * <code>Format.format(Object)</code> to convert string and values.</p>
     *
     * @param format the <code>java.text.Format</code> to wrap.
     * @return a new ItsNat formatter wrapping the specified format.
     */
    public ItsNatFormatter createItsNatFormatter(Format format);

    /**
     * Defines an ItsNat formatter. A formatter is used to convert a String from/to
     * an Object.
     */
    public interface ItsNatFormatter
    {
        /**
         * Converts the specified <code>String</code> to an <code>Object</code> using some kind of conversion rules.
         *
         * @param text the string to convert.
         * @param comp the component using this formatter.
         * @return the converted Object value.
         * @throws java.text.ParseException the text can not be converted.
         */
        public Object stringToValue(String text,ItsNatFormattedTextField comp) throws ParseException;

        /**
         * Converts the specified <code>Object</code> to <code>String</code> using some kind of conversion rules.
         *
         * @param value the value to convert.
         * @param comp the component using this formatter.
         * @return the converted string.
         * @throws java.text.ParseException the object value can not be converted.
         */
        public String valueToString(Object value,ItsNatFormattedTextField comp) throws ParseException;
    }

    /**
     * Defines an ItsNat formatter factory. A formatter factory is used to create
     * ItsNat formatters.
     *
     */
    public interface ItsNatFormatterFactory
    {
        /**
         * Returns an appropiated formatter to the specified formatted text field.
         *
         * <p>The target formatted text field is used to build the appropriated formatter,
         * for instance using the current component value ({@link #getValue()}) to determine
         * what kind of conversion is needed.</p>
         *
         * @param comp the formatted text field used to build the formatter.
         * @return the formatter.
         */
        public ItsNatFormatter getItsNatFormatter(ItsNatFormattedTextField comp);
    }

}

