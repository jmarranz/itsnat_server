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

import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.ItsNatUserData;

/**
 * This interface is the parent of any ItsNat event interface: normal
 * and remote view/control events.
 *
 * <p>Any <code>org.w3c.dom.events.Event</code> object managed by ItsNat
 * implements this interface.</p>
 *
 * <p>Current implementation inherits from <code>java.util.EventObject</code>.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatEvent extends ItsNatUserData
{
    /**
     * Returns the associated ItsNat Servlet Request of this event.
     *
     * @return the current request.
     */
    public ItsNatServletRequest getItsNatServletRequest();

    /**
     * Returns the associated ItsNat Servlet Response of this event.
     *
     * @return the current response.
     */
    public ItsNatServletResponse getItsNatServletResponse();

    /**
     * Returns the associated ItsNat document target of this event.
     *
     * @return the ItsNat document. May be null.
     * @see ItsNatServletRequest#getItsNatDocument() 
     */
    public ItsNatDocument getItsNatDocument();

    /**
     * Returns the associated client document target of this event.
     *
     * @return the client document. Is null {@link #getItsNatDocument()} is null too.
     * @see ItsNatServletRequest#getClientDocument() 
     */
    public ClientDocument getClientDocument();

    /**
     * Returns the communication mode used to send this event.
     *
     * @return the communication mode.
     */
    public int getCommMode();

    /**
     * Returns the remote value carried alongside this event with the specified name.
     *
     * <p>Usually these name/value pairs are declared using {@link org.itsnat.core.event.ParamTransport} objects.
     *
     * <p>Current implementation calls: <code>ServletRequest.getParameter(String)</code> with
     * the specified name if the name/value pair was not set calling
     * {@link #setExtraParam(String,Object)}.</p>
     *
     *
     * @param name the name to search for.
     * @return the value carried with the specified name.
     */
    public Object getExtraParam(String name);

    /**
     * Sets the specified name and value as a remote browser data carried by this event.
     *
     * <p>This method may be useful to simulate data transportation from the browser
     * in an event directly (locally) dispatched to the server DOM calling
     * {@link org.itsnat.core.ItsNatDocument#dispatchEventLocally(EventTarget,Event)}.
     * </p>
     * @param name the name of the value.
     * @param value the value associated the specified name.
     */
    public void setExtraParam(String name,Object value);

    /**
     * Returns the event listener chain flow control object associated to this event.
     *
     * @return the listener flow control object of this event.
     *          May be null if this capability is not enabled for this type of event or this event is no longer valid.
     */
    public ItsNatEventListenerChain getItsNatEventListenerChain();
}
