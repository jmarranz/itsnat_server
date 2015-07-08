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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.batik.dom.AbstractAttr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
    Batik mete la pata haciendo que AbstractDocument.elementsById pueda apuntar a elementos que ya no están
    en el documento (aunque luego el getElementsById sólo pida los del documento)
    y por otra parte via Garbage Collector se eliminan de la colección por el hilo CleanerThread
    pero a través con referencias SoftReference que son más conservadoras.
    El resultado es un elementsById lleno de elementos inútiles temporalmente, que tenemos que evitar que
    se serialicen en el caso de replicación de sesiones.
    El resultado es que el nuevo elementsById al deserializar no contendrá los nodos fuera del documento
    esto es compatible con Batik pues una posible búsqueda de nodos fuera del documento
    puede estar en el método getElementById de SVGSVGElement (implementado en SVGOMSVGElement)
    pero nosotros no usamos la API SVG en el servidor.
    Habría que estudiar el problema de los nodos que no están en el documento
    pero que de alguna forma se han serializado, al insertar de nuevo en el documento
    quizás no se inserten en elementsById

    Gracias a estos cambios no tenemos el dichoso CleanerThread que tantos problemas dio,
    por ejemplo tuvimos que hacer aquel org.apache.batik.util.CleanerThreadFinisher para matarlo cuando
    "se estropeaba" al recargar muchas veces la aplicación en desarrollo, y el caso de GAE que 
    no dejaba crearlo (creando memory leaks)
    Era la gestión del registro de ids el único caso que creaba el hilo indirectamente al crear las
    SoftReference. Hay otros casos de uso de CleanerThread en Batik pero nosotros no los usamos
   (apenas usamos el DOM y sin APIs SVG)


 * @author jmarranz
 */
public class ElementsByIdImpl implements EventListener,Serializable
{
    protected DocumentImpl doc;
    protected Map<String,Object> elementsById;

    public ElementsByIdImpl(DocumentImpl doc)
    {
        this.doc = doc;

        doc.addEventListenerInternal("DOMNodeInserted",this,false);
        doc.addEventListenerInternal("DOMNodeRemoved",this,false);
    }

    public Node getRoot(Node n)
    {
        Node r = n;
        while (n != null) {
            r = n;
            n = n.getParentNode();
        }
        return r;
    }

    public boolean isInsideDoc(Node node)
    {
        return getRoot(node) == doc;
    }

    public Element getElementById(String id)
    {
        return getChildElementById(doc.getDocumentElement(), id);
    }

    public Element getChildElementById(Node requestor, String id)
    {
        if ((id == null) || (id.length()==0)) return null;
        if (elementsById == null) return null;

        Object o = elementsById.get(id);
        if (o == null) return null;

        Node root = getRoot(requestor);
        boolean all = (root == doc);
        if (o instanceof Element)
        {
            Element e = (Element)o;
            if (all || (getRoot(e) == root))
                return e;
        }
        else
        {
            @SuppressWarnings("unchecked")
            List<Element> l = (List<Element>)o;
            for (Element e : l)
            {
                if (all || (getRoot(e) == root))
                    return e;
            }
        }
        return null;
    }

    public void removeIdEntry(Element e, String id)
    {
        removeIdEntry(e,id,true);
    }

    public void removeIdEntry(Element e, String id,boolean checkInDoc)
    {
        if (id == null) return;
        if (elementsById == null) return;

        if (checkInDoc && !isInsideDoc(e))
            return;

        Object o = elementsById.get(id);
        if (o == null) return;

        if (o instanceof Element)
        {
            elementsById.remove(id);
        }
        else
        {
            @SuppressWarnings("unchecked")
            List<Element> l = (List<Element>)o;
            for (Iterator<Element> li = l.iterator(); li.hasNext(); )
            {
                Element currElem = li.next();
                if (e == currElem)
                {
                    li.remove();
                    break;
                }
            }
            if (l.isEmpty())
                elementsById.remove(id);
        }
    }

    public void addIdEntry(Element e, String id)
    {
        addIdEntry(e,id,true);
    }    

    public void addIdEntry(Element e, String id,boolean checkInDoc)
    {
        if (id == null) return;

        if (checkInDoc && !isInsideDoc(e))
            return;

        if (elementsById == null)
            this.elementsById = new HashMap<String,Object>();

        // Add new Id mapping.
        Object o = elementsById.get(id);
        if (o == null)
        {
            elementsById.put(id,e);
        }
        else if (o instanceof Element)
        {
            Element currElem = (Element)o;
            // Create new List for this new element (and current).
            List<Element> l = new ArrayList<Element>(4);
            l.add(currElem);
            l.add(e);
            elementsById.put(id, l);
        }
        else // List
        {
            @SuppressWarnings("unchecked")
            List<Element> l = (List<Element>)o;
            l.add(e);
        }
    }

    public void updateIdEntry(Element e, String oldId, String newId)
    {
        updateIdEntry(e, oldId, newId,true);
    }

    public void updateIdEntry(Element e, String oldId, String newId,boolean checkInDoc)
    {
        if ((oldId == newId) || ((oldId != null) && (oldId.equals(newId))))
            return;

        if (checkInDoc && !isInsideDoc(e))
            return;

        removeIdEntry(e, oldId,false);
        addIdEntry(e, newId,false);
    }

    public void handleEvent(Event evt)
    {
        Node node = (Node)evt.getTarget(); // node es el nuevo o a eliminar

        String type = evt.getType();
        if (type.equals("DOMNodeInserted"))
            processTreeAddRemoveElementsWithId(node,true);
        else // DOMNodeRemoved
            processTreeAddRemoveElementsWithId(node,false);
    }

    protected void processTreeAddRemoveElementsWithId(Node node,boolean add)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE) return;

        Element elem = (Element)node;
        if (elem.hasAttributes())
        {
            NamedNodeMap attributes = elem.getAttributes();
            for(int i = 0; i < attributes.getLength(); i++)
            {
                AbstractAttr attr = (AbstractAttr)attributes.item(i);
                if (attr.isId())
                {
                    if (add)
                        addIdEntry(elem,attr.getValue(),false); // checkInDoc es false porque SABEMOS que está en el documento
                    else
                        removeIdEntry(elem,attr.getValue(),false);   // checkInDoc es false porque SABEMOS que está en el documento (incluso cuando eliminamos porque no se ha eliminado todavía)
                    break;
                }
            }
        }

        Node child = elem.getFirstChild();
        while (child != null)
        {
            processTreeAddRemoveElementsWithId(child,add);
            child = child.getNextSibling();
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        
        doc.addEventListenerInternal("DOMNodeInserted",this,false);
        doc.addEventListenerInternal("DOMNodeRemoved",this,false);
    }
}
