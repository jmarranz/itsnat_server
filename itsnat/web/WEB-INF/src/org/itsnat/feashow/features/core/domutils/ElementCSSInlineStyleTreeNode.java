/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.core.domutils;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSS2Properties;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;
import org.w3c.dom.css.ElementCSSInlineStyle;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class ElementCSSInlineStyleTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element linkToStart;

    public ElementCSSInlineStyleTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkToStart = doc.getElementById("linkToStartId");

        ((EventTarget)linkToStart).addEventListener("click",this,false);
    }

    public void endExamplePanel()
    {
        ((EventTarget)linkToStart).removeEventListener("click",this,false);

        this.linkToStart = null;
    }

    public void handleEvent(Event evt)
    {
        Element currTarget = (Element)evt.getCurrentTarget();

        ElementCSSInlineStyle styleElem = (ElementCSSInlineStyle)currTarget;
        CSSStyleDeclaration cssDec = (CSSStyleDeclaration)styleElem.getStyle();

        CSSValueList border = (CSSValueList)cssDec.getPropertyCSSValue("border");
        int len = border.getLength();
        String cssText = "";
        for(int i = 0; i < len; i++)
        {
            CSSValue value = border.item(i);
            if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE)
            {
                CSSPrimitiveValue primValue = (CSSPrimitiveValue)value;
                if (primValue.getPrimitiveType() == CSSPrimitiveValue.CSS_RGBCOLOR)
                {
                    RGBColor rgb = primValue.getRGBColorValue();
                    log("Current border color: rgb(" + rgb.getRed().getCssText() + "," + rgb.getGreen().getCssText() + "," + rgb.getBlue().getCssText() + ")");
                }
                else cssText += primValue.getCssText() + " ";
            }
            else cssText += value.getCssText() + " ";
        }
        cssDec.setProperty("border",cssText,null); // Removed border color

        CSS2Properties cssDec2 = (CSS2Properties)styleElem.getStyle();
        String newColor = "rgb(255,100,150)";
        cssDec2.setBorderColor(newColor);// border-color property
        log("New border color: " + cssDec2.getBorderColor());
    }

}
