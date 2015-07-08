/*
 * TestHTMLLabel.java
 *
 * Created on 21 de diciembre de 2006, 18:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import java.beans.PropertyVetoException;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.label.ItsNatHTMLLabel;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLElement;
import test.web.shared.TestBaseHTMLDocument;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestHTMLLabel extends TestBaseHTMLDocument
{

    /**
     * Creates a new instance of TestHTMLLabel
     */
    public TestHTMLLabel(ItsNatHTMLDocument itsNatDoc)
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
        HTMLElement elem = (HTMLElement)doc.getElementById("labelId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLLabel comp = (ItsNatHTMLLabel)componentMgr.findItsNatComponent(elem);

        comp.setValue(null); // Para probar
        String text = "Label Test";
        comp.setValue(text);
        TestUtil.checkError(comp.getElement().hasChildNodes());
        comp.setValue(null); // Para probar si se vacía
        TestUtil.checkError(!comp.getElement().hasChildNodes());
        comp.setValue(text);

        TestUtil.checkError(comp.getForComponent() != null);
    }
}
