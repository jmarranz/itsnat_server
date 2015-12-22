/*
 * TestAsyncServerResponse.java
 *
 * Created on 3 de enero de 2007, 12:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatNormalEvent;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.event.ItsNatUserEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.web.shared.EventListenerSerial;

/**
 *
 * @author jmarranz
 */
public class TestDroidUserListener extends TestDroidBase implements EventListener
{
    protected Element link;

    /**
     * Creates a new instance of TestAsyncServerResponse
     */
    public TestDroidUserListener(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testUserEventId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }


    @Override
    public void handleEvent(final Event evt)
    {
        ItsNatNormalEvent itsNatEvent = (ItsNatNormalEvent)evt;
        Document doc = getDocument();

        Element testLauncherHidden = getDocument().getElementById("testUserEventHiddenId");        
        
        final Element testLog = getDocument().getElementById("testUserEventLogId");        
        
        EventListener listener = new EventListenerSerial()
        {
            @Override
            public void handleEvent(Event evt)
            {
                ItsNatUserEvent userEvt = (ItsNatUserEvent)evt;
                EventTarget currTarget = userEvt.getCurrentTarget();

                String model = (String)userEvt.getExtraParam("model");                
                if (model == null || model.isEmpty()) throw new RuntimeException("Failed test");
                
                String tagName = (currTarget != null ? ((Element)currTarget).getTagName() : null);                

                String tagNameMsg = tagName;
                int count;
                if ("ScrollView".equals(tagName)) count = 1;
                else if (tagName == null) { count = 2; tagNameMsg = "(no curr target)"; }
                else if ("TextView".equals(tagName)) count = 3;
                else throw new RuntimeException("Unexpected " + tagName);                

                logToTextView(testLog,"OK " + count + "/3 " + tagNameMsg  + " Model: " + model + "\"\n");                 

                String name = userEvt.getName();
                
                itsNatDoc.removeUserEventListener(currTarget,name,this);
            }
        };

        ParamTransport modelParam = new CustomParamTransport("model","android.os.Build.MODEL");        
        ParamTransport[] extraParams = new ParamTransport[]{ modelParam };         
        
        String name = "myUserAction";
        
        itsNatDoc.addUserEventListener((EventTarget)doc.getDocumentElement(),name,listener,itsNatEvent.getCommMode(),extraParams,null,-1);
        itsNatDoc.addCodeToSend("itsNatDoc.fireUserEvent(itsNatDoc.getRootView(),\"" + name + "\");");

        itsNatDoc.addUserEventListener(null,"myUserAction",listener,itsNatEvent.getCommMode(),extraParams,null,-1);
        itsNatDoc.addCodeToSend("itsNatDoc.fireUserEvent(null,\"" + name + "\");");

        name = "myUserAction2";        
        
        itsNatDoc.addUserEventListener((EventTarget)testLauncherHidden,name,listener,itsNatEvent.getCommMode(),(ParamTransport[])null,null,-1);
        String code = "";
        code += "UserEvent userEvt = itsNatDoc.createUserEvent(\"" + name + "\");";
        code += "userEvt.setExtraParam(\"model\",android.os.Build.MODEL);";     
        code += "itsNatDoc.dispatchUserEvent(" + itsNatDoc.getScriptUtil().getNodeReference(testLauncherHidden) + ",userEvt);";
        itsNatDoc.addCodeToSend(code);
    }
}
