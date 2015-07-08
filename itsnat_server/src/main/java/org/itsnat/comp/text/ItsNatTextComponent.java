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

import javax.swing.text.Document;
import org.itsnat.comp.*;

/**
 * Is the base interface of text based components.
 *
 * <p>A text based component manages a single piece of text.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatTextComponent extends ItsNatElementComponent
{
    /**
     * Returns the current data model of this component.
     *
     * @return the current data model
     * @see #setDocument(javax.swing.text.Document)
     */
    public Document getDocument();

    /**
     * Changes the data model of this component.
     *
     * <p>Current data model is disconnected from this component, and the new
     * data model is bound to this component, every change is tracked and
     * updates the user interfaces accordingly.</p>
     *
     * <p>If the specified data model is the same instance as the current data model,
     * then is reset, component listener is removed and added again. Use this technique if
     * you want to add a data model listener to be executed <i>before</i> the default component listener.
     *
     * @param dataModel the new data model.
     * @see #getDocument()
     */
    public void setDocument(Document dataModel);

    /**
     * Returns the text contained in this component.
     *
     * <p>This method is a quick access to the data model text
     * (see <code>javax.swing.text.Document.getText(int,int)</code>)</p>
     *
     * @return the component text.
     * @see #setText(String)
     * @see #getDocument()
     */
    public String getText();

    /**
     * Updates the text contained in this component.
     *
     * <p>This method is a quick access to modify the data model text
     * (see <code>javax.swing.text.Document.remove(int,int)</code>
     * and <code>javax.swing.text.Document.insertString(int,int,AttributeSet)</code>).</p>
     *
     * <p>The component keeps track of data model changes the user interface is updated
     * accordingly.</p>
     *
     * @param t the text.
     * @see #getText()
     * @see #getDocument()
     */
    public void setText(String t);

    /**
     * Fetches the text contained within the given portion
     * of the text of this component.
     *
     * <p>This method is a direct wrapper of <code>javax.swing.text.Document.getText(int,int)</code>.</p>
     *
     * @param offset  the offset into the document representing the desired start of the text >= 0
     * @param length  the length of the desired string >= 0
     * @see #getText()
     * @see #getDocument()
     */
    public String getText(int offset,int length);

    /**
     * Appends a new string at the end.
     *
     * <p>This method is a quick access to modify the data model text
     * (see <code>javax.swing.text.Document.insertString(int,int,AttributeSet)</code>).</p>
     *
     * <p>The component keeps track of data model changes the user interface is updated
     * accordingly.</p>
     *
     * @param str the string to add.
     * @see #getText()
     * @see #getDocument()
     * @see #insertString(String,int)
     */
    public void appendString(String str);

    /**
     * Inserts a new string at the specified position.
     *
     * <p>This method is a quick access to modify the data model text
     * (see <code>javax.swing.text.Document.insertString(int,int,AttributeSet)</code>).</p>
     *
     * <p>The component keeps track of data model changes the user interface is updated
     * accordingly.</p>
     *
     * @param str the text.
     * @see #getText()
     * @see #getDocument()
     * @see #appendString(String)
     */
    public void	insertString(String str, int pos);

    /**
     * Replaces a text part with a new string at the specified position.
     *
     * <p>This method is a quick access to modify the data model text
     * (see <code>javax.swing.text.Document.insertString(int,int,AttributeSet)</code>).</p>
     *
     * <p>This method is a quick access to modify the data model text
     * (see <code>javax.swing.text.Document.remove(int,int)</code>
     * and <code>javax.swing.text.Document.insertString(int,int,AttributeSet)</code>).</p>
     *
     * @param str the text.
     * @see #getText()
     * @see #getDocument()
     * @see #appendString(String)
     */
    public void replaceString(String str, int start, int end);

    /**
     * Create a new instance of the default data model.
     *
     * <p>Returned data model is not bound to this component.</p>
     *
     * @return a new default data model instance.
     * @see #setDocument(javax.swing.text.Document)
     */
    public Document createDefaultDocument();

    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatTextComponentUI getItsNatTextComponentUI();

}
