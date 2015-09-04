package manual;

import manual.coretut.CoreExampleLoadListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

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
   public void handleEvent(Event evt)
   {
    //...
   }
};
itsNatDoc.addUserEventListener(null,"rem_ctrl_request",listener);
    }
    
}
