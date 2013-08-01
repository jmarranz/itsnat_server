/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inexp.xpathex;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class XPathExampleDocument implements EventListener
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected ItsNatHTMLInputText expressionInput;
    protected Element resultsElem;    

    public XPathExampleDocument(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        HTMLDocument doc = itsNatDoc.getHTMLDocument();
   
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.expressionInput = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("expressionId");
        expressionInput.setText("//*[name() = 'div']");

        
        Element evaluateElem = doc.getElementById("evaluateId");
        ((EventTarget)evaluateElem).addEventListener("click", this, false);

        this.resultsElem = doc.getElementById("resultsId");
    }

    public void handleEvent(Event evt)
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();        
        
        while (resultsElem.getFirstChild() != null) 
            resultsElem.removeChild(resultsElem.getFirstChild());        
        
        String expression = expressionInput.getText();
        try
        {

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();  
            xpath.setNamespaceContext(new UniversalNamespaceResolver(doc));    
            XPathExpression exp = xpath.compile(expression); //   "//*[name()='h1' or name()='h2']"
            NodeList nodeList = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
            

            for(int i = 0; i < nodeList.getLength(); i++)
            {
                Node node = nodeList.item(i);
                
                Element child = doc.createElement("div");
                String value;
                if (node.getNodeType() == Node.TEXT_NODE) value = "Text node: " + ((Text)node).getData();
                else if (node.getNodeType() == Node.ELEMENT_NODE) value = "Element: " + node.getNodeName();
                else 
                    value = node.getNodeName();
                
                child.appendChild(doc.createTextNode(value));
                resultsElem.appendChild(child);
            }        
        }
        catch(XPathExpressionException ex)
        {
            //ex.printStackTrace();
            
            String exMsg = ex.toString();

            Throwable cause = ex.getCause(); // javax.xml.transform.TransformerException
            if (cause != null) exMsg = exMsg + "\n" + cause.toString();
            
            Text text = doc.createTextNode("ERROR: " + exMsg);
            resultsElem.appendChild(text);            
        }        
    }
}


    /* Alternative with Saxon
   try  // http://stackoverflow.com/questions/926222/using-saxon-xpath-engine-in-java
   {
           System.setProperty("javax.xml.xpath.XPathFactory:"+NamespaceConstant.OBJECT_MODEL_SAXON, "net.sf.saxon.xpath.XPathFactoryImpl");
           XPathFactory factory = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);    

           //XPathFactory factory = net.sf.saxon.xpath.XPathFactoryImpl.newInstance();
           XPath xpath = factory.newXPath();

           XPathExpression expr = xpath.compile("h1");

           Object result = expr.evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
           NodeList res = (NodeList) result;        
       for(int i = 0; i < res.getLength(); i++)
       {
           System.out.println(res.item(i));
       }     
   }
   catch(Exception ex)
   {
       ex.printStackTrace();       
   }
   */    