/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inexp.hybridcs;

import inexp.xpathex.UniversalNamespaceResolver;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLImageElement;

public class HybridCSDocument implements EventListener
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element carouselContainerElem;
    protected Element carouselElem;
    protected Element itemPatternElem;
    protected ItsNatHTMLInputText imgURLComp;
    protected Element addImageFirstElem;
    protected Element addImageLastElem;
    protected DocumentFragment content;
    protected int size;

    public HybridCSDocument(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        HTMLDocument doc = itsNatDoc.getHTMLDocument();
   
         
try
{

    XPathFactory factory = XPathFactory.newInstance();
    XPath xpath = factory.newXPath();  
    xpath.setNamespaceContext(new UniversalNamespaceResolver(doc));    
    XPathExpression exp = xpath.compile("//*[name()='div'][1]"); //   "//*[name()='h1' or name()='h2']"
    NodeList res = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
    //NodeList res = (NodeList)xpath.evaluate("/body", doc, XPathConstants.NODESET);
    for(int i = 0; i < res.getLength(); i++)
    {
        System.out.println(res.item(i));
    }       
}
catch(XPathExpressionException ex)
{
    ex.printStackTrace();
}        
 

/*        
try
{
    XPath xpath = new DOMXPath("//h1");
    SimpleNamespaceContext context = new SimpleNamespaceContext()
    {
        public String translateNamespacePrefixToUri(String string)        
        {
            String res = super.translateNamespacePrefixToUri(string);
            return res;
        }
    };
    
    context.addNamespace("", "http://www.w3.org/1999/xhtml");
    xpath.setNamespaceContext(context);
    List res = xpath.selectNodes(doc.getDocumentElement());    
    
            
    for(int i = 0; i < res.size(); i++)
    {
        System.out.println(res.get(i));
    }       
}
catch(Exception ex)
{
    ex.printStackTrace();    
}
*/


 /*
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
        
        this.carouselContainerElem = doc.getElementById("carouselContainerId");
        this.carouselElem = doc.getElementById("carouselId");
        this.itemPatternElem = ItsNatTreeWalker.getFirstChildElement(carouselElem);
        this.content = (DocumentFragment)itsNatDoc.disconnectChildNodesFromClient(carouselContainerElem);

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.imgURLComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("imgURLId");
        HTMLImageElement firstImgElem = (HTMLImageElement)itemPatternElem.getFirstChild();
        imgURLComp.setText(firstImgElem.getSrc());

        this.addImageFirstElem = doc.getElementById("addImageFirstId");
        ((EventTarget)addImageFirstElem).addEventListener("click", this, false);
        this.addImageLastElem = doc.getElementById("addImageLastId");
        ((EventTarget)addImageLastElem).addEventListener("click", this, false);

        ElementList list = itsNatDoc.getElementGroupManager().createElementList(carouselElem,false);
        this.size = list.getLength();

        StringBuilder code = new StringBuilder();
        code.append("jQuery(document).ready(function() {");
        code.append("  jQuery('#carouselId').jcarousel(); ");
        code.append("  }); ");
        
        itsNatDoc.addCodeToSend(code.toString());
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == addImageFirstElem || currTarget == addImageLastElem)
        {
            if (size == 30)
            {
                itsNatDoc.addCodeToSend("alert('Too many images');");
                return;
            }

            size++;
            
            int index;
            // Pattern: <li><img src="image url" width="75" height="75" alt="" /></li>
            Element newItemElem = (Element)itemPatternElem.cloneNode(true);
            HTMLImageElement newImgElem = (HTMLImageElement)newItemElem.getFirstChild();
            String newUrl = imgURLComp.getText();
            newImgElem.setSrc(newUrl);
            if (currTarget == addImageFirstElem)
            {
                carouselElem.insertBefore(newItemElem, carouselElem.getFirstChild());
                index = 1;
            }
            else
            {
                carouselElem.appendChild(newItemElem);
                index = size;
            }
            carouselContainerElem.appendChild(content);
            this.content = (DocumentFragment)itsNatDoc.disconnectChildNodesFromClient(carouselContainerElem);

            itsNatDoc.addCodeToSend("jQuery('#carouselId').jcarousel( { start:" + index + " });");
        }
    }
}
