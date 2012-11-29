/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.svg;

import test.shared.*;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public class TestSVGScriptInsertion extends TestScriptInsertion
{
    protected boolean asv_batik;

    public TestSVGScriptInsertion(ItsNatDocument itsNatDoc,boolean asv_batik)
    {
        super(itsNatDoc);

        this.asv_batik = asv_batik;
        addLoadEventListener();
    }

    public void addLoadEventListener()
    {
        // FireFox por ejemplo no soporta SVGLoad

        Document doc = itsNatDoc.getDocument();
        if (asv_batik)
        {
            ((EventTarget)doc.getDocumentElement()).addEventListener("SVGLoad",this,false);
        }
        else
        {
            AbstractView view = ((DocumentView)doc).getDefaultView();
            ((EventTarget)view).addEventListener("load",this,false);
        }
    }

    public Element getScriptParentElement()
    {
        Document doc = itsNatDoc.getDocument();
        return doc.getDocumentElement();
    }

    public Element createScriptElement()
    {
        Document doc = itsNatDoc.getDocument();
        return doc.createElementNS("http://www.w3.org/2000/svg","script");
    }
}
