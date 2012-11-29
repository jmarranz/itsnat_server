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

package org.itsnat.impl.core.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.RGBColor;
import org.itsnat.impl.core.css.lex.Comma;
import org.itsnat.impl.core.css.lex.HexNumber;
import org.itsnat.impl.core.css.lex.SourceCode;

/**
 *
 * @author jmarranz
 */
public class RGBColorImpl extends CSSPrimitiveValueLiteralImpl implements RGBColor,ObjectValueParent
{
    protected CSSPrimitiveValueImpl parent;
    protected CSSPrimitiveValueImpl red;
    protected CSSPrimitiveValueImpl green;
    protected CSSPrimitiveValueImpl blue;

    /** Creates a new instance of RGBColorImpl */
    public RGBColorImpl(SourceCode cssTextCode,CSSPrimitiveValueImpl parent)
    {
        this.parent = parent;
        SourceCode[] colorList = cssTextCode.split(Comma.getSingleton());
        if (colorList.length != 3)
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"RGBColor syntax error, property: " + getPropertyName() + " value: " + cssTextCode.toString());

        createColors(colorList);
    }

    public RGBColorImpl(HexNumber token,CSSPrimitiveValueImpl parent)
    {
        this.parent = parent;
        String color = token.toString();
        SourceCode[] colorList = new SourceCode[3];
        if (color.length() == 4) // #rgb format
        {
            char r = color.charAt(1);
            colorList[0] = SourceCode.newSourceCode(Integer.toString(HexNumber.toIntFromHex(r)));

            char g = color.charAt(1);
            colorList[1] = SourceCode.newSourceCode(Integer.toString(HexNumber.toIntFromHex(g)));

            char b = color.charAt(2);
            colorList[2] = SourceCode.newSourceCode(Integer.toString(HexNumber.toIntFromHex(b)));
        }
        else if (color.length() == 7) // #rrggbb format
        {
            String r = color.substring(1,3);
            colorList[0] = SourceCode.newSourceCode(Integer.toString(HexNumber.toIntFromHex(r)));

            String g = color.substring(3,5);
            colorList[1] = SourceCode.newSourceCode(Integer.toString(HexNumber.toIntFromHex(g)));

            String b = color.substring(5,7);
            colorList[2] = SourceCode.newSourceCode(Integer.toString(HexNumber.toIntFromHex(b)));
        }
        else
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"RGBColor syntax error, property: " + getPropertyName() + " value: " + color);

        createColors(colorList);
    }

    public int getCode()
    {
        return RGB;
    }

    private void createColors(SourceCode[] colorList)
    {
        this.red = new CSSPrimitiveValueImpl(colorList[0],  0,this);
        this.green = new CSSPrimitiveValueImpl(colorList[1],1,this);
        this.blue = new CSSPrimitiveValueImpl(colorList[2], 2,this);
    }

    public CSSPrimitiveValue getRed()
    {
        return red;
    }

    public CSSPrimitiveValue getGreen()
    {
        return green;
    }

    public CSSPrimitiveValue getBlue()
    {
        return blue;
    }

    public String getPropertyName()
    {
        return parent.getCSSValueParent().getPropertyName();
    }

    public Object getUpdatedChildObjectValueFromElement(Object requester,int requesterCode)
    {
        RGBColor current = (RGBColor)parent.getUpdatedChildObjectValueFromElement(this,getCode());
        if (current != this)
        {
            if (requesterCode == 0)
                return current.getRed();
            else if (requesterCode == 1)
                return current.getGreen();
            else if (requesterCode == 2)
                return current.getBlue();

            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"Internal Error");
        }
        else
            return requester;
    }

    public void notifyToElementChangedCSSText(SourceCode cssTextCode, Object requester)
    {
        // Una de las partes se ha actualizado (da igual cual sea), hay que notificar el cambio
        // de toda la estructura RGB.
        String cssTextRGB = "rgb(" + red.getCssTextSourceCode(false) + "," + green.getCssTextSourceCode(false) + "," + blue.getCssTextSourceCode(false) + ")";
        SourceCode cssTextRGBCode = new SourceCode(cssTextRGB);
        parent.setCssTextSourceCode(cssTextRGBCode,true);
    }

}
