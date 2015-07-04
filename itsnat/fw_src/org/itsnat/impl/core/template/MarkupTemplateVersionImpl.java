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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.MarkupContainerImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.markup.parse.XercesDOMParserWrapperImpl;
import org.itsnat.impl.core.markup.render.DOMRenderImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.util.HasUniqueId;
import org.itsnat.impl.core.util.MapUniqueId;
import org.itsnat.impl.core.util.UniqueId;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public abstract class MarkupTemplateVersionImpl extends MarkupContainerImpl implements HasUniqueId
{
    protected MarkupTemplateImpl markupTemplate;
    protected Document templateDoc;
    protected long timeStamp;
    protected MarkupTemplateVersionDelegateImpl templateDelegate;
    protected MapUniqueId<CachedSubtreeImpl> elementCacheMap;  // No sincronizamos porque sólo se modifica en tiempo de carga
    protected LinkedList<ItsNatDocFragmentTemplateVersionImpl> fragments = new LinkedList<ItsNatDocFragmentTemplateVersionImpl>(); // Se recorrerá en multihilo no podemos ahorrar memoria con creación demorada

    /**
     * Creates a new instance of MarkupTemplateVersionImpl
     */
    public MarkupTemplateVersionImpl(MarkupTemplateImpl markupTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        super(markupTemplate.getItsNatServletImpl().getUniqueIdGenerator());

        this.markupTemplate = markupTemplate;
        this.timeStamp = timeStamp;

        this.templateDelegate = createMarkupTemplateVersionDelegate();
        this.templateDoc = parseDocumentOrFragment(source,markupTemplate.getMarkupParser(),false); // No hay problema de usar el parser compartido por todas las versiones pues la construcción de versiones se hace en monohilo

        // Podemos considerar el templateDoc patrón de como thread-safe en sólo lectura (recorriendo)
        // porque en Batik DOM las colecciones de atributos y childNodes se crean
        // cuando se necesitan por lo que si hay atributos y nodos hijos ya estarán
        // creadas pues incluso el getFirstChild etc se hace a través de la colección
        // interna de childNodes.

        // Resolvemos lo primero los includes, así permitimos que pueda cachearse su contenido
        // Hay que tener en cuenta que al incluir un fragmento los nodos pueden estar cacheados
        // con el sistema de cacheado de los fragmentos incluidos con <include>, que es diferente al del documento
        // por tanto puede haber un doble cacheado (que no tienen por qué interferir)
        Element rootElem = templateDoc.getDocumentElement();
        processCommentsIncludesInTree(rootElem,request,response);
    }

    public static void writeObject(MarkupTemplateVersionImpl templateVersion,ObjectOutputStream out) throws IOException
    {
        MarkupTemplateImpl.writeObject(templateVersion.getMarkupTemplate(), out);
    }

    public static String[] readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        return MarkupTemplateImpl.readObject(in);
    }

    public static MarkupTemplateVersionImpl getNewestMarkupTemplateVersion(ItsNatServletImpl itsNatServlet,String[] templateId,MarkupSourceStringMarkupImpl source,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        MarkupTemplateImpl template = MarkupTemplateImpl.getMarkupTemplate(itsNatServlet, templateId);
        if (template instanceof ItsNatStfulDocumentTemplateAttachedServerImpl)
        {
            MarkupTemplateVersionImpl templateVersion = ((ItsNatStfulDocumentTemplateAttachedServerImpl)template).getNewestItsNatStfulDocumentTemplateVersion(source,request,response);
            // Como es deserialización no se necesita más el Document patrón pues ya lo tiene el documento
            // y sólo hay un template version por documento en este caso
            templateVersion.cleanDOMPattern();
            return templateVersion;
        }
        else
        {
            if (source != null) throw new ItsNatException("INTERNAL ERROR");
            return template.getNewestMarkupTemplateVersion(request,response);
        }
    }

    public MarkupTemplateImpl getMarkupTemplate()
    {
        return markupTemplate;
    }

    public void setMarkupTemplate(MarkupTemplateImpl markupTemplate)
    {
        // Se usa solo en serialización
        this.markupTemplate = markupTemplate;
    }    

    protected void doCacheAndNormalizeDocument()
    {
        if (isOnLoadCacheStaticNodes())
        {
            // El cacheado se produce únicamente en el proceso de carga
            doCacheDocument();
        }

        // Normalizamos después del cacheado pues sólo nos interesa normalizar aquello que es dinámico y por tanto debe coincidir exactamente el DOM cliente y servidor, además es más rápido pues las partes cacheadas son nodos de texto
        templateDelegate.normalizeDocument(getDocument());
    }

           
    public DOMRenderImpl createNodeDOMRender(Document doc,boolean nodeOnlyRender)
    {
        // Sirve para serializar nodos concretos no el documento completo
        return DOMRenderImpl.createDOMRender(doc,getMIME(),getEncoding(),nodeOnlyRender);
    }

    public Document parseDocumentOrFragment(String code,XercesDOMParserWrapperImpl parser,boolean isFragment)
    {
        StringReader reader = new StringReader(code);
        return parseDocumentOrFragment(new InputSource(reader),parser,isFragment);
    }

    public Document parseDocumentOrFragment(InputSource input,XercesDOMParserWrapperImpl parser,boolean isFragment)
    {
        String encoding = getEncoding();
        input.setEncoding(encoding);
        return parser.parse(input);
    }

    public boolean isDocFragment()
    {
        return markupTemplate.isDocFragment();
    }

    public String getIdGenPrefix()
    {
        return "mt";
    }

    public ItsNatServletImpl getItsNatServlet()
    {
        return markupTemplate.getItsNatServletImpl();
    }

    public boolean isOnLoadCacheStaticNodes()
    {
        return markupTemplate.isOnLoadCacheStaticNodes();
    }

    public boolean isMIME_XHTML()
    {
         return markupTemplate.isMIME_XHTML();
    }

    public boolean isMIME_HTML()
    {
         return markupTemplate.isMIME_HTML();
    }

    public String getId()
    {
        return idObj.getId();
    }

    public UniqueId getUniqueId()
    {
        return idObj;
    }

    public Document getDocument()
    {
        return templateDoc;
    }

    public String getEncoding()
    {
        return markupTemplate.getEncoding();
    }

    public String getNamespace()
    {
        return markupTemplate.getNamespace();
    }

    public String getMIME()
    {
        // El MIME en el template no puede cambiar, no hay problema de versiones
        return markupTemplate.getMIME();
    }

    public boolean isInvalid(MarkupSourceImpl source,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        boolean invalid = source.isMustReload(timeStamp,request,response);
        if (invalid)
            return true;

        // Seguimos con los fragmentos incluidos, si alguno ha sido cambiado en el disco duro el template padre es inválido y necesita recargarse

        for(ItsNatDocFragmentTemplateVersionImpl docFragTemplateIncVersion : fragments)
        {
            if (docFragTemplateIncVersion.isInvalid(request,response))
                return true;
        }

        return false;  // Nada ha cambiado
    }


    public boolean processCommentsIncludesInNode(Node node,ItsNatServletRequest request,ItsNatServletResponse response)
    {
         if (node.getNodeType() != Node.ELEMENT_NODE)
             return false;

         Element nodeElem = (Element)node;
         String namespace = nodeElem.getNamespaceURI();
         if (NamespaceUtil.isItsNatNamespace(namespace))
         {
             String localName = nodeElem.getLocalName();
             if (!getMarkupTemplate().isItsNatTagsAllowed())
                throw new ItsNatException("ItsNat tags are not allowed in this context, detected: " + localName,node);

             if (localName.equals("include"))
             {
                 String fragName = nodeElem.getAttribute("name");
                 processIncludeReplacingNode(nodeElem,fragName,request,response);
                 return true;
             }
             else if (localName.equals("comment"))
             {
                 nodeElem.getParentNode().removeChild(nodeElem);
                 return true;
             }
             else throw new ItsNatDOMException("Unknown itsnat tag name:" + localName,nodeElem);
         }
         else if (nodeElem.hasAttributes())
         {
            NamedNodeMap attribs = nodeElem.getAttributes();
            for(int i = 0; i < attribs.getLength(); i++)
            {
                Attr attr = (Attr)attribs.item(i);
                namespace = attr.getNamespaceURI();
                if (NamespaceUtil.isItsNatNamespace(namespace))
                {
                    String localName = attr.getLocalName(); // Fundamental llamar a getLocalName
                    if (localName.equals("include"))
                    {
                        String fragName = attr.getValue();
                        processIncludeReplacingNode(nodeElem,fragName,request,response);
                        nodeElem.removeAttributeNode(attr);
                    }
                    else if (localName.equals("includeInside"))
                    {
                        String fragName = attr.getValue();
                        processIncludeInsideNode(nodeElem,fragName,request,response);
                        nodeElem.removeAttributeNode(attr);
                    }
                    else if (localName.equals("comment"))
                    {
                        nodeElem.removeAttributeNode(attr);
                    }
                    // Pueden ser otros atributos con prefijo itsnat:
                }
            }
         }

         return false; // No es ni include ni comment
    }

    public DocumentFragment loadDocumentFragmentByIncludeTag(Element includeElem,String fragName,ItsNatServletRequest request,ItsNatServletResponse response)
    {
         ItsNatDocFragmentTemplateImpl docFragTemplateInc = (ItsNatDocFragmentTemplateImpl)markupTemplate.getItsNatServlet().getItsNatDocFragmentTemplate(fragName);
         if (docFragTemplateInc == null)
             throw new ItsNatDOMException("Document fragment not found: " + fragName,includeElem);

         ItsNatDocFragmentTemplateVersionImpl docFragTemplateIncVersion = docFragTemplateInc.getNewestItsNatDocFragmentTemplateVersion(request,response);
         fragments.add(docFragTemplateIncVersion);
         return docFragTemplateIncVersion.loadDocumentFragmentByIncludeTag(this,includeElem);
    }

    public void processIncludeReplacingNode(Element includeElem,String fragName,ItsNatServletRequest request,ItsNatServletResponse response)
    {
         DocumentFragment docFrag = loadDocumentFragmentByIncludeTag(includeElem,fragName,request,response);
         Node parent = includeElem.getParentNode();
         parent.insertBefore(docFrag,includeElem);
         parent.removeChild(includeElem);
    }

    public void processIncludeInsideNode(Element includeElem,String fragName,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        DocumentFragment docFrag = loadDocumentFragmentByIncludeTag(includeElem,fragName,request,response);

        DOMUtilInternal.removeAllChildren(includeElem); // Por si acaso no está vacío
        includeElem.appendChild(docFrag); // El DocumentFragment queda vacío (creo)
    }

    public void processCommentsIncludesInTree(Node node,ItsNatServletRequest request,ItsNatServletResponse response)
    {
         if (!processCommentsIncludesInNode(node,request,response))
             processCommentsIncludesInChildren(node,request,response);
    }

    public void processCommentsIncludesInChildren(Node parent,ItsNatServletRequest request,ItsNatServletResponse response)
    {
         Node child = parent.getFirstChild();
         while(child != null)
         {
             Node nextChild = child.getNextSibling(); // Antes de que pueda ser removido/reemplazado

             processCommentsIncludesInTree(child,request,response);

             child = nextChild;
         }
    }


    private String serializeChildNodes(Element parent,DOMRenderImpl nodeRender)
    {
        StringWriter out = new StringWriter();
        Node child = parent.getFirstChild();
        while(child != null)
        {
            serializeNode(child,out,nodeRender);

            child = child.getNextSibling();
        }
        return out.toString();
    }

    public String serializeNode(Node node,DOMRenderImpl nodeRender)
    {
        StringWriter out = new StringWriter();
        serializeNode(node,out,nodeRender);
        return out.toString();
    }

    public void serializeNode(Node node,Writer out,DOMRenderImpl nodeRender)
    {
        templateDelegate.serializeNode(node,out,nodeRender);
    }

    protected boolean isNodeStaticAndFindCacheable(Node node,LinkedList<Node> cacheableList)
    {
        if (node instanceof Element)
            return isElementStaticOrFindCacheableChildren((Element)node,cacheableList);
        else if (node instanceof CharacterData)
            return isCharacterDataStaticAndFindCacheable((CharacterData)node,cacheableList);
        else
            return false;
            // Otros tipos (incluido el objeto Document), no sabemos como cachearlos
            // o no tiene sentido (caso Document,DocumentFragment) y podrían tener variables que no consideramos analizar,
            // nos curamos en salud, evitarán que los nodos superiores sean cacheables
            // pero hay que tener en cuenta que son nodos raros
            // en el contenido de un documento
    }

    private boolean isCharacterDataStaticAndFindCacheable(CharacterData node,LinkedList<Node> cacheableList)
    {
        // Nodos de texto, comentarios etc.
        // El objetivo en este caso no es reducir el número de objetos DOM
        // pues el Text etc se mantendrá, pero en los documentos clonados
        // no aparecerá todo el rollo de texto estático repetidas veces.
        // Es cacheable si es estático (no contiene variables)
        if (isCharacterDataStatic(node))
        {
            cacheableList.add(node);
            return true;
        }
        else return false;
    }

    private boolean isCharacterDataStatic(CharacterData node)
    {
        return !hasVariables(node.getData());
    }

    protected boolean isElementValidForCaching(Element elem)
    {
        // Si el contenido del elemento puede cachearse.
        // Varias posibilidades:
        // - Desde un punto de vista técnico: es decir, si se puede serializar y reinsertar posteriormente utilizando el innerHTML en X/HTML o el setInnerXML
        // - Elementos especiales que no debemos cachear
        // Esta evaluación no supone que los hijos no puedan cachearse

        return true; // En este nivel no tenemos más razones, derivar
    }

    protected boolean isElementStaticOrFindCacheableChildren(Element elem,LinkedList<Node> cacheableList)
    {
        if (isDeclaredNotCacheable(elem))
            return false; // Se ha declarado que el contenido del elemento será dinámico, por tanto el elemento es no estático y no cacheable
        if (declaredAsComponent(elem))
            return false; // Los componentes por su naturaleza modifican los nodos contenidos, por tanto el contenido del elemento será dinámico, por tanto el elemento es no estático y no cacheable

        LinkedList<Node> cacheableChildrenLocal = new LinkedList<Node>();
        boolean childrenAreStatic = areChildNodesStaticAndFindCacheable(elem,cacheableChildrenLocal);

        boolean elementCacheableCapable = isElementValidForCaching(elem);

        if (childrenAreStatic && elementCacheableCapable)
        {
            // Elemento es cacheable, no es necesario que el elemento sea estático
            cacheableList.add(elem);
        }
        else
        {
            // El elemento no es cacheable pero lo pueden ser los hijos
            cacheableList.addAll(cacheableChildrenLocal);
        }

        boolean attributesStatic = areAllAttributesStatic(elem);

        // Aunque sea attributesStatic = true, los hijos puede que no sean estáticos todos
        return elementCacheableCapable && attributesStatic && childrenAreStatic;
    }

    protected boolean areChildNodesStaticAndFindCacheable(Element elem,LinkedList<Node> cacheableList)
    {
        if (!elem.hasChildNodes())
            return true; // El elemento es estático desde el pto. de vista de los hijos (no tiene), otra cosa es que no merezca la pena cachear pues el contenido es vacío, es importante devolver true y no false pues si devolvemos false hacemos creer que el contenido no es estático

        boolean allChildrenStatic = true;
        Node child = elem.getFirstChild();
        while(child != null)
        {
            if (!isNodeStaticAndFindCacheable(child,cacheableList))
            {
                // Este hijo no lo es por lo que no podemos decir que todos lo sean:
                allChildrenStatic = false;
            }

            child = child.getNextSibling();
        }

        return allChildrenStatic;
    }

    protected boolean isRootElementCacheableOrFindCacheableChildren(Element elem,LinkedList<Node> cacheableChildren)
    {
        // Si es estático el elemento el contenido es cacheable.
        return isElementStaticOrFindCacheableChildren(elem,cacheableChildren);
    }

    private boolean isDeclaredNotCacheable(Element elem)
    {
        // atributo itsnat:nocache
        String nocache = elem.getAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,"nocache");
        
        return "true".equals(nocache);
    }

    private boolean areAllAttributesStatic(Node node)
    {
        if (!node.hasAttributes())
            return true;

        boolean allStatic = true;
        NamedNodeMap attributes = node.getAttributes();
        for(int i = 0; i < attributes.getLength(); i++)
        {
            Attr attr = (Attr)attributes.item(i);
            if (hasVariables(attr.getValue()))
            {
                allStatic = false;
                break;
            }
        }

        return allStatic;
    }

    private boolean hasVariables(String content)
    {
        int index = content.indexOf("${");
        return index != -1;
    }

    public boolean declaredAsComponent(Element elem)
    {
        // Si el elemento esta declarado que será un componente
        // o bien en un control HTML apto para ser automáticamente un componente.
        return templateDelegate.declaredAsComponent(elem);
    }

    protected void doCacheDocument()
    {
        Document docTemplate = getDocument();
        DOMRenderImpl nodeRender = createNodeDOMRender(docTemplate,true);

        Element rootElem = docTemplate.getDocumentElement();
        LinkedList<Node> cacheableChildren = new LinkedList<Node>();
        boolean cacheable = isRootElementCacheableOrFindCacheableChildren(rootElem,cacheableChildren);
        if (cacheable)
        {
            addNodeToCache(rootElem,nodeRender);
        }
        else
        {
            for(Node child : cacheableChildren)
            {
                addNodeToCache(child,nodeRender);
            }
        }
    }

    protected void addNodeToCache(Node node,DOMRenderImpl nodeRender)
    {
        if (node instanceof Element)
            addElementToCache((Element)node,nodeRender);
        else if (node instanceof CharacterData)
            addCharacterDataToCache((CharacterData)node,nodeRender);
        else
            throw new ItsNatDOMException("INTERNAL ERROR",node);
    }

    protected void addElementToCache(Element elem,DOMRenderImpl nodeRender)
    {
        // No vale la pena obviamente cachear elementos sin hijos
        if (!elem.hasChildNodes())
            return;

        Node firstChild = elem.getFirstChild();

        if ((firstChild.getNextSibling() == null) &&
            (firstChild instanceof CharacterData))
        {
            // Elemento con un único nodo hijo y de texto
            addCharacterDataToCache((CharacterData)firstChild,nodeRender);
        }
        else
        {
            String code = serializeChildNodes(elem,nodeRender);  // No se expanden los nodos cacheados de fragmentos incluidos con nodos <include>, ya se hará cuando se envíe al cliente

            DOMUtilInternal.removeAllChildren(elem);

            Document doc = elem.getOwnerDocument();
            String markedCode = addToCache(elem,code);
            Text markNode = doc.createTextNode(markedCode);
            elem.appendChild(markNode);
        }
    }

    public MapUniqueId<CachedSubtreeImpl> getElementCacheMap()
    {
        if (elementCacheMap == null)
            this.elementCacheMap = new MapUniqueId<CachedSubtreeImpl>(idGenerator); // Así ahorramos memoria si el cache está desactivado
        return elementCacheMap;
    }

    public boolean hasCachedNodes()
    {
        return (elementCacheMap != null) || (usedTemplatesWithCachedNodes != null);
    }

    protected void addCharacterDataToCache(CharacterData node,DOMRenderImpl nodeRender)
    {
        // El objetivo en este caso no es reducir el número de objetos DOM
        // pues el Text etc se mantendrá, pero en los documentos clonados
        // no aparecerá todo el rollo de texto estático repetidas veces.

        String value = node.getData();
        if (value.length() < 100) return; // Menos de 100 letras no merece la pena, así excluimos nodos de texto cortos, separadores etc, pues la ganancia de cachear nodos de texto es únicamente para disminuir el tamaño de los documentos clonados y el "descacheado" lleva su tiempo

        // Aunque estemos en la serialización del proceso de cacheo
        // puede haber nodos cacheados de fragmentos incluidos con nodos <include>
        // no expandimos dichos nodos cacheados, ya
        // se hará cuando se envíe el código al cliente

        String code = serializeNode(node,nodeRender);
        int nodeType = node.getNodeType();
        if ((nodeType == Node.COMMENT_NODE) ||
            (nodeType == Node.CDATA_SECTION_NODE))
        {
            String prefix;
            String suffix;
            if (nodeType == Node.COMMENT_NODE)
            {
                prefix = "<!--";
                suffix = "-->";
            }
            else
            {
                prefix = "<![CDATA[";
                suffix = "]]>";
            }

            // Quitamos el prefijo y sufijo pues nos interesa guardar el contenido
            // pues al serializar de nuevo el nodo al enviar al cliente se añadirá
            // de nuevo el prefijo/sufijo
            code = removePrefixSuffix(code,prefix,suffix);
        }
        else if ((nodeType == Node.TEXT_NODE) && isMIME_XHTML())
        {
            // El serializador XHTML puede englobar el texto dentro de un <![CDATA[ ... ]]>
            // pero como substituimos el texto por la marca al serializar el nodo finalmente
            // de nuevo se añadirá <![CDATA[ ... ]]> existiendo por tanto dos erróneamente.
            // Por tanto quitamos los de ahora si existen, en el caso de contenido de <script> depende
            // de si el original tiene o no <![CDATA[

            String prefix = "<![CDATA[";
            String suffix = "]]>";
            if (code.startsWith(prefix))
                code = removePrefixSuffix(code,prefix,suffix);
        }

        String markCode = addToCache(node,code);
        node.setData(markCode);
    }

    public static String removePrefixSuffix(String code,String prefix,String suffix)
    {
        code = code.substring(prefix.length(),code.length() - suffix.length());
        return code;
    }

    protected CachedSubtreeImpl createCachedSubtree(Node node,String code)
    {
        if (node instanceof Text) return new CachedTextNodeImpl(this,code,((Text)node).getData());
        else return new CachedSubtreeDefaultImpl(this,code);
    }

    protected String addToCache(Node node,String code)
    {
        CachedSubtreeImpl cachedNode = createCachedSubtree(node,code);
        MapUniqueId<CachedSubtreeImpl> elementCacheMap = getElementCacheMap();
        elementCacheMap.put(cachedNode);
        return cachedNode.getMarkCode();
    }

    @Override
    public MarkupTemplateVersionImpl getUsedMarkupTemplateVersion(String id)
    {
        if (getId().equals(id))
            return this;
        else
            return super.getUsedMarkupTemplateVersion(id);
    }

    protected abstract MarkupTemplateVersionDelegateImpl createMarkupTemplateVersionDelegate();

    public void cleanDOMPattern()
    {
        // Está versión ya no es la última por lo que ya no servirá más
        // para clonar el objeto Documento para crear nuevos documentos
        // o nuevos fragmentos.
        // Por ello podemos liberar memoria
        // En el caso de fragmentos se deriva para liberar los fragmentos patrones (templateDoc ya es nulo)
        this.templateDoc = null;
    }

}
