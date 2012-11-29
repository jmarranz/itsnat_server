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

import java.util.Enumeration;

/**
 * Is the ItsNat user session, the session concept is borrowed from the HTTP Java Servlet
 * session concept. In fact ItsNat HTTP implementation is a wrapper over
 * the <code>javax.servlet.http.HttpSession</code> object.
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatServletRequest#getItsNatSession()
 * @see ClientDocument#getItsNatSession()
 */
public interface ItsNatSession extends ItsNatUserData
{
    /**
     * Returns the client identity. This value is unique per {@link ItsNatServletContext}
     * and never reused in this context.
     *
     * <p>The identity value is unique no other context-identified object in the same ItsNat servlet context
     * shares the same id.</p>
     *
     * <p>Although this object is garbage collected, the identity value is never reused
     * by another context-identified object in the same context.</p>
     *
     * @return the identity value.
     */
    public String getId();

    /**
     * Returns the ItsNat servlet context of this ItsNat application.
     *
     * @return the ItsNat servlet contex.
     */
    public ItsNatServletContext getItsNatServletContext();

    /**
     * Returns the "live" ItsNat document with the given id loaded by this session.
     *
     * <p>A live document is a document still loaded by the user (not invalid).
     * When a document is unloaded (the user leaves the page) the document
     * is invalid and automatically unregistered.</p>
     *
     * @param id the document id to search for.
     * @return the document with this id or null if not found.
     * @see ItsNatDocument#getId()
     * @see ItsNatDocument#isInvalid()
     */
    public ItsNatDocument getItsNatDocumentById(String id);

    /**
     * Returns the number of live ItsNat documents loaded by this session.
     *
     * @return the number of live documents.
     * @see #getItsNatDocuments()
     */
    public int getItsNatDocumentCount();

    /**
     * Returns all live ItsNat documents loaded by this session.
     *
     * @return an array containing all live documents in this session.
     * @see #getItsNatDocumentCount()
     * @see #getItsNatDocumentById(String)
     */
    public ItsNatDocument[] getItsNatDocuments();

    /**
     * Creates a variable resolver bound to this session.
     *
     * @return a variable resolver bound to this session.
     */
    public ItsNatVariableResolver createItsNatVariableResolver();

    /**
     * Returns the object bound with the specified name in this session
     *
     * <p>{@link org.itsnat.core.http.ItsNatHttpSession} implementation delegates to <code>HttpSession.getAttribute(String)</code></p>
     *
     * @param name the object name.
     * @return the object bound with the specified name or null if not found.
     */
    public Object getAttribute(String name);

    /**
     * Returns a <code>String</code> enumeration of names of all the objects bound to this session.
     *
     * <p>{@link org.itsnat.core.http.ItsNatHttpSession} implementation delegates to <code>HttpSession.getAttributeNames()</code></p>
     *
     * @return a <code>String</code> enumeration of atribute names.
     */
    public Enumeration getAttributeNames();

    /**
     * Binds an object to this session, using the name specified.
     * If an object of the same name is already bound to the session, the object is replaced.
     *
     * <p>{@link org.itsnat.core.http.ItsNatHttpSession} implementation delegates to <code>HttpSession.setAttribute(String,Object)</code></p>
     *
     * @param name the attribute name.
     * @param value the attribute value.
     */
    public void setAttribute(String name,Object value);

    /**
     * Removes the object bound with the specified name from this session.
     * If the session does not have an object bound with the specified name, this method does nothing.
     *
     * <p>{@link org.itsnat.core.http.ItsNatHttpSession} implementation delegates to <code>HttpSession.removeAttribute(String)</code></p>
     *
     * @param name the attribute name to remove.
     */
    public void removeAttribute(String name);

}
