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
import org.w3c.dom.css.CSSValue;
import org.itsnat.impl.core.css.lex.SourceCode;
import org.itsnat.impl.core.css.lex.Space;

/**
 *
 * @author jmarranz
 */
public abstract class CSSValueImpl implements CSSValue,ObjectValueParent,Serializable
{
    protected SourceCode cssTextCode;
    protected ObjectValueParent parent;
    protected int code;

    /** Creates a new instance of CSSValueImpl */
    public CSSValueImpl(SourceCode cssTextCode,int code,ObjectValueParent parent)
    {
        this.cssTextCode = cssTextCode;
        this.code = code;
        this.parent = parent;
    }

    public static CSSValueImpl newCSSValue(SourceCode cssTextCode,int code,ObjectValueParent parent)
    {
        if (CSSValueInheritImpl.isCSSValueInherit(cssTextCode))
            return new CSSValueInheritImpl(cssTextCode,code,parent);
        else
        {
            SourceCode cssTextCodeTmp = cssTextCode; // El original no lo tocamos pues necesitamos asignar el original
            cssTextCodeTmp = cssTextCodeTmp.trim();
            SourceCode[] parts = cssTextCodeTmp.split(new Space(' '));
            if (parts.length > 1)
                return new CSSValueListImpl(cssTextCode,parts,code,parent);
            else
                return new CSSPrimitiveValueImpl(cssTextCode,code,parent);
        }
    }

    public int getCode()
    {
        return code;
    }

    public ObjectValueParent getCSSValueParent()
    {
        return parent;
    }

    public abstract void rebuild(SourceCode cssText);

    public String getCssText()
    {
        return getCssTextSourceCode(true).toString();
    }

    public SourceCode getCssTextSourceCode(boolean checkFromElem)
    {
        if (checkFromElem)
        {
            CSSValueImpl current = (CSSValueImpl)parent.getUpdatedChildObjectValueFromElement(this,getCode());
            if (current != this)
            {
                if (!this.cssTextCode.equals(current.getCssTextSourceCode(false))) // Si ha cambiado la propiedad puede haber cambiado el tipo de valor etc, el objeto no vale (pues es un objeto vinculado al elemento)
                    throw new DOMException(DOMException.INVALID_ACCESS_ERR,"CSS property " + parent.getPropertyName() + " has been indirectly changed");
            }
        }

        return this.cssTextCode;
    }

    public void setCssText(String cssText) throws DOMException
    {
        setCssTextSourceCode(new SourceCode(cssText),true);
    }

    public void setCssTextSourceCode(SourceCode cssTextCode,boolean rebuild) throws DOMException
    {
        if (!this.cssTextCode.equals(cssTextCode))
        {
            this.cssTextCode = cssTextCode;
            if (rebuild) rebuild(cssTextCode);
        }
        // Enviamos el valor siempre al elemento aunque aquí no haya cambiado pues puede haberse cambiado indirectamente por el Element
        parent.notifyToElementChangedCSSText(cssTextCode,this);
    }

    public abstract short getCssValueType();

    public String getPropertyName()
    {
        return parent.getPropertyName();
    }


}
