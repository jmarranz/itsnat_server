/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.mobile;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;
import test.web.shared.TestHTMLModalLayerBase;

/**
 *
 * @author jmarranz
 */
public class TestMobileHTMLModalLayer extends TestHTMLModalLayerBase
{
    protected Element link;

    public TestMobileHTMLModalLayer(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        load();
    }

    public void load()
    {
        this.link = itsNatDoc.getDocument().getElementById("testModalLayerId");
        ((EventTarget)link).addEventListener("click",this,false);

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.cleanModeCheck1 = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("cleanModeId1");
        cleanModeCheck1.setSelected(false);
        this.cleanModeCheck2 = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("cleanModeId2");
        cleanModeCheck2.setSelected(false);
    }

    public boolean isMobile()
    {
        return true;
    }
}
