/*
 * TestCoreLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import java.io.Serializable;
import test.shared.TestFireEventFromServerWithBrowser;
import test.shared.TestFireEventFromServerNoBrowser;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.CodeToSendEvent;
import org.itsnat.core.event.CodeToSendListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import test.shared.Shared;
import test.core.treecustom.TestTreeCustom;
import test.shared.CodeToSendListenerSerial;
import test.shared.TestAttachedServerLoadEventReceived;
import test.shared.TestScriptRendering;
import test.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestCoreDocument implements Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;

    /**
     * Creates a new instance of TestCoreLoadListener
     */
    public TestCoreDocument(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = itsNatDoc;

//        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
//        session.getHttpSession().setMaxInactiveInterval(10);

        CodeToSendListener listener = new CodeToSendListenerSerial()
        {
            // Para probar simplemente que funciona

            public Object preSendCode(CodeToSendEvent event)
            {
                return event.getCode();
            }

            public void postSendCode(CodeToSendEvent event)
            {
            }
        };
        itsNatDoc.addCodeToSendListener(listener);

        new TestIgnoreIntrusiveNodes(itsNatDoc,request); // Este test conviente que sea el primer test
        new TestAttachedServerLoadEventReceived(itsNatDoc);
        new TestMutationEventsInternal(itsNatDoc);

        new TestFastLoadErrors(itsNatDoc);

        new TestScriptRendering(itsNatDoc);
        new TestHTMLScriptInsertion(itsNatDoc);

        new TestInputFileInsertion(itsNatDoc);

        new TestComments(itsNatDoc,request);

        new TestEmbedRenderingMSIE(itsNatDoc);
        
        new TestCodeBeforeSendListener(itsNatDoc);

        new TestEventCapture(itsNatDoc);

        new TestSetTextNode(itsNatDoc);

        new TestAddRowListener(itsNatDoc);
        new TestPlainHTMLTextArea(itsNatDoc);

        new TestSyncPropertyListener(itsNatDoc);

        new TestInsertBeforeNodeListener(itsNatDoc);

        new TestRemoveNodeListener(itsNatDoc);

        new TestRemoveAndInsertNodeListener(itsNatDoc);

        new TestCloneAndInsertNodeListener(itsNatDoc);

        new TestNewAttrListener(itsNatDoc);

        new TestChangeAttrListener(itsNatDoc);

        new TestRemoveAttrListener(itsNatDoc);

        new TestChangeTextListener(itsNatDoc);

        new TestSyncNewAttrListener(itsNatDoc);

        new TestSyncExistingAttrListener(itsNatDoc);
        new TestSyncDeleteAttrListener(itsNatDoc);
        new TestSyncDeleteAttrListenerMethod2(itsNatDoc,request);
        new TestSyncNewNodeListener(itsNatDoc);
        new TestSyncCompleteNodeListener(itsNatDoc,request);

        new TestErrorGeneration(itsNatDoc);

        new TestContinueListener(itsNatDoc);
        new TestUserListener(itsNatDoc);
        new TestSyncEventNonBlocking(itsNatDoc);
        new TestEventTimeout(itsNatDoc);
        new TestEventListenerGC(itsNatDoc);

        new TestFireEventFromServerWithBrowser(itsNatDoc);
        new TestFireEventFromServerNoBrowser(itsNatDoc);

        new TestToDOM(itsNatDoc);
        new TestInnerHTML(itsNatDoc);
        new TestInsertHTMLSVGFragment(itsNatDoc);

        new TestGlobalDOMListener(itsNatDoc);

        new TestEventListenerChain(itsNatDoc);

        new TestDisconnectNode(itsNatDoc);
        
        new TestTimer(itsNatDoc);

        new TestAsyncServerTask(itsNatDoc);

        new TestCometNotifier(itsNatDoc);

        new TestInsertFragment(itsNatDoc);

        new TestFireBug(itsNatDoc);

        new TestSetOnClickAttribute(itsNatDoc);

        new TestNamespacedAttribute(itsNatDoc);

        new TestNameAttribute(itsNatDoc);
        new TestClassAttribute(itsNatDoc);

        new TestFastLoadMode(itsNatDoc);


        new TestElementListFree(itsNatDoc);

        new TestElementTableFreeMaster(itsNatDoc);
        new TestElementTableFreeSlave(itsNatDoc);

        new TestElementList(itsNatDoc);

        new TestElementTable(itsNatDoc);
        new TestElementTable2(itsNatDoc);
        new TestElementTableStructure(itsNatDoc);

        new TestElementTableWithoutTable(itsNatDoc);
        new TestElementTableWithoutTable2(itsNatDoc);

        new TestTreeRootFixed(itsNatDoc);
        new TestTreeRootRemovable(itsNatDoc);
        new TestTreeRootRemovable2(itsNatDoc);
        new TestTreeRootRemovable3(itsNatDoc);
        new TestTreeRootRemovable4(itsNatDoc);
        new TestTreeRootRemovable5(itsNatDoc);
        new TestTreeRootRemovable6(itsNatDoc);
        new TestTreeNoRoot(itsNatDoc);
        new TestTreeTable(itsNatDoc);
        new TestTreeTable2(itsNatDoc);
        new TestTreeTableNoRoot(itsNatDoc);
        new TestTreeCustom(itsNatDoc);

        new TestCSSProperties(itsNatDoc);

        new TextScriptGenerator(itsNatDoc);

        Shared.setRemoteControlLink(request,response);

        new TestSerialization(request);
    }

}
