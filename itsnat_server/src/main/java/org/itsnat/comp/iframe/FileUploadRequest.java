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
package org.itsnat.comp.iframe;

import java.io.InputStream;

/**
 * The default implementation of this interface parses the headers of a file upload request
 * and exposes the file being uploaded as an input stream.
 *
 * @author Jose Maria Arranz Santamaria
 * @see HTMLIFrameFileUpload#processFileUploadRequest(ItsNatServletRequest request, ItsNatServletResponse response)
 */
public interface FileUploadRequest
{
    /**
     * Returns the value of the name attribute specified in the &lt;input type=file&gt; element
     * which was used for file uploading.
     *
     * <p>If no name attribute is specified in the original &lt;input type=file&gt; element
     * ItsNat provides a temporary one.
     * </p>
     *
     * @return the value of the name attribute.
     */
    public String getFieldName();

    /**
     * Returns the file name as it was specified by end user in the &lt;input type=file&gt; element
     * which was used for file uploading.
     *
     * @return the file name.
     */
    public String getFileName();

    /**
     * Returns the content type of the file being uploaded as specified by browser.
     *
     * @return the content type of the file.
     */
    public String getContentType();

    /**
     * Returns the file size of the file being uploaded.
     *
     * @return the file size.
     */
    public long getFileSize();

    /**
     * Returns a stream of the file being uploaded.
     *
     * <p>The size of this stream must be the same as the value returned by {@link #getFileSize()},
     * and content is the same as the original file in client.
     * </p>
     *
     * @return a stream of the file.
     */
    public InputStream getFileUploadInputStream();
}
