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

import java.util.LinkedList;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableCaptionElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author jmarranz
 */
public class HTMLTableElementImpl extends HTMLElementImpl implements HTMLTableElement
{
    protected HTMLCollection rows;
    protected HTMLCollection bodies;

    protected HTMLTableElementImpl()
    {
    }

    public HTMLTableElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLTableElementImpl();
    }

    public String getAlign()
    {
        return getAttribute( "align" );
    }

    public void setAlign( String align )
    {
        setAttribute( "align", align );
    }

    public String getBgColor()
    {
        return getAttribute( "bgcolor" );
    }

    public void setBgColor( String bgColor )
    {
        setAttribute( "bgcolor", bgColor );
    }

    public String getBorder()
    {
        return getAttribute( "border" );
    }

    public void setBorder( String border )
    {
        setAttribute( "border", border );
    }

    public String getCellPadding()
    {
        return getAttribute( "cellpadding" );
    }

    public void setCellPadding( String cellPadding )
    {
        setAttribute( "cellpadding", cellPadding );
    }

    public String getCellSpacing()
    {
        return getAttribute( "cellspacing" );
    }

    public void setCellSpacing( String cellSpacing )
    {
        setAttribute( "cellspacing", cellSpacing );
    }

    public String getFrame()
    {
        return getAttribute( "frame" );
    }

    public void setFrame( String frame )
    {
        setAttribute( "frame", frame );
    }

    public String getRules()
    {
        return getAttribute( "rules" );
    }

    public void setRules( String rules )
    {
        setAttribute( "rules", rules );
    }

    public String getSummary()
    {
        return getAttribute( "summary" );
    }

    public void setSummary( String summary )
    {
        setAttribute( "summary", summary );
    }

    public String getWidth()
    {
        return getAttribute( "width" );
    }

    public void setWidth( String width )
    {
        setAttribute( "width", width );
    }

    public HTMLTableCaptionElement getCaption()
    {
        Node child = getFirstChild();
        while (child != null)
        {
            if (child instanceof HTMLTableCaptionElement)
                return (HTMLTableCaptionElement)child;
            child = child.getNextSibling();
        }
        return null;
    }

    public void setCaption( HTMLTableCaptionElement caption )
    {
        // La idea es que si ya existe lo reemplazamos en la misma exacta
        // posición incluso respecto a blancos y comentarios
        Node current = getCaption();
        if (current != null)
        {
            if (current == caption) return; // Nada que hacer
            Node next = current.getNextSibling(); // puede ser null (es el caption el último)
            removeChild(current);
            insertBefore(caption, next);
        }
        else insertBefore(caption,getFirstChild());
    }

    public HTMLElement createCaption()
    {
        HTMLTableCaptionElement caption = getCaption();
        if (caption != null)
            return caption;
        caption = (HTMLTableCaptionElement)getOwnerDocument().createElementNS(NamespaceUtil.XHTML_NAMESPACE,"caption");
        setCaption(caption);
        return caption;
    }

    public void deleteCaption()
    {
        HTMLTableCaptionElement caption = getCaption();
        if (caption != null)
            removeChild(caption);
    }

    public HTMLTableSectionElement getTableSection(String localName)
    {
        Node child = getFirstChild();
        while (child != null)
        {
            if ((child instanceof HTMLTableSectionElement) &&
                 child.getLocalName().equals( localName ) )
                return (HTMLTableSectionElement)child;
            child = child.getNextSibling();
        }
        return null;
    }

    public void deleteTableSection(String localName)
    {
        HTMLTableSectionElement section = getTableSection(localName);
        if (section != null)
            removeChild( section );
    }

    public void setTableSection(HTMLTableSectionElement section,String localName )
    {
        if ((section != null) && !section.getLocalName().equals(localName))
            throw new IllegalArgumentException( "Argument is not an element of type <" + localName + ">." );

        Node current = getTableSection(localName);
        if (current != null)
        {
            if (current == section) return; // Nada que hacer
            // La idea es que si ya existe lo reemplazamos en la misma exacta
            // posición incluso respecto a blancos y comentarios
            Node next = current.getNextSibling(); // puede ser null (es el último)
            removeChild(current);
            insertBefore(section, next);
        }
        else
        {
            if (localName.equals("thead"))
            {
                // <thead> está antes que <tfoot>
                HTMLTableSectionElement tfoot = getTFoot();
                if (tfoot != null)
                {
                    insertBefore(section,tfoot);
                }
                else
                {
                    // O al menos antes del primer <tbody>
                    HTMLTableSectionElement tbody = getTableSection("tbody");
                    if (tbody != null)
                        insertBefore(section,tbody);
                    else
                        appendChild(section); // Al final entonces
                }
            }
            else if (localName.equals("tfoot"))
            {
                // <tfoot> está antes que el primer <tbody>
                HTMLTableSectionElement tbody = getTableSection("tbody");
                if (tbody != null)
                    insertBefore(section,tbody);
                else
                    appendChild(section); // Al final entonces
            }
        }
    }

    public HTMLTableSectionElement createTableSection(String localName)
    {
        HTMLTableSectionElement section = getTableSection(localName);
        if (section != null)
            return section;
        section = (HTMLTableSectionElement)getOwnerDocument().createElementNS(NamespaceUtil.XHTML_NAMESPACE,localName);
        setTableSection(section,localName);
        return section;
    }

    public HTMLTableSectionElement getTHead()
    {
        return getTableSection("thead");
    }

    public void setTHead( HTMLTableSectionElement tHead )
    {
        setTableSection(tHead,"thead");
    }

    public HTMLElement createTHead()
    {
        return createTableSection("thead");
    }

    public void deleteTHead()
    {
        deleteTableSection("thead");
    }

    public HTMLTableSectionElement getTFoot()
    {
        return getTableSection("tfood");
    }

    public void setTFoot( HTMLTableSectionElement tFoot )
    {
        setTableSection(tFoot,"tfood");
    }

    public HTMLElement createTFoot()
    {
        return createTableSection("tfood");
    }

    public void deleteTFoot()
    {
        deleteTableSection("tfood");
    }

    public HTMLCollection getRows()
    {
        if (rows == null)
            rows = new HTMLCollectionImpl( this, HTMLCollectionImpl.ROW );
        return rows;
    }

    public HTMLCollection getTBodies()
    {
        if (bodies == null)
            bodies = new HTMLCollectionImpl( this, HTMLCollectionImpl.TBODY );
        return bodies;
    }

    public static LinkedList getRowsArray(HTMLTableElement table)
    {
        // Puede haber varios <thead> <tfoot> y <tbody> dentro del table padre
        LinkedList total = null;
        Element section = ItsNatTreeWalker.getFirstChildElement(table);
        while(section != null)
        {
            if (section instanceof HTMLTableSectionElement)
            {
                LinkedList rows = DOMUtilInternal.getChildElementListWithTagNameNS(section,NamespaceUtil.XHTML_NAMESPACE,"tr",false);
                if (rows != null)
                {
                    if (total == null) total = new LinkedList();
                    total.addAll(rows);
                }
            }
            section = ItsNatTreeWalker.getNextSiblingElement(section);
        }
        return total;
    }

    public LinkedList getRowsArray()
    {
        return getRowsArray(this);
    }

    public HTMLTableRowElement getHTMLTableRowElement(int index)
    {
        return (HTMLTableRowElement)DOMUtilInternal.getChildElementWithTagNameNS(this,NamespaceUtil.XHTML_NAMESPACE,"tr",index); // Puede ser null (no hay)
    }

    public HTMLElement insertRow(int index)
    {
        if (index < 0) throw new DOMException(DOMException.INDEX_SIZE_ERR,"Index is negative");

        HTMLTableRowElement newRow = (HTMLTableRowElement)getOwnerDocument().createElementNS(NamespaceUtil.XHTML_NAMESPACE,"tr");
        LinkedList rows = getRowsArray();
        if (rows != null)
        {
            int len = rows.size();
            if (index > len) throw new DOMException(DOMException.INDEX_SIZE_ERR,"Index " + index + " is greater than the number of rows: " + len);
            else if (index < len)
            {
                HTMLTableRowElement rowRef = (HTMLTableRowElement)rows.get(index);
                rowRef.getParentNode().insertBefore(newRow,rowRef);
            }
            else // index == len  Añadir al final
            {
                HTMLTableSectionElement tbody = (HTMLTableSectionElement)getTBodies().item(0);
                if (tbody == null)
                {
                    tbody = (HTMLTableSectionElement)getOwnerDocument().createElementNS(NamespaceUtil.XHTML_NAMESPACE,"tbody");
                    appendChild(tbody);
                }
                tbody.appendChild(newRow);
            }
        }
        else if (index > 0) throw new DOMException(DOMException.INDEX_SIZE_ERR,"Index " + index + " is greater than the number of rows: 0");
        else
        {
            HTMLTableSectionElement tbody = (HTMLTableSectionElement)getTBodies().item(0);
            if (tbody == null)
            {
                tbody = (HTMLTableSectionElement)getOwnerDocument().createElementNS(NamespaceUtil.XHTML_NAMESPACE,"tbody");
                appendChild(tbody);
            }
            tbody.appendChild(newRow);
        }
        return newRow;
    }

    public void deleteRow(int index)
    {
        LinkedList rows = getRowsArray();
        deleteElement(index,rows);
    }
}
