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

package org.itsnat.impl.core.template;

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import java.text.DateFormat;
import java.text.NumberFormat;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.domimpl.BatikItsNatDOMImplementationImpl;
import org.itsnat.impl.core.domimpl.DocumentImpl;
import org.itsnat.impl.core.domimpl.XMLDecImpl;
import org.itsnat.impl.core.markup.parse.XercesDOMParserWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalLoadDocImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
     Si fastLoadMode es true no se procesan los mutation events en carga haciendo que el código JavaScript de carga sea mucho menor
     serializándose el documento tras el proceso del usuario en carga.
     El inconveniente es que durante la carga no se pueden usar nodos que al final de la carga
     han sido eliminados o ha cambiado su path, pues el JavaScript se ejecuta actúa sobre el documento
     generado tras el proceso de carga.
     Si fastLoadMode es false se envía serializado al cliente la plantilla original
     y se envía como JavaScript todas las mutaciones del documento, elimina la
     restricción considerada anteriormente.
     El inconveniente es que se genera mucho JavaScript (más tráfico) y la ejecución del código tarda mucho
    (el JavaScript es muy lento).
    Es preferible el modo fastLoadMode = true si la aplicación lo permite

 * @author jmarranz
 */
public abstract class ItsNatDocumentTemplateVersionImpl extends MarkupTemplateVersionImpl
{
    /**
     * Creates a new instance of ItsNatDocumentTemplateVersionImpl
     */
    public ItsNatDocumentTemplateVersionImpl(ItsNatDocumentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        super(docTemplate,source,timeStamp,request,response);

        if (isReferrerEnabled() && !isEventsEnabled())
            throw new ItsNatException("Referrer feature needs events enabled",docTemplate); // Dará error la primera vez que se cargue un documento, mientras tanto ambos parámetros pueden ser incorrectos a nivel de ItsNatDocumentTemplateImpl

        if (!isScriptingEnabled())
        {
            if (isEventsEnabled())
                throw new ItsNatException("Events cannot be enabled whether scripting is disabled",docTemplate); // Dará error la primera vez que se cargue un documento, mientras tanto los parámetros pueden ser incorrectos a nivel de ItsNatDocumentTemplateImpl
            if (!isFastLoadMode())
                throw new ItsNatException("Fast load mode must be enabled if scripting is disabled",docTemplate); // Dará error la primera vez que se cargue un documento, mientras tanto los parámetros pueden ser incorrectos a nivel de ItsNatDocumentTemplateImpl
        }

        doCacheAndNormalizeDocument();
    }
    
    public XercesDOMParserWrapperImpl createMarkupParser(String defaultEncoding)
    {
        return markupTemplate.createMarkupParser(defaultEncoding);
    }

    public boolean isFastLoadMode()
    {
        return getItsNatDocumentTemplate().isFastLoadMode();
    }

    public boolean isDebugMode()
    {
        return getItsNatDocumentTemplate().isDebugMode();
    }

    public int getClientErrorMode()
    {
        return getItsNatDocumentTemplate().getClientErrorMode();
    }

    public int getCommMode()
    {
        return getItsNatDocumentTemplate().getCommMode();
    }

    public long getEventTimeout()
    {
        return getItsNatDocumentTemplate().getEventTimeout();
    }

    public int getUseGZip()
    {
        return getItsNatDocumentTemplate().getUseGZip();
    }

    public boolean isNodeCacheEnabled()
    {
        return getItsNatDocumentTemplate().isNodeCacheEnabled();
    }

    public boolean isAutoBuildComponents()
    {
        return getItsNatDocumentTemplate().isAutoBuildComponents();
    }

    public boolean isLoadScriptInline()
    {
        return getItsNatDocumentTemplate().isLoadScriptInline();
    }

    public boolean isReferrerPushEnabled()
    {
        return getItsNatDocumentTemplate().isReferrerPushEnabled();
    }

    public boolean canVersionBeSharedBetweenDocs()
    {
        return getItsNatDocumentTemplate().canVersionBeSharedBetweenDocs();
    }

    public ItsNatDocumentImpl loadItsNatDocument(RequestNormalLoadDocImpl request)
    {
        ItsNatServletRequestImpl itsNatRequest = request.getItsNatServletRequest();
        Browser browser = getBrowser(itsNatRequest);
        Document doc = loadDocument(browser);
        ItsNatDocumentImpl itsNatDoc = createItsNatDocument(doc,browser,itsNatRequest.getRequestURLInternal().toString(),itsNatRequest.getItsNatSessionImpl());
        if (!canVersionBeSharedBetweenDocs()) cleanDOMPattern(); // Ya no necesitamos más el Document patrón pues el template no se utiliza para crear más documentos
        return itsNatDoc;
    }

    public Browser getBrowser(ItsNatServletRequestImpl itsNatRequest)
    {
        // Se redefine en un caso (SVG y MSIE)
        String userAgent = itsNatRequest.getHeader("User-Agent");
        Browser currBrowser = itsNatRequest.getItsNatSessionImpl().getBrowser();
        if (currBrowser.getUserAgent().equals(userAgent))
            return currBrowser;
        else
        {
            // Casos detectados:
            
            // Caso del plugin SVG Savarese Ssrc en MSIE desktop cuando hay auto-binding.
            // En ese caso la sesión es compartida (forzadamente) en el servidor
            // pero obviamente el navegador de la sesión (el del MSIE) no es el del plugin
            // el cual necesita el objeto Browser del FireFox cuando se carga el documento SVG.

            // Caso MSIE Mobile (WM 6.1.4): cuando se cambia al modo desktop cambia el user-agent,
            // este caso no es muy importante, y daría igual usar el pimer
            // objeto Browser creado pues se detecta siempre que es MSIE Mobile.

            // Caso de Batik applet: la cookie de sesión es compartida con el navegador
            // que contiene el applet, la página contenedora es la que crea la sesión
            // pero obviamente el applet al cargar un nuevo documento genera un nuevo objeto browser (Batik).
            return Browser.createBrowser(itsNatRequest);
        }
    }

    protected abstract ItsNatDocumentImpl createItsNatDocument(Document doc,Browser browser,String requestURL,ItsNatSessionImpl session);


    public Document loadDocument(Browser browser)
    {
        // Es una falsa carga, es un clonado
        // return (Document)getDocument().cloneNode(true);
        return manualClone();
    }

    protected Document manualClone()
    {
        // Ahora que siempre es un documento DOM Batik tanto el del
        // template como el clonado podríamos hacer un simple cloneNode
        // al documento, pero por ahora lo dejamos pues así tenemos mucho mayor control
        // del proceso y tenemos mayor seguridad de que es multihilo.

        Document doc = getDocument();
        Document cloneDoc = manualCloneDocument(doc);

        if (doc.hasChildNodes()) // Es imposible que no tenga
        {
            Element root = doc.getDocumentElement();
            Element cloneRoot = cloneDoc.getDocumentElement(); // Se añade al crear el documento
            Node child = doc.getFirstChild();
            while(child != null)
            {
                if (!(child instanceof DocumentType)) // El DocumentType ya está añadido al crear el documento
                {
                    if (child == root) // Aunque cloneRoot ya existe faltan los atributos y los hijos
                        manualCloneTree(child,cloneDoc,cloneDoc);
                    else
                    {
                        // Puede ser un comentario o un ProcessingInstruction
                        Node cloneChild = manualCloneNode(child,cloneDoc); // No inserta
                        cloneDoc.insertBefore(cloneChild, cloneRoot); // Insertamos antes del elemento root que ya está insertado
                    }                        
                }
                    
                child = child.getNextSibling();
            }
        }

        return cloneDoc;
    }

    protected Node manualCloneTree(Node node,Node cloneParent,Document cloneDoc)
    {
        Node cloneNode = manualCloneNodeAndAppend(node,cloneParent,cloneDoc);

        if (node.hasAttributes())
        {
            Element cloneElem = (Element)cloneNode;
            NamedNodeMap attribs = node.getAttributes();
            int len = attribs.getLength();
            for(int i = 0; i < len; i++)
            {
                Attr attr = (Attr)attribs.item(i);
                Attr cloneAttr = (Attr)manualCloneNode(attr,cloneDoc);
                String namespace = cloneAttr.getNamespaceURI();
                if (namespace != null)
                    cloneElem.setAttributeNodeNS(cloneAttr);
                else
                    cloneElem.setAttributeNode(cloneAttr);
            }
        }

        if (node.hasChildNodes())
        {
            Node child = node.getFirstChild();
            while(child != null)
            {
                manualCloneTree(child,cloneNode,cloneDoc);
                child = child.getNextSibling();
            }
        }

        return cloneNode;
    }

    protected Node manualCloneNodeAndAppend(Node node,Node cloneParent,Document cloneDoc)
    {
        Node cloneNode;
        if ((node == node.getOwnerDocument().getDocumentElement()) &&
            (cloneDoc.getDocumentElement() != null))
        {
            // El método DOMImplementation.createDocument(...) ya crea un documento
            // con un elemento root y un namespace. Por tanto lo que queda será
            // añadir atributos y nodos hijo
            cloneNode = cloneDoc.getDocumentElement();
        }
        else
        {
            cloneNode = manualCloneNode(node,cloneDoc);
            cloneParent.appendChild(cloneNode);
        }

        return cloneNode;
    }


    protected Node manualCloneNode(Node node,Document cloneDoc)
    {
        Node cloneNode = null;
        switch(node.getNodeType())
        {
            case Node.ATTRIBUTE_NODE:
                Attr attr = (Attr)node;
                
                // Un atributo tiene namespace únicamente cuando
                // tiene un prefijo asociado pues el namespace no se hereda del elemento contenedor
                // Por ello usamos siempre Document.createAttributeNS() porque admite el caso
                // de namespace null, aparentemente sería lo mismo que Document.createAttribute()
                // pero lo evitamos porque si es un HTMLDocument el HTMLDocument.createAttribute()
                // podría ponerlo en minúsculas creyendo que es HTML y podemos tener los casos
                // de elementos no HTML embebidos en XHTML, tal y como SVG embebido en XHTML
                // en donde el caso exacto del atributo (y tags) es muy importante por ejemplo preserveAspectRatio
                // La verdad es que DOM Batik llama a HTMLDocument.createAttribute() sin cambiar a minúsculas,
                // pero de todas formas lo dejamos así
                cloneNode = cloneDoc.createAttributeNS(attr.getNamespaceURI(),attr.getName()); // NO usar getLocalName()
                ((Attr)cloneNode).setValue(attr.getValue());
                break;
            case Node.CDATA_SECTION_NODE:
                cloneNode = cloneDoc.createCDATASection(((CDATASection)node).getData());
                break;
            case Node.COMMENT_NODE:
                cloneNode = cloneDoc.createComment(((Comment)node).getData());
                break;
            case Node.DOCUMENT_FRAGMENT_NODE:
                throw new ItsNatException("INTERNAL ERROR"); // No ocurrirá
            case Node.DOCUMENT_NODE:
                throw new ItsNatException("INTERNAL ERROR"); // No ocurrirá
            case Node.DOCUMENT_TYPE_NODE:
                throw new ItsNatException("INTERNAL ERROR"); // Se hace al crear el documento
            case Node.ELEMENT_NODE:
                Element elem = (Element)node;
                cloneNode = manualCloneElement(elem,cloneDoc);
                break;
            case Node.ENTITY_NODE:
                throw new ItsNatException("INTERNAL ERROR"); // No debería ocurrir
            case Node.ENTITY_REFERENCE_NODE:
                cloneNode = cloneDoc.createEntityReference(((EntityReference)node).getNodeName()); // No estoy muy seguro sobre la llamada a getNodeName()
                break;
            case Node.NOTATION_NODE:
                throw new ItsNatException("INTERNAL ERROR"); // No debería ocurrir
            case Node.PROCESSING_INSTRUCTION_NODE:
                ProcessingInstruction procInst = (ProcessingInstruction)node;
                cloneNode = cloneDoc.createProcessingInstruction(procInst.getTarget(),procInst.getData());
                break;
            case Node.TEXT_NODE:
                cloneNode = cloneDoc.createTextNode(((Text)node).getData());
                break;
        }

        return cloneNode;
    }

    protected Document manualCloneDocument(Document doc)
    {
        // No es un clonado profundo, sólo el objeto documento el DocumentType
        // y el elemento root aunque sin atributos ni hijos
        DOMImplementation domImplClone = BatikItsNatDOMImplementationImpl.getBatikItsNatDOMImplementation();
        DocumentType cloneDocType = null;
        DocumentType docType = doc.getDoctype();
        if (docType != null)
            cloneDocType = domImplClone.createDocumentType(docType.getNodeName(),docType.getPublicId(),docType.getSystemId());
        Element docElem = doc.getDocumentElement();
        DocumentImpl docClone = (DocumentImpl)domImplClone.createDocument(docElem.getNamespaceURI(),docElem.getTagName(),cloneDocType);
        XMLDecImpl xmlDec = ((DocumentImpl)doc).getXMLDec();
        if (xmlDec != null)
            docClone.setXMLDec(xmlDec.getVersion(),xmlDec.getEncoding(),xmlDec.getStandalone());
        return docClone;
    }

    protected Element manualCloneElement(Element elem,Document cloneDoc)
    {
        // No es un clonado profundo, tampoco los atributos
        String namespace = elem.getNamespaceURI();
        if (namespace != null)
            return cloneDoc.createElementNS(namespace,elem.getTagName());
        else
            return cloneDoc.createElement(elem.getTagName());
    }

    public ItsNatDocumentTemplateImpl getItsNatDocumentTemplate()
    {
        return (ItsNatDocumentTemplateImpl)markupTemplate;
    }

    public DateFormat getDefaultDateFormat()
    {
        return getItsNatDocumentTemplate().getDefaultDateFormat();
    }

    public DateFormat getDefaultDateFormatCloned()
    {
        // Hay que tener en cuenta que DateFormat NO es thread safe
        DateFormat format = getDefaultDateFormat();
        if (format != null)
            format = (DateFormat)format.clone();        
        return format;
    }

    public NumberFormat getDefaultNumberFormat()
    {
        return getItsNatDocumentTemplate().getDefaultNumberFormat();
    }

    public NumberFormat getDefaultNumberFormatCloned()
    {
        // Hay que tener en cuenta que NumberFormat NO es thread safe
        NumberFormat format = getDefaultNumberFormat();
        if (format != null)
            format = (NumberFormat)format.clone();
        return format;
    }

    public long getEventDispatcherMaxWait()
    {
        return getItsNatDocumentTemplate().getEventDispatcherMaxWait();
    }

    public int getMaxOpenClientsByDocument()
    {
        return getItsNatDocumentTemplate().getMaxOpenClientsByDocument();
    }

    public boolean isReferrerEnabled()
    {
        return getItsNatDocumentTemplate().isReferrerEnabled();
    }

    public boolean isEventsEnabled()
    {
        return getItsNatDocumentTemplate().isEventsEnabled();
    }

    public boolean isScriptingEnabled()
    {
        return getItsNatDocumentTemplate().isScriptingEnabled();
    }

    public boolean isAutoCleanEventListeners()
    {
        return getItsNatDocumentTemplate().isAutoCleanEventListeners();
    }

    public boolean isUseXHRSyncOnUnloadEvent()
    {
        return getItsNatDocumentTemplate().isUseXHRSyncOnUnloadEvent();
    }
    
    public boolean isUsePatternMarkupToRender()
    {
        return getItsNatDocumentTemplate().isUsePatternMarkupToRender();
    }

    public void setUsePatternMarkupToRender(boolean usePatternMarkupToRender)
    {
        getItsNatDocumentTemplate().setUsePatternMarkupToRender(usePatternMarkupToRender);
    }

    public boolean isSelectionOnComponentsUsesKeyboard()
    {
        return getItsNatDocumentTemplate().isSelectionOnComponentsUsesKeyboard();
    }

    public boolean isJoystickMode()
    {
        return getItsNatDocumentTemplate().isJoystickMode();
    }

    public boolean isMarkupDrivenComponents()
    {
        return getItsNatDocumentTemplate().isMarkupDrivenComponents();
    }

    public abstract DocumentFragment parseFragmentToDocFragment(String source,ItsNatDocumentImpl docTarget);

}
