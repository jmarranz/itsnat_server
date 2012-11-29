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

package org.itsnat.impl.core.resp.attachsrv;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedServerFormImpl;
import org.itsnat.impl.core.req.attachsrv.RequestAttachedServerLoadDocImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseAttachedServerLoadDocFormImpl extends ResponseAttachedServerLoadDocImpl
{

    /** Creates a new instance of ResponseNormalLoadDocImpl */
    public ResponseAttachedServerLoadDocFormImpl(RequestAttachedServerLoadDocImpl request)
    {
        super(request);
    }

    public void processResponse()
    {
        // Esto no funciona en GAE por varias razones:
        // 1) GAE sólo permite un request cada vez, no son posibles requests concurrentes
        // 2) La instancia del clientDoc puede ser diferente a la del otro request
        //    y hasta que no haya serialización/deserialización (lo que supone terminar
        //    algun request) no se podrá detectar cambios en el atributo markupLoaded
        // 3) En cualquier momento un request puede enviarse a un nodo diferente

        // Puede funcionar en modo SessionReplicationCapable si no hay verdadera replicación
        // pero no funciona con ItsNatHttpSessionReplicationCapableImpl.simulateSerialization
        // a true pues es precisamente una simulación de GAE en donde los objetos
        // bloqueados ClientDocumentAttachedServerFormImpl son diferentes (por la serialización) por tanto
        // este thread se queda indefinidamente parado (hasta que llegue al timeout si no es cero).

        ClientDocumentAttachedServerFormImpl clientDoc =
                (ClientDocumentAttachedServerFormImpl)getClientDocumentAttachedServer();
        synchronized(clientDoc)
        {
            if (!clientDoc.isMarkupLoaded())
            {
                try { clientDoc.wait(clientDoc.getTimeoutLoadMarkup()); }
                catch(InterruptedException ex){ throw new ItsNatException(ex); }

                if (!clientDoc.isMarkupLoaded())
                    throw new ItsNatException("Timeout, client markup is not received",clientDoc);
            }
        }

        super.processResponse();
    }

}
