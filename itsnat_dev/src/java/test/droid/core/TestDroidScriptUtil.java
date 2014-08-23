/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.script.ScriptUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidScriptUtil extends TestDroidBase implements EventListener
{
   
    public TestDroidScriptUtil(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        Element testLauncher = getDocument().getElementById("testScriptUtilId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
    }
    
    public void handleEvent(Event evt)
    {     
        Document doc = getDocument();
        Element testLauncherHidden = doc.getElementById("testScriptUtilHiddenId");  
        
        String code;
        ScriptUtil codeGen = itsNatDoc.getScriptUtil();

        code = codeGen.getCallMethodCode(codeGen.createScriptExpr("itsNatDoc"),"alert",new Object[]{"OK getCallMethodCode "},true);
        itsNatDoc.addCodeToSend(code);
        
        code = "var view = " + codeGen.getNodeReference(testLauncherHidden) + "; itsNatDoc.alert(\"view 1 :\" + view); itsNatDoc.alert(\"view 2 :\" + itsNatDoc.findViewByXMLId(\"testScriptUtilHiddenId\"));     if (view == itsNatDoc.findViewByXMLId(\"testScriptUtilHiddenId\")) itsNatDoc.alert(\"OK getNodeReference:\"); else itsNatDoc.alert(\"FAILED getNodeReference:\");";
        itsNatDoc.addCodeToSend(code);    
  
    }
    
}
