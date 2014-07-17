/*
 * TestSVGDocument.java
 *
 * Created on 11 de febrero de 2008, 18:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.xul;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import test.web.shared.Shared;
import test.web.shared.TestAttachedServerLoadEventReceived;
import test.web.shared.TestScriptRendering;
import test.web.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestXULDocument
{
    protected ItsNatDocument itsNatDoc;

    /** Creates a new instance of TestXULDocument */
    public TestXULDocument(final ItsNatDocument itsNatDoc,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        this.itsNatDoc = itsNatDoc;

        load(request,response);
    }

    public void load(ItsNatServletRequest request,ItsNatServletResponse response)
    {
        new TestAttachedServerLoadEventReceived(itsNatDoc);
        new TestScriptRendering(itsNatDoc);
        new TestXULListBox(itsNatDoc);
        new TestXULIncludeFragment(itsNatDoc);
        new TestXULIncludeToDOM(itsNatDoc);
        new TestXULModalLayer(itsNatDoc);

        ItsNatDocument referrer = request.getItsNatDocumentReferrer();
        if ((referrer != null)&&(referrer.getItsNatDocumentTemplate() == itsNatDoc.getItsNatDocumentTemplate())) // por si fuera de otra página
        {
            Document doc = itsNatDoc.getDocument();
            Element refLink = referrer.getDocument().getElementById("referrerId");
            Text refText = (Text)refLink.getFirstChild();
            Element link = doc.getElementById("referrerId");
            Text currText = (Text)link.getFirstChild();
            currText.setData(refText.getData() + "=>OK");
        }

        Shared.setRemoteControlLink(request,response);

        new TestSerialization(request);
    }


}
