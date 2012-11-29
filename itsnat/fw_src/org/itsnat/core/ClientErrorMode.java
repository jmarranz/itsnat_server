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
 * Contains the constants used to declare the whether the browser catches JavaScript errors
 * and if they are shown to the user using a JavaScript <code>alert</code> call.</p>
 *
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
