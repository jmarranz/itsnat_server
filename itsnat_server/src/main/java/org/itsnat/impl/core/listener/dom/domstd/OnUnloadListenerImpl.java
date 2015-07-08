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

package org.itsnat.impl.core.listener.dom.domstd;

import java.io.Serializable;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class OnUnloadListenerImpl implements EventListener,Serializable
{
    public static final OnUnloadListenerImpl SINGLETON = new OnUnloadListenerImpl();

    /**
     * Creates a new instance of OnUnloadListenerImpl
     */
    private OnUnloadListenerImpl()
    {
    }

    public void handleEvent(Event evt)
    {
        // Marcamos el documento como inválido pues el usuario se sale de la página
        // de otra manera la sesión referencia el documento hasta que la sesión
        // se pierda, el problema es que un usuario que hace mucho reload puede
        // saturar el servidor.
        ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        ItsNatDocumentImpl itsNatDoc = (ItsNatDocumentImpl)itsNatEvt.getItsNatDocument();
        itsNatDoc.setInvalid();
    }

}
