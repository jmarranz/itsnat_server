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

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.impl.core.servlet.ItsNatServletContextImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import javax.servlet.http.HttpSession;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.http.ItsNatHttpSession;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.browser.Browser;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHttpSessionImpl extends ItsNatSessionImpl implements ItsNatHttpSession
{
    public static final String SESSION_CLEAN_ATTRIBUTE_NAME = "itsnat_session_clean";

    protected transient HttpSession session; // No es serializable y el estandar Servlet no proporciona un registro de sesiones

    /**
     * Creates a new instance of ItsNatHttpSessionImpl
     */
    protected ItsNatHttpSessionImpl(HttpSession session,ItsNatServletContextImpl context,Browser browser)
    {
        super(context,browser);

        this.session = session; // No es null
        this.idObj = context.getUniqueIdGenerator().generateUniqueId(session,"ss"); // ss = session
    }

    protected static ItsNatHttpSessionImpl getItsNatHttpSession(ItsNatHttpServletRequestImpl itsNatRequest)
    {
        ItsNatHttpSessionImpl itsNatSession;
        ItsNatServletContextImpl context = itsNatRequest.getItsNatServletContext();
        HttpServletRequest request = itsNatRequest.getHttpServletRequest();
        HttpSession session = request.getSession();
        if (session == null) throw new ItsNatException("INTERNAL ERROR");

        boolean useAttribute = context.isSessionReplicationCapable();

        synchronized(session)
        {
            // Quizás se pudiera evitar el synchronized usando AtomicReference (cuando portemos a Java 1.5)
            // http://www.ibm.com/developerworks/java/library/j-jtp09238.html?ca=drs-
            // De todas maneras el impacto en el rendimiento es ínfimo
            if (useAttribute)
                itsNatSession = ItsNatHttpSessionReplicationCapableImpl.readItsNatHttpSessionFromAttribute(session,context,itsNatRequest);
            else
                itsNatSession = ItsNatHttpSessionStickyImpl.getItsNatHttpSessionStickyByStandardId(session,context);

            if (itsNatSession == null)
            {
                itsNatSession = createItsNatHttpSession(session, context, itsNatRequest);
                context.addItsNatSession(itsNatSession); // útil para el control remoto, siempre hay que hacerlo

                if (session.getAttribute(SESSION_CLEAN_ATTRIBUTE_NAME) == null)
                {
                    // Esta anterior comprobación es muy importante en SessionReplicationCapable = false
                    // pues el cleaner es lo único que se serializa en el caso SessionReplicationCapable = false
                    // por lo que al recargar por ejemplo el servidor de aplicaciones, éste tiende a recrear
                    // las sesiones serializadas y recupera por tanto la sesión guardada con el cleaner ya registrado,
                    // si lo reemplazamos por uno nuevo provocaremos la invalidación de la sesión con el cleaner anterior.
                    session.setAttribute(SESSION_CLEAN_ATTRIBUTE_NAME,new ItsNatHttpSessionCleanListenerImpl());
                }
            }
        }

        return itsNatSession;
    }



    protected static ItsNatHttpSessionImpl createItsNatHttpSession(HttpSession session,ItsNatServletContextImpl context,ItsNatServletRequestImpl itsNatRequest)
    {
        Browser browser = Browser.createBrowser(itsNatRequest);
        if (context.isSessionReplicationCapable())
            return new ItsNatHttpSessionReplicationCapableImpl(session,context,browser);
        else
            return new ItsNatHttpSessionStickyImpl(session,context,browser);
    }

    public HttpSession getHttpSession()
    {
        return session;
    }

    public Object getStandardSessionObject()
    {
        return session;
    }

    public String getStandardSessionId()
    {
        return session.getId();
    }

    public Object getAttribute(String name)
    {
        return session.getAttribute(name);
    }

    public Enumeration getAttributeNames()
    {
        return session.getAttributeNames();
    }

    public void removeAttribute(String name)
    {
        session.removeAttribute(name);
    }

    public void setAttribute(String name, Object value)
    {
        session.setAttribute(name,value);
    }

    public int getMaxInactiveInterval()
    {
        return session.getMaxInactiveInterval();
    }
}
