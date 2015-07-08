/*
 * TestMobileDocument.java
 *
 * Created on 12 de agosto de 2007, 9:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.mobile;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import test.web.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestMobileDocument
{
    protected ItsNatHTMLDocument itsNatDoc;

    /**
     * Creates a new instance of TestMobileDocument
     */
    public TestMobileDocument(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;
        load(request);
    }

    public void load(ItsNatServletRequest request)
    {
        new TestMobileEventLoad(itsNatDoc);
        new TestMobileSelectMultiple(itsNatDoc,request);

        new TestMobileFreeListMultiple(itsNatDoc);
        new TestMobileCheckBox(itsNatDoc);

        new TestEmptyAnswer(itsNatDoc);

        new TestMobileHTMLModalLayer(itsNatDoc);


        new TestSerialization(request);
    }
}
