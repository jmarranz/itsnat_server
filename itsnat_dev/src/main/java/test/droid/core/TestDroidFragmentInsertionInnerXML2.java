/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulOwnerImpl;
import org.itsnat.impl.core.clientdoc.CodeToSendRegistryImpl;
import org.itsnat.impl.core.scriptren.bsren.node.BSRenderElementImpl;
import org.itsnat.impl.core.scriptren.shared.node.InnerMarkupCodeImpl;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.web.shared.EventListenerSerial;

/**
 * Este test únicamente difiere del TestDroidFragmentInsertionInnerXML (sin 2) en que hay un atributo con valor "@remote:..." lo cual IMPIDE usar
 * setInnerXML pues necesitamos usar DOM normal para poder parsear el código BS generado extrayendo los atributos @remote:, si se usa setInnerXML no podemos parsear
 * 
 * @author jmarranz
 */
public class TestDroidFragmentInsertionInnerXML2 extends TestDroidBase implements EventListener
{

    public TestDroidFragmentInsertionInnerXML2(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testFragmentInsertionInnerXMLId2");
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }

    @Override
    public void handleEvent(Event evt)
    {
        Document doc = getDocument();
        Element testLauncherHidden = doc.getElementById("testFragmentInsertionInnerXMLHiddenId2");

        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        DocumentFragment docFrag = servlet.getItsNatDocFragmentTemplate("test_droid_core_fragment_2").loadDocumentFragment(itsNatDoc);

        final Element frameLayoutViewToRemove = ItsNatTreeWalker.getFirstChildElement(docFrag);

        // En el template fragment hay un <script> que DEBE desaparecer
        NodeList scripts = frameLayoutViewToRemove.getElementsByTagName("script");
        if (scripts.getLength() != 2) throw new RuntimeException("Expected 2 <string> elements");

        // Sabemos con seguridad que el fragment se insertará (parcialmente) via markup, nos aseguramos de todas formas que está activado
        if (!BSRenderElementImpl.SUPPORT_INSERTION_AS_MARKUP) throw new RuntimeException("CANNOT TEST");

        testLauncherHidden.getParentNode().insertBefore(frameLayoutViewToRemove, testLauncherHidden);
       
        // Verificamos que el último código generado NO es un objeto InnerMarkupCodeImpl, lo que significa que NO se usa setInnerXML, pues NO debemos usarlo 
        ClientDocumentStfulOwnerImpl clientTmp = (ClientDocumentStfulOwnerImpl)itsNatDoc.getClientDocumentOwner();
        CodeToSendRegistryImpl codeToSendRegistry = clientTmp.getCodeToSendRegistry();
        Object lastCodeToSend = codeToSendRegistry.getLastCodeToSend();
        if (lastCodeToSend instanceof InnerMarkupCodeImpl) throw new RuntimeException("IS USING setInnerXML TEST FAILED");        
        
        Element frameLayoutViewToRemove2 = (Element)testLauncherHidden.getPreviousSibling();
        if (frameLayoutViewToRemove != frameLayoutViewToRemove2) throw new RuntimeException("TEST ERROR");

        String layout_width = frameLayoutViewToRemove.getAttributeNS(ANDROID_NS,"layout_width");
        if (!"250dp".equals(layout_width))
            throw new RuntimeException("TEST FAIL");        
        
        if (doc.getElementById("fragmentTestId") == null) throw new RuntimeException("FAIL");

        itsNatDoc.addCodeToSend(" if (null == itsNatDoc.findViewByXMLId(\"fragmentTestId\")) alert(\"FAIL TEST\"); ");

        ((EventTarget)frameLayoutViewToRemove).addEventListener("click",new EventListenerSerial(){
            @Override
            public void handleEvent(Event evt)
            {
                frameLayoutViewToRemove.getParentNode().removeChild(frameLayoutViewToRemove);
            }
        },false);

        // En el template fragment hay un <script> que DEBE desaparecer
        scripts = doc.getElementsByTagName("script");
        if (scripts.getLength() > 0) throw new RuntimeException("Unexpected <string> element");
    }

}
