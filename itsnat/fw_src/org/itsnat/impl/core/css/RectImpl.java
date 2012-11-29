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
import org.w3c.dom.css.Rect;
import org.itsnat.impl.core.css.lex.Comma;
import org.itsnat.impl.core.css.lex.SourceCode;
import org.itsnat.impl.core.css.lex.Space;

/**
 * rect (<top> <right> <bottom> <left>)
 *
 * @author jmarranz
 */
public class RectImpl extends CSSPrimitiveValueLiteralImpl implements Rect
{
    protected CSSPrimitiveValueImpl parent;
    protected CSSPrimitiveValueImpl top;
    protected CSSPrimitiveValueImpl right;
    protected CSSPrimitiveValueImpl bottom;
    protected CSSPrimitiveValueImpl left;

    /** Creates a new instance of RectImpl */
    public RectImpl(SourceCode cssTextCode,CSSPrimitiveValueImpl parent)
    {
        this.parent = parent;
        SourceCode[] rectList = cssTextCode.split(Comma.getSingleton());
        if (rectList.length != 4)
        {
            rectList = cssTextCode.split(new Space(' '));
            if (rectList.length != 4)
                throw new DOMException(DOMException.INVALID_ACCESS_ERR,"Rect syntax error, property: " + getPropertyName() + " value: " + cssTextCode.toString());
        }

        this.top = new CSSPrimitiveValueImpl(rectList[0],   0,this);
        this.right = new CSSPrimitiveValueImpl(rectList[1], 1,this);
        this.bottom = new CSSPrimitiveValueImpl(rectList[2],2,this);
        this.left = new CSSPrimitiveValueImpl(rectList[3],  3,this);
    }

    public int getCode()
    {
        return RECT;
    }

    public CSSPrimitiveValue getTop()
    {
        return top;
    }

    public CSSPrimitiveValue getRight()
    {
        return right;
    }

    public CSSPrimitiveValue getBottom()
    {
        return bottom;
    }

    public CSSPrimitiveValue getLeft()
    {
        return left;
    }

    public String getPropertyName()
    {
        return parent.getPropertyName();
    }

    public Object getUpdatedChildObjectValueFromElement(Object requester,int requesterCode)
    {
        Rect current = (Rect)parent.getUpdatedChildObjectValueFromElement(this,getCode());
        if (current != this)
        {
            if (requesterCode == 0)
                return current.getTop();
            else if (requesterCode == 1)
                return current.getRight();
            else if (requesterCode == 2)
                return current.getBottom();
            else if (requesterCode == 3)
                return current.getLeft();
            throw new DOMException(DOMException.INVALID_ACCESS_ERR,"Internal Error");
        }
        else
            return requester;
    }

    public void notifyToElementChangedCSSText(SourceCode cssText, Object requester)
    {
        // Una de las partes se ha actualizado (da igual cual sea), hay que notificar el cambio
        // de toda la estructura Rect.
        String topValue = top.getCssTextSourceCode(false).toString();
        String rightValue = right.getCssTextSourceCode(false).toString();
        String bottomValue = bottom.getCssTextSourceCode(false).toString();
        String leftValue = left.getCssTextSourceCode(false).toString();

        String cssTextRect = "rect(" + topValue + "," + rightValue + "," + bottomValue + "," + leftValue + ")";
        SourceCode cssTextRectCode = new SourceCode(cssTextRect);
        parent.setCssTextSourceCode(cssTextRectCode,true);
    }

}
