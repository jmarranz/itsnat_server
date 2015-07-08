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
 * This event object is sent to registered {@link ItsNatAttachedClientEventListener} objects
 * to notify the several phases of a remote control process using a timer or comet
 * to notify the client any document change.
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatAttachedClientEvent extends ItsNatEvent
{
    /**
     * Constant used to inform that an event is a new request to
     * remote control a document.
     *
     * @see #getPhase()
     */
    public static final int REQUEST = 1;

    /**
     * Constant used to inform that a remote control client document is being loaded.
     *
     * @see #getPhase()
     */
    public static final int LOAD = 2;

    /**
     * Constant used to inform that an event has been received to refresh (update)
     * the remote control client with any document change.
     *
     * @see #getPhase()
     */
    public static final int REFRESH = 3;

    /**
     * Constant used to inform that a remote control client document is being unloaded.
     *
     * @see #getPhase()
     */
    public static final int UNLOAD = 4;

    /**
     * Returns the life cycle phase/notification type of this event.
     *
     * @return the phase or notification type.
     * @see #REQUEST
     * @see #LOAD
     * @see #REFRESH
     * @see #UNLOAD
     */
    public int getPhase();

    /**
     * Returns true if this event request is accepted.
     *
     * <p>If this event is a new remote control
     * request ({@link #REQUEST} phase) by default returns false
     * otherwise returns true.</p>
     *
     * @return true if this request is accepted.
     * @see #setAccepted(boolean)
     */
    public boolean isAccepted();

    /**
     * Used to accept or deny a remote control request.
     *
     * <p>This method must be called with a true value to accept a new remote control request
     * ({@link #REQUEST} phase), because by default ItsNat does not accept new remote control requests.
     * </p>
     *
     * <p>If called with false informs ItsNat to deny a new remote control request or to end
     * an already running remote control process.</p>
     *
     * @param accepted if set to false rejects or ends a new or existing remote control process.
     */
    public void setAccepted(boolean accepted);

    /**
     * Returns the timeout in client of asynchronous AJAX/SCRIPT events used for remote control. If this event is a {@link ItsNatAttachedClientEvent#REQUEST}
     * the returned value may be used to validate the remote control request.
     *
     * <p>Returned value is proposed initially by the client but can not change
     * during the remote control life cycle.</p>
     *
     * @return the timeout of asynchronous events. If negative no timeout is defined.
     * @see #setAccepted(boolean)
     */
    public long getEventTimeout();

    /**
     * Returns the maximum time in milliseconds the remote control request
     * will wait to the document to be bound if this document is not
     * in server yet.
     *
     * <p>This timeout only applies to &lt;iframe&gt;, &lt;object&gt; and &lt;embed&gt;
     * elements with associated documents automatically bound to the parent.
     * </p>
     *
     * @return the timeout waiting for the document to remote control. If 0 there is no wait.
     */
    public long getWaitDocTimeout();

    /**
     * Returns true if this attached client cannot send events to the server (read only).
     *
     * @return true if this client is read only.
     */
    public boolean isReadOnly();
}
