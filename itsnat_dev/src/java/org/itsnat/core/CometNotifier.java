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

import org.w3c.dom.events.EventListener;

/**
 * Is used to implement the <i>COMET</i> or <i>server push</i> technique to notify
 * the client to be updated usually when the document is changed.
 *
 * <p>The ItsNat COMET approach is based on AJAX or SCRIPT, the client is ever waiting for an
 * asynchronous event to return. When new code must be sent to the client
 * this event returns and updates the client and automatically a new asynchronous
 * request is sent to the server waiting to new asynchronous server changes
 * (technique sometimes called as
 * <a href="http://weblogs.java.net/blog/jfarcand/archive/2007/05/new_adventures.html">"long polling"</a>).
 * </p>
 *
 * <p>Use COMET if you have to monitor by web a never ending server process.</p>
 *
 * <p>Current implementation does not need a special server but it locks a thread per client
 * (and a HTTP connection per client). This thread is stalled most of the time,
 * the scalability issue is more related to the maximun number of threads
 * the system can manage.</p>
 *
 * @see ClientDocument#createCometNotifier()
 * @author Jose Maria Arranz Santamaria
 */
public interface CometNotifier extends ItsNatUserData
{
    /**
     * Returns the parent client document this object belongs to.
     *
     * @return the parent client document.
     */
    public ClientDocument getClientDocument();

    /**
     * Returns the asociated document.
     *
     * @return the document object this Comet notifier is bound to.
     */
    public ItsNatDocument getItsNatDocument();

    /**
     * Notifies the client thread to send pending document modifications.
     *
     * <p>This method may be called by a non-servlet based thread, and no
     * synchronization of the {@link ItsNatDocument} is necessary.</p>
     *
     * <p>The client thread is woke up to send document modifications to the client and
     * a new web request is sent to the document again to wait a new notification.</p>
     */
    public void notifyClient();

    /**
     * Stops and disposes this notifier. The stalled client thread is woke up
     * to send any pending modification and no new request is sent.
     *
     * <p>A stopped Comet notifier is invalid and cannot be reused.</p>
     */
    public void stop();

    /**
     * Informs whether this notifier is stopped. A Comet notifier is stopped if it was
     * explicitly stopped calling {@link #stop()} or the associated document was destroyed.
     *
     * @return true if this notifier is stopped.
     */
    public boolean isStopped();

    /**
     * Returns the maximum expiration delay. This is the maximum time a stalled client thread
     * will wait to receive a notification, if this limit is reached this notifier is automatically
     * stopped.
     *
     * <p>This limit is defined to avoid an unlimited wait because the notifier process has ended and the notifier
     * was not explicitly stopped.</p>
     *
     * @return the maximum expiration delay in milliseconds. By default is 1 hour.
     * @see #setExpirationDelay(long)
     */
    public long getExpirationDelay();

    /**
     * Sets the maximum expiration delay.
     *
     * @param expirationDelay the maximum expiration delay in milliseconds.
     * @see #getExpirationDelay()
     */
    public void setExpirationDelay(long expirationDelay);

    /**
     * Returns the default timeout in client of COMET AJAX/SCRIPT events.
     *
     * <p>This is the maximum time a stalled COMET request will wait to receive
     * a notification, if this limit is reached the last request is aborted
     * stopping the COMET process. If a timeout is defined this value should be greater than {@link #getExpirationDelay()}.</p>
     *
     * <p>COMET events are ever asynchronous.</p>
     *
     * @return the timeout of COMET events in milliseconds.
     */
    public long getEventTimeout();

    /**
     * Adds an event listener to this Comet notifier. This listener is called when the client
     * is going to be notified.
     *
     * <p>This method is not synchronized and must be called from a web request thread
     * (in this context this method is thread safe because the ItsNat document is automatically synchronized).
     * </p>
     *
     * <p>Event listeners are dispatched using a web request thread, therefore the ItsNat document
     * object is already synchronized and Java code can be the same as a normal event processing.
     * </p>
     *
     * <p>The event object used when dispatching the listener is a not a "real" DOM client event and most
     * of methods have no meaning.
     * </p>
     *
     * @param listener the listener to add.
     * @see #removeEventListener(EventListener)
     */
    public void addEventListener(EventListener listener);

    /**
     * Removes the specified event listener from this Comet notifier.
     *
     * <p>This method is not synchronized and must be called from a web request thread
     * (in this context this method is thread safe because the ItsNat document is automatically synchronized).
     * </p>
     *
     * @param listener the listener to remove.
     * @see #addEventListener(EventListener)
     */
    public void removeEventListener(EventListener listener);
}
