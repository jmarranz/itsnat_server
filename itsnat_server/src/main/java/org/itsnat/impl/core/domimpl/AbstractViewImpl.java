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

package org.itsnat.impl.core.domimpl;

import org.itsnat.impl.core.domimpl.deleg.DelegateNotDocumentImpl;
import org.itsnat.impl.core.domimpl.deleg.DelegateNodeImpl;
import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.impl.core.domimpl.deleg.DelegateNotDocWithoutParentNodeImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public class AbstractViewImpl implements AbstractView,ItsNatNodeNoChildInternal,Serializable
{
    public final static int ABSTRACT_VIEW = -1; // Un valor que no se confunde con los valores de Node.CONSTANTE, así cualquier comparación con Node.CONSTANTE será fallida
    protected DelegateNotDocumentImpl delegate;
    protected DocumentView document;

    public AbstractViewImpl(DocumentView document)
    {
        this.document = document;
        getDelegateNode();
    }

    public DocumentView getDocument()
    {
        return document;
    }

    public ItsNatDocument getItsNatDocument()
    {
        return getDelegateNode().getItsNatDocument();
    }

    public DelegateNodeImpl getDelegateNode()
    {
        if (delegate == null) this.delegate = new DelegateNotDocWithoutParentNodeImpl(this);
        return delegate;
    }

    public void addEventListenerInternal(String type, EventListener listener, boolean useCapture)
    {
        // No hacemos nada a nivel local, Batik no soporta eventos para el AbstractView
    }

    public void removeEventListenerInternal(String type, EventListener listener, boolean useCapture)
    {
        // No hacemos nada a nivel local, Batik no soporta eventos para el AbstractView
    }

    public boolean isInternalMode()
    {
        return getDelegateNode().isInternalMode();
    }

    public void setInternalMode(boolean mode)
    {
        getDelegateNode().setInternalMode(mode);
    }

    public void fireDOMNodeInsertedIntoDocumentEvent()
    {
        // Nada que hacer pues es una clase totalmente nuestra y es una vista
    }

    public void fireDOMNodeRemovedFromDocumentEvent()
    {
        // Nada que hacer pues es una clase totalmente nuestra y es una vista
    }

    public void fireDOMCharacterDataModifiedEvent(String oldv,String newv)
    {
        // Nada que hacer pues es una clase totalmente nuestra y es una vista
    }

    // Métodos de EventTarget

    public boolean dispatchEvent(Event evt) throws EventException
    {
        DelegateNodeImpl delegate = getDelegateNode();
        if (delegate.isDispatchEventInternal(evt))
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR,"Batik does not support event dispatching in the view");
        else
            return delegate.dispatchEventRemote(evt);
    }

    public void addEventListener(String type, EventListener listener, boolean useCapture)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        if (delegate.isAddRemoveEventListenerInternal())
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR,"Batik does not support event dispatching in the view");
        else
            delegate.addEventListenerRemote(type,listener,useCapture);
    }

    public void removeEventListener(String type, EventListener listener, boolean useCapture)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        if (delegate.isAddRemoveEventListenerInternal())
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR,"Batik does not support event dispatching in the view");
        else
            delegate.removeEventListenerRemote(type,listener,useCapture);
    }

    // Métodos de Node

    public short getNodeType()
    {
        return ABSTRACT_VIEW;
    }

    public void normalize()
    {
    }

    public boolean hasAttributes()
    {
        return false;
    }

    public boolean hasChildNodes()
    {
        return false;
    }

    public String getLocalName()
    {
        return null;
    }

    public String getNamespaceURI()
    {
        return null;
    }

    public String getNodeName()
    {
        return null;
    }

    public String getNodeValue() throws DOMException
    {
        return null;
    }

    public String getPrefix()
    {
        return null;
    }

    public void setNodeValue(String nodeValue) throws DOMException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPrefix(String prefix) throws DOMException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Document getOwnerDocument()
    {
        return (Document)document;
    }

    public NamedNodeMap getAttributes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getFirstChild()
    {
        return null;
    }

    public Node getLastChild()
    {
        return null;
    }

    public Node getNextSibling()
    {
        return null;
    }

    public Node getParentNode()
    {
        return null;
    }

    public Node getPreviousSibling()
    {
        return null;
    }

    public Node cloneNode(boolean deep)
    {
        // No es necesario este método porque la ventana es absurdo clonar,
        // así evitamos que haya dos ventanas con el mismo documento padre
        // pues la idea es que sea un singleton respecto al documento.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NodeList getChildNodes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSupported(String feature, String version)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node appendChild(Node newChild) throws DOMException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node removeChild(Node oldChild) throws DOMException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node insertBefore(Node newChild, Node refChild) throws DOMException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node replaceChild(Node newChild, Node oldChild) throws DOMException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // ItsNatUserData

    public boolean containsUserValueName(String name)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.containsUserValueName(name);
    }

    public Object getUserValue(String name)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.getUserValue(name);
    }

    public Object setUserValue(String name, Object value)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.setUserValue(name,value);
    }

    public Object removeUserValue(String name)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.removeUserValue(name);
    }

    public String[] getUserValueNames()
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.getUserValueNames();
    }

    // Estos métodos son de Node pero nosotros implementamos Node en AbstractView para
    // simplificarnos no para cumplir todo el contrato

    public String getBaseURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public short compareDocumentPosition(Node other) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getTextContent() throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTextContent(String textContent) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSameNode(Node other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String lookupPrefix(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDefaultNamespace(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String lookupNamespaceURI(String prefix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isEqualNode(Node arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getFeature(String feature, String version) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object setUserData(String key, Object data, UserDataHandler handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getUserData(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
