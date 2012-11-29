package org.itsnat.batik.applet;

import java.io.Serializable;
import java.util.Locale;
import java.util.MissingResourceException;
import org.apache.batik.css.engine.CSSNavigableDocument;
import org.apache.batik.css.engine.CSSNavigableDocumentListener;
import org.apache.batik.dom.ExtendedNode;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.xbl.NodeXBL;
import org.apache.batik.dom.xbl.XBLManagerData;
import org.apache.batik.i18n.Localizable;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.DocumentCSS;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.stylesheets.StyleSheetList;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathNSResolver;

/**
 * Esta clase tiene como única finalidad permitir el binding de la propiedad
 * itsNatDoc (se usará setItsNatDoc(...)) que se obtendrá también a través de getItsNatDoc()
 * desde JavaScript, pues un objeto nativo Java (el "document" que define
 * Batik es un ItsNatSVGOMDocument) no permite la asociación de nuevas propiedades y métodos.
 * Podríamos intentar hacer un wrapper ScriptableObject pero el trabajo
 * de puenteo entre los métodos expuestos a JavaScript y los métodos nativos
 * sería un trabajo INMENSO y total en ItsNat sólo necesitamos hacer binding de la
 * propiedad itsNatDoc (el método getItsNatDoc ya está definido).
 *
 * Implementamos todos los métodos posibles de SVGOMDocument que puedan
 * interesar a un programador JavaScript, quitamos alguna interface IdContainer
 * que no está en los .jar usados por el applet (no están todos).
 * Implementamos sólo interfaces pues a nivel estándar la API del DOM es a base
 * de interfaces (algunas no son estándar pero en fin), sería rarísimo que
 * un usuario desde JavaScript accediera a métodos y atributos internos de Batik,
 * si es así siempre puede acceder al objeto original via getSVGOMDocument()
 * 
 * @author jmarranz
 */
public abstract class ItsNatSVGOMDocument implements
        /* SVGOMDocument: */ SVGDocument,SVGConstants,CSSNavigableDocument, /*,IdContainer*/
        /* AbstractStylableDocument: */ DocumentCSS, DocumentView,
        /* AbstractDocument: */ Document, DocumentEvent, DocumentTraversal, Localizable, XPathEvaluator,
        /* AbstractParentNode, AbstractNode */ ExtendedNode, NodeXBL, XBLManagerData, Serializable
{
   
    public ItsNatSVGOMDocument()
    {
    }

    public abstract SVGOMDocument getSVGOMDocument();

    public Object getUserData(String key)
    {
        // No está en el interface Document de Java 1.4 pero es importante
        // tenerlo en JavaScript
        return getSVGOMDocument().getUserData(key);
    }
    
    public Object setUserData(String key,Object data,UserDataHandler handler)
    {
        // No está en el interface Document de Java 1.4 pero es importante
        // tenerlo en JavaScript
        return getSVGOMDocument().setUserData(key,data,handler);
    }

    public String getTitle()
    {
        return getSVGOMDocument().getTitle();
    }

    public String getReferrer()
    {
        return getSVGOMDocument().getReferrer();
    }

    public String getDomain()
    {
        return getSVGOMDocument().getDomain();
    }

    public String getURL()
    {
        return getSVGOMDocument().getURL();
    }

    public SVGSVGElement getRootElement()
    {
        return getSVGOMDocument().getRootElement();
    }

    public DOMImplementation getImplementation()
    {
        return getSVGOMDocument().getImplementation();
    }

    public DocumentFragment createDocumentFragment()
    {
        return getSVGOMDocument().createDocumentFragment();
    }

    public DocumentType getDoctype()
    {
        return getSVGOMDocument().getDoctype();
    }

    public Element getDocumentElement()
    {
        return getSVGOMDocument().getDocumentElement();
    }

    public Attr createAttribute(String name) throws DOMException
    {
        return getSVGOMDocument().createAttribute(name);
    }

    public CDATASection createCDATASection(String data) throws DOMException
    {
        return getSVGOMDocument().createCDATASection(data);
    }

    public Comment createComment(String data)
    {
        return getSVGOMDocument().createComment(data);
    }

    public Element createElement(String tagName) throws DOMException
    {
        return getSVGOMDocument().createElement(tagName);
    }

    public Element getElementById(String elementId)
    {
        return getSVGOMDocument().getElementById(elementId);
    }

    public EntityReference createEntityReference(String name) throws DOMException
    {
        return getSVGOMDocument().createEntityReference(name);
    }

    public Node importNode(Node importedNode, boolean deep) throws DOMException
    {
        return getSVGOMDocument().importNode(importedNode, deep);
    }

    public NodeList getElementsByTagName(String tagname)
    {
        return getSVGOMDocument().getElementsByTagName(tagname);
    }

    public Text createTextNode(String data)
    {
        return getSVGOMDocument().createTextNode(data);
    }

    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException
    {
        return getSVGOMDocument().createAttributeNS(namespaceURI, qualifiedName);
    }

    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException
    {
        return getSVGOMDocument().createElementNS(namespaceURI, qualifiedName);
    }

    public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
    {
        return getSVGOMDocument().getElementsByTagNameNS(namespaceURI, localName);
    }

    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException
    {
        return getSVGOMDocument().createProcessingInstruction(target, data);
    }

    public short getNodeType()
    {
        return getSVGOMDocument().getNodeType();
    }

    public void normalize()
    {
        getSVGOMDocument().normalize();
    }

    public boolean hasAttributes()
    {
        return getSVGOMDocument().hasAttributes();
    }

    public boolean hasChildNodes()
    {
        return getSVGOMDocument().hasChildNodes();
    }

    public String getLocalName()
    {
        return getSVGOMDocument().getLocalName();
    }

    public String getNamespaceURI()
    {
        return getSVGOMDocument().getNamespaceURI();
    }

    public String getNodeName()
    {
        return getSVGOMDocument().getNodeName();
    }

    public String getNodeValue() throws DOMException
    {
        return getSVGOMDocument().getNodeValue();
    }

    public String getPrefix()
    {
        return getSVGOMDocument().getPrefix();
    }

    public void setNodeValue(String nodeValue) throws DOMException
    {
        getSVGOMDocument().setNodeValue(nodeValue);
    }

    public void setPrefix(String prefix) throws DOMException
    {
        getSVGOMDocument().setPrefix(prefix);
    }

    public Document getOwnerDocument()
    {
        return getSVGOMDocument().getOwnerDocument();
    }

    public NamedNodeMap getAttributes()
    {
        return getSVGOMDocument().getAttributes();
    }

    public Node getFirstChild()
    {
        return getSVGOMDocument().getFirstChild();
    }

    public Node getLastChild()
    {
        return getSVGOMDocument().getLastChild();
    }

    public Node getNextSibling()
    {
        return getSVGOMDocument().getNextSibling();
    }

    public Node getParentNode()
    {
        return getSVGOMDocument().getParentNode();
    }

    public Node getPreviousSibling()
    {
        return getSVGOMDocument().getPreviousSibling();
    }

    public Node cloneNode(boolean deep)
    {
        return getSVGOMDocument().cloneNode(deep);
    }

    public NodeList getChildNodes()
    {
        return getSVGOMDocument().getChildNodes();
    }

    public boolean isSupported(String feature, String version)
    {
        return getSVGOMDocument().isSupported(feature, version);
    }

    public Node appendChild(Node newChild) throws DOMException
    {
        return getSVGOMDocument().appendChild(newChild);
    }

    public Node removeChild(Node oldChild) throws DOMException
    {
        return getSVGOMDocument().removeChild(oldChild);
    }

    public Node insertBefore(Node newChild, Node refChild) throws DOMException
    {
        return getSVGOMDocument().insertBefore(newChild, refChild);
    }

    public Node replaceChild(Node newChild, Node oldChild) throws DOMException
    {
        return getSVGOMDocument().replaceChild(newChild, oldChild);
    }

    public Event createEvent(String eventType) throws DOMException
    {
        return getSVGOMDocument().createEvent(eventType);
    }

    public void addCSSNavigableDocumentListener(CSSNavigableDocumentListener arg0)
    {
        getSVGOMDocument().addCSSNavigableDocumentListener(arg0);
    }

    public void removeCSSNavigableDocumentListener(CSSNavigableDocumentListener arg0)
    {
        getSVGOMDocument().removeCSSNavigableDocumentListener(arg0);
    }

    public CSSStyleDeclaration getOverrideStyle(Element elt, String pseudoElt)
    {
        return getSVGOMDocument().getOverrideStyle(elt, pseudoElt);
    }

    public StyleSheetList getStyleSheets()
    {
        return getSVGOMDocument().getStyleSheets();
    }

    public AbstractView getDefaultView()
    {
        return getSVGOMDocument().getDefaultView();
    }

    public NodeIterator createNodeIterator(Node root, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion) throws DOMException
    {
        return getSVGOMDocument().createNodeIterator(root, whatToShow, filter, entityReferenceExpansion);
    }

    public TreeWalker createTreeWalker(Node root, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion) throws DOMException
    {
        return getSVGOMDocument().createTreeWalker(root, whatToShow, filter, entityReferenceExpansion);
    }

    public void setLocale(Locale arg0)
    {
        getSVGOMDocument().setLocale(arg0);
    }

    public Locale getLocale()
    {
        return getSVGOMDocument().getLocale();
    }

    public String formatMessage(String arg0, Object[] arg1) throws MissingResourceException
    {
        return getSVGOMDocument().formatMessage(arg0,arg1);
    }

    public XPathExpression createExpression(String arg0, XPathNSResolver arg1) throws XPathException, DOMException
    {
        return getSVGOMDocument().createExpression(arg0, arg1);
    }

    public XPathNSResolver createNSResolver(Node arg0)
    {
        return getSVGOMDocument().createNSResolver(arg0);
    }

    public Object evaluate(String arg0, Node arg1, XPathNSResolver arg2, short arg3, Object arg4) throws XPathException, DOMException
    {
        return getSVGOMDocument().evaluate(arg0, arg1, arg2, arg3, arg4);
    }

    public void setNodeName(String arg0)
    {
        getSVGOMDocument().setNodeName(arg0);
    }

    public boolean isReadonly()
    {
        return getSVGOMDocument().isReadonly();
    }

    public void setReadonly(boolean arg0)
    {
        getSVGOMDocument().setReadonly(arg0);
    }

    public void setOwnerDocument(Document arg0)
    {
        getSVGOMDocument().setOwnerDocument(arg0);
    }

    public void setParentNode(Node arg0)
    {
        getSVGOMDocument().setParentNode(arg0);
    }

    public void setPreviousSibling(Node arg0)
    {
        getSVGOMDocument().setPreviousSibling(arg0);
    }

    public void setNextSibling(Node arg0)
    {
        getSVGOMDocument().setNextSibling(arg0);
    }

    public void setSpecified(boolean arg0)
    {
        getSVGOMDocument().setSpecified(arg0);
    }

    public EventSupport getEventSupport()
    {
        return getSVGOMDocument().getEventSupport();
    }

    public NodeEventTarget getParentNodeEventTarget()
    {
        return getSVGOMDocument().getParentNodeEventTarget();
    }

    public void addEventListenerNS(String arg0, String arg1, EventListener arg2, boolean arg3, Object arg4)
    {
        getSVGOMDocument().addEventListenerNS(arg0, arg1, arg2, arg3, arg4);
    }

    public void removeEventListenerNS(String arg0, String arg1, EventListener arg2, boolean arg3)
    {
        getSVGOMDocument().removeEventListenerNS(arg0, arg1, arg2, arg3);
    }

    public boolean willTriggerNS(String arg0, String arg1)
    {
        return getSVGOMDocument().willTriggerNS(arg0, arg1);
    }

    public boolean hasEventListenerNS(String arg0, String arg1)
    {
        return getSVGOMDocument().hasEventListenerNS(arg0, arg1);
    }

    public boolean dispatchEvent(Event evt) throws EventException
    {
        return getSVGOMDocument().dispatchEvent(evt);
    }

    public void addEventListener(String type, EventListener listener, boolean useCapture)
    {
        getSVGOMDocument().addEventListener(type, listener, useCapture);
    }

    public void removeEventListener(String type, EventListener listener, boolean useCapture)
    {
        getSVGOMDocument().removeEventListener(type, listener, useCapture);
    }

    public Node getXblParentNode()
    {
        return getSVGOMDocument().getXblParentNode();
    }

    public NodeList getXblChildNodes()
    {
        return getSVGOMDocument().getXblChildNodes();
    }

    public NodeList getXblScopedChildNodes()
    {
        return getSVGOMDocument().getXblScopedChildNodes();
    }

    public Node getXblFirstChild()
    {
        return getSVGOMDocument().getXblFirstChild();
    }

    public Node getXblLastChild()
    {
        return getSVGOMDocument().getXblLastChild();
    }

    public Node getXblPreviousSibling()
    {
        return getSVGOMDocument().getXblPreviousSibling();
    }

    public Node getXblNextSibling()
    {
        return getSVGOMDocument().getXblNextSibling();
    }

    public Element getXblFirstElementChild()
    {
        return getSVGOMDocument().getXblFirstElementChild();
    }

    public Element getXblLastElementChild()
    {
        return getSVGOMDocument().getXblLastElementChild();
    }

    public Element getXblPreviousElementSibling()
    {
        return getSVGOMDocument().getXblPreviousElementSibling();
    }

    public Element getXblNextElementSibling()
    {
        return getSVGOMDocument().getXblNextElementSibling();
    }

    public Element getXblBoundElement()
    {
        return getSVGOMDocument().getXblBoundElement();
    }

    public Element getXblShadowTree()
    {
        return getSVGOMDocument().getXblShadowTree();
    }

    public NodeList getXblDefinitions()
    {
        return getSVGOMDocument().getXblDefinitions();
    }

    public Object getManagerData()
    {
        return getSVGOMDocument().getManagerData();
    }

    public void setManagerData(Object arg0)
    {
        getSVGOMDocument().setManagerData(arg0);
    }
}
