/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inexp.jooxex;

import java.util.List;
import static org.joox.JOOX.*;


import javax.xml.xpath.XPathExpressionException;


import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class JOOXExampleDocument implements EventListener
{
    protected ItsNatHTMLDocument itsNatDoc;
    
    protected ItsNatHTMLInputText htmlCodeInput;  
    protected Element htmlAddElem;    
    protected ItsNatHTMLInputText xpathExpressionInput;
    protected Element xpathEvaluateElem;
    protected Element xpathResultsElem;    

    public JOOXExampleDocument(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        HTMLDocument doc = itsNatDoc.getHTMLDocument();
   
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        
        
        this.htmlCodeInput = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("htmlCodeId");
        htmlCodeInput.setText("<span style='color:red'>HELLO</span>");        
        
        this.htmlAddElem = doc.getElementById("htmlAddId");
        ((EventTarget)htmlAddElem).addEventListener("click", this, false);        
        
        this.xpathExpressionInput = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("xpathExpressionId");
        xpathExpressionInput.setText("//*[name() = 'div']");
        
        this.xpathEvaluateElem = doc.getElementById("xpathEvaluateId");
        ((EventTarget)xpathEvaluateElem).addEventListener("click", this, false);

        this.xpathResultsElem = doc.getElementById("xpathResultsId");
    }

    public void handleEvent(Event evt)
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();        

        if (evt.getCurrentTarget() == htmlAddElem )
        {
            $(doc).find(":ul#someULId").children() // <li> items
                    .eq(2)  // <li> item 2
                    .children() // content of <li>
                    .eq(0) // <span>
                    .children().remove();
            
            
            String htmlCode = htmlCodeInput.getText();            
            
            if (htmlCode.length() > 10 * 1024) return;
            
            $(doc).find(":ul#someULId").children() // <li> items
                    .eq(2)  // <li> item 2
                    .children() // content of <li>
                    .eq(0) // <span>
                    .append(htmlCode);            

        }
        else if (evt.getCurrentTarget() == xpathEvaluateElem)
        {
            $(xpathResultsElem).children().remove();
            
            String expression = xpathExpressionInput.getText();

            try
            {
                List<Element> elemList = $(doc).xpath(expression).get();     

                for(Element elem : elemList) 
                {
                    Element child = doc.createElement("div");
                    String value = "Element: " + elem.getNodeName();

                    child.appendChild(doc.createTextNode(value));
                    xpathResultsElem.appendChild(child);
                }   
            }
            catch(Exception ex)
            {
                //ex.printStackTrace();

                String exMsg = ex.toString();

                Throwable cause = ex.getCause(); 
                if (cause != null) exMsg = exMsg + "\n" + cause.toString();

                Text text = doc.createTextNode("ERROR: " + exMsg);
                xpathResultsElem.appendChild(text);            
            }        
       }
    }

}
