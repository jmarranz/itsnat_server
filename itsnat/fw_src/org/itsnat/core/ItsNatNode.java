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

package org.itsnat.core;

/**
 * Is an interface extending the standard <code>org.w3c.dom.Node</code> objects, for
 * instance to introduce the concept of "internal/remote event mode".
 *
 */
public interface ItsNatNode extends ItsNatUserData
{
    /**
     * Returns the current mode of this node, internal or remote.
     *
     * <p>If the internal mode is set to false:
     *    <ul>
     *      <li>Any event listener registered/unregistered calling
     *          <code>EventTarget.addEventListener(String,EventListener,boolean)</code>
     *          or <code>EventTarget.addEventListener(String,EventListener,boolean)</code>
     *          is registered as a <i>remote</i> event listener forwarding this call
     *          to {@link ItsNatDocument#addEventListener(EventTarget,String,EventListener,boolean)},
     *          otherwise the event listener is registered internally.
     *      </li>
     *      <li>Events created with <code>DocumentEvent.createEvent(String)</code>
     *          are <i>remote</i> events otherwise the event is an internal event of Batik.
     *          When a remote event is submitted as parameter to <code>EventTarget.dispatchEvent(Event)</code>
     *          this call is redirected to {@link org.itsnat.core.ItsNatDocument#dispatchEvent(EventTarget,Event)}, otherwise
     *          the event is dispatched by the internal event listeners registered.
     *          The method <code>EventTarget.dispatchEvent(Event)</code> does not depend on
     *          <code>isInternalMode()</code>, it only depends on the event type (remote or internal).
     *      </li>
     *      <li>Methods like <code>HTMLInputElement.focus()</code> generates the appropriated
     *          JavaScript to execute this action in the client. Otherwise do nothing.
     *      </li>
     *    </ul>
     * </p>
     *
     * <p>Unless explicitly defined, the default value is the event mode defined
     * in the <code>Document</code> (this object implements {@link ItsNatNode} too),
     * false by default.
     * </p>
     *
     * <p>In ItsNat the internal event listener registry is almost only useful to
     * monitor DOM changes with mutation event listeners. In this case change
     * to true calling {@link #setInternalMode(boolean)} before registering
     * an event listener for mutation events, then restore the original mode.
     * </p>
     *
     * @return true if the current event mode is internal otherwise is remote. False by default.
     * @see #setInternalMode(boolean)
     */
    public boolean isInternalMode();

    /**
     * Sets the current mode of this node, internal or remote.
     *
     * @param mode if true the mode of this node is internal otherwise is remote.
     * @see #isInternalMode()
     */
    public void setInternalMode(boolean mode);

    /**
     * Returns the ItsNat document object containing this DOM node.
     * 
     * @return the ItsNat document associated.
     */
    public ItsNatDocument getItsNatDocument();
}
