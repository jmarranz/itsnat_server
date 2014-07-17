/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.svgweb;

import test.web.shared.TestScriptInsertion;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TestSVGWebScriptInsertion extends TestScriptInsertion
{
    protected Element svgRoot;

    public TestSVGWebScriptInsertion(ItsNatDocument itsNatDoc,Element svgRoot)
    {
        super(itsNatDoc);
        this.svgRoot = svgRoot;
    }

    public void addLoadEventListener()
    {
        // No se usará
    }

    public Element getScriptParentElement()
    {
        return svgRoot;
    }

    public Element createScriptElement()
    {
        Document doc = itsNatDoc.getDocument();
        return doc.createElementNS("http://www.w3.org/2000/svg","script");
    }
}
