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
 * Contains the constants used to declare the communication modes (AJAX and SCRIPT) for events.
 *
 * <p>Communication by SCRIPT elements is essentially asynchronous.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatDocument#addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int)
 */
public class CommMode
{
    /**
     * Indicates that AJAX is used (XMLHttpRequest) to transport events and sent synchronously, the client is locked
     * until the current event sent to server returns.
     */
    public static final int XHR_SYNC  = 1;

    /**
     * Indicates that AJAX is used (XMLHttpRequest) to transport events and sent asynchronously.
     */
    public static final int XHR_ASYNC = 2;

    /**
     * Indicates that AJAX is used (XMLHttpRequest) to transport events and sent asynchronously but new events are queued
     * (a FIFO list) until the current event sent to server returns.
     */
    public static final int XHR_ASYNC_HOLD = 3;

    /**
     * Indicates that SCRIPT elements are used to transport events.
     */
    public static final int SCRIPT = 4;

    /**
     * Indicates that SCRIPT elements are used to transport events but new events are queued
     * (a FIFO list) until the current event sent to server returns.
     */
    public static final int SCRIPT_HOLD = 5;
}
