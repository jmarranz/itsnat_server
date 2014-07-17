/*
 * TestHTMLButtonDefault.java
 *
 * Created on 28 de septiembre de 2007, 21:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import org.itsnat.core.html.ItsNatHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestHTMLButtonDefault extends TestHTMLButton
{

    /**
     * Creates a new instance of TestHTMLButtonDefault
     */
    public TestHTMLButtonDefault(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        init("buttonId");
    }

}
