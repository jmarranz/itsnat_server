/*
 * TestFreeRadioDefault.java
 *
 * Created on 28 de septiembre de 2007, 22:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;

import org.itsnat.comp.button.toggle.ItsNatFreeRadioButton;
import org.itsnat.comp.button.toggle.ItsNatFreeRadioButtonLabel;
import org.itsnat.core.html.ItsNatHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestFreeRadioLabel extends TestFreeRadio
{

    /** Creates a new instance of TestFreeRadioDefault */
    public TestFreeRadioLabel(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        init("freeRadioLabelId");

        setLabel(this.radioComp1,1);
        setLabel(this.radioComp2,2);
        setLabel(this.radioComp3,3);
    }

    public void setLabel(ItsNatFreeRadioButton radioComp,int num)
    {
        ItsNatFreeRadioButtonLabel button = (ItsNatFreeRadioButtonLabel)radioComp;
        button.setLabelValue("Free Elem as Radio with Label " + num);
    }
}
