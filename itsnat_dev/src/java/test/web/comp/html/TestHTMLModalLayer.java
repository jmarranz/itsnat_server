/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.comp.html;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.ItsNatButton;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.core.html.ItsNatHTMLDocument;
import test.web.shared.TestHTMLModalLayerBase;

/**
 *
 * @author jmarranz
 */
public class TestHTMLModalLayer extends TestHTMLModalLayerBase
{
    protected ItsNatButton button;

    public TestHTMLModalLayer(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        load();
    }

    public void load()
    {
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatButton button = (ItsNatButton)compMgr.findItsNatComponentById("testModalLayerId");
        this.button = button;
        button.addEventListener("click",this);

        this.cleanModeCheck1 = (ItsNatHTMLInputCheckBox)compMgr.findItsNatComponentById("cleanModeId1");
        cleanModeCheck1.setSelected(false);
        this.cleanModeCheck2 = (ItsNatHTMLInputCheckBox)compMgr.findItsNatComponentById("cleanModeId2");
        cleanModeCheck2.setSelected(false);
    }

    public boolean isMobile()
    {
        return false;
    }
}
