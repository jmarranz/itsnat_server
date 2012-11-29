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

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;
import org.itsnat.impl.core.css.lex.SourceCode;
import org.itsnat.impl.core.css.lex.Space;

/**
 *
 * @author jmarranz
 */
public class CSSValueListImpl extends CSSValueImpl implements CSSValueList
{
    protected List values = new ArrayList();

    /** Creates a new instance of CSSValueListImpl */
    public CSSValueListImpl(SourceCode cssTextCode,SourceCode[] parts,int code,ObjectValueParent parent)
    {
        super(cssTextCode,code,parent);

        rebuild(parts);
    }

    public int getLength()
    {
        return values.size();
    }

    public CSSValue item(int index)
    {
        return (CSSValue)values.get(index);
    }

    public short getCssValueType()
    {
        return CSS_VALUE_LIST;
    }

    public void rebuild(SourceCode cssTextCode)
    {
        values.clear();
        SourceCode[] parts = cssTextCode.split(new Space(' '));
        rebuild(parts);
    }

    public void rebuild(SourceCode[] parts)
    {
        values.clear();
        for(int i = 0; i < parts.length; i++)
        {
            CSSValueImpl item = CSSValueImpl.newCSSValue(parts[i],i,this);
            values.add(item);
        }
    }

    public Object getUpdatedChildObjectValueFromElement(Object requester,int requesterCode)
    {
        CSSValueListImpl current = (CSSValueListImpl)parent.getUpdatedChildObjectValueFromElement(this,getCode());
        if (current != this) // Ha cambiado (si hubiera cambiado además el tipo de objeto fallaría el cast)
        {
            if (current.getLength() != getLength())
                throw new DOMException(DOMException.INVALID_ACCESS_ERR,"Changed the number of values"); // Ha cambiado el número de elementos

            return current.item(requesterCode);
        }
        else
            return requester;
    }

    public void notifyToElementChangedCSSText(SourceCode cssText, Object requester)
    {
        // Basta que haya cambiado uno para regenerar la cadena con todos los elementos
        StringBuffer parentCssText = new StringBuffer();
        for(int i = 0; i < values.size(); i++)
        {
            if (i != 0) parentCssText.append(" ");
            CSSValueImpl valueItem = (CSSValueImpl)values.get(i);
            parentCssText.append( valueItem.getCssTextSourceCode(false).toString() );
        }

        this.cssTextCode = new SourceCode(parentCssText.toString());
        parent.notifyToElementChangedCSSText(cssTextCode,this);
    }
}
