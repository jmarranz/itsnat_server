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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLAppletElement;
import org.w3c.dom.html.HTMLAreaElement;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLFieldSetElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLLabelElement;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author jmarranz
 */
public class HTMLCollectionImpl extends NodeListBaseImpl implements HTMLCollection
{

    /**
     * Request collection of all anchors in document: &lt;A&gt; elements that
     * have a <code>name</code> attribute.
     */
    public static final short        ANCHOR = 1;


    /**
     * Request collection of all forms in document: &lt;FORM&gt; elements.
     */
    public static final short        FORM = 2;


    /**
     * Request collection of all images in document: &lt;IMAGE&gt; elements.
     */
    public static final short        IMAGE = 3;


    /**
     * Request collection of all Applets in document: &lt;APPLET&gt; and
     * &lt;OBJECT&gt; elements (&lt;OBJECT&gt; must contain an Applet).
     */
    public static final short        APPLET = 4;


    /**
     * Request collection of all links in document: &lt;A&gt; and &lt;AREA&gt;
     * elements (must have a <code>href</code> attribute).
     */
    public static final short        LINK = 5;


    /**
     * Request collection of all options in selection: &lt;OPTION&gt; elments in
     * &lt;SELECT&gt; or &lt;OPTGROUP&gt;.
     * Hacemos búsqueda recursiva porque los <option> pueden estar también
     * bajo <optgroup>
     *
     */
    public static final short        OPTION = 6;


    /**
     * Request collection of all rows in table: &lt;TR&gt; elements in table or
     * table section.
     */
    public static final short        ROW = 7;


    /**
     * Request collection of all form elements: &lt;INPUT&gt;, &lt;BUTTON&gt;,
     * &lt;SELECT&gt;, &lt;TEXT&gt; and &lt;TEXTAREA&gt; elements inside form
     * &lt;FORM&gt;.
     */
    public static final short        FORM_ELEMENT = 8;


    /**
     * Request collection of all areas in map: &lt;AREA&gt; element in &lt;MAP&gt;
     * (non recursive).
     */
    public static final short        AREA = -1;


    /**
     * Request collection of all table bodies in table: &lt;TBODY&gt; element in
     * table &lt;TABLE&gt; (non recursive).
     */
    public static final short        TBODY = -2;


    /**
     * Request collection of all cells in row: &lt;TD&gt; elements in &lt;TR&gt;
     * (non recursive).
     */
    public static final short        CELL = -3;

    protected short type;

    public HTMLCollectionImpl(Element topElement,short type)
    {
        super(topElement);
        this.type = type;
    }

    public boolean isRecursive()
    {
        return type > 0;
    }

    public boolean machElement(Element elem,String name)
    {
        boolean match = false;

        switch (type)
        {
            case ANCHOR:
                // Anchor is an <A> element with a 'name' attribute. Otherwise, it's
                // just a link.
                match = ( elem instanceof HTMLAnchorElement ) &&
                        elem.getAttribute( "name" ).length() > 0;
                break;
            case FORM:
                // Any <FORM> element.
                match = ( elem instanceof HTMLFormElement );
                break;
            case IMAGE:
                // Any <IMG> element. <OBJECT> elements with images are not returned.
                match = ( elem instanceof HTMLImageElement );
                break;
            case APPLET:
                // Any <APPLET> element, and any <OBJECT> element which represents an
                // Applet. This is determined by 'codetype' attribute being
                // 'application/java' or 'classid' attribute starting with 'java:'.
                match = ( elem instanceof HTMLAppletElement ) ||
                        ( elem instanceof HTMLObjectElement &&
                          ( "application/java".equals( elem.getAttribute( "codetype" ) ) ||
                            elem.getAttribute( "classid" ).startsWith( "java:" ) ) );
                break;
            case FORM_ELEMENT:
                // All form elements.
                match = (elem instanceof HTMLButtonElement) ||
                        (elem instanceof HTMLFieldSetElement) ||
                        (elem instanceof HTMLInputElement) ||
                        (elem instanceof HTMLLabelElement) ||
                        (elem instanceof HTMLSelectElement) ||
                        (elem instanceof HTMLTextAreaElement);
                break;
            case LINK:
                // Any <A> element, and any <AREA> elements with an 'href' attribute.
                match = ( ( elem instanceof HTMLAnchorElement ||
                            elem instanceof HTMLAreaElement ) &&
                          elem.getAttribute( "href" ).length() > 0 );
                break;
            case AREA:
                // Any <AREA> element.
                match = ( elem instanceof HTMLAreaElement );
                break;
            case OPTION:
                // Any <OPTION> element.
                match = ( elem instanceof HTMLOptionElement );
                break;
            case ROW:
                // Any <TR> element.
                match = ( elem instanceof HTMLTableRowElement );
                break;
            case TBODY:
                // Any <TBODY> element (one of three table section types).
                match = ( (elem instanceof HTMLTableSectionElement) &&
                          elem.getLocalName().equals( "tbody" ) );
                break;
            case CELL:
                // Any <TD> element.
                match = ( elem instanceof HTMLTableCellElement );
                break;
        }

        // If element type was matched and a name was specified, must also match
        // the name against either the 'id' or the 'name' attribute. The 'name'
        // attribute is relevant only for <A> elements for backward compatibility.
        if ( match && (name != null) )
        {
            // If an anchor and 'name' attribute matches, return true. Otherwise,
            // try 'id' attribute.
            if ( (elem instanceof HTMLAnchorElement) &&
                 name.equals( elem.getAttribute( "name" ) ) )
                return true;
            else if (name.equals( elem.getAttribute( "id" ) ))
                return true;
            else
                return false;
        }

        return match; // Búsqueda sin "name"
    }

    public int getLength()
    {
        return getLength(null);
    }

    public Node item(int index)
    {
        return item(index,null);
    }

    public Node namedItem(String name)
    {
        if (name == null) throw new IllegalArgumentException("name parameter is null");
        return item(0,name);
    }
}
