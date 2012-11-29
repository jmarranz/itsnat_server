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

import java.io.Serializable;
import org.w3c.dom.DOMException;
import org.itsnat.impl.core.css.lex.Colon;
import org.itsnat.impl.core.css.lex.Identifier;
import org.itsnat.impl.core.css.lex.SourceCode;

/**
 *
 * @author jmarranz
 */
public class CSSPropertyImpl implements ObjectValueParent,Serializable
{
    protected String propertyName;
    protected CSSValueImpl value;
    protected CSSStyleDeclarationImpl parent;
    protected SourceCode cssTextValue; // El valor

    /** Creates a new instance of CSSPropertyImpl */
    public CSSPropertyImpl(SourceCode cssTextProp,CSSStyleDeclarationImpl parent)
    {
        SourceCode[] pairNameValue = cssTextProp.split(Colon.getSingleton());
        if (pairNameValue.length < 2)
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"Missing : or missing name-value, code: " + cssTextProp.toString());
        else if (pairNameValue.length > 2)
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS: unexpected \":\" , code: " + cssTextProp.toString());

        SourceCode srcPropName = pairNameValue[0];
        srcPropName = srcPropName.trim();
        if (srcPropName.tokenCount() > 1)
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS: syntax error , code: " + srcPropName.toString());
        if (!(srcPropName.getToken(0) instanceof Identifier))
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS: expected an identifier: " + srcPropName.toString());

        String propertyName = srcPropName.toString();
        propertyName = propertyName.toLowerCase();

        this.propertyName = propertyName;
        this.cssTextValue = pairNameValue[1];
        this.parent = parent;
    }

    public CSSPropertyImpl(String propertyName,String cssText,CSSStyleDeclarationImpl parent)
    {
        propertyName = propertyName.trim();
        propertyName = propertyName.toLowerCase();

        this.propertyName = propertyName;
        this.cssTextValue = new SourceCode(cssText);
        this.parent = parent;
    }

    public String getCssText(boolean updateIfNeeded)
    {
        SourceCode sourceCode = getCssTextSourceCode(updateIfNeeded);
        return sourceCode.toString();
    }

    public void setCssText(String value,boolean updateParent)
    {
        setCssTextSourceCode(new SourceCode(value),updateParent);
    }

    public SourceCode getCssTextSourceCode()
    {
        return getCssTextSourceCode(true);
    }

    public SourceCode getCssTextSourceCode(boolean updateIfNeeded)
    {
        if (updateIfNeeded) rebuild();
        return this.cssTextValue;
    }

    public void setCssTextSourceCode(SourceCode cssText,boolean updateParent)
    {
        rebuild(cssText);

        if (updateParent)
            parent.notifyToElementChangedProperty(this,cssText);
    }

    public CSSValueImpl getCSSValue()
    {
        if (value == null) // Sólo lo creamos si se solicita pues hace parseos que llevan tiempo
            this.value = CSSValueImpl.newCSSValue(cssTextValue,-1,this);
        return value;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public Object getUpdatedChildObjectValueFromElement(Object requester,int requesterCode)
    {
        return parent.getPropertyCSSValue(propertyName);
    }

    public void rebuild()
    {
        CSSPropertyImpl property = parent.getPropertyObject(propertyName);
        if (property != this) // Ha cambiado
        {
            SourceCode cssText = property.getCssTextSourceCode(false);
            rebuild(cssText);
        }
    }

    public void rebuild(SourceCode cssText)
    {
        if (this.cssTextValue.equals(cssText))
            return; // Nada que hacer

        this.cssTextValue = cssText;
        this.value = null; // Pues puede haber cambiado el tipo de valor
    }

    public void notifyToElementChangedCSSText(SourceCode cssText,Object requester)
    {
        setCssTextSourceCode(cssText,true);
    }
}
