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

import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.ItsNatUserData;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.html.HTMLInputElement;

/**
 * An instance of this interface represents a file uploading task
 * associated to an iframe component and an input type=file element.
 *
 * <p>When this instance is created by calling {@link org.itsnat.comp.iframe.ItsNatHTMLIFrame#getHTMLIFrameFileUpload(HTMLInputElement)}
 * or {@link org.itsnat.comp.iframe.ItsNatHTMLIFrame#getHTMLIFrameFileUpload(ClientDocument,HTMLInputElement)},
 * JavaScript code is generated to create a temporary form to submit the file specified in
 * the <code>&lt;input type=file&gt;</code> element to the <code>&lt;iframe&gt;</code> as target. Because this code is sent
 * in the end of the request, developers can register some listener calling {@link #addItsNatServletRequestListener(ItsNatServletRequestListener)}
 * to receive and process the file uploading request.
 * </p>
 *
 * <p>In spite of developers have absolute freedom to process the file upload request, the method
 * {@link #processFileUploadRequest(ItsNatServletRequest,ItsNatServletResponse)} can be optionally called,
 * this method executes the typical header parsing and exposes the file being uploaded as an input stream.
 * </p>
 *
 * <p>The thread dispatching the file upload listeners does not synchronize the {@link org.itsnat.core.ItsNatDocument}
 * object, therefore any access to the {@link org.itsnat.core.ItsNatDocument} or dependent objects must be synchronized before.
 * </p>
 *
 * <p>When file upload processing ends (when listeners have been executed) this object is automatically disposed and cannot be reused
 * (explicit call to {@link #dispose()} is not needed).
 * </p>
 *
 * <p>{@link ItsNatUserData} methods are thread safe.</p>
 *
 * <p>ItsNat provides a default implementation of this interface.
 * </p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.iframe.ItsNatHTMLIFrame
 */
public interface HTMLIFrameFileUpload extends ItsNatUserData
{
    /**
     * Returns the parent iframe component which created this instance.
     *
     * @return the parent component.
     */
    public ItsNatHTMLIFrame getItsNatHTMLIFrame();

    /**
     * Returns the input type=file element being used for file upload.
     *
     * @return the input element with the file to load.
     */
    public HTMLInputElement getHTMLInputElement();

    /**
     * Returns the client document which originated this file upload request.
     *
     * @return the associated client document.
     */
    public ClientDocument getClientDocument();

    /**
     * Registers a new request listener to be executed when the file upload request is received.
     *
     * @param listener the listener to register.
     */
    public void addItsNatServletRequestListener(ItsNatServletRequestListener listener);

    /**
     * Unregisters the specified file upload request listener.
     *
     * @param listener the request listener to remove.
     * @see #addItsNatServletRequestListener(ItsNatServletRequestListener)
     */
    public void removeItsNatServletRequestListener(ItsNatServletRequestListener listener);

    /**
     * This method can be optionally called when a file upload request listener is being executed
     * to parse request headers and expose the file being uploaded as an input stream.
     *
     * @param request the ItsNat servlet request.
     * @param response the ItsNat servlet response.
     * @see #addItsNatServletRequestListener(ItsNatServletRequestListener)
     */
    public FileUploadRequest processFileUploadRequest(ItsNatServletRequest request, ItsNatServletResponse response);

    /**
     * Informs whether this component was disposed.
     *
     * <p>This method is thread safe and can be called
     * directly from the thread reading the file being uploaded.
     * </p>
     *
     * @return true if this component was disposed.
     * @see #dispose()
     */
    public boolean isDisposed();

    /**
     * Disposes this component. A disposed component can no longer be used.
     *
     * <p>If disposed before the file upload request is received, this request is ignored and listeners are not executed.
     *
     * <p>Because this method synchronizes the <code>ItsNatDocument</code> it can be called
     * directly from the thread reading the file being uploaded.
     * </p>
     *
     * @see #isDisposed()
     */
    public void dispose();

}
