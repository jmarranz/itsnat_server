/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.remres;

import java.io.Serializable;
import org.itsnat.comp.android.graphics.drawable.AnimationDrawable;
import org.itsnat.comp.android.graphics.drawable.ClipDrawable;
import org.itsnat.comp.android.graphics.drawable.LevelListDrawable;
import org.itsnat.comp.android.graphics.drawable.RotateDrawable;
import org.itsnat.comp.android.graphics.drawable.ScaleDrawable;
import org.itsnat.comp.android.graphics.drawable.TransitionDrawable;
import org.itsnat.comp.android.widget.TextView;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.web.shared.EventListenerSerial;

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
        new TestDroidRemoteResFragmentInsertionUsingDOMAPI(itsNatDoc);

        final Element testChangeDrawableElem = doc.getElementById("testChangeDrawableId");
        ((EventTarget)testChangeDrawableElem).addEventListener("click", new EventListenerSerial() {
            @Override
            public void handleEvent(Event evt)
            {
                testChangeDrawableElem.setAttribute("android:background", "@remote:drawable/droid/res/drawable/test_nine_patch_remote.xml");
            }
        }, false);


        //TextView testChangeDrawableComp = (TextView)itsNatDoc.getItsNatComponentManager().createItsNatComponent(testChangeDrawableElem);
        //ClipDrawable clipDrawable = testChangeDrawableComp.getBackground(ClipDrawable.class);

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

        Element testTransitionDrawableElem = doc.getElementById("testTransitionDrawableId");
        TextView testTransitionDrawableComp = (TextView)itsNatDoc.getItsNatComponentManager().createItsNatComponent(testTransitionDrawableElem);
        final TransitionDrawable transitionDrawable = testTransitionDrawableComp.getBackground(TransitionDrawable.class);
        ((EventTarget)testTransitionDrawableElem).addEventListener("click", new EventListener()
        {
            @Override
            public void handleEvent(Event evt)
            {
                transitionDrawable.startTransition(1000);
            }
        }, false);

        Element testLevelListDrawableElem = doc.getElementById("testLevelListDrawableId");
        TextView testLevelListDrawableComp = (TextView)itsNatDoc.getItsNatComponentManager().createItsNatComponent(testLevelListDrawableElem);
        final LevelListDrawable levelListDrawable = testLevelListDrawableComp.getBackground(LevelListDrawable.class);
        levelListDrawable.setLevel(1);
        ((EventTarget)testLevelListDrawableElem).addEventListener("click", new EventListener()
        {
            @Override
            public void handleEvent(Event evt)
            {
                levelListDrawable.setLevel(4);
            }
        }, false);

        Element testScaleDrawableElem = doc.getElementById("testScaleDrawableId");
        TextView testScaleDrawableComp = (TextView)itsNatDoc.getItsNatComponentManager().createItsNatComponent(testScaleDrawableElem);
        ScaleDrawable scaleDrawable = testScaleDrawableComp.getBackground(ScaleDrawable.class);
        scaleDrawable.setLevel(1);

        Element testAnimationDrawableElem = doc.getElementById("testAnimationDrawableId");
        TextView testAnimationDrawableComp = (TextView)itsNatDoc.getItsNatComponentManager().createItsNatComponent(testAnimationDrawableElem);
        AnimationDrawable animationDrawable = testAnimationDrawableComp.getBackground(AnimationDrawable.class);
        animationDrawable.start();

        Element testRotateDrawableElem = doc.getElementById("testRotateDrawableId");
        TextView testRotateDrawableComp = (TextView)itsNatDoc.getItsNatComponentManager().createItsNatComponent(testRotateDrawableElem);
        RotateDrawable rotateDrawable = testRotateDrawableComp.getBackground(RotateDrawable.class);
        rotateDrawable.setLevel(10000);

    }

}
