/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import javax.servlet.http.HttpServletRequest;
import test.droid.shared.TestDroidBase;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidMiscAutomatic extends TestDroidBase implements EventListener
{
   
    public TestDroidMiscAutomatic() // For RelProxy
    {
        super(null);
    }    
    
    public TestDroidMiscAutomatic(ItsNatDocument itsNatDoc,ItsNatServletRequest request)
    {
        super(itsNatDoc);

        Document doc = getDocument();
        Element rootElem = doc.getDocumentElement();

        ((EventTarget)rootElem).addEventListener("click", this,false);
        ((EventTarget)rootElem).addEventListener("touchstart", this,false);        
        ((EventTarget)rootElem).addEventListener("touchend", this,false);        
        
        
        ((EventTarget)rootElem).removeEventListener("click", this,false);            
        ((EventTarget)rootElem).removeEventListener("touchstart", this,false);        
        ((EventTarget)rootElem).removeEventListener("touchend", this,false);        
          
        itsNatDoc.addEventListener((EventTarget)rootElem, "load", this, false);
        itsNatDoc.addEventListener((EventTarget)rootElem, "unload", this, false);       
        
        if (!itsNatDoc.getItsNatDocumentTemplate().isFastLoadMode())
        {
            throw new RuntimeException("These <string> tests must be executed in fast load mode"); 
        }
        
        // Hay más de un <script> en el template inicial que DEBEN desaparecer en tiempo de carga antes de poder acceder al Document
        NodeList scripts = doc.getElementsByTagName("script");
        if (scripts.getLength() > 0) throw new RuntimeException("Unexpected <string> element");        
        
        Element elemTemp = itsNatDoc.getDocument().createElement("script");
        elemTemp.setTextContent("toast(\"LOADING \\n(OK testing on load <script> test 2)\");");
        rootElem.appendChild(elemTemp);  // Da igual donde se inserte pues se elimina inmediatamente      
        
        elemTemp = itsNatDoc.getDocument().createElement("script");
        
        HttpServletRequest httpReq = (HttpServletRequest)request.getServletRequest();
        String host = httpReq.getLocalAddr();
        int port = httpReq.getLocalPort();
        elemTemp.setAttribute("src", "http://" + host + ":" + port + "/itsnat_dev/bs/test_script_loading_3.bs");
        rootElem.appendChild(elemTemp);  // Da igual donde se inserte pues se elimina inmediatamente       
        
        scripts = doc.getElementsByTagName("script");
        if (scripts.getLength() > 0) throw new RuntimeException("Unexpected <string> element");           
    }
    
    @Override
    public void handleEvent(Event evt)
    {     
        String type = evt.getType();
        if (type.equals("load"))
        {
            itsNatDoc.addCodeToSend("var view = itsNatDoc.findViewByXMLId(\"testLoadListenerId\"); view.setText(view.getText() + \"OK\");");            
            
            Element rootElem = getDocument().getDocumentElement();            
            itsNatDoc.removeEventListener((EventTarget)rootElem, "load", this, false);  // No sirve para nada pero por si fallara
        }
        else if (type.equals("unload"))
        {
            ClientDocument clientDoc = itsNatDoc.getClientDocumentOwner(); // En el caso de control remoto NO interesa enviar este código
            clientDoc.addCodeToSend("var view = itsNatDoc.findViewByXMLId(\"testUnloadListenerId\"); view.setText(view.getText() + \"OK\");");            
        }        
        else
        {
            itsNatDoc.addCodeToSend("alert(\"Unexpected event\");");
        }
  
    }
    
}
