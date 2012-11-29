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

import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
 *
 * @author jmarranz
 */
public class ReferrerStrong extends Referrer
{
    protected ItsNatStfulDocumentImpl referrer;

    /** Creates a new instance of ReferrerStrong */
    public ReferrerStrong()
    {
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return referrer;
    }

    public synchronized void pushItsNatStfulDocument(ItsNatStfulDocumentImpl referrer)
    {
        this.referrer = referrer;
    }

    public synchronized ItsNatStfulDocumentImpl popItsNatStfulDocument()
    {
        ItsNatStfulDocumentImpl itsNatDoc = this.referrer;
        this.referrer = null;
        return itsNatDoc;
    }

    public void cleanItsNatStfulDocument()
    {
        // Este método es llamado en unload, en el caso de referencia strong
        // no tenemos la seguridad de que el nuevo documento se haya cargado
        // antes de que el anterior se destruya con unload.
        // Es el caso por ejemplo de "Reload" en MSIE o de ir a una página a través
        // de back/forward en un navegador que cachea en el cliente (no se carga de nuevo la página, aunque luego esta carga se fuerza por ItsNat)
        // tal y como Opera (el unload de la anterior probablemente se ejecute).
        // así evitamos que al ejecutarse el evento "unload"
        // se pierda el referrer porque el target del back/forward lo recogerá a través
        // en una posterior recarga aunque después del unload

        // En Opera curiosamente
        // se ejecuta el unload cuando el back/forward se hace via JavaScript
        // (por ej. window.history.go(-1) en el href de un link)
        // Así dejamos el referrer hasta que el evento load
        // de la página cacheada haga pop.
    }
}
