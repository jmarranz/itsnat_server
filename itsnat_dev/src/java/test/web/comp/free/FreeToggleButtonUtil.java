/*
 * FreeToggleButtonUtil.java
 *
 * Created on 23 de noviembre de 2006, 13:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;


import javax.swing.ButtonModel;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.ElementCSSInlineStyle;

/**
 *
 * @author jmarranz
 */
public class FreeToggleButtonUtil
{

    /** Creates a new instance of FreeToggleButtonUtil */
    public FreeToggleButtonUtil()
    {
    }

    public static void updateDecoration(ButtonModel model,ElementCSSInlineStyle elemStyle)
    {
        if (model.isSelected())
        {
            selectDecoration(elemStyle);
        }
        else
        {
            unselectDecoration(elemStyle);
        }
    }

    public static void selectDecoration(ElementCSSInlineStyle elemStyle)
    {
        CSSStyleDeclaration style = elemStyle.getStyle();
        CSSValue cssValue = style.getPropertyCSSValue("color"); // Usar el CSSValue es la forma más lenta pero es para testear
        if (cssValue == null)
            style.setProperty("color","red","");
        else
            cssValue.setCssText("red");
    }

    public static void unselectDecoration(ElementCSSInlineStyle elemStyle)
    {
        CSSStyleDeclaration style = elemStyle.getStyle();
        style.removeProperty("color");
    }
}
