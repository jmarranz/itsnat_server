/*
 * TestClassAttribute.java
 *
 * Created on 9 de marzo de 2007, 13:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestNameAttribute
{
    /** Creates a new instance of TestClassAttribute */
    public TestNameAttribute(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        // El atributo "name" es puñetero en MSIE
        // http://msdn.microsoft.com/library/default.asp?url=/workshop/author/dhtml/reference/properties/name_2.asp
        // El caso es que en MSIE 6 funciona bien (parece)

        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element elem = doc.getElementById("nameTestId");
        Element anchor = doc.createElement("a");
        anchor.setAttribute("name","nameExample");
        anchor.appendChild(doc.createTextNode("anchor"));
        elem.appendChild(anchor); // El padre tiene ya un nodo de texto se evita que se use innerHTML que no nos interesa
    }

}
