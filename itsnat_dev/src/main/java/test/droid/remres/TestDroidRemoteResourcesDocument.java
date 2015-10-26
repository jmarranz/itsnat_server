/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.remres;

import java.io.Serializable;
import org.itsnat.comp.android.graphics.drawable.ClipDrawable;
import org.itsnat.comp.android.widget.TextView;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class TestDroidRemoteResourcesDocument implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element rootElem;


    public TestDroidRemoteResourcesDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request)
    {      
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
        
        Element testClipDrawableElem = doc.getElementById("testClipDrawableId");        
        TextView testClipDrawableComp = (TextView)itsNatDoc.getItsNatComponentManager().createItsNatComponent(testClipDrawableElem);
        ClipDrawable clipDrawable = testClipDrawableComp.getBackground(ClipDrawable.class);
        clipDrawable.setLevel(5000); // Half of the image is shown
        
        Element testClipDrawableElem2 = doc.getElementById("testClipDrawableId2");        
        TextView testClipDrawableComp2 = (TextView)itsNatDoc.getItsNatComponentManager().createItsNatComponent(testClipDrawableElem2);
        ClipDrawable clipDrawable2 = testClipDrawableComp2.getBackground(ClipDrawable.class);
        clipDrawable2.setLevel(5000); // Half of the image is shown
        
        /*
        Element testClipDrawable = doc.getElementById("testClipDrawableId");        
        String testClipDrawableRef = itsNatDoc.getScriptUtil().getNodeReference(testClipDrawable);
        itsNatDoc.addCodeToSend("var view = " + testClipDrawableRef + ";");
        itsNatDoc.addCodeToSend("view.getBackground().setLevel(5000);"); // Half of the image is shown

        Element testClipDrawable2 = doc.getElementById("testClipDrawableId2");
        String testClipDrawableRef2 = itsNatDoc.getScriptUtil().getNodeReference(testClipDrawable2);
        itsNatDoc.addCodeToSend("var view = " + testClipDrawableRef2 + ";");
        itsNatDoc.addCodeToSend("view.getBackground().setLevel(5000);"); // Half of the image is shown    
        */
    }

}
