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

package org.itsnat.core;

import javax.servlet.ServletContext;

/**
 * Is the ItsNat wrapper object of the standard <code>javax.servlet.ServletContext</code>.
 *
 * <p>There is only one instance of this interface per web application.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatServlet#getItsNatServletContext()
 * @see ItsNatServletConfig#getItsNatServletContext()
 * @see ItsNatSession#getItsNatServletContext()
 */
public interface ItsNatServletContext extends ItsNatUserData
{
    /**
     * Returns the standard servlet context wrapped.
     *
     * @return the servlet context.
     */
    public ServletContext getServletContext();

    /**
     * Returns the ItsNat "root" object used to create this object.
     *
     * @return the parent ItsNat object.
     */
    public ItsNat getItsNat();

    /**
     * Returns the max number of open documents in a user web session (documents saving state on the server).
     *
     * <p>When this number is surpassed, older documents (documents not accesed for a long time ago)
     * are invalidated/removed from the server.
     * </p>
     *
     * <p>A negative number means no limit.</p>
     *
     * <p>Documents open through iframe, object or embed elements are included in this number,
     * so the max number of open documents must be greater than the number of child documents
     * of any page +1 (the container page). A number slightly greater
     * is preferred (sometimes MSIE needs two requests to load the content of
     * an iframe/object/page, that is, the page is loaded twice).
     * </p>
     *
     * <p>This feature is very useful to limit the server memory used by web bots (crawlers)
     * unsupported browsers and browsers with JavaScript disabled
     * traversing pages/documents with state (events enabled).</p>
     *
     * <p>This feature only affects to documents not to fragments.</p>
     *
     * @return the max number of open documents. Defaults to 10.
     * @see #setMaxOpenDocumentsBySession(int)
     */
    public int getMaxOpenDocumentsBySession();

    /**
     * Sets the max number of open documents in a user web session (documents saving state on the server).
     *
     * @param value the new max number of open documents.
     * @see #getMaxOpenDocumentsBySession()
     */
    public void setMaxOpenDocumentsBySession(int value);

    /**
     * Controls whether the ItsNat application is ready for automatic session replication of the servlet container.
     *
     * <p>When this method returns true, the ItsNat session ({@link org.itsnat.core.http.ItsNatHttpSession}) is registered as an attribute
     * in the native session calling <code>HttpSession.setAttribute(String,Object)</code>
     * and the ItsNat session is ever obtained calling <code>HttpSession.getAttribute(String)</code>.
     * The method <code>HttpSession.setAttribute(String,Object)</code> is ever called when any event processing finishes.
     * </p>
     *
     * <p>In an automatic session replication environment like Google App Engine (GAE),
     * GAE automatically serializes the ItsNat session per web request, in theory
     * GAE automatically manages concurrency between nodes and only processes one web request
     * each time in every session.
     * </p>
     *
     * <p>If session replication is set to true, the ItsNat session id as returned
     * by {@link ItsNatSession#getId()} is the SHA-1 value of the native session id
     * returned by <code>HttpSession.getId()</code>, this ensures the ItsNat session id
     * is the same between nodes. This non-reversible ItsNat session id (SHA-1 is a one-way algorithm)
     * is interesting because in remote/view control ItsNat session ids can be
     * public, only ItsNat session ids are recognized because publishing the native id
     * is not a good idea because it grants full control to other users of the monitored session outside ItsNat control.
     * </p>
     * 
     * <p>If the servlet environment is one only node (one servlet container instance)
     * or is not doing session replication, and session replication capable
     * is set to true, there is no appreciable difference in performance and behaviour.
     * </p>
     * 
     * <p>Anyway if this method returns true the servlet container can serialize
     * the session content, this implies any user defined class bound to the session
     * usually through ItsNat documents, must implement <code>java.io.Serializable</code>.
     * </p>
     *
     * <p>If the servlet environment actually does session replication, like GAE, some ItsNat features
     * may not work.
     * </p>
     *
     * <p>If session replication capable feature is set to false the method
     * <code>HttpSession.setAttribute(String,Object)</code> 
     * is not used to store the session, hence no <code>java.io.Serializable</code> requirement applies to user code.
     * Use this mode when running your web application in a single node or
     * multiple nodes configured to use sticky sessions.
     * </p>
     *
     * @return true if session replication capability is enabled. Default is false.
     * @see #setSessionReplicationCapable(boolean)
     */
    public boolean isSessionReplicationCapable();

    /**
     * Sets whether the ItsNat application is ready for automatic session replication by the servlet container.
     *
     * @param value the new mode.
     * @see #isSessionReplicationCapable()
     */
    public void setSessionReplicationCapable(boolean value);

    /**
     * Defines whether the serialized data of the ItsNat session is compressed when the session
     * is serialized.
     *
     * <p>This feature is useful to reduce the amount of transported data between nodes in a cloud
     * environment with session replication.
     * </p>
     * 
     * <p>This configuration option set to true only has sense if session replication
     * is enabled and when the servlet container serializes sessions.
     * </p>
     *
     * @return true if session compression is enabled. Default is false.
     * @see #setSessionSerializeCompressed(boolean)
     * @see #isSessionReplicationCapable()
     */
    public boolean isSessionSerializeCompressed();

    /**
     * Sets whether the serialized data of the ItsNat session is compressed when the session
     * is serialized.
     *
     * @param value the new mode.
     * @see #isSessionSerializeCompressed()
     */
    public void setSessionSerializeCompressed(boolean value);

    /**
     * Defines whether ItsNat explicitly serializes user sessions and saves serialized
     * data to the native sessions as attributes when any web request ends
     * and deserializing in the beginning of any web request.
     *
     * <p>This feature is useful to simulate how cloud environments with session replication works
     * and to avoid serialization time limits imposed by the cloud (for instance Google App Engine).

     * <p>This configuration option set to true only does something if session replication is enabled.
     * </p>
     *
     * @return true if explicit session serialization is enabled. Default is false.
     * @see #setSessionExplicitSerialize(boolean)
     * @see #isSessionReplicationCapable()
     */
    public boolean isSessionExplicitSerialize();

    /**
     * Configures ItsNat to explicitly serialize user sessions and save serialized
     * data to the native sessions as attributes when any web request ends
     * and deserializing in the beginning of any web request.
     *
     * @param value the new mode.
     * @see #isSessionExplicitSerialize()
     */
    public void setSessionExplicitSerialize(boolean value);

    /**
     * Enumerates all ItsNat sessions associated to this context.
     *
     * <p>The specified <code>callback</code> is called per each session.</p>
     *
     * @param callback the callback object to call per each session.
     */
    public void enumerateSessions(ItsNatSessionCallback callback);


    /**
     * Creates a variable resolver bound to this context.
     *
     * @return a variable resolver bound to this context.
     */
    public ItsNatVariableResolver createItsNatVariableResolver();
}
