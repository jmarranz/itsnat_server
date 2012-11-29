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

package org.itsnat.impl.core.clientdoc;

import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateAttachedServerImpl;

/**
 * Este cliente es temporal únicamente para el proceso de carga
 *
 * @author jmarranz
 */
public class ClientDocumentAttachedServerFormImpl extends ClientDocumentAttachedServerImpl
{
    protected boolean markupLoaded;
    protected long timeoutLoadMarkup; // Milisegundos

    public ClientDocumentAttachedServerFormImpl(ItsNatSessionImpl session,
                ItsNatStfulDocumentTemplateAttachedServerImpl docTemplate,
                long timeoutLoadMarkup)
    {
        super(session,docTemplate);
        this.timeoutLoadMarkup = timeoutLoadMarkup;
    }

    public boolean isMarkupLoaded()
    {
        return markupLoaded;
    }

    public void setMarkupLoaded(boolean markupLoaded)
    {
        this.markupLoaded = markupLoaded;
    }

    public long getTimeoutLoadMarkup()
    {
        return timeoutLoadMarkup;
    }

    public boolean isOnLoadHanderUsed()
    {
        // En el caso de SessionReplicationCapable, por ejemplo en GAE, no podemos
        // usar dos requests concurrentes pues GAE sólo admite uno por sesión
        // para serializar la sesión tras el mismo.
        // Por otra parte está el problema de que el client document no puede
        // servir de monitor de threads pues es diferente instancia
        // incluso en simulación de serialización.

        // En este caso se utilizará el handler onload del <iframe>, cuando
        // es ejecutado se considera el documento ya cargado y ya no se puede
        // usar document.write()

        return getItsNatSessionImpl().getItsNatServletContextImpl().isSessionReplicationCapable();
    }
}
