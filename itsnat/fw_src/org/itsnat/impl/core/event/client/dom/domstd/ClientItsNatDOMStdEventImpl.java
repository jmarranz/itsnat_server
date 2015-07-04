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

package org.itsnat.impl.core.event.client.dom.domstd;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.Document;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 * Quizás en el futuro se podría usar REX:
 * http://www.w3.org/TR/rex/
    Algo parecido anterior:
    http://xmldb-org.sourceforge.net/xupdate/xupdate-wd.html
 *
 * @author jmarranz
 */
public abstract class ClientItsNatDOMStdEventImpl extends ClientItsNatNormalEventImpl implements ItsNatDOMStdEvent
{
    protected NodeContainerImpl target;

    /** Creates a new instance of EventBaseImpl */
    public ClientItsNatDOMStdEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public AbstractView getAbstractView()
    {
        Document doc = getItsNatStfulDocument().getDocument();
        return ((DocumentView)doc).getDefaultView();
    }

    public ItsNatDOMStdEventListenerWrapperImpl getDOMStdEventListenerWrapper()
    {
        return (ItsNatDOMStdEventListenerWrapperImpl)listenerWrapper;
    }

    public void checkTampering()
    {
        // El listener sabe cual es el type pero el propio evento también
        // lo transporta porque se necesita para hacer procesos previos a crear este objeto (los métodos isUnloadEvent etc).
        // La regla es no fiarse del cliente, así validamos que los "preprocesos"
        // fueron válidos.

        if (!getType().equals(getTypeFromClient()))
            throw new ItsNatException("TAMPERING ATTEMPT FROM CLIENT!!");
    }

    protected abstract String getTypeFromClient();
}
