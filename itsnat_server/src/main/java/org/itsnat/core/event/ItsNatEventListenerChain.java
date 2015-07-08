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

package org.itsnat.core.event;

/**
 * This interface is used to control the flow of event listeners processing.
 *
 * <p>ItsNat provides a default implementation returned by {@link ItsNatEvent#getItsNatEventListenerChain()}.
 * </p>
 *
 * <p>Methods of this object must be called only inside the processing of an event listener
 * and must not be saved beyond the lifetime of the related event.
 * </p>
 *
 * <p>Usually global event listeners and event listeners registered with
 * {@link org.itsnat.comp.ItsNatComponent#addEventListener(String,org.w3c.dom.events.EventListener)}
 * can benefit of this feature.
 * </p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatEventListenerChain
{
    /**
     * If called, the listener dispatching flow will follow this path,
     * that is to say, next listeners will be executed before returning this method.
     *
     * <p>This method can be used to catch exceptions thrown by followin listeners, to open/commit transactions before/after
     * calling this method etc.</p>
     */
    public void continueChain();

    /**
     * Instructs ItsNat to stop the normal event listener flow, next listeners are not dispatched.
     */
    public void stop();

    /**
     * Informs whether the processing of this event is stopped.
     *
     * @return true whether event processing is stopped.
     */
    public boolean isStopped();
}
