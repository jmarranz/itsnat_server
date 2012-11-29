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
package org.itsnat.comp.label;

import org.itsnat.comp.*;
import java.beans.PropertyVetoException;
import org.itsnat.comp.label.ItsNatLabelUI;

/**
 * Is the base interface of label components.
 *
 * <p>A label component contains a single object value, this value is rendered
 * as markup using a special object, the renderer, and may be optionally edited "in place"
 * using a user defined editor.</p>
 *
 * <p>Any change to the internal value is notified with a "value" property change event
 * and may be vetoed (see {@link #setValue(Object)}. The new value, if accepted, is rendered
 * as markup.</p>
 *
 * <p>This component family is the "componentized" version of {@link org.itsnat.core.domutil.ElementLabel} and
 * follows a similar philosophy.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatLabel extends ItsNatElementComponent
{
    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatLabelUI getItsNatLabelUI();

    /**
     * Returns the current value.
     *
     * @return the current value. The default value is null.
     */
    public Object getValue();

    /**
     * Sets the value. The new value will be rendered automatically to markup.
     *
     * <p>This new value is "voted" before is set firing a <code>java.beans.PropertyChangeEvent</code>
     *  event, with name "value", sent to the listeners registered with {@link ItsNatComponent#addVetoableChangeListener(VetoableChangeListener)}
     * if some listener does a "veto" (throws a <code>java.beans.PropertyVetoException</code>)
     * the new value is not set. If finally set the <code>PropertyChangeEvent</code> event
     * is sent to the listeners registered with
     * {@link ItsNatComponent#addPropertyChangeListener(java.beans.PropertyChangeListener)}
     * or {@link ItsNatComponent#addPropertyChangeListener(String,java.beans.PropertyChangeListener)}
     * with property name "value".
     * </p>
     *
     * @param value value to display (and may be edit).
     * @throws PropertyVetoException if the new value was vetoed.
     */
    public void setValue(Object value) throws PropertyVetoException;

    /**
     * Returns the current component renderer. This renderer converts the label value to markup.
     *
     * @return the current renderer. By default uses the default renderer ({@link ItsNatComponentManager#createDefaultItsNatLabelRenderer()})
     * @see #setItsNatLabelRenderer(ItsNatLabelRenderer)
     */
    public ItsNatLabelRenderer getItsNatLabelRenderer();

    /**
     * Sets the component renderer.
     *
     * @param renderer the new renderer.
     * @see #getItsNatLabelRenderer()
     */
    public void setItsNatLabelRenderer(ItsNatLabelRenderer renderer);

    /**
     * Returns the current label editor. This object is used to edit in place
     * the current label value.
     *
     * @return the current editor. By default uses the default editor
     *      calling ({@link ItsNatComponentManager#createDefaultItsNatLabelEditor(ItsNatComponent)}) with a null parameter.
     * @see #setItsNatLabelEditor(ItsNatLabelEditor)
     */
    public ItsNatLabelEditor getItsNatLabelEditor();

    /**
     * Sets the label editor.
     *
     * <p>This component automatically adds a listener calling
     * <code>javax.swing.CellEditor.addCellEditorListener(javax.swing.event.CellEditorListener)</code>
     * this way the component is informed when the editor stops or cancels editing.</p>
     *
     * <p>The edition process starts programmatically by calling {@link #startEditing()}
     * or by user action (usually double clicking the label or the action/event type
     * specified by {@link #getEditorActivatorEvent()}). Then the label markup
     * (below the parent element) is removed and the method
     * {@link ItsNatLabelEditor#getLabelEditorComponent(ItsNatLabel,Object,Element)}
     * is called, this method usually places the editor component inside the label.
     * Current label implementations do nothing with the editor component returned and may be null.</p>
     *
     * <p>When the editor stops the component calls <code>CellEditor.getCellEditorValue()</code>
     * to obtain the new value and sets this value to the label (calling {@link #setValue(Object)})
     * and the editor is removed and the original label markup is restored modified by the renderer
     * with the new value. If the editor is cancelled all is the same but no markup change is made.</p>
     *
     * @param editor the new editor. May be null (edition disabled).
     * @see #getItsNatLabelEditor()
     */
    public void setItsNatLabelEditor(ItsNatLabelEditor editor);

    /**
     * Used to start programmatically the label edition process "in place".
     *
     * @see #isEditing()
     */
    public void startEditing();

    /**
     * Informs whether the label value is being edited.
     *
     * @return true if the label is being edited.
     * @see #startEditing()
     */
    public boolean isEditing();

    /**
     * Returns the event type used to activate the label edition process by the user.
     *
     * <p>If returns null edition activated by events is disabled .</p>
     *
     * @return the event type used to activate the edition. By default is "dblclick".
     * @see #setEditorActivatorEvent(String)
     */
    public String getEditorActivatorEvent();

    /**
     * Sets the event type used to activate the label edition process by the user.
     *
     * @param eventType the event type used to activate the edition.
     * @see #getEditorActivatorEvent()
     */
    public void setEditorActivatorEvent(String eventType);

    /**
     * Informs whether the in place edition is enabled.
     *
     * @return false if the editing in place is temporally disabled. True by default.
     * @see #setEditingEnabled(boolean)
     */
    public boolean isEditingEnabled();

    /**
     * Enables or disables temporally the in place edition.
     *
     * @param value true to enable in place edition.
     * @see #isEditingEnabled()
     */
    public void setEditingEnabled(boolean value);

}
