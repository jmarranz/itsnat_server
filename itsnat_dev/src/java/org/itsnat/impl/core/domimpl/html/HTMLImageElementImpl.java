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

import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.w3c.dom.html.HTMLImageElement;

/**
 *
 * @author jmarranz
 */
public class HTMLImageElementImpl extends HTMLElementImpl implements HTMLImageElement
{
    protected HTMLImageElementImpl()
    {
    }

    public HTMLImageElementImpl(String name,DocumentImpl owner)
    {
        super(name,owner);
    }

    protected HTMLElementImpl newHTMLElement()
    {
        return new HTMLImageElementImpl();
    }

    public String getLowSrc()
    {
        return getAttribute( "lowsrc" );
    }

    public void setLowSrc( String lowSrc )
    {
        setAttribute( "lowsrc", lowSrc );
    }

    public String getSrc()
    {
        return getAttribute( "src" );
    }

    public void setSrc( String src )
    {
        setAttribute( "src", src );
    }

    public String getName()
    {
        return getAttribute( "name" );
    }

    public void setName( String name )
    {
        setAttribute( "name", name );
    }

    public String getAlign()
    {
        return getAttribute( "align" );
    }

    public void setAlign( String align )
    {
        setAttribute( "align", align );
    }

    public String getAlt()
    {
        return getAttribute( "alt" );
    }

    public void setAlt( String alt )
    {
        setAttribute( "alt", alt );
    }

    public String getBorder()
    {
        return getAttribute( "border" );
    }

    public void setBorder( String border )
    {
        setAttribute( "border", border );
    }

    public String getHeight()
    {
        return getAttribute( "height" );
    }

    public void setHeight( String height )
    {
        setAttribute( "height", height );
    }

    public String getHspace()
    {
        return getAttribute( "hspace" );
    }

    public void setHspace( String hspace )
    {
        setAttribute( "hspace", hspace );
    }

    public boolean getIsMap()
    {
        return getAttributeBoolean( "ismap" );
    }

    public void setIsMap( boolean isMap )
    {
        setAttributeBoolean( "ismap", isMap );
    }

    public String getLongDesc()
    {
        return getAttribute( "longdesc" );
    }

    public void setLongDesc( String longDesc )
    {
        setAttribute( "longdesc", longDesc );
    }

    public String getUseMap()
    {
        return getAttribute( "usemap" );
    }

    public void setUseMap( String useMap )
    {
        setAttribute( "usemap", useMap );
    }

    public String getVspace()
    {
        return getAttribute( "vspace" );
    }

    public void setVspace( String vspace )
    {
        setAttribute( "vspace", vspace );
    }

    public String getWidth()
    {
        return getAttribute( "width" );
    }

    public void setWidth( String width )
    {
        setAttribute( "width", width );
    }
}
