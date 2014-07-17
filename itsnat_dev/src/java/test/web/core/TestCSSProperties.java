/*
 * TestCSSProperties.java
 *
 * Created on 23 de noviembre de 2006, 19:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;
import org.w3c.dom.css.ElementCSSInlineStyle;
import org.w3c.dom.css.Rect;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestCSSProperties
{
    protected ItsNatDocument itsNatDoc;

    /** Creates a new instance of TestCSSProperties */
    public TestCSSProperties(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        tests();
    }

    public void tests()
    {
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        HTMLElement body = doc.getBody();
        ElementCSSInlineStyle elemCSS = (ElementCSSInlineStyle)body;
        CSSStyleDeclaration props = elemCSS.getStyle();
        body.setAttribute("style","color: red ; border: solid 1.5px ; clip: rect( 1px 2in 3cm 4 ); border-color: #aabbcc; width: 50%; anytext: \"hello guy\" ");

        CSSPrimitiveValue color = (CSSPrimitiveValue)props.getPropertyCSSValue("color");
        TestUtil.checkError(color.getPrimitiveType() == CSSPrimitiveValue.CSS_IDENT);
        TestUtil.checkError(color.getStringValue().equals("red"));
        color.setStringValue(CSSPrimitiveValue.CSS_IDENT," blue ");
        TestUtil.checkError(color.getStringValue().equals("blue"));
        color.setCssText(" red "); // Restauramos el valor original

        CSSValue border = props.getPropertyCSSValue("border");
        TestUtil.checkError(border.getCssValueType() == CSSValue.CSS_VALUE_LIST);
        CSSValueList borderList = (CSSValueList)border;
        TestUtil.checkError(borderList.getLength() == 2);

        CSSPrimitiveValue borderType = (CSSPrimitiveValue)borderList.item(0);
        TestUtil.checkError(borderType.getPrimitiveType() == CSSPrimitiveValue.CSS_IDENT);
        TestUtil.checkError(borderType.getStringValue().equals("solid"));
        borderType.setStringValue(CSSPrimitiveValue.CSS_IDENT,"dotted");
        TestUtil.checkError(borderType.getStringValue().equals("dotted"));
        TestUtil.checkError(body.getAttribute("style").equals("color: red ;border:dotted 1.5px;clip: rect( 1px 2in 3cm 4 );border-color: #aabbcc;width: 50%;anytext: \"hello guy\" "));
        borderType.setStringValue(CSSPrimitiveValue.CSS_IDENT,"solid"); // restaurar

        CSSPrimitiveValue borderWidth = (CSSPrimitiveValue)borderList.item(1);
        TestUtil.checkError(borderWidth.getPrimitiveType() == CSSPrimitiveValue.CSS_PX);
        TestUtil.checkError(borderWidth.getFloatValue(CSSPrimitiveValue.CSS_PX) == 1.5);
        borderWidth.setFloatValue(CSSPrimitiveValue.CSS_IN,(float)2.5);
        TestUtil.checkError(borderWidth.getPrimitiveType() == CSSPrimitiveValue.CSS_IN);
        TestUtil.checkError(borderWidth.getFloatValue(CSSPrimitiveValue.CSS_IN) == 2.5);
        borderWidth.setFloatValue(CSSPrimitiveValue.CSS_PX,(float)1.5); // Restaurar

        CSSValue clip = props.getPropertyCSSValue("clip");
        TestUtil.checkError(clip.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE);
        CSSPrimitiveValue clipValue = (CSSPrimitiveValue)clip;
        Rect clipRect = clipValue.getRectValue();
        CSSPrimitiveValue top = clipRect.getTop();
        TestUtil.checkError(top.getPrimitiveType() == CSSPrimitiveValue.CSS_PX);
        TestUtil.checkError(top.getFloatValue(CSSPrimitiveValue.CSS_PX) == 1);
        top.setFloatValue(CSSPrimitiveValue.CSS_IN,(float)3.5);
        TestUtil.checkError(top.getPrimitiveType() == CSSPrimitiveValue.CSS_IN);
        TestUtil.checkError(top.getFloatValue(CSSPrimitiveValue.CSS_IN) == 3.5);
        TestUtil.checkError(body.getAttribute("style").equals("color: red ;border:solid 1.5px;clip:rect(3.5in,2in,3cm,4);border-color: #aabbcc;width: 50%;anytext: \"hello guy\" "));
        top.setFloatValue(CSSPrimitiveValue.CSS_PX,(float)1); // restaurar

        // HACER test de valores: #rgb y #rrggbb
        CSSPrimitiveValue borderColor = (CSSPrimitiveValue)props.getPropertyCSSValue("border-color");
        CSSPrimitiveValue red = borderColor.getRGBColorValue().getRed();
        TestUtil.checkError(red.getCssText().equals("170"));  // aa
        CSSPrimitiveValue green = borderColor.getRGBColorValue().getGreen();
        TestUtil.checkError(green.getCssText().equals("187"));  // bb
        CSSPrimitiveValue blue = borderColor.getRGBColorValue().getBlue();
        TestUtil.checkError(blue.getCssText().equals("204"));  // cc


        CSSPrimitiveValue width = (CSSPrimitiveValue)props.getPropertyCSSValue("width");
        TestUtil.checkError(width.getPrimitiveType() == CSSPrimitiveValue.CSS_PERCENTAGE);
        TestUtil.checkError(width.getFloatValue(CSSPrimitiveValue.CSS_PERCENTAGE) == 50);
        width.setFloatValue(CSSPrimitiveValue.CSS_PERCENTAGE,(float)25);
        TestUtil.checkError(width.getFloatValue(CSSPrimitiveValue.CSS_PERCENTAGE) == 25);
        TestUtil.checkError(width.getCssText().equals("25.0%"));
        width.setCssText(" 50%"); // Restauramos el valor original


        CSSPrimitiveValue anytext = (CSSPrimitiveValue)props.getPropertyCSSValue("anytext");
        TestUtil.checkError(anytext.getStringValue().equals("hello guy"));
        anytext.setStringValue(CSSPrimitiveValue.CSS_STRING,"hello girl");
        TestUtil.checkError(anytext.getStringValue().equals("hello girl"));
        TestUtil.checkError(anytext.getCssText().equals("\"hello girl\""));

        body.removeAttribute("style");
    }
}
