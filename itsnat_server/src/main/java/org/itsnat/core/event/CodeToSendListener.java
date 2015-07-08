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
 * This listener is called when a new JavaScript code is being added to an
 * {@link org.itsnat.core.ItsNatDocument} or {@link org.itsnat.core.ClientDocument}.
 *
 * @see org.itsnat.core.ItsNatDocument#addCodeToSend(Object)
 * @see org.itsnat.core.ClientDocument#addCodeToSend(Object)
 * @see org.itsnat.core.ItsNatDocument#addCodeToSendListener(CodeToSendListener)
 * @see org.itsnat.core.ClientDocument#addCodeToSendListener(CodeToSendListener)
 * @author Jose Maria Arranz Santamaria
 */
public interface CodeToSendListener
{
    /**
     * Is called <i>before</i> the new code is definitely added to the document
     * or client, the event object contains the new code.
     *
     * <p>If null is returned the new code is rejected, otherwise the returned value is used as the new code.
     * In this scenario the listener may be used as a filter.</p>
     *
     * @param event the event containing the new code to send.
     * @return the new code or null if code must be rejected.
     */
    public Object preSendCode(CodeToSendEvent event);

    /**
     * Is called <i>after</i> the new code is definitely added to the document
     * or client, the event object contains the new code.
     *
     * @param event the event containing the new code added to send.
     */
    public void postSendCode(CodeToSendEvent event);
}
