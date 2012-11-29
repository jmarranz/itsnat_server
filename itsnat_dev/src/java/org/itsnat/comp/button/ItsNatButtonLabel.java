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

package org.itsnat.comp.button;

import org.itsnat.core.domutil.ElementRenderer;

/**
 * Is the base interface of button components with a label.
 *
 * <p>The label is set calling {@link #setLabelValue(Object)}. If never called
 * this component does not change the original markup. A renderer is used to
 * render the label as markup.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatButtonLabel extends ItsNatButton
{
    /**
     * Returns the current label.
     *
     * <p>Returned value is the value set by the last call to {@link #setLabelValue(Object)}.</p>
     *
     * @return the current label value. Null by default.
     * @see #setLabelValue(Object)
     */
    public Object getLabelValue();

    /**
     * Sets the current label value, this value is saved as is and rendered
     * as markup using the current renderer returned by {@link #getElementRenderer()}.
     *
     * @param value the new label value.
     * @see #getLabelValue()
     */
    public void setLabelValue(Object value);

    /**
     * Returns the current renderer used to render the label as markup.
     *
     * <p>By default uses the default renderer returned by
     * {@link org.itsnat.core.domutil.ElementGroupManager#createDefaultElementRenderer()}. Only
     * {@link org.itsnat.comp.button.normal.ItsNatHTMLInputReset},
     * {@link org.itsnat.comp.button.normal.ItsNatHTMLInputSubmit},
     * and {@link org.itsnat.comp.button.normal.ItsNatHTMLInputButton}
     * components use by default an internal
     * renderer to render the label as the value of the "value" attribute
     * of the &lt;input&gt; element.</p>
     *
     * @return the current renderer.
     * @see #setLabelValue(Object)
     * @see #setElementRenderer(ElementRenderer)
     */
    public ElementRenderer getElementRenderer();

    /**
     * Sets the current renderer.
     *
     * @param renderer the new renderer.
     * @see #getElementRenderer()
     */
    public void setElementRenderer(ElementRenderer renderer);
}
