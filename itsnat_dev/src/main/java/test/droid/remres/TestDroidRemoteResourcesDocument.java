/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.remres;

import java.io.Serializable;
import org.itsnat.comp.android.widget.TextView;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import static test.droid.shared.TestDroidBase.ANDROID_NS;

public class TestDroidRemoteResourcesDocument implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element rootElem;


    public TestDroidRemoteResourcesDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request)
    {      
        // HttpServletRequest httpReq = (HttpServletRequest)request.getServletRequest();       
        
        this.itsNatDoc = itsNatDoc;
      
        Document doc = itsNatDoc.getDocument();
        this.rootElem = doc.getDocumentElement();
        ((EventTarget)rootElem).addEventListener("load", this, false);         
        
        new TestDroidRemoteResFragmentInsertionInnerXML(itsNatDoc);
        new TestDroidRemoteResFragmentInsertionUsingAPI(itsNatDoc);        
    }

    @Override
    public void handleEvent(Event evt)
    {
        Document doc = itsNatDoc.getDocument();     
        
        Element testClipDrawable = doc.getElementById("testClipDrawableId");
        String testClipDrawableRef = itsNatDoc.getScriptUtil().getNodeReference(testClipDrawable);
        itsNatDoc.addCodeToSend("var view = " + testClipDrawableRef + ";");
        itsNatDoc.addCodeToSend("view.getBackground().setLevel(5000);"); // La mitad se verá

        Element testClipDrawable2 = doc.getElementById("testClipDrawableId2");
        String testClipDrawableRef2 = itsNatDoc.getScriptUtil().getNodeReference(testClipDrawable2);
        itsNatDoc.addCodeToSend("var view = " + testClipDrawableRef2 + ";");
        itsNatDoc.addCodeToSend("view.getBackground().setLevel(5000);"); // La mitad se verá        
        
    }

}
