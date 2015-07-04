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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.GenericElementNS;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.impl.core.css.CSS2PropertiesImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public abstract class ElementNSImpl extends GenericElementNS
        implements ItsNatElementInternal
{
    protected CSS2PropertiesImpl styleDec;
    protected DelegateNotDocumentImpl delegate;

    public ElementNSImpl()
    {
        getDelegateNode();
    }

    public ElementNSImpl(String nsURI,String name,AbstractDocument owner)
    {
        super(nsURI, name, owner);
        getDelegateNode();
    }

    public abstract DelegateNotDocumentImpl createDelegateNotDocument();


    public DelegateNodeImpl getDelegateNode()
    {
        if (delegate == null) this.delegate = createDelegateNotDocument();
        return delegate;
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        // Estos métodos writeObject/readObject son innecesarios
        // la verdad es que los pongo para poder añadir este comentario

        // AbstractElement tiene un atributo transient liveAttributeValues
        // pero sólo tiene utilidad en la API SVG DOM de Batik (no usada)

        // AbstractNode tiene un atributo transient eventSupport
        // al añadir un EventListener interno automáticamente se inicializa
        // e ItsNat trata de restaurar los EventListener internos usados
        // al de-serializar

        in.defaultReadObject();
    }

    public CSSStyleDeclaration getStyle()
    {
        // Es muy conveniente memorizar este objeto porque el parseo es una tarea lenta.
        if (styleDec == null)
            this.styleDec = new CSS2PropertiesImpl(this); // Pasamos el wrapper para evitar tener una referencia fuerte al elemento original
        return styleDec;
    }

    public ItsNatDocument getItsNatDocument()
    {
        return getDelegateNode().getItsNatDocument();
    }

    public void addEventListenerInternal(String type, EventListener listener, boolean useCapture)
    {
        super.addEventListener(type,listener,useCapture);
    }

    public void removeEventListenerInternal(String type, EventListener listener, boolean useCapture)
    {
        super.removeEventListener(type,listener,useCapture);
    }

    public boolean isInternalMode()
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.isInternalMode();
    }

    public void setInternalMode(boolean mode)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setInternalMode(mode);
    }

    @Override
    public void fireDOMSubtreeModifiedEvent()
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMSubtreeModifiedEvent();
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public void fireDOMNodeInsertedEvent(Node node)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMNodeInsertedEvent(node);
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public void fireDOMNodeRemovedEvent(Node node)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMNodeRemovedEvent(node);
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public void fireDOMNodeInsertedIntoDocumentEvent()
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMNodeInsertedIntoDocumentEvent();
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public void fireDOMNodeRemovedFromDocumentEvent()
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMNodeRemovedFromDocumentEvent();
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public void fireDOMCharacterDataModifiedEvent(String oldv,String newv)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMCharacterDataModifiedEvent(oldv,newv);
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public void fireDOMAttrModifiedEvent(String name, Attr node, String oldv,
                                         String newv, short change)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMAttrModifiedEvent(name,node,oldv,newv,change);
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    // Métodos de EventTarget

    @Override
    public boolean dispatchEvent(Event evt) throws EventException
    {
        DelegateNodeImpl delegate = getDelegateNode();
        if (delegate.isDispatchEventInternal(evt))
            return super.dispatchEvent(evt);
        else
            return delegate.dispatchEventRemote(evt);
    }

    @Override
    public void addEventListener(String type, EventListener listener, boolean useCapture)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        if (delegate.isAddRemoveEventListenerInternal())
            addEventListenerInternal(type,listener,useCapture);
        else
            delegate.addEventListenerRemote(type,listener,useCapture);
    }

    @Override
    public void removeEventListener(String type, EventListener listener, boolean useCapture)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        if (delegate.isAddRemoveEventListenerInternal())
            removeEventListenerInternal(type,listener,useCapture);
        else
            delegate.removeEventListenerRemote(type,listener,useCapture);
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
}
