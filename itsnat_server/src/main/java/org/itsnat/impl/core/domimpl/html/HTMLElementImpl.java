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

package org.itsnat.impl.core.domimpl.html;

import org.itsnat.impl.core.domimpl.deleg.DelegateHTMLElementImpl;
import java.util.LinkedList;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.itsnat.impl.core.domimpl.ElementNSImpl;
import org.itsnat.impl.core.domimpl.deleg.DelegateNotDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;

/**
 * Derivamos de ElementNSImpl y no de ElementNS porque hay que apostar
 * lo más posible por XHTML NO por HTML. Por ejemplo en los navegadores
 * WebKit (probado con Safari 3.? y Chrome 1.?) los elementos de una página HTML
 * (no XHTML) tienen namespace de XHTML.
 *
 * La diferencia entre un elemento de un documento HTML/XHTML y un elemento
 * XHTML embebido en otro namespace, es que el primero implementa
 * las interfaces HTMLElement y el documento padre es un HTMLDocument
 * pero no debería haber más diferencias.
 *
 * @author jmarranz
 */
public abstract class HTMLElementImpl extends ElementNSImpl implements HTMLElement
{
    protected HTMLElementImpl()
    {
    }

    public HTMLElementImpl(String name, DocumentImpl owner)
    {
        super(NamespaceUtil.XHTML_NAMESPACE,name, owner);
    }

    @Override
    public Node newNode()
    {
        return newHTMLElement();
    }

    protected abstract HTMLElementImpl newHTMLElement();

    public DelegateNotDocumentImpl createDelegateNotDocument()
    {
        return new DelegateHTMLElementImpl(this);
    }

    public DelegateHTMLElementImpl getDelegateHTMLElement()
    {
        return (DelegateHTMLElementImpl)getDelegateNode();
    }

    public String getClassName()
    {
        return getAttribute("class");
    }

    public void setClassName(String className)
    {
        setAttribute("class",className);
    }

    public String getDir()
    {
        return getAttribute("dir");
    }

    public void setDir(String dir)
    {
        setAttribute("dir",dir);
    }

    public String getLang()
    {
        return getAttribute("lang");
    }

    public void setLang(String lang)
    {
        setAttribute("lang",lang);
    }

    public String getTitle()
    {
        return getAttribute("title");
    }

    public void setTitle(String title)
    {
        setAttribute("title",title);
    }

    @Override
    public String getId()
    {
        // getId() ya está definido en una clase más arriba, ponemos esto por simetría
        return super.getId();
    }

    public void setId(String id)
    {
        setAttribute( "id", id );
    }


    public HTMLFormElement getFormBase()
    {
        // Para las clases derivadas que lo necesiten
        Node parent = getParentNode();
        while(parent != null)
        {
            if (parent instanceof HTMLFormElement)
                return (HTMLFormElement)parent;
            parent = parent.getParentNode();
        }
        return null;
    }

    @Override
    public String getTextContent()
    {
        // Consideramos que puede haber comentarios (raro)
        // o bien varios nodos de texto seguidos
        StringBuilder text = new StringBuilder();
        Node child = getFirstChild();
        while ( child != null )
        {
            if ( child instanceof Text ) {
                text.append(( (Text) child ).getData());
            }
            child = child.getNextSibling();
        }
        return text.toString();
    }

    @Override
    public void setTextContent( String text )
    {
        Node child = getFirstChild();
        if ((child != null)&&(child instanceof Text))
            ((Text)child).setData(text);
        else
        {
            if (child != null) // lo que hay no es un Text node
            {
                do
                {
                    Node next = child.getNextSibling();
                    removeChild( child );
                    child = next;
                }
                while ( child != null );
            }
            appendChild(getOwnerDocument().createTextNode( text ));
        }
    }

    public LinkedList<Node> getChildrenArray(String localName,boolean recursive)
    {
        return DOMUtilInternal.getChildElementListWithTagNameNS(this,NamespaceUtil.XHTML_NAMESPACE,localName,recursive); // Puede ser null (no hay)
    }

    public HTMLElement insertElement(int index,String localName,boolean recursive)
    {
        return insertElement(index,localName,recursive,null);
    }

    public HTMLElement insertElement(int index,String localName,LinkedList<Node> children)
    {
        if (children == null) throw new ItsNatException("INTERNAL ERROR");
        return insertElement(index,localName,true,children); // El valor de recursive da igual no se usará
    }

    private HTMLElement insertElement(int index,String localName,boolean recursive,LinkedList<Node> children)
    {
        if (index < 0) throw new DOMException(DOMException.INDEX_SIZE_ERR,"Index is negative");
        HTMLElement newElem = (HTMLElement)getOwnerDocument().createElementNS(NamespaceUtil.XHTML_NAMESPACE,localName);
        if (children == null) children = getChildrenArray(localName,recursive);
        if (children != null)
        {
            if (index > children.size()) throw new DOMException(DOMException.INDEX_SIZE_ERR,"Index " + index + " is greater than the number of rows: " + children.size());
            HTMLElement elemRef = (HTMLElement)children.get(index);
            insertBefore(newElem,elemRef); // elemRef puede ser null (añadir al final)
        }
        else  // No hay elementos
        {
            appendChild(newElem);
        }
        return newElem;
    }

    public void deleteElement(int index,LinkedList<Node> children)
    {
        if (index < 0) throw new DOMException(DOMException.INDEX_SIZE_ERR,"Index is negative");
        if (children != null)
        {
            if (index >= children.size()) throw new DOMException(DOMException.INDEX_SIZE_ERR,"Index " + index + " is equal or greater than the number of rows: " + children.size());
            HTMLElement elem = (HTMLElement)children.get(index);
            removeChild(elem);
        }
        else throw new DOMException(DOMException.INDEX_SIZE_ERR,"Index " + index + " is equal or greater than the number of rows: 0");
    }

    public void deleteElement(int index,String localName,boolean recursive)
    {
        LinkedList<Node> children = getChildrenArray(localName,recursive);
        deleteElement(index,children);
    }

    public void methodCallNoParam(String methodName)
    {
        if (!delegate.isInternalMode())
            getDelegateHTMLElement().methodCallNoParam(methodName);

        // En caso contrario no hacer nada
    }

    public boolean getAttributeBoolean(String name)
    {
        return hasAttribute( name );
    }

    public void setAttributeBoolean(String name, boolean value )
    {
        setAttributeBoolean(this,name,value);
    }

    public static void setAttributeBoolean(Element elem,String name, boolean value )
    {
        if (value) elem.setAttribute(name,name);
        else elem.removeAttribute(name);
    }
}
