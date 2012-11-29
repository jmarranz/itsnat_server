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

import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.Referrer;
import org.itsnat.impl.core.browser.Browser;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class ClientDocumentStfulOwnerImpl extends ClientDocumentStfulImpl
{

    /** Creates a new instance of ClientDocumentStfulOwnerImpl */
    public ClientDocumentStfulOwnerImpl(ItsNatStfulDocumentImpl itsNatDoc,Browser browser,ItsNatSessionImpl itsNatSession)
    {
        super(itsNatDoc,browser,itsNatSession);
    }

    public boolean isScriptingEnabled()
    {
        return getItsNatStfulDocument().isScriptingEnabled();
    }

    public boolean isEventsEnabled()
    {
        return getItsNatStfulDocument().isEventsEnabled();
    }

    public boolean canReceiveALLNormalEvents()
    {
        return isEventsEnabled(); // Si AJAX está desactivado el documento no se guardará en la sesión (tampoco el cliente) por tanto no habrá eventos posibles
    }

    public boolean canReceiveNormalEvents(EventListener listener)
    {
        return canReceiveALLNormalEvents(); // O todos o ninguno.
    }

    public boolean canReceiveSOMENormalEvents()
    {
        return canReceiveALLNormalEvents(); // O todos o ninguno.
    }

    protected void setInvalidInternal()
    {
        super.setInvalidInternal();

        // Es el propietario del documento, si es inválido lo será también el documento asociado
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        itsNatDoc.setInvalid();

        ItsNatSessionImpl itsNatSession = getItsNatSessionImpl();
        itsNatSession.unregisterClientDocumentStfulOwner(this);

        if (itsNatDoc.isReferrerEnabled())
        {
            // Normalmente se llega aquí via unload, la posible nueva página
            // tuvo la oportunidad de obtener el referrer tras el onbeforeunload de la página origen
            // ahora que seguramente se está ejecutando el unload ya no.

            Referrer referrer = itsNatSession.getReferrer();
            ItsNatStfulDocumentImpl itsNatDocRef = referrer.getItsNatStfulDocument();
            if (itsNatDocRef == itsNatDoc)
                referrer.cleanItsNatStfulDocument();
        }
    }

    public void registerInSession()
    {
        getItsNatSessionImpl().registerClientDocumentStfulOwner(this); // Pasa a ser accesible el documento (aunque no se puede tocar por otros hilos hasta que se libere el lock)
    }

}
