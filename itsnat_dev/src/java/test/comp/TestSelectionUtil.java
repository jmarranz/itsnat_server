/*
 * TestSelectionUtil.java
 *
 * Created on 19 de diciembre de 2006, 22:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp;

import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TestSelectionUtil
{

    /**
     * Creates a new instance of TestSelectionUtil
     */
    public TestSelectionUtil()
    {
    }

    public static void decorateSelection(Element elem,boolean selected)
    {
        // PENSAR en usar CSS2Properties para que pueda soportar
        // la presencia previa de atributos en el style ajenos a la selección

        if (selected)
            elem.setAttribute("style","background:rgb(0,0,255); color:white;");
        else
            elem.removeAttribute("style");
    }
}
