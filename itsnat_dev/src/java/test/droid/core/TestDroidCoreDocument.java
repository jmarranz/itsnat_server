/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.w3c.dom.Element;

public class TestDroidCoreDocument implements Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element elem;


    public TestDroidCoreDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;

        new TestDroidMiscAutomatic(itsNatDoc);
        new TestDroidEventTimeout(itsNatDoc);    
        new TestDroidServerException(itsNatDoc);
        new TestDroidClientException(itsNatDoc);        
        new TestDroidNativeListeners(itsNatDoc); 
        new TestDroidXHR_ASYNC(itsNatDoc);
        new TestDroidViewInsertionAndSetAttributes(itsNatDoc);
        new TestDroidCustomViewInsertion(itsNatDoc);
        new TestDroidViewTreeInsertion(itsNatDoc);
        new TestDroidViewTreeRemoving(itsNatDoc);
        new TestDroidScriptUtil(itsNatDoc);
        new TestDroidTouchEvent(itsNatDoc);
        new TestDroidEventPreSendCode(itsNatDoc);
        new TestDroidParamTransport(itsNatDoc);
        new TestDroidCapture(itsNatDoc);   
        new TestDroidFragmentInsertion(itsNatDoc);
    }

}