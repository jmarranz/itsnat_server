/*
 * TestSVGDocument.java
 *
 * Created on 11 de febrero de 2008, 18:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.svg;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import test.shared.BrowserUtil2;
import test.shared.Shared;
import test.shared.TestAttachedServerLoadEventReceived;
import test.shared.TestFireEventFromServerNoBrowser;
import test.shared.TestFireEventFromServerWithBrowser;
import test.shared.TestIFrameBoundHTML;
import test.shared.TestScriptRendering;
import test.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestSVGDocument
{
    protected ItsNatDocument itsNatDoc;

    /** Creates a new instance of TestSVGDocument */
    public TestSVGDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        this.itsNatDoc = itsNatDoc;

        load(request,response);
    }

    public void load(ItsNatServletRequest request,ItsNatServletResponse response)
    {
        boolean asv_batik = BrowserUtil2.isMSIE(request)||BrowserUtil2.isBatik(request);

        registerComponents(asv_batik);

        ItsNatDocument referrer = request.getItsNatDocumentReferrer();
        if ((referrer != null)&&(referrer.getItsNatDocumentTemplate() == itsNatDoc.getItsNatDocumentTemplate())) // por si fuera de otra página
        {
            Element refLink = referrer.getDocument().getElementById("referrerId");
            Text refText = (Text)refLink.getFirstChild();
            Document doc = itsNatDoc.getDocument();
            Element link = doc.getElementById("referrerId");
            Text currText = (Text)link.getFirstChild();
            currText.setData(refText.getData() + "=>OK");
        }

        Shared.setRemoteControlLink(request,response);

        new TestSerialization(request);
    }

    public void registerComponents(boolean asv_batik)
    {
        new TestAttachedServerLoadEventReceived(itsNatDoc);
        new TestScriptRendering(itsNatDoc);
        new TestSVGScriptInsertion(itsNatDoc,asv_batik);
        new TestSVGKeyEvents(itsNatDoc);
        new TestSVGFreeList(itsNatDoc);
        new TestSVGIncludeFragment(itsNatDoc);
        new TestSVGIncludeToDOM(itsNatDoc);

        new TestSVGModalLayer(itsNatDoc);

        new TestFireEventFromServerWithBrowser(itsNatDoc);
        new TestFireEventFromServerNoBrowser(itsNatDoc);

        // El ASV3 no soporta <foreignObject> y el ASV6 apenas nada por lo que los ejemplos de XHTML no harán nada
        //if (!asv)
        {
            new TestSVGXHTMLComponent(itsNatDoc);
            new TestIFrameBoundHTML(itsNatDoc);
        }

    }

}
