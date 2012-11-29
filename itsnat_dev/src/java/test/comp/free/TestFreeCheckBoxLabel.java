/*
 * TestFreeCheckBoxDefault.java
 *
 * Created on 28 de septiembre de 2007, 22:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.free;

import org.itsnat.comp.button.toggle.ItsNatFreeCheckBoxLabel;
import org.itsnat.core.html.ItsNatHTMLDocument;
import test.comp.*;

/**
 *
 * @author jmarranz
 */
public class TestFreeCheckBoxLabel extends TestFreeCheckBox
{

    /** Creates a new instance of TestFreeCheckBoxDefault */
    public TestFreeCheckBoxLabel(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        init("freeCheckBoxLabelId");

        ItsNatFreeCheckBoxLabel button = (ItsNatFreeCheckBoxLabel)this.button;
        button.setLabelValue("Free Elem as Check Box With Label");
    }

}
