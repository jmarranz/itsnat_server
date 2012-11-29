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
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;
import org.itsnat.impl.core.css.lex.FloatNumber;
import org.itsnat.impl.core.css.lex.HexNumber;
import org.itsnat.impl.core.css.lex.Identifier;
import org.itsnat.impl.core.css.lex.ParenthesisBlock;
import org.itsnat.impl.core.css.lex.Percent;
import org.itsnat.impl.core.css.lex.SourceCode;
import org.itsnat.impl.core.css.lex.StringDoubleQuote;
import org.itsnat.impl.core.css.lex.StringSimpleQuote;
import org.itsnat.impl.core.css.lex.Token;

/**
 * http://www.w3.org/TR/DOM-Level-2-Style/css.html#CSS-CSSPrimitiveValue
 * http://www.w3.org/TR/REC-CSS2/grammar.html
 * http://www.w3.org/TR/REC-CSS2/syndata.html
 * http://www.princexml.com/doc/properties/
 *
 * @author jmarranz
 */
public class CSSPrimitiveValueImpl extends CSSValueImpl implements CSSPrimitiveValue
{
    protected short primitiveType = -1;
    protected float floatValue;
    protected Counter counterValue;
    protected Rect rectValue;
    protected RGBColor rgbValue;

    /** Creates a new instance of CSSPrimitiveValueImpl */
    public CSSPrimitiveValueImpl(SourceCode cssTextCode,int code,ObjectValueParent parent)
    {
        super(cssTextCode,code,parent);
        rebuild(cssTextCode);
    }

    public static String floatUnitToString(short unitType)
    {
        switch(unitType)
        {
            case CSS_PERCENTAGE:
                return "%";
            case CSS_S:
                return "s";
            case CSS_EMS:
                return "em";
            case CSS_EXS:
                return "ex";
            case CSS_PX:
                return "px";
            case CSS_CM:
                return "cm";
            case CSS_MM:
                return "mm";
            case CSS_IN:
                return "in";
            case CSS_PT:
                return "pt";
            case CSS_PC:
                return "pc";
            case CSS_MS:
                return "ms";
            case CSS_HZ:
                return "hz";
            case CSS_DEG:
                return "deg";
            case CSS_RAD:
                return "rad";
            case CSS_KHZ:
                return "khz";
            case CSS_GRAD:
                return "grad";
            case CSS_NUMBER:
                return "";
            case CSS_DIMENSION:
                return "";  // Precisamente por ser desconocida no podemos saber su nombre
        }

        throw new DOMException(DOMException.INVALID_ACCESS_ERR,"Invalid unit type: " + unitType);
    }

    public boolean setFloatValueInternal(SourceCode cssTextCode)
    {
        // Se supone que hay almenos un token
        Token token0 = cssTextCode.getToken(0);
        if (!(token0 instanceof FloatNumber))
            return false;

        FloatNumber tokenNumber = (FloatNumber)token0;
        float floatValue = tokenNumber.getFloat();
        short unitType = -1;

        if (cssTextCode.tokenCount() > 2)
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS: number format error: " + cssTextCode.toString());

        if (cssTextCode.tokenCount() == 1)
        {
            // No tiene sufijo
            unitType = CSS_NUMBER;
        }
        else
        {
            // Tiene sufijo
            Token token1 = cssTextCode.getToken(1);
            if (!(token1 instanceof Identifier) &&  !(token1 instanceof Percent))
                throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS: expected a unit identifier: " + cssTextCode.toString());

            String suffix = token1.toString();
            suffix = suffix.toLowerCase();

            if ("%".equals(suffix))
            {
                unitType = CSS_PERCENTAGE;
            }
            else
            {

                if ("s".equals(suffix))
                {
                    unitType = CSS_PERCENTAGE;
                }
                else if ("em".equals(suffix))
                {
                    unitType = CSS_EMS;
                }
                else if ("ex".equals(suffix))
                {
                    unitType = CSS_EXS;
                }
                else if ("px".equals(suffix))
                {
                    unitType = CSS_PX;
                }
                else if ("cm".equals(suffix))
                {
                    unitType = CSS_CM;
                }
                else if ("mm".equals(suffix))
                {
                    unitType = CSS_MM;
                }
                else if ("in".equals(suffix))
                {
                    unitType = CSS_IN;
                }
                else if ("pt".equals(suffix))
                {
                    unitType = CSS_PT;
                }
                else if ("pc".equals(suffix))
                {
                    unitType = CSS_PC;
                }
                else if ("ms".equals(suffix))
                {
                    unitType = CSS_MS;
                }
                else if ("hz".equals(suffix))
                {
                    unitType = CSS_HZ;
                }
                else if ("deg".equals(suffix))
                {
                    unitType = CSS_DEG;
                }
                else if ("rad".equals(suffix))
                {
                    unitType = CSS_RAD;
                }
                else if ("khz".equals(suffix))
                {
                    unitType = CSS_KHZ;
                }
                else if ("grad".equals(suffix))
                {
                    unitType = CSS_GRAD;
                }
                else // Unidad desconocida, es el caso CSS_DIMENSION
                {
                    unitType = CSS_DIMENSION;
                }
            }
        }

        this.primitiveType = unitType;
        this.floatValue = floatValue;

        return true;
    }

    public boolean setFunctionBasedValueInternal(SourceCode cssTextCode)
    {
        if (cssTextCode.tokenCount() != 2)
            return false;

        Token token0 = cssTextCode.getToken(0);
        if (!(token0 instanceof Identifier))
            return false;
        Identifier tokIdent = (Identifier)token0;

        Token token1 = cssTextCode.getToken(1);
        if (!(token1 instanceof ParenthesisBlock))
            return false;
        ParenthesisBlock tokBlock = (ParenthesisBlock)token1;

        String identif = tokIdent.toString();
        identif = identif.toLowerCase();

        if (identif.equals("url"))
        {
            this.primitiveType = CSS_URI;
            // No hay ningún objeto especial, el url queda almacenado en this.cssTextCode
            return true;
        }
        else if (identif.equals("rect"))
        {
            this.primitiveType = CSS_RECT;
            this.rectValue = new RectImpl(tokBlock.getContent(),this);
            return true;
        }
        else if (identif.equals("rgb"))
        {
            // FALTA procesar los valores #rgb y #rrggbb
            this.primitiveType = CSS_RGBCOLOR;
            this.rgbValue = new RGBColorImpl(tokBlock.getContent(),this);
            return true;
        }
        else if (identif.equals("attr"))
        {
            this.primitiveType = CSS_ATTR;
            // No hay ningún objeto especial, el attr queda almacenado en this.cssTextCode
            return true;
        }
        else if (identif.equals("counter"))
        {
            // Es un CSS_COUNTER pero no lo soportamos
            this.primitiveType = CSS_UNKNOWN;
            // el valor queda almacenado en this.cssTextCode
            return true;
        }
        else if (identif.equals("counters"))
        {
            // Es un CSS_COUNTER pero no lo soportamos
            this.primitiveType = CSS_UNKNOWN;
            // el valor queda almacenado en this.cssTextCode
            return true;
        }

        return false;
    }

    public void rebuild(SourceCode cssTextCode)
    {
        cssTextCode = cssTextCode.trim();

        if (cssTextCode.tokenCount() == 0)
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS value is not specified, property: " + parent.getPropertyName() + " value: " + this.cssTextCode.toString());

        if (setFloatValueInternal(cssTextCode))
            return;

        // No es un número ni un número + sufijo
        if (setFunctionBasedValueInternal(cssTextCode))
            return;

        if (cssTextCode.tokenCount() == 1)
        {
            Token token = cssTextCode.getToken(0);
            if ((token instanceof StringDoubleQuote) ||
                (token instanceof StringSimpleQuote))
            {
                this.primitiveType = CSS_STRING;
                return;
            }
            else if (token instanceof Identifier)
            {
                this.primitiveType = CSS_IDENT;
                return;
            }
            else if (token instanceof HexNumber)
            {
                // Casos #rgb y #rrggbb
                this.primitiveType = CSS_RGBCOLOR;
                this.rgbValue = new RGBColorImpl((HexNumber)token,this);
                return;
            }
            else
            {
                this.primitiveType = CSS_UNKNOWN;
                return;
            }
        }
        else
        {
            this.primitiveType = CSS_UNKNOWN;
            return;
        }
    }

    public short getCssValueType()
    {
        return CSS_PRIMITIVE_VALUE;
    }

    public short getPrimitiveType()
    {
        return primitiveType;
    }

    public float getFloatValue(short unitType) throws DOMException
    {
        if (this.primitiveType != unitType)
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"Unit conversions is not supported");

        getCssText(); // Verifica que no ha cambiado indirectamente

        return floatValue;
    }

    public void setFloatValue(short unitType, float floatValue) throws DOMException
    {
        String suffix = floatUnitToString(unitType);
        String cssText = Float.toString(floatValue) + suffix;
        setCssTextSourceCode(new SourceCode(cssText),false);

        this.primitiveType = unitType;
        this.floatValue = floatValue;
    }

    public static boolean isStringBasedType(short primitiveType)
    {
        return ((primitiveType == CSS_STRING) ||
                (primitiveType == CSS_URI) ||
                (primitiveType == CSS_IDENT) ||
                (primitiveType == CSS_ATTR));
    }

    public String getStringValue() throws DOMException
    {
        if (!isStringBasedType(this.primitiveType))
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS value doesn't contain a string");

        String str = getCssText();
        // Por si la cadena ha sido definida via setCssTextSourceCode
        str = str.trim();
        if (primitiveType == CSS_STRING)
        {
            // Quitamos las comillas " o ' (es obligatorio que estén)
            str = str.substring(1,str.length() - 1);
        }

        return str;
    }

    public void setStringValue(short stringType, String stringValue) throws DOMException
    {
        if (!isStringBasedType(this.primitiveType))
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS value doesn't contain a string");
        if (!isStringBasedType(stringType))
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"New CSS value doesn't contain a string, invalid string type");

        stringValue = stringValue.trim();
        if (primitiveType == CSS_STRING)
        {
            // Añadimos los literales (es obligatorio que estén)
            stringValue = '"' + stringValue + '"';
        }

        setCssTextSourceCode(new SourceCode(stringValue),false);
        this.primitiveType = stringType;
    }

    public Counter getCounterValue() throws DOMException
    {
        if (primitiveType != CSS_COUNTER)
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS value doesn't contain a Counter value");

        return this.counterValue; // Por ahora devuelve null, no soportamos counters
    }

    public Rect getRectValue() throws DOMException
    {
        if (primitiveType != CSS_RECT)
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS value doesn't contain a Rect value");

        return rectValue;
    }

    public RGBColor getRGBColorValue() throws DOMException
    {
        if (primitiveType != CSS_RGBCOLOR)
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS value doesn't contain a RGBColor value");

        return this.rgbValue;
    }

    public Object getUpdatedChildObjectValueFromElement(Object requester,int requesterCode)
    {
        CSSPrimitiveValueImpl current = (CSSPrimitiveValueImpl)parent.getUpdatedChildObjectValueFromElement(this,getCode());
        if (current != this)  // Ha cambiado en el elemento (si además hubiera cambiado el tipo de objeto daría error antes)
        {
           if (requesterCode == CSSPrimitiveValueLiteralImpl.COUNTER)
               return current.getCounterValue();
           else if (requesterCode == CSSPrimitiveValueLiteralImpl.RECT)
               return current.getRectValue();
           else if (requesterCode == CSSPrimitiveValueLiteralImpl.RGB)
               return current.getRGBColorValue();

           throw new DOMException(DOMException.INVALID_ACCESS_ERR,"INTERNAL ERROR");
        }
        else
            return requester;
    }

    public void notifyToElementChangedCSSText(SourceCode cssText, Object requester)
    {
        setCssTextSourceCode(cssText,false);
    }
}
