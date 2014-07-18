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

package org.itsnat.impl.core.event.server.dom.domstd;

import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.server.dom.ServerItsNatDOMEventImpl;
import org.w3c.dom.DOMException;

/**
 * Sirve para dos tipos de eventos:
 * 1) Eventos creados en el servidor y enviados al cliente: El objeto evento no será "dispatched"
 * a los listeners por lo que algunos métodos (getCurrentTarget() etc) no son útiles
 * 2) Eventos creados en el servidor y "dispatched" localmente: hay que simular
 * que el evento viene del cliente lo más posible.
 *
 * @author jmarranz
 */
public abstract class ServerItsNatDOMStdEventImpl extends ServerItsNatDOMEventImpl implements ItsNatDOMStdEvent
{
    /**
     * Creates a new instance of ServerItsNatDOMStdEventImpl
     */
    public ServerItsNatDOMStdEventImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public static ServerItsNatDOMStdEventImpl createServerItsNatDOMStdEvent(String eventGroup,ItsNatStfulDocumentImpl itsNatDoc) throws DOMException
    {
        // http://developer.mozilla.org/en/docs/DOM:document.createEvent
        // El nombre "KeyboardEvent" es soportado por FireFox ("KeyboardEvents" no curiosamente)
        // y por Safari de acuerdo con el DOM 3, pero nos lo reservamos
        // hasta que el DOM 3 sea de verdad final y tengamos una interface definitiva,
        // pues actualmente FireFox no soporta la interface actual de DOM 3 (ni siquiera Safari)
        // De esa manera en el futuro se podrán usar ambos interfaces (FireFox y DOM 3) sin problemas
        // y con compatibilidad con el pasado.

        if ("UIEvents".equals(eventGroup) || "UIEvent".equals(eventGroup) )
            return new ServerUIEventImpl(itsNatDoc);
        else if ("MouseEvents".equals(eventGroup) || "MouseEvent".equals(eventGroup) )
            return new ServerMouseEventImpl(itsNatDoc);
        else if ("KeyEvents".equals(eventGroup) || "KeyEvent".equals(eventGroup) )
            return new ServerItsNatKeyEventImpl(itsNatDoc);
        else if ("MutationEvents".equals(eventGroup) || "MutationEvent".equals(eventGroup) )
            return new ServerMutationEventImpl(itsNatDoc);
        else if ("HTMLEvents".equals(eventGroup) || "HTMLEvent".equals(eventGroup) )
            return new ServerHTMLEventImpl(itsNatDoc);
        else if ("Events".equals(eventGroup) || "Event".equals(eventGroup) )
            return new ServerItsNatDOMStdEventDefaultImpl(itsNatDoc);

        throw new DOMException(DOMException.NOT_SUPPORTED_ERR,"NOT_SUPPORTED_ERR: The implementation does not support the requested type of object or operation.");
    }

}
