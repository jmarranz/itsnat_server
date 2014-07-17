/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import org.itsnat.comp.button.normal.ItsNatHTMLAnchorLabel;
import org.itsnat.core.html.ItsNatHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestHTMLAnchorLabel extends TestHTMLAnchorBase
{
    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLAnchorLabel(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        init("anchorLabelId");

        ItsNatHTMLAnchorLabel button = (ItsNatHTMLAnchorLabel)this.button;
        button.setLabelValue("Link as Normal Button With Label");
    }

}
