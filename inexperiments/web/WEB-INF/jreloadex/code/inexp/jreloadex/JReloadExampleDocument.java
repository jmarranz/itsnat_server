package inexp.jreloadex;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class JReloadExampleDocument
{
    protected ItsNatHTMLDocument itsNatDoc; // ItsNatHTMLDocument
    protected ItsNatHTMLInputText textInput; // ItsNatHTMLInputText
    protected Element resultsElem; // Element   

    public static class AuxMember 
    { 
        public static void log()
        {
            System.out.println("JReloadExampleDocument.AuxMember: 8 " + AuxMember.class.getClassLoader().hashCode());
        }        
    }
           
    
    public JReloadExampleDocument(ItsNatServletRequest request,ItsNatHTMLDocument itsNatDoc,FalseDB db)
    {
        this.itsNatDoc = itsNatDoc;

        if (db.getCityList().size() != 3) 
            throw new RuntimeException("Unexpected");

        HTMLDocument doc = itsNatDoc.getHTMLDocument();

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.textInput = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("inputId");

        Element buttonElem = doc.getElementById("buttonId");
        ((EventTarget)buttonElem).addEventListener("click", new EventListener(){
            public void handleEvent(Event evt) 
            {
                String text = textInput.getText(); 
                resultsElem.setTextContent(text);
 System.out.println("JReloadExampleDocument Inner 20 " + this.getClass().getClassLoader().hashCode());                
            }},
            false);

        this.resultsElem = doc.getElementById("resultsId");
        
System.out.println("JReloadExampleDocument 29 " + this.getClass().getClassLoader().hashCode());        
        AuxMember.log();
        JReloadExampleAux.log();

    }

}
