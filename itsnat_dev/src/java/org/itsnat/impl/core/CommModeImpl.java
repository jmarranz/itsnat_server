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

package org.itsnat.impl.core;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.CommMode;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.webkit.BrowserWebKit;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;

/**
 *
 * @author jmarranz
 */
public class CommModeImpl
{
    public static void checkMode(int value)
    {
        // No consideramos el UNKNOWN pues este valor es interno, se trata
        // de evaluar si el modo del usuario es correcto.

        //int SYNC  = 1;
        //int ASYNC = 2;
        //int ASYNC_HOLD = 3;
        //int SCRIPT = 4;
        //int SCRIPT_HOLD = 5;
        switch(value)
        {
            case CommMode.XHR_SYNC:
            case CommMode.XHR_ASYNC:
            case CommMode.XHR_ASYNC_HOLD:
            case CommMode.SCRIPT:
            case CommMode.SCRIPT_HOLD: break;
            default: throw new ItsNatException("Synch mode is not valid: " + value);
        }
    }

    public static int getCommMode(int commModeDeclared,Browser browser)
    {
        // Aunque esté declarado como síncrono es posible que
        // no esté soportado, este método devuelve el modo efectivo usado
        // por el cliente que es ASYNC_HOLD (usando además una capa de bloqueo)

        if ((commModeDeclared == CommMode.XHR_SYNC) &&
            (browser instanceof BrowserWebKit) &&
            !((BrowserWebKit)browser).isXHRSyncSupported())
                return CommMode.XHR_ASYNC_HOLD;

        return commModeDeclared;
    }

    public static boolean isPureAsyncMode(int mode)
    {
        return (mode == CommMode.XHR_ASYNC)||(mode == CommMode.SCRIPT);
    }

    public static int getPreferredPureAsyncMode(ClientDocumentStfulImpl clientDoc)
    {
        int defaultMode = clientDoc.getCommMode();
        switch(defaultMode)
        {
            case CommMode.XHR_SYNC:
            case CommMode.XHR_ASYNC:
            case CommMode.XHR_ASYNC_HOLD:
                            return CommMode.XHR_ASYNC; // El modo asíncrono puro dentro del modo AJAX
            case CommMode.SCRIPT:
            case CommMode.SCRIPT_HOLD:
                            return CommMode.SCRIPT; // El modo asíncrono puro dentro del modo SCRIPT
        }

        throw new ItsNatException("INTERNAL ERROR");
    }

    public static boolean isXHRDefaultMode(ClientDocumentStfulImpl clientDoc)
    {
        int defaultMode = clientDoc.getCommMode();
        return isXHRMode(defaultMode);
    }

    public static boolean isXHRMode(int commMode)
    {
        switch(commMode)
        {
            case CommMode.XHR_SYNC:
            case CommMode.XHR_ASYNC:
            case CommMode.XHR_ASYNC_HOLD:
                            return true; // Modo AJAX
            case CommMode.SCRIPT:
            case CommMode.SCRIPT_HOLD:
                            return false; // Modo SCRIPT
        }
        throw new ItsNatException("INTERNAL ERROR");
    }
}
