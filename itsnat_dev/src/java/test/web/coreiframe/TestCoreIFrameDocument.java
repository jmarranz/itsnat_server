/*
 * TestCoreLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.coreiframe;

import test.web.core.TestIFrameInsertion;
import java.io.Serializable;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.html.ItsNatHTMLDocument;
import test.web.shared.Shared;
import test.web.shared.TestIFrameBoundHTML;
import test.web.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestCoreIFrameDocument implements Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;

    /**
     * Creates a new instance of TestCoreLoadListener
     */
    public TestCoreIFrameDocument(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = itsNatDoc;

        new TestIFrameInsertion(itsNatDoc);

        new TestIFrameBoundHTML(itsNatDoc);

        Shared.setRemoteControlLink(request,response);

        new TestSerialization(request);
    }

}
