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

package org.itsnat.core.domutil;

import org.w3c.dom.DocumentFragment;

/**
 * This utility interface is used to render a value inside the associated
 * DOM element using a pattern. The content of the DOM element is used as pattern
 * to render the first value and consecutive values if {@link #isUsePatternMarkupToRender()} returns true.
 *
 * <p>Objects implementing this interface are attached to the specified DOM element
 * this element usually is not empty.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementLabel extends ElementGroup
{
    /**
     * Returns the current renderer used by this label.
     *
     * @return the current renderer.
     * @see #setElementLabelRenderer(ElementLabelRenderer)
     */
    public ElementLabelRenderer getElementLabelRenderer();

    /**
     * Sets the renderer used by this label.
     *
     * @param renderer the new renderer.
     * @see #getElementLabelRenderer()
     */
    public void setElementLabelRenderer(ElementLabelRenderer renderer);

    /**
     * Informs whether the original (saved as pattern) markup is used to render.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatDocument#isUsePatternMarkupToRender()}</p>
     *
     * @return true if the original markup is used.
     * @see #setUsePatternMarkupToRender(boolean)
     */
    public boolean isUsePatternMarkupToRender();

    /**
     * Sets whether the original (saved as pattern) markup is used to render.
     *
     * @param value true to enable the use of original markup to render.
     * @see #isUsePatternMarkupToRender()
     */
    public void setUsePatternMarkupToRender(boolean value);

    /**
     * Returns the pattern used to render values if {@link #isUsePatternMarkupToRender()}
     * is true.
     *
     * @return the pattern used to render values.
     */
    public DocumentFragment getContentPatternFragment();

    /**
     * Renders the specified value into the label
     * using the current renderer. If the label does not contains markup is added
     * using the pattern.
     *
     * @param value the value to render.
     * @see #getElementLabelRenderer()
     * @see #addLabelMarkup(Object)
     * @see ElementRenderer#render(Object,Object,Element,boolean)
     */
    public void setLabelValue(Object value);

    /**
     * Informs whether this label contains markup.
     *
     * @return true if this label contains markup.
     */
    public boolean hasLabelMarkup();

    /**
     * Adds the label pattern markup inside the label element.
     *
     * @see #addLabelMarkup(Object)
     */
    public void addLabelMarkup();

    /**
     * Adds the label pattern markup inside the label element and renders the specified value.
     *
     *
     * @param value the value to render.
     * @see #setLabelValue(Object)
     */
    public void addLabelMarkup(Object value);

    /**
     * Removes the label markup inside the label element.
     *
     * @see #addLabelMarkup()
     */
    public void removeLabelMarkup();
}
