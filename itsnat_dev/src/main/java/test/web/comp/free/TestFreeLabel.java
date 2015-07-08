/*
 * TestFreeLabel.java
 *
 * Created on 21 de diciembre de 2006, 18:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;

import java.beans.PropertyVetoException;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.label.ItsNatFreeLabel;
import org.w3c.dom.html.HTMLElement;
import test.web.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestFreeLabel extends TestBaseHTMLDocument
{

    /**
     * Creates a new instance of TestFreeLabel
     */
    public TestFreeLabel(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        try
        {
        load();
        }
        catch(PropertyVetoException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void load() throws PropertyVetoException
    {
        org.w3c.dom.Document doc = itsNatDoc.getDocument();
        HTMLElement elem = (HTMLElement)doc.getElementById("freeLabelId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatFreeLabel comp = (ItsNatFreeLabel)componentMgr.findItsNatComponent(elem);

        String text = "Free Label Test";
        comp.setValue(text);
    }
}
