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

import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author jmarranz
 */
public class HTMLTableRowElementImpl extends HTMLElementImpl implements HTMLTableRowElement
{
    protected HTMLCollection cells;

    protected HTMLTableRowElementImpl()
    {
    }

    public HTMLTableRowElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLTableRowElementImpl();
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

    public String getCh()
    {
        return getAttribute( "char" );
    }

    public void setCh( String ch )
    {
        setAttribute( "char", ch );
    }

    public String getChOff()
    {
        return getAttribute( "charoff" );
    }

    public void setChOff( String chOff )
    {
        setAttribute( "charoff", chOff );
    }

    public String getVAlign()
    {
        return getAttribute( "valign" );
    }

    public void setVAlign( String vAlign )
    {
        setAttribute( "valign", vAlign );
    }

    public int getRowIndex()
    {
        // No admitimos que un <row> esté directamente bajo <table>
        HTMLTableSectionElement section = (HTMLTableSectionElement)getParentNode();
        HTMLTableElement table = (HTMLTableElement)section.getParentNode();
        LinkedList<Node> rows = HTMLTableElementImpl.getRowsArray(table);
        // rows NO puede ser nulo pues está dentro este <row>
        int i = 0;
        for(Iterator<Node> it = rows.iterator(); it.hasNext(); i++)
            if (it.next() == this) return i;
        return -1;
    }

    // Incluido recientemente en Xerces, no lo soportamos
    // http://xerces.apache.org/xerces2-j/javadocs/api/org/w3c/dom/html/HTMLTableRowElement.html
    public void setRowIndex(int rowIndex)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getSectionRowIndex()
    {
        // No admitimos que un <row> esté directamente bajo <table>
        HTMLTableSectionElement section = (HTMLTableSectionElement)getParentNode();
        LinkedList<Node> rows = DOMUtilInternal.getChildElementListWithTagNameNS(section,NamespaceUtil.XHTML_NAMESPACE,"tr",false);
        // rows NO puede ser nulo pues está dentro este <row>
        int i = 0;
        for(Iterator<Node> it = rows.iterator(); it.hasNext(); i++)
        {
            if (it.next() == this) return i;
        }
        
        return -1;
    }

    // Incluido recientemente en Xerces, no lo soportamos
    // http://xerces.apache.org/xerces2-j/javadocs/api/org/w3c/dom/html/HTMLTableRowElement.html
    public void setSectionRowIndex(int sectionRowIndex)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }    

    public HTMLCollection  getCells()
    {
        if (cells == null)
            cells = new HTMLCollectionImpl( this, HTMLCollectionImpl.CELL );
        return cells;
    }

    // Incluido recientemente en Xerces, no lo soportamos
    // http://xerces.apache.org/xerces2-j/javadocs/api/org/w3c/dom/html/HTMLTableRowElement.html
    public void setCells(HTMLCollection cells)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public LinkedList<Node> getCellsArray()
    {
        // Lo normal es que las celdas TH estén ANTES que las TD
        LinkedList<Node> thCells = getChildrenArray("th",false); // Puede ser null (no hay)
        LinkedList<Node> tdCells = getChildrenArray("td",false); // Puede ser null (no hay)
        LinkedList<Node> cells;
        if ((thCells != null)&&(tdCells != null))
        {
            cells = new LinkedList<Node>();
            cells.addAll(thCells);
            cells.addAll(tdCells);
        }
        else if (thCells != null)
            cells = thCells;
        else if (tdCells != null)
            cells = tdCells;
        else
            cells = null;
        return cells;
    }

    public HTMLElement insertCell( int index )
    {
        LinkedList<Node> cells = getCellsArray();
        return insertElement(index, "td", cells);
    }

    public void deleteCell( int index )
    {
        LinkedList<Node> cells = getCellsArray();
        deleteElement(index,cells);
    }
}
