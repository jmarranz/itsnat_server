/*
 * TestHTMLButtonDefault.java
 *
 * Created on 28 de septiembre de 2007, 21:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.html;

import org.itsnat.comp.button.normal.ItsNatHTMLButtonLabel;
import org.itsnat.core.html.ItsNatHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestHTMLButtonLabel extends TestHTMLButton
{

    /**
     * Creates a new instance of TestHTMLButtonDefault
     */
    public TestHTMLButtonLabel(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        init("buttonLabelId");

        ItsNatHTMLButtonLabel button = (ItsNatHTMLButtonLabel)this.button;
        button.setLabelValue("Test Button With Label");
    }

}
