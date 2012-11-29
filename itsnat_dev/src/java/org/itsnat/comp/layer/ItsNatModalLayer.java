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
package org.itsnat.comp.layer;

import org.itsnat.comp.*;
import org.w3c.dom.events.EventListener;

/**
 * Is the interface of Modal Layer components.
 *
 * <p>A modal layer is an absolute positioned element usually opaque or translucent
 * with a z-index greater than the elements being covered and covering the
 * complete area of the web page.
 * </p>
 * <p>The modal layer is added as a direct child of the root element (&lt;body&gt; in HTML
 * documents) added in the end. This way any mouse click is not dispatched to
 * any covered element in the document.
 * </p>
 * <p>By default there is no markup content contained by the modal layer, new markup
 * can be added "on top" of the modal layer specifing a greater z-index.
 * This new markup should be added in the end of the root element (&lt;body&gt; in HTML).
 * </p>
 * <p>The component automatically removes the modal layer element when the component
 * is disposed, markup with greater z-index should be removed before.
 * </p>
 * <p>ItsNat default implementation supports several stacked modal layers
 * and automatically fixes the problem of HTML SELECT elements of MSIE v6,
 * in this case HTML SELECT elements are hidden when covered by a modal layer
 * (stacked layers are supported too).
 * </p>
 * <p>In Pocket IE 6 (WM 6 &amp; 6.1) there is no support of absolute positioning and z-index,
 * in this case covered elements are visually removed using the CSS property dispose
 * with value "none".
 * </p>
 * <p>If no element is provided when the component is created, the default implementation
 * creates one used internally.
 * </p>
 *
 * <p>In pure SVG documents behavior is very similar to X/HTML, the main difference is
 * z-index is ignored in SVG because z-order in SVG follows the rendering order,
 * this order coincides with document order. <a href="http://wiki.svg.org/Rendering_Order">More info</a>.
 * </p>
 * <p>In XUL documents z-index is ignored because XUL ignores the z-index CSS property.
 * By default uses a &lt;panel&gt; element as layer, this element must be used as the container
 * of any user markup shown "on top" of the layer. If <code>popuphidden</code> event is enabled
 * calling {@link ItsNatComponent#enableEventListener(String)} or adding an
 * event listener listening to the component for this event (calling {@link ItsNatComponent#addEventListener(String,EventListener,boolean)}),
 * the component will be automatically disposed when an event is received
 * (in XUL the popuphidden event closes the panel, by this way the component
 * is "closed" in server too).
 * </p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createItsNatModalLayer(Element,boolean,int,float,String,NameValue[])
 * @see org.itsnat.comp.ItsNatComponentManager#createItsNatModalLayer(Element,boolean,float,String,NameValue[])
 * @see org.itsnat.comp.ItsNatComponentManager#createItsNatModalLayer(Element,NameValue[])
 */
public interface ItsNatModalLayer extends ItsNatElementComponent,ItsNatFreeComponent
{
    /**
     * Returns the z-index associated to the modal layer.
     *
     * @return the z-index value.
     */
    public int getZIndex();

    /**
     * Registers an event listener to catch events received by
     * elements "hidden" by this modal layer.
     *
     * <p>Modal layers try to be <i>modal</i>, in a web environment
     * this is not fully accomplished, for instance, in desktop
     * browsers the end user can navigate through hidden form elements
     * and links using the TAB key, and "click" them by pressing ENTER.
     * </p>
     *
     * <p>This event listener may be used to alert the end user
     * about this unwanted circumstance.
     * </p>
     *
     * @param listener the listener
     * @see #removeUnexpectedEventListener(EventListener)
     */
    public void addUnexpectedEventListener(EventListener listener);

    /**
     * Unregisters the specified event listener used for catching events received by
     * elements "hidden" by this modal layer.
     *
     * @param listener
     * @see #addUnexpectedEventListener(EventListener)
     */
    public void removeUnexpectedEventListener(EventListener listener);
}
