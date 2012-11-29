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

import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedServerFormImpl;
import org.itsnat.impl.core.req.attachsrv.RequestAttachedServerLoadMarkupImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseAttachedServerLoadMarkupFormImpl extends ResponseAttachedServerLoadMarkupImpl
{

    /** Creates a new instance of ResponseNormalLoadDocImpl */
    public ResponseAttachedServerLoadMarkupFormImpl(RequestAttachedServerLoadMarkupImpl request)
    {
        super(request);
    }

    public void processResponse()
    {
        // En el caso de <form> se envía el markup de una sola vez
        ClientDocumentAttachedServerFormImpl clientDoc =
                (ClientDocumentAttachedServerFormImpl)getClientDocumentAttachedServer();
        synchronized(clientDoc)
        {
            clientDoc.setMarkupLoaded(true);
            clientDoc.notifyAll();
        }

        // No devolvemos nada pues es la "página" de retorno del iframe auxiliar
        // como el form ha podido ser enviado a un dominio diferente
        // cualquier script metido no tendrá acceso a la página padre
        // por eso será el onload del iframe el que siga el proceso.
        // Si no devolvemos nada valdrá el resultado para cualquier MIME
        // teniendo en cuenta que el MIME resultado es del template (lo normal será text/html).
    }

}
