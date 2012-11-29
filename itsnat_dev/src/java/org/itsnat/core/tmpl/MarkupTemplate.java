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

package org.itsnat.core.tmpl;

import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatUserData;

/**
 * Represents a generic markup template.
 *
 * <p>A markup template can be a document (page) or a fragment (to insert into documents)
 * and is obtained from an {@link ItsNatServlet} where it was previously registered.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface MarkupTemplate extends ItsNatUserData
{
    /**
     * Returns the ItsNat servlet where the template was registered.
     *
     * @return the ItsNat servlet wrapper.
     */
    public ItsNatServlet getItsNatServlet();

    /**
     * Returns the used name to register the template.
     *
     * @return the template name.
     */
    public String getName();

    /**
     * Returns the source specified to load the template.
     *
     * <p>Current implementation returns the template source provided, if it was a String (file path or URL)
     * it returns a String with URL format (including local files in this case the URL starts with "file:").</p>
     *
     * @return the template source. Null if this template is of "attached server" type
     */
    public Object getSource();

    /**
     * Returns the template MIME type. This MIME is the value used to render
     * to text and in the header sent to clients.
     *
     * @return the MIME type.
     */
    public String getMIME();

    /**
     * Returns the encoding used. This encoding is used to render
     * to text and in the header sent to clients.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#getDefaultEncoding()}</p>
     *
     * @return the encoding.
     * @see #setEncoding(String)
     */
    public String getEncoding();

    /**
     * Sets the encoding used.
     *
     * @param encoding the new encoding.
     * @see #getEncoding()
     */
    public void setEncoding(String encoding);

    /**
     * Informs whether static nodes are serialized as text and globally cached when
     * the template is loaded to save memory (it improves the performance too).
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isOnLoadCacheStaticNodes(String)}
     * using the mime type of this template.</p>
     *
     * @return true if caching is enabled.
     * @see #setOnLoadCacheStaticNodes(boolean)
     */
    public boolean isOnLoadCacheStaticNodes();

    /**
     * Sets if static nodes are serialized as text and globally cached when
     * the template is loaded to save memory (it improves the performance too).
     *
     * @param cache true to enable the static node cache.
     * @see #isOnLoadCacheStaticNodes()
     */
    public void setOnLoadCacheStaticNodes(boolean cache);


}
