/*
 * TestClassAttribute.java
 *
 * Created on 9 de marzo de 2007, 13:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestClassAttribute
{
    protected ItsNatDocument itsNatDoc;

    /** Creates a new instance of TestClassAttribute */
    public TestClassAttribute(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        tests();
    }

    public void tests()
    {
        // El atributo "class" es puñetero en MSIE, ItsNat deberá
        // generar automáticamente la asignación usando la propiedad className

        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element elem = doc.getElementById("classTestId");
        elem.setAttribute("class","classExample");
    }

}
