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

import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.core.ClientDocument;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 * Is the interface of HTML &lt;iframe&gt; based components.
 *
 * <p>This component alongside an <code>&lt;input type=file&gt;</code> element can be used for
 * file uploading.
 * </p>
 *
 * <p>When this component is disposed, any pending file upload process is disposed and not
 * executed.
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * </p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatHTMLComponentManager#createItsNatHTMLIFrame(org.w3c.dom.html.HTMLIFrameElement,org.itsnat.core.NameValue[])
 */
public interface ItsNatHTMLIFrame extends ItsNatHTMLElementComponent
{
    /**
     * Returns the associated DOM element to this component.
     *
     * @return the associated DOM element.
     */
    public HTMLIFrameElement getHTMLIFrameElement();

    /**
     * Creates a file upload task associated to this iframe component and to the specified <code>&lt;input type=file&gt;</code>
     * element.
     *
     * @param elem the input file element, cannot be null.
     * @param clientDoc the client to send the file upload request.
     * @return a file upload task instance to initiate and process the file upload request.
     */
    public HTMLIFrameFileUpload getHTMLIFrameFileUpload(ClientDocument clientDoc,HTMLInputElement elem);

    /**
     * Creates a file upload task associated to this iframe component and to the specified <code>&lt;input type=file&gt;</code>
     * element.
     *
     * <p>The {@link ClientDocument} to send the file upload request is the client associated
     * to the current web request or, if this call is not executed into a web request, the client
     * which loaded the document (the first client).
     *
     * @param elem the input file element, cannot be null.
     * @return a file upload task instance to initiate and process the file upload request.
     * @see #getHTMLIFrameFileUpload(ClientDocument,HTMLInputElement)
     */
    public HTMLIFrameFileUpload getHTMLIFrameFileUpload(HTMLInputElement elem);
}
