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

package org.itsnat.impl.core.resp.norm;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestDOMEventImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseDOMEventImpl extends ResponseNormalEventImpl
{
    /** Creates a new instance of ResponseNormalEventImpl */
    public ResponseDOMEventImpl(RequestDOMEventImpl request,ItsNatNormalEventListenerWrapperImpl listener)
    {
        super(request,listener);

        if ((listener != null) &&
             !getClientDocumentStful().canReceiveNormalEvents(listener))
        {
            // Chequeo de seguridad para evitar que clientes de control remoto
            // envíen eventos cuando no están autorizados (son read only por ejemplo). En teoría no se envió
            // código JavaScript para ello (registro de listener) pero un malicioso usuario
            // podría intentarlo enviando requests AJAX "a pelo".
            throw new ItsNatException("Security violation attempt");
        }
    }
}
