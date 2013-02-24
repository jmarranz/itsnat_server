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

package org.itsnat.impl.core.resp.shared.html;

import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateHTMLLoadDocOperaMobileImpl extends ResponseDelegateHTMLLoadDocOperaImpl
{
    public ResponseDelegateHTMLLoadDocOperaMobileImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public boolean isDelayedInit()
    {
        return true;
    }

    public void dispatchRequestListeners()
    {
        fixOnLoad();

        super.dispatchRequestListeners();
    }

    protected void fixOnLoad()
    {
        // Problema del onload, este problema es similar al de Opera Mini, si no se hace esto el addEventListener estándar no funciona,
        // sin embargo sólo es necesario para el evento "load". Lo añadimos
        // antes del código de inicialización que es ejecutado tras procesar
        // el evento DOMContentLoaded porque ha de definirse antes de que se procese el evento DOMContentLoaded
        // También podría ponerse este código como código FixDOM
        String code = "if (!document.body.hasAttribute(\"onload\")) document.body.setAttribute(\"onload\",\"\");\n";
        addFixDOMCodeToSend(code);
    }

}

