package org.itsnat.droid.impl.browser.clientdoc;

/**
 * Created by jmarranz on 14/07/14.
 */
public class ClientErrorMode
{
    /**
     * The browser does not catch errors. Use this mode to break the script execution
     * useful when debugging because a JavaScript debugger can stop automatically.
     */
    public static final int NOT_CATCH_ERRORS = 0;

    /**
     * The browser catch errors but no info is shown to the user. Use this mode to
     * silently hide errors.
     */
    public static final int NOT_SHOW_ERRORS = 1;

    /**
     * The browser catch errors. Only server errors (server exceptions) are shown
     * to the user using an <code>alert</code>.
     */
    public static final int SHOW_SERVER_ERRORS = 2;

    /**
     * The browser catch errors. Only client (JavaScript) errors are shown
     * to the user using an <code>alert</code>.
     */
    public static final int SHOW_CLIENT_ERRORS = 3;

    /**
     * The browser catch errors. Client (JavaScript) and server (server exceptions) errors are shown
     * to the user using an <code>alert</code>.
     */
    public static final int SHOW_SERVER_AND_CLIENT_ERRORS = 4;
}
