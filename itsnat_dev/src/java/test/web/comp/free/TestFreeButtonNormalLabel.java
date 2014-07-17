/*
 * TestFreeButtonNormalDefault.java
 *
 * Created on 28 de septiembre de 2007, 22:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;

import org.itsnat.comp.button.normal.ItsNatFreeButtonNormalLabel;
import org.itsnat.core.html.ItsNatHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestFreeButtonNormalLabel extends TestFreeButtonNormal
{

    /** Creates a new instance of TestFreeButtonNormalDefault */
    public TestFreeButtonNormalLabel(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        init("freeButtonNormalLabelId");

        ItsNatFreeButtonNormalLabel button = (ItsNatFreeButtonNormalLabel)this.button;
        button.setLabelValue("Free Elem as Normal Button With Label");
    }

}
