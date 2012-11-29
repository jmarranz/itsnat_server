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

import java.io.Serializable;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import org.itsnat.core.ItsNatBoot;
import org.itsnat.impl.core.ItsNatImpl;
import org.itsnat.impl.core.servlet.ItsNatServletContextImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatHttpSessionCleanListenerImpl implements HttpSessionBindingListener,Serializable
{
    public static final long serialVersionUID = 1L; // ¡¡NO CAMBIAR!!

    public ItsNatHttpSessionCleanListenerImpl()
    {
    }

    public void valueBound(HttpSessionBindingEvent event)
    {
    }

    public void valueUnbound(HttpSessionBindingEvent event)
    {
        // Cuando la sesión se elimina es llamado este método para los atributos
        // cuyo valor implementa HttpSessionBindingListener (como este objeto)
        // pues lo normal es invalidar antes la sesión llamando a invalidate()
        // el cual según el estándar desvincula antes los atributos.
        // Permite liberar la sesión en los maps del ItsNatServletContext

        HttpSession session = event.getSession();

        ItsNatImpl itsNat = (ItsNatImpl)ItsNatBoot.get();
        ItsNatServletContextImpl itsNatContext = itsNat.getItsNatServletContext(session.getServletContext());

        ItsNatSessionImpl itsNatSession = itsNatContext.getItsNatSessionByStandardId(session.getId());
        if (itsNatSession == null) return; // Por si acaso
        itsNatSession.destroy();
    }

}
