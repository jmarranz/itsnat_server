/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.core.domimpl;

import org.itsnat.impl.core.domimpl.deleg.DelegateDocumentImpl;
import org.itsnat.impl.core.domimpl.deleg.DelegateNodeImpl;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.GenericDocument;
import org.apache.batik.dom.GenericDocumentType;
import org.apache.batik.dom.xbl.GenericXBLManager;
import org.apache.batik.i18n.LocalizableSupport;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.NameValue;
import org.itsnat.impl.core.domimpl.html.HTMLAnchorElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLAppletElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLAreaElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLBRElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLBaseElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLBaseFontElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLBodyElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLButtonElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLDListElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLDirectoryElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLDivElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLDocumentImpl;
import org.itsnat.impl.core.domimpl.html.HTMLElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLElementOtherImpl;
import org.itsnat.impl.core.domimpl.html.HTMLEmbedElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLFieldSetElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLFontElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLFormElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLFrameElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLFrameSetElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLHRElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLHeadElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLHeadingElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLHtmlElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLIFrameElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLImageElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLInputElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLIsIndexElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLLIElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLLabelElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLLegendElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLLinkElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLMapElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLMenuElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLMetaElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLModElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLOListElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLObjectElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLOptGroupElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLOptionElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLParagraphElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLParamElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLPreElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLQuoteElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLScriptElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLSelectElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLStyleElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLTableCaptionElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLTableCellElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLTableColElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLTableElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLTableRowElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLTableSectionElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLTextAreaElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLTitleElementImpl;
import org.itsnat.impl.core.domimpl.html.HTMLUListElementImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.util.WeakMapExpungeableImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public abstract class DocumentImpl extends GenericDocument
        implements DocumentView,ItsNatDocumentInternal
{
    protected final static Map<String,HTMLElementImpl> htmlElemFactories = new HashMap<String,HTMLElementImpl>();
    protected final static DocumentImpl dummyDoc = new HTMLDocumentImpl(null,BatikItsNatDOMImplementationImpl.getBatikItsNatDOMImplementation());
    protected final static HTMLElementOtherImpl htmlUnkownElemFactory = new HTMLElementOtherImpl("notvalid",dummyDoc);

    static
    {
        // 52 clases (son más tags por los HX etc)
        // dummyDoc es necesario para que el constructor no de error
        addHTMLFactory(new HTMLAnchorElementImpl("a",dummyDoc));
        addHTMLFactory(new HTMLAppletElementImpl("applet",dummyDoc));
        addHTMLFactory(new HTMLAreaElementImpl("area",dummyDoc));
        addHTMLFactory(new HTMLBRElementImpl("br",dummyDoc));
        addHTMLFactory(new HTMLBaseElementImpl("base",dummyDoc));
        addHTMLFactory(new HTMLBaseFontElementImpl("basefont",dummyDoc));
        addHTMLFactory(new HTMLBodyElementImpl("body",dummyDoc));
        addHTMLFactory(new HTMLButtonElementImpl("button",dummyDoc));
        addHTMLFactory(new HTMLDListElementImpl("dl",dummyDoc));
        addHTMLFactory(new HTMLDirectoryElementImpl("dir",dummyDoc));
        addHTMLFactory(new HTMLDivElementImpl("div",dummyDoc));
        addHTMLFactory(new HTMLEmbedElementImpl("embed",dummyDoc)); // Este es especial para ItsNat, no por que haya un interface HTMLEmbedElement
        addHTMLFactory(new HTMLFieldSetElementImpl("fieldset",dummyDoc));
        addHTMLFactory(new HTMLFontElementImpl("font",dummyDoc));
        addHTMLFactory(new HTMLFormElementImpl("form",dummyDoc));
        addHTMLFactory(new HTMLFrameElementImpl("frame",dummyDoc));
        addHTMLFactory(new HTMLFrameSetElementImpl("frameset",dummyDoc));
        addHTMLFactory(new HTMLHRElementImpl("hr",dummyDoc));
        addHTMLFactory(new HTMLHeadElementImpl("head",dummyDoc));
        addHTMLFactory(new HTMLHeadingElementImpl("h1",dummyDoc));
        addHTMLFactory(new HTMLHeadingElementImpl("h2",dummyDoc));
        addHTMLFactory(new HTMLHeadingElementImpl("h3",dummyDoc));
        addHTMLFactory(new HTMLHeadingElementImpl("h4",dummyDoc));
        addHTMLFactory(new HTMLHeadingElementImpl("h5",dummyDoc));
        addHTMLFactory(new HTMLHeadingElementImpl("h6",dummyDoc));
        addHTMLFactory(new HTMLHtmlElementImpl("html",dummyDoc));
        addHTMLFactory(new HTMLIFrameElementImpl("iframe",dummyDoc));
        addHTMLFactory(new HTMLImageElementImpl("img",dummyDoc));
        addHTMLFactory(new HTMLInputElementImpl("input",dummyDoc));
        addHTMLFactory(new HTMLIsIndexElementImpl("isindex",dummyDoc));
        addHTMLFactory(new HTMLLIElementImpl("li",dummyDoc));
        addHTMLFactory(new HTMLLabelElementImpl("label",dummyDoc));
        addHTMLFactory(new HTMLLegendElementImpl("legend",dummyDoc));
        addHTMLFactory(new HTMLLinkElementImpl("link",dummyDoc));
        addHTMLFactory(new HTMLMapElementImpl("map",dummyDoc));
        addHTMLFactory(new HTMLMenuElementImpl("menu",dummyDoc));
        addHTMLFactory(new HTMLMetaElementImpl("meta",dummyDoc));
        addHTMLFactory(new HTMLModElementImpl("ins",dummyDoc));
        addHTMLFactory(new HTMLModElementImpl("del",dummyDoc));
        addHTMLFactory(new HTMLOListElementImpl("ol",dummyDoc));
        addHTMLFactory(new HTMLObjectElementImpl("object",dummyDoc));
        addHTMLFactory(new HTMLOptGroupElementImpl("optgroup",dummyDoc));
        addHTMLFactory(new HTMLOptionElementImpl("option",dummyDoc));
        addHTMLFactory(new HTMLParagraphElementImpl("p",dummyDoc));
        addHTMLFactory(new HTMLParamElementImpl("param",dummyDoc));
        addHTMLFactory(new HTMLPreElementImpl("pre",dummyDoc));
        addHTMLFactory(new HTMLQuoteElementImpl("blockquote",dummyDoc));
        addHTMLFactory(new HTMLScriptElementImpl("script",dummyDoc));
        addHTMLFactory(new HTMLSelectElementImpl("select",dummyDoc));
        addHTMLFactory(new HTMLStyleElementImpl("style",dummyDoc));
        addHTMLFactory(new HTMLTableCaptionElementImpl("caption",dummyDoc));
        addHTMLFactory(new HTMLTableCellElementImpl("td",dummyDoc));
        addHTMLFactory(new HTMLTableCellElementImpl("th",dummyDoc));
        addHTMLFactory(new HTMLTableColElementImpl("col",dummyDoc));
        addHTMLFactory(new HTMLTableColElementImpl("colgroup",dummyDoc));
        addHTMLFactory(new HTMLTableElementImpl("table",dummyDoc));
        addHTMLFactory(new HTMLTableRowElementImpl("tr",dummyDoc));
        addHTMLFactory(new HTMLTableSectionElementImpl("thead",dummyDoc));
        addHTMLFactory(new HTMLTableSectionElementImpl("tfood",dummyDoc));
        addHTMLFactory(new HTMLTableSectionElementImpl("tbody",dummyDoc));
        addHTMLFactory(new HTMLTextAreaElementImpl("textarea",dummyDoc));
        addHTMLFactory(new HTMLTitleElementImpl("title",dummyDoc));
        addHTMLFactory(new HTMLUListElementImpl("ul",dummyDoc));
    }

    protected DelegateDocumentImpl delegate;
    protected AbstractViewImpl view;
    protected XMLDecImpl xmlDec;
    protected ElementsByIdImpl elementsByIdFixed = new ElementsByIdImpl(this);

    public DocumentImpl()
    {
        this(null,BatikItsNatDOMImplementationImpl.getBatikItsNatDOMImplementation());
    }

    public DocumentImpl(DocumentType dt,DOMImplementation impl)
    {
        super(dt, impl);
        getDelegateNode();
    }

    protected static void addHTMLFactory(HTMLElementImpl elem)
    {
        // Los localName DEBEN estar en minúsculas para que valgan también
        // para XHTML
        htmlElemFactories.put(elem.getLocalName(),elem);
    }

    protected static HTMLElementImpl getHTMLFactory(String localName)
    {
        // Puede ser null, es el caso de localName sin una interface específica
        HTMLElementImpl factory = htmlElemFactories.get(localName);
        if (factory != null) return factory;
        return htmlUnkownElemFactory; //Ej. el caso de <span>
    }

    @Override
    public DOMImplementation getImplementation()
    {
        // Existe un caso de que al deserializar el documento se leen antes los nodos hijos
        // que este nivel, si el elemento necesita registrar un listener interno el caso es
        // que necesita que esté el DOMImplementation definido en el documento todavía no
        // deserializado del todo (el DOMImplementation se define en este nivel).
        // Es el caso de elementos "disconnected"
        if (implementation == null)
            this.implementation = BatikItsNatDOMImplementationImpl.getBatikItsNatDOMImplementation();
        return super.getImplementation();
    }

    @Override
    public Element getChildElementById(Node requestor, String id)
    {
        // Redefine el comportamiento por defecto para evitar el uso de elementsById
        // aunque la verdad es que este método no es
        // público y no se usará pues también redefinimos getElementById
        // pero como nos hemos molestado el mantener la funcionalidad por si acaso se necesita...
        return elementsByIdFixed.getChildElementById(requestor, id);
    }

    @Override
    public Element getElementById(String id)
    {
        // Redefine el comportamiento por defecto para evitar el uso de elementsById
        return elementsByIdFixed.getElementById(id);
    }

    @Override
    public void removeIdEntry(Element e, String id)
    {
        // Redefine el comportamiento por defecto para evitar el uso de elementsById
        elementsByIdFixed.removeIdEntry(e,id);
    }

    @Override
    public void addIdEntry(Element e, String id)
    {
        // Redefine el comportamiento por defecto para evitar el uso de elementsById
        elementsByIdFixed.addIdEntry(e,id);
    }

    @Override
    public void updateIdEntry(Element e, String oldId, String newId)
    {
        // Redefine el comportamiento por defecto para evitar el uso de elementsById
        elementsByIdFixed.updateIdEntry(e,oldId,newId);
    }
    
    private void writeAbstractDocument(ObjectOutputStream out) throws IOException
    {
    }

    private void readAbstractDocument(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        // AbstractNode tiene un atributo transient eventSupport
        // al añadir un EventListener interno automáticamente se inicializa
        // e ItsNat trata de restaurar los EventListener internos usados
        // al de-serializar

        // Atributos transient:

        this.localizableSupport = new LocalizableSupport(RESOURCES, getClass().getClassLoader());
        if (implementation == null)
            this.implementation = BatikItsNatDOMImplementationImpl.getBatikItsNatDOMImplementation();

        // traversalSupport : parece que se crea cuando se necesita
        // documentEventSupport : parece que se crea cuando se necesita
        // eventsEnabled : parece que es una variable que se pone a true cuando se añade un EventListener
        //                 en otros puntos de ItsNat se restauran los EventListener internos (mutation events)
        //                  al de-serializar, indirectamente pondrán esta variable a true
        // elementsByTagNames : es una cache que se crea cuando se necesita
        // elementsByTagNamesNS : idem
        this.xblManager = new GenericXBLManager();
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        writeAbstractDocument(out);

        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        readAbstractDocument(in);

        in.defaultReadObject();
    }

    public XMLDecImpl getXMLDec()
    {
        return xmlDec; // Puede ser null
    }

    public void setXMLDec(String version, String encoding, String standalone)
    {
        this.xmlDec = new XMLDecImpl(version,encoding,standalone);
    }

    @Override
    public Node cloneNode(boolean deep)
    {
        DocumentImpl docClone = (DocumentImpl)super.cloneNode(deep);
        if (xmlDec != null)
            docClone.setXMLDec(xmlDec.getVersion(),xmlDec.getEncoding(),xmlDec.getStandalone());
        return docClone;
    }

    public HTMLElement createHTMLElement(String qualifiedName)
    {
        String prefix = null;
        String localName = null;
        int pos = qualifiedName.indexOf(':');
        if (pos != -1) // Tiene prefijo
        {
            prefix = qualifiedName.substring(0,pos);
            localName = qualifiedName.substring(pos + 1);
        }
        else
        {
            localName = qualifiedName;
        }
        return createHTMLElement(prefix,localName);
    }

    public HTMLElement createHTMLElement(String prefix,String localName)
    {
        // El que hagamos que todos los elementos X/HTML implementen los
        // interfaces HTML incluso en documentos no X/HTML (SVG, XUL) es
        // MUY ventajoso pues SVG y XUL admiten XHTML embebido, además
        // esto no es raro FireFox 3, Opera 9, Chrome 1 y Safari 3 lo hacen también.
        localName = localName.toLowerCase();
        HTMLElementImpl factory = getHTMLFactory(localName); // Nunca es nulo
        HTMLElementImpl elem = (HTMLElementImpl)factory.cloneNode(false);
        String nodeName = (prefix == null)||prefix.equals("") ? localName : (prefix + ":" + localName);
        elem.setNodeName(nodeName);  // Hay que definirlo por dos razones: porque en la factoría el nodeName está sin prefijo y en el caso de elemento con factoría por defecto (ej. <span>) el nodeName definido en el factory no es válido
        elem.setOwnerDocument(this);
        return elem;
    }

    public abstract Element createElementInternal(String localName);

    @Override
    public Element createElement(String tagName)
    {
        // Redefinimos el método por defecto de Batik

        // tagName NO puede ser un nombre cualificado (con prefijo)
        // pues para eso hay que usar namespace y usar createElementNS
        // Aunque la verdad es que FireFox 3, Opera 9, Chrome 1 y Safari 3
        // se lo tragan pero poniendo prefix como null y el nombre cualificado
        // como localName lo que me parece un error. Además Batik parsea el ":"
        // para extraer el prefix si existe por tanto no podemos permitir que localName
        // tenga un ":" (como sí permiten los navegadores reales).
        if (tagName.indexOf(':') != -1)
            throw new DOMException(DOMException.NAMESPACE_ERR,"Tag name cannot contain a prefix in this context");

        return createElementInternal(tagName); // tagName es el localName
    }

    @Override
    public Element createElementNS(String namespaceURI,String qualifiedName)
    {
        // Redefinimos el método por defecto de Batik
        if (namespaceURI == null)
            return createElement(qualifiedName);
        if (NamespaceUtil.isXHTMLNamespace(namespaceURI))
            return createHTMLElement(qualifiedName);
        else
            return new ElementNSDefaultImpl(namespaceURI,qualifiedName,this);
    }

    @Override
    public Text createTextNode(String data)
    {
        return new TextImpl(data,this);
    }

    @Override
    public Comment createComment(String data)
    {
        return new CommentImpl(data,this);
    }

    @Override
    public Attr createAttribute(String name) throws DOMException
    {
        return new AttrImpl(name.intern(), this);
    }

    @Override
    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException
    {
        if (namespaceURI != null && namespaceURI.length() == 0) {
            namespaceURI = null;
        }
        if (namespaceURI == null) {
            return new AttrImpl(qualifiedName.intern(), this);
        } else {
            return new AttrNSImpl(namespaceURI.intern(),
                                     qualifiedName.intern(),
                                     this);
        }
    }

    @Override
    public DocumentFragment createDocumentFragment()
    {
        return new DocumentFragmentImpl(this);
    }

    @Override
    public CDATASection createCDATASection(String data) throws DOMException
    {
        return new CDATASectionImpl(data, this);
    }

    @Override
    public ProcessingInstruction createProcessingInstruction(String target,
                                         String data) throws DOMException
    {
        return new ProcessingInstructionImpl(target, data, this);
    }

    @Override
    public EntityReference createEntityReference(String name) throws DOMException
    {
        return new EntityReferenceImpl(name, this);
    }

    public AbstractView getDefaultView()
    {
        // Este método está en AbstractStylableDocument de Batik pero nosotros
        // no derivamos de este clase porque no nos aporta nada más que dependencias
        // que no queremos.
        if (view == null) this.view = new AbstractViewImpl(this);
        return view;
    }

    public ItsNatDocument getItsNatDocument()
    {
        // Este método es posible que se llame multihilo en el caso
        // acceder desde un hilo request de un documento padre al documento de un iframe/object/embed/applet hijo
        // (sería esta instancia el documento del iframe).
        // Tenemos la seguridad de que "delegate" no es nulo
        // pues se define lo antes posible en la construcción del documento
        return getDelegateNode().getItsNatDocument();
    }

    public DelegateNodeImpl getDelegateNode()
    {
        if (delegate == null) this.delegate = new DelegateDocumentImpl(this);
        return delegate;
    }

    public DelegateDocumentImpl getDelegateDocument()
    {
        return (DelegateDocumentImpl)getDelegateNode();
    }

    public void addEventListenerInternal(String type, EventListener listener, boolean useCapture)
    {
        super.addEventListener(type,listener,useCapture);
    }

    public void removeEventListenerInternal(String type, EventListener listener, boolean useCapture)
    {
        super.removeEventListener(type,listener,useCapture);
    }

    @Override
    public void fireDOMSubtreeModifiedEvent()
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMSubtreeModifiedEvent();
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public void fireDOMNodeInsertedEvent(Node node)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMNodeInsertedEvent(node);
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public void fireDOMNodeRemovedEvent(Node node)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMNodeRemovedEvent(node);
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public void fireDOMNodeInsertedIntoDocumentEvent()
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMNodeInsertedIntoDocumentEvent();
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public void fireDOMNodeRemovedFromDocumentEvent()
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMNodeRemovedFromDocumentEvent();
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public void fireDOMCharacterDataModifiedEvent(String oldv,String newv)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            super.fireDOMCharacterDataModifiedEvent(oldv,newv);
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public Node renameNode(Node n, String namespaceURI, String qualifiedName)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setMutationEventInternal(true);
        try
        {
            return super.renameNode(n,namespaceURI,qualifiedName);
        }
        finally
        {
            delegate.setMutationEventInternal(false);
        }
    }

    @Override
    public Node importNode(Node importedNode, boolean deep) throws DOMException
    {
        // El método importNode con tres parámetros de Batik, es únicamente
        // llamado con el tercer parámetro a true por la parte SVG de Batik
        if (importedNode instanceof DocumentType)
        {
            DocumentType docType = (DocumentType)importedNode;
            DOMImplementation domImpl = getImplementation();
            GenericDocumentType docTypeCloned = (GenericDocumentType)domImpl.createDocumentType(docType.getNodeName(),docType.getPublicId(),docType.getSystemId());
            docTypeCloned.setOwnerDocument(this);
            if (importedNode instanceof AbstractNode)
            {
                // Only fire the UserDataHandler if the imported node is from
                // Batik's DOM implementation.
                fireUserDataHandlers(UserDataHandler.NODE_IMPORTED,
                                     importedNode,
                                     docTypeCloned);
            }
            return docTypeCloned;
        }
        else return super.importNode(importedNode, deep);
    }

    // Métodos de EventTarget

    @Override
    public boolean dispatchEvent(Event evt) throws EventException
    {
        DelegateNodeImpl delegate = getDelegateNode();
        if (delegate.isDispatchEventInternal(evt))
            return super.dispatchEvent(evt);
        else
            return delegate.dispatchEventRemote(evt);
    }

    @Override
    public void addEventListener(String type, EventListener listener, boolean useCapture)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        if (delegate.isAddRemoveEventListenerInternal())
            addEventListenerInternal(type,listener,useCapture);
        else
            delegate.addEventListenerRemote(type,listener,useCapture);
    }

    @Override
    public void removeEventListener(String type, EventListener listener, boolean useCapture)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        if (delegate.isAddRemoveEventListenerInternal())
            removeEventListenerInternal(type,listener,useCapture);
        else
            delegate.removeEventListenerRemote(type,listener,useCapture);
    }

    public boolean isInternalMode()
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.isInternalMode();
    }

    public void setInternalMode(boolean mode)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        delegate.setInternalMode(mode);
    }

    // Métodos de DocumentEvent

    @Override
    public Event createEvent(String eventType) throws DOMException
    {
        DelegateDocumentImpl delegate = getDelegateDocument();
        if (delegate.isMutationEventInternal())
            return super.createEvent(eventType);
        else if (delegate.isInternalMode()) // Definido por el programador, es un caso muy raro pues los mutation events que apenas son los que tienen sentido, son creados/despachados por Batik.
            return super.createEvent(eventType);
        else
            return delegate.createRemoteEvent(eventType);
    }

    // ItsNatUserData

    public boolean containsUserValueName(String name)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.containsUserValueName(name);
    }

    public Object getUserValue(String name)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.getUserValue(name);
    }

    public Object setUserValue(String name, Object value)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.setUserValue(name,value);
    }

    public Object removeUserValue(String name)
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.removeUserValue(name);
    }

    public String[] getUserValueNames()
    {
        DelegateNodeImpl delegate = getDelegateNode();
        return delegate.getUserValueNames();
    }
}
