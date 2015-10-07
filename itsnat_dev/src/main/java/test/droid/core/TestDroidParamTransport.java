/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.event.ParamTransport;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidParamTransport extends TestDroidBase implements EventListener
{
   
    public TestDroidParamTransport(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testEventParamTransportId");        
        ParamTransport manufacturerParam = new CustomParamTransport("manufacturer","android.os.Build.MANUFACTURER");
        ParamTransport modelParam = new CustomParamTransport("model","android.os.Build.MODEL");        
        ParamTransport multivalue = new CustomParamTransport("multivalue","new String[]{\"one\",\"2\"}");        
        ParamTransport visibility = new NodePropertyTransport("getVisibility()",false);      
        itsNatDoc.addEventListener((EventTarget)testLauncher,"click",this,false,new ParamTransport[]{ manufacturerParam,modelParam,multivalue,visibility });        
    }
    
    @Override
    public void handleEvent(Event evt)
    {     
        ItsNatEvent evt2 = (ItsNatEvent)evt;
        
        Element logElem = getDocument().getElementById("testEventParamTransportLogId");         

        StringBuilder msg = new StringBuilder();
        msg.append("OK manufacturer/model: " + evt2.getExtraParam("manufacturer") + "/" + evt2.getExtraParam("model") + "\n"); 
        
        String[] multivalue = (String[])evt2.getExtraParamMultiple("multivalue");
        if (!"one".equals(multivalue[0])) throw new RuntimeException("Unexpected " + multivalue[0]);
        if (!"2".equals(multivalue[1])) throw new RuntimeException("Unexpected " + multivalue[1]);        
        msg.append("OK multivalue (expected: one 2): " + multivalue[0] + " " + multivalue[1] + "\n");        
        
        int visibility = Integer.parseInt((String)evt2.getExtraParam("getVisibility()"));   
        if (visibility != 0) throw new RuntimeException("Unexpected " + visibility);        
        msg.append("OK visibility (expected: 0): " + visibility + "\n");        
      
        
        logToTextView(logElem,msg.toString()); 
    }
    
}
