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
package org.itsnat.impl.core.servlet.http;

import javax.servlet.http.HttpSession;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.servlet.ItsNatServletContextImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatHttpSessionStickyImpl extends ItsNatHttpSessionImpl
{
    protected ItsNatHttpSessionStickyImpl(HttpSession session,ItsNatServletContextImpl context,Browser browser)
    {
        super(session,context,browser);
    }

    public void endOfRequestBeforeSendCode()
    {
        // Nada que hacer
    }

    public void endOfRequest()
    {
        // Nada que hacer
    }

    public static ItsNatHttpSessionStickyImpl getItsNatHttpSessionStickyByStandardId(HttpSession session,ItsNatServletContextImpl context)
    {
        ItsNatHttpSessionStickyImpl itsNatSession = (ItsNatHttpSessionStickyImpl)context.getItsNatSessionByStandardId(session.getId());
        if ((itsNatSession != null) && (itsNatSession.session != session))
        {
            // He comprobado que en GAE la instancia de sesión en la misma JVM puede cambiar
            // entre requests aunque no haya expirado (mismo id de sesión) y la anterior ya no vale (sesión vacía por ejemplo)
            // en modo de no replicación es importante ésto.
            itsNatSession.session = session;
        }
        return itsNatSession;
    }
}
