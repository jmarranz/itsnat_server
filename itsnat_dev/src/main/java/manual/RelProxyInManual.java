package manual;

import javax.swing.DefaultButtonModel;
import manual.coretut.CoreExampleLoadListener;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.shared.TestUtil;
import test.web.shared.EventListenerSerial;

public class RelProxyInManual 
{
    public static void What_type_of_code_can_be_reloaded()
    {
String pathPrefix = null;
ItsNatHttpServlet itsNatServlet = null;
ItsNatDocumentTemplate docTemplate = itsNatServlet.registerItsNatDocumentTemplate("manual.core.example","text/html", pathPrefix + "core_example.html");
docTemplate.addItsNatServletRequestListener(new CoreExampleLoadListener());
    }
    
    public static void What_type_of_code_can_be_reloaded_2()
    {    
ItsNatHTMLDocument itsNatDoc = null;        
EventListener listener = new EventListener()
{
   @Override
   public void handleEvent(Event evt) {
    //...
   }
};
itsNatDoc.addUserEventListener(null,"rem_ctrl_request",listener);
        
    }
    
    public static void What_type_of_code_can_be_reloaded_3()
    {    
Element element = null;        
EventListener listener = new EventListener()
{
    @Override
    public void handleEvent(Event evt)
    {
        //...
    }
};
((EventTarget)element).addEventListener("click",listener,false);       
    }    

    public static void What_type_of_code_can_be_reloaded_4()
    {    
ItsNatHTMLInputButton input = null;
input.addEventListener("click",new EventListener() {
    @Override
    public void handleEvent(Event evt) {
       //...          
    }
});                    
    }    
}
