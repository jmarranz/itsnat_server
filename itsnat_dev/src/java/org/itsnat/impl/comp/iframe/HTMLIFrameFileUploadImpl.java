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
package org.itsnat.impl.comp.iframe;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.comp.iframe.FileUploadRequest;
import org.itsnat.comp.iframe.HTMLIFrameFileUpload;
import org.itsnat.comp.iframe.ItsNatHTMLIFrame;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.impl.core.ItsNatUserDataImpl;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatHTMLDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.mut.doc.BeforeAfterMutationRenderListener;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerImpl;
import org.itsnat.impl.core.servlet.ItsNatServletResponseImpl;
import org.itsnat.impl.core.util.HasUniqueId;
import org.itsnat.impl.core.util.UniqueId;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class HTMLIFrameFileUploadImpl extends ItsNatUserDataImpl
        implements HTMLIFrameFileUpload,HasUniqueId,Serializable,BeforeAfterMutationRenderListener
{
    protected ItsNatHTMLIFrameImpl comp;
    protected HTMLInputElement inputElem;
    protected ClientDocumentStfulImpl clientDoc;
    protected UniqueId idObj;
    protected LinkedList<ItsNatServletRequestListener> requestListeners;
    protected boolean receiving = false;
    protected boolean disposed = false;
    protected boolean processed = false;

    public HTMLIFrameFileUploadImpl(ItsNatHTMLIFrameImpl comp,HTMLInputElement inputElem,ClientDocumentStfulImpl clientDoc)
    {
        super(true); // Sincronizamos pues este objeto es normalmente accedido por al menos dos hilos

        this.comp = comp;
        this.inputElem = inputElem;
        this.clientDoc = clientDoc;
        this.idObj = clientDoc.getUniqueIdGenerator().generateUniqueId("ifur"); // ifur = IFrame File Upload Request

        if (!DOMUtilHTML.isHTMLInputFile(inputElem))
            throw new ItsNatException("Expected an <input type='file'> element");

        HTMLIFrameElement iframeElem = comp.getHTMLIFrameElement();
        String targetName = iframeElem.getAttribute("name");
        if (DOMUtilInternal.isNodeInside(iframeElem,null))
        {
            // Una restricción de seguridad que tienen los navegadores es que el
            // atributo name del iframe no puede definirse/cambiarse de forma efectiva
            // tras la inserción (o bien tras la carga del elemento desde markup),
            // mejor dicho, se cambia, pero dicho nuevo nombre no es válido como
            // target de un form, el inicial es el que sigue siendo válido.
            if (targetName.equals("")) // Ya no podemos imponerlo nosotros, no funcionaría
                throw new ItsNatDOMException("Expected a non-empty attribute name",iframeElem);
        }
        else
        {
            // Insertamos en el documento definiendo un name si es necesario
            // Recuerda que en MSIE el name se define en el createElement("<iframe name='...'>")
            if (targetName.equals(""))
                targetName = "itsnat_iffu_" + idObj.getId(); // iffu = IFrame File Upload
            iframeElem.setAttribute("name",targetName);  // Necesario
            Element rootElem = getItsNatStfulDocument().getVisualRootElement();
            rootElem.appendChild(iframeElem);
        }

        // Llegados a este punto es seguro que el <iframe> está en el documento
        DocMutationEventListenerImpl mainListener = clientDoc.getItsNatStfulDocument().getDocMutationEventListener();
        mainListener.addBeforeAfterMutationRenderListener(iframeElem,this);

        clientDoc.addHTMLIFrameFileUploadImpl(this);
        comp.addHTMLIFrameFileUploadImpl(this);

        StringBuilder code = new StringBuilder();

        code.append("var elem = " + clientDoc.getNodeReference(inputElem,true,true) + ";\n");
        code.append("var elemClone = elem.cloneNode(true);\n");
        code.append("var parentNode = itsNatDoc.getParentNode(elem);\n");
        code.append("itsNatDoc.insertBefore(parentNode,elemClone,elem);\n");

        if (comp.getItsNatDocumentImpl() instanceof ItsNatHTMLDocumentImpl)
            code.append("var form = itsNatDoc.doc.createElement(\"form\");\n");
        else
            code.append("var form = itsNatDoc.doc.createElementNS(\"" + NamespaceUtil.XHTML_NAMESPACE + "\",\"form\");\n"); // No probado
        code.append("itsNatDoc.setAttribute(form,\"method\",\"post\");\n");
        code.append("itsNatDoc.setAttribute(form,\"enctype\",\"multipart/form-data\");\n");
        if (clientDoc.getBrowser() instanceof BrowserMSIEOld)
            code.append("form.encoding = \"multipart/form-data\";\n"); // El atributo enctype es ignorado

        code.append("itsNatDoc.setAttribute(form,\"action\"," + getActionURL() + ");\n");
        code.append("itsNatDoc.setAttribute(form,\"target\",\"" + targetName + "\");\n");
        code.append("itsNatDoc.insertBefore(parentNode,form,elemClone);\n");
        code.append("itsNatDoc.appendChild(form,elem);\n");

        code.append("var oldName = elem.name;\n");

        String name = inputElem.getAttribute("name");
        if (name.equals("")) name = "fileContent"; // Lo imponemos temporalmente, es necesario

        code.append("elem.name = \"" + name + "\";\n"); // Es necesario que el input file tenga un nombre definido no vacío para el form, da igual cual sea pues es el único elemento del form, imponiendo uno nosotros temporalmente evitamos que el programador tenga que inventarse uno en el elemento
        code.append("form.submit();\n");
        code.append("elem.name = oldName;\n");  // Restauramos el nombre original (si no existe será una cadena vacía)

        code.append("itsNatDoc.removeChild(form);\n");

        code.append("itsNatDoc.insertBefore(parentNode,elem,elemClone);\n");
        code.append("itsNatDoc.removeChild(elemClone);\n");

        clientDoc.addCodeToSend(code);
    }

    public String getActionURL()
    {
        return "itsNatDoc.getServletPath() + \"?itsnat_action=iframe_file_upload&\" + itsNatDoc.genParamURL() + \"&itsnat_listener_id=" + idObj.getId() + "\"";
        //return "static/file_upload_res.jsp"; // Para pruebas
    }

    public ClientDocument getClientDocument()
    {
        return clientDoc;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return (ItsNatStfulDocumentImpl)comp.getItsNatDocumentImpl();
    }

    public HTMLInputElement getHTMLInputElement()
    {
        return inputElem;
    }

    public ItsNatHTMLIFrame getItsNatHTMLIFrame()
    {
        return comp;
    }

    public String getId()
    {
        return idObj.getId();
    }

    public UniqueId getUniqueId()
    {
        return idObj;
    }

    public boolean isDisposed()
    {
        return disposed;
    }

    public void dispose()
    {
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        synchronized(itsNatDoc) // Sincronizamos pues es apenas el único método que se puede llamar y es llamado sin error durante la carga del archivo
        {
            if (disposed) return;

            HTMLIFrameElement iframeElem = comp.getHTMLIFrameElement();
            DocMutationEventListenerImpl mainListener = clientDoc.getItsNatStfulDocument().getDocMutationEventListener();
            mainListener.removeBeforeAfterMutationRenderListener(iframeElem,this);

            clientDoc.removeHTMLIFrameFileUploadImpl(this);
            comp.removeHTMLIFrameFileUploadImpl(this);

            this.disposed = true;
        }
    }

    public void checkIsAlreadyUsed()
    {
        if (isDisposed()) throw new ItsNatException("File upload processing is disposed");

        // Este chequeo es sobre todo para detectar en tiempo de desarrollo un mal uso
        if (receiving)
            throw new ItsNatException("This " + HTMLIFrameFileUpload.class.getName() + " is already receiving client data",this);
    }

    protected boolean hasItsNatServletRequestListeners()
    {
        if (requestListeners == null)
            return false;
        return !requestListeners.isEmpty();
    }

    protected LinkedList<ItsNatServletRequestListener> getItsNatServletRequestListenerList()
    {
        if (requestListeners == null)
            this.requestListeners = new LinkedList<ItsNatServletRequestListener>();
        return requestListeners;
    }

    protected Iterator<ItsNatServletRequestListener> getItsNatServletRequestListenerIterator()
    {
        // No sincronizamos porque sólo admitimos sólo lectura
        if (requestListeners == null) return null;
        if (requestListeners.isEmpty()) return null;
        return requestListeners.iterator();
    }

    public void dispatchRequestListeners(ItsNatServletResponseImpl response)
    {
        if (isDisposed()) return;

        // Recuerda que este método es llamado SIN SINCRONIZAR EL DOCUMENTO
        this.receiving = true;
        response.dispatchItsNatServletRequestListeners(getItsNatServletRequestListenerIterator());
        dispose(); // Una vez usado no se puede reutilizar
    }

    public void addItsNatServletRequestListener(ItsNatServletRequestListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList requestListeners = getItsNatServletRequestListenerList();
        requestListeners.add(listener);
    }

    public void removeItsNatServletRequestListener(ItsNatServletRequestListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList requestListeners = getItsNatServletRequestListenerList();
        requestListeners.remove(listener);
    }

    public void beforeRender(Node node, MutationEvent evt)
    {
        String type = evt.getType();
        if (type.equals("DOMNodeRemoved"))
        {
            // Aseguramos de esta manera que no siga registrado en el objeto clientDoc
            // pues es una colección de referencias normales
            // No importa que se quite el registro del BeforeAfterMutationRenderListener
            // la iteración soporta modificación concurrente.
            dispose();
        }
    }

    public void afterRender(Node node, MutationEvent evt)
    {
    }

    public FileUploadRequest processFileUploadRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        // Si este método ha sido llamado y isDisposed() es true ha sido por "mala suerte",
        // pues el chequeo del isDisposed() ha sido hecho antes de hacer el dispatch
        // a los listeners.
        // Si provocamos error obligamos al programador a chequear antes de llamar,
        // idem si devolvemos null, no pasa nada si se procesa el archivo aunque
        // el objeto este disposed, viene a ser lo que ocurriría si se hace dispose
        // tras salir de este método, el proceso que hace FileUploadRequestImpl es apenas
        // el de leer la cabecera del request.

        // Sólo se puede llamar una sola vez
        if (processed) throw new ItsNatException("File upload request processing is already executed");
        this.processed = true;
        // No pasamos una referencia al padre (este objeto) por dos razones:
        // 1) No se necesita
        // 2) Así evitamos la tentación de acceder al documento sin sincronizar
        return new FileUploadRequestImpl(request,response);
    }
}
