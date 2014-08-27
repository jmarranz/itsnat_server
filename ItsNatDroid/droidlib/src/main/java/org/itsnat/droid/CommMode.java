package org.itsnat.droid;

/**
 * Contains the constants used to declare the communication modes for events.
 *
 * <p>Names are the same as web for API compatibility</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public class CommMode
{
    /**
     * Conceptually similar to AJAX synchronous to transport events and sent synchronously, the client is locked
     * until the current event sent to server returns.
     */
    public static final int XHR_SYNC  = 1;

    /**
     * Conceptually similar to AJAX asynchronous to transport events and sent asynchronously.
     */
    public static final int XHR_ASYNC = 2;

    /**
     * Conceptually similar to AJAX asynchronous hold mode (XMLHttpRequest) to transport events and sent asynchronously
     * but new events are queued (a FIFO list) until the current event sent to server returns.
     */
    public static final int XHR_ASYNC_HOLD = 3;
}
