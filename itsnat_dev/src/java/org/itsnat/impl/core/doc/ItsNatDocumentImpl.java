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

package org.itsnat.impl.core.doc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.ItsNatVariableResolver;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.core.*;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserUnknown;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.domimpl.ItsNatDocumentInternal;
import org.itsnat.impl.core.domutil.ElementGroupManagerImpl;
import org.itsnat.impl.core.markup.parse.XercesDOMParserWrapperImpl;
import org.itsnat.impl.core.markup.render.DOMRenderImpl;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerImpl;
import org.itsnat.impl.core.servlet.DeserialPendingTask;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionObjectInputStream;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.MarkupSourceStringMarkupImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.itsnat.impl.core.util.HasUniqueId;
import org.itsnat.impl.core.util.UniqueId;
import org.itsnat.impl.core.util.UserDataMonoThreadImpl;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatDocumentImpl extends MarkupContainerImpl implements ItsNatDocument,HasUniqueId
{
    protected transient ItsNatDocumentTemplateVersionImpl docTemplateVersion; // Apuntamos al document loader usado pues si cambia el archivo cambia el loader por defecto en el ItsNatDocumentTemplateImpl y perderíamos el caché al serializar
    protected Document doc; 
    protected ClientDocumentImpl clientDocOwner;
    protected BoundElementDocContainerImpl parentDocContainer;
    protected boolean loading = false;
    protected long creationTime = System.currentTimeMillis();
    protected transient ItsNatServletRequestImpl currentRequest;
    protected UserDataMonoThreadImpl userData;
    protected boolean invalid = false;
    protected Hashtable<String,Object> attributes;
    protected ItsNatDocComponentManagerImpl componentMgr;
    protected Map<String,Object> artifacts;
    protected boolean scriptingEnabled;
    protected DateFormat dateFormat;
    protected NumberFormat numberFormat;
    protected boolean usePatternMarkupToRender;
    protected boolean joystickMode;
    protected ElementGroupManagerImpl elemGroupMgr;
    protected DocMutationEventListenerImpl mutationListener;
    protected transient DOMRenderImpl nodeRender; // Sirve para serializar nodos concretos no el documento completo
    protected transient XercesDOMParserWrapperImpl parser;  // Guardamos un parser porque las operaciones en ItsNatDocument son monohilo y así reutilizamos
    protected String requestURL;
    protected boolean stateless;

    /**
     * Creates a new instance of ItsNatDocumentImpl
     */
    public ItsNatDocumentImpl(Document doc,ItsNatDocumentTemplateVersionImpl docTemplateVersion,Browser browser,String requestURL,ItsNatSessionImpl ownerSession,boolean stateless)
    {
        super(ownerSession.getUniqueIdGenerator());

        this.doc = doc;
        ((ItsNatDocumentInternal)doc).getDelegateDocument().setItsNatDocument(this); // Hace que este documento represente un documento remoto
        this.docTemplateVersion = docTemplateVersion;
        this.requestURL = requestURL;
        this.stateless = stateless;
        
        if (browser instanceof BrowserUnknown)
            this.scriptingEnabled = false; // Suponemos que es un robot
        else
            this.scriptingEnabled = docTemplateVersion.isScriptingEnabled();
        //this.dateFormat = docTemplateVersion.getDefaultDateFormatCloned();
        //this.numberFormat = docTemplateVersion.getDefaultNumberFormatCloned();
        this.usePatternMarkupToRender = docTemplateVersion.isUsePatternMarkupToRender();
        this.joystickMode = docTemplateVersion.isJoystickMode();      

        // ownerSession es la sesión del propietario del documento no de los observadores
        this.clientDocOwner = createClientDocumentOwner(browser,ownerSession);

        addUsedMarkupTemplateVersionWithCachedNodes(docTemplateVersion); // Además podrán añadirse los includes que haga el usuario explícitamente en cualquier momento

        this.mutationListener = createInternalMutationEventListener();
        this.componentMgr = createItsNatComponentManager();
    }


    private void writeObject(ObjectOutputStream out) throws IOException
    {
        ItsNatServletImpl itsNatServlet = getItsNatServlet();
        ItsNatServletImpl.writeObject(itsNatServlet,out);

        MarkupSourceStringMarkupImpl source = null;
        if (docTemplateVersion instanceof ItsNatStfulDocumentTemplateVersionImpl)
        {
            // Aunque no he conseguido que funcione el modo attached server en GAE
            // al menos así evitamos que de error. (NO ME ACUERDO YO CREO QUE SI FUNCIONAN)
             source = ((ItsNatStfulDocumentTemplateVersionImpl)docTemplateVersion).getMarkupSourceStringMarkup();
        }
        out.writeObject(source);

        int size = 0;
        if (usedTemplatesWithCachedNodes != null) size = usedTemplatesWithCachedNodes.size();
        out.writeInt(size);

        if (size > 0)
        {
            // usedTemplatesWithCachedNodes es transient, necesita un tratamiento especial
            // y aunque pertenezca a MarkupContainerImpl sólo la derivada ItsNatDocumentImpl
            // se serializa (no es el caso de MarkupTemplateVersionImpl
            // Al menos está con seguridad el propio template del documento
            // por lo que salvamos el propio docTemplateVersion a través de la colección

            int posDocTemplateVersion = -1;
            MarkupTemplateVersionImpl[] templates = usedTemplatesWithCachedNodes.values().toArray(new MarkupTemplateVersionImpl[size]);
            for(int i = 0; i < templates.length; i++ )
            {
                MarkupTemplateVersionImpl templateVersion = templates[i];
                if (templateVersion == docTemplateVersion)
                {
                    posDocTemplateVersion = i;
                    break;
                }
            }
            out.writeInt(posDocTemplateVersion);

            for(int i = 0; i < templates.length; i++ )
            {
                MarkupTemplateVersionImpl templateVersion = templates[i];
                MarkupTemplateVersionImpl.writeObject(templateVersion, out);
            }
        }
        else
        {
            // Hay que salvar específicamente el docTemplateVersion
            // Se pasa por aquí por ejemplo en el test de carga remota de google.com
            // pues el template que construimos le decimos que no cachee (para poder retocar la página)
            MarkupTemplateVersionImpl.writeObject(docTemplateVersion, out);
        }

        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        final String servletName = ItsNatServletImpl.readObject(in);

        final MarkupSourceStringMarkupImpl source = (MarkupSourceStringMarkupImpl)in.readObject(); // Puede ser null (lo normal)

        int size = in.readInt();

        DeserialPendingTask task = null;
        if (size > 0)
        {
            final int posDocTemplateVersion = in.readInt();

            final ArrayList<String[]> templateIds = new ArrayList<String[]>(size);
            for(int i = 0; i < size; i++)
            {
                String[] templateId = MarkupTemplateVersionImpl.readObject(in);
                templateIds.add(templateId);
            }

            task = new DeserialPendingTask()
            {
                public void process(ItsNatServletImpl itsNatServlet,ItsNatServletRequest request, ItsNatServletResponse response)
                {
                    ItsNatDocumentImpl.this.usedTemplatesWithCachedNodes = createUsedTemplateVersionsWithCachedNodesMap(itsNatServlet);
                    for(int i = 0; i < templateIds.size(); i++)
                    {
                        // Recargamos los templates, si no han cambiado todo será igual
                        // pero si ha cambiado alguno no se si funcionará bien pues
                        // en teoría los templates del documento no deben cambiar,
                        // la alternativa de serializar los MarkupTemplateVersionImpl
                        // podría ser peor
                        String[] templateId = templateIds.get(i);
                        MarkupTemplateVersionImpl templateVersion;
                        if (i == posDocTemplateVersion)
                        {
                            templateVersion = MarkupTemplateVersionImpl.getNewestMarkupTemplateVersion(itsNatServlet,templateId,source,request,response);
                            ItsNatDocumentImpl.this.docTemplateVersion = (ItsNatDocumentTemplateVersionImpl)templateVersion;
                        }
                        else
                            templateVersion = MarkupTemplateVersionImpl.getNewestMarkupTemplateVersion(itsNatServlet,templateId,null,request,response);

                        usedTemplatesWithCachedNodes.put(templateVersion);
                    }
                }
            };
        }
        else
        {
            final String[] templateId = MarkupTemplateVersionImpl.readObject(in);

            task = new DeserialPendingTask()
            {
                public void process(ItsNatServletImpl itsNatServlet,ItsNatServletRequest request, ItsNatServletResponse response)
                {
                    MarkupTemplateVersionImpl templateVersion = MarkupTemplateVersionImpl.getNewestMarkupTemplateVersion(itsNatServlet,templateId,source,request,response);
                    ItsNatDocumentImpl.this.docTemplateVersion = (ItsNatDocumentTemplateVersionImpl)templateVersion;
                }
            };
        }

        in.defaultReadObject();  // Antes pues podemos necesitar el ClientDocumentOwnerImpl

        ItsNatSessionImpl itsNatSession = ItsNatSessionObjectInputStream.getItsNatSession(in);
        itsNatSession.addDeserialPendingTask(servletName,task); // Lo dejamos pendiente tras la deserialización


        // Hay que tener en cuenta que los event listeners internos no se serializan
        DocMutationEventListenerImpl mutationListener = getDocMutationEventListener();

        ItsNatDocumentInternal doc = (ItsNatDocumentInternal)getDocument();
        doc.addEventListenerInternal("DOMNodeInserted",mutationListener,false);
        doc.addEventListenerInternal("DOMNodeRemoved",mutationListener,false);
        doc.addEventListenerInternal("DOMAttrModified",mutationListener,false);
        doc.addEventListenerInternal("DOMCharacterDataModified",mutationListener,false);
    }

    @Override    
    public boolean isCreatedByStatelessEvent()    
    {
        return stateless;
    }
    
    public String getRequestURL()
    {
        return requestURL;
    }

    public String getIdGenPrefix()
    {
        return "doc";
    }

    public abstract DocMutationEventListenerImpl createInternalMutationEventListener();

    public Node getContainerNode()
    {
        if (parentDocContainer == null) return null;
        return (Node)parentDocContainer.getElementDocContainer();
    }

    public ItsNatStfulDocumentImpl getParentItsNatStfulDocument()
    {
        if (parentDocContainer == null) return null;
        return parentDocContainer.getItsNatStfulDocument();
    }

    public BoundElementDocContainerImpl getParentHTMLDocContainer()
    {
        return parentDocContainer;
    }

    public void setParentHTMLDocContainer(BoundElementDocContainerImpl parentDocContainer)
    {
        this.parentDocContainer = parentDocContainer;
    }

    public DOMRenderImpl getDOMRenderForNodes()
    {
        if (nodeRender == null) this.nodeRender = docTemplateVersion.createNodeDOMRender(doc);
        return nodeRender;
    }

    public XercesDOMParserWrapperImpl getMarkupParser()
    {
        if (parser == null) this.parser = docTemplateVersion.createMarkupParser(getEncoding()); // Creamos un parser propio porque no es multihilo (no puede ser compartido a nivel de template)
        return parser;
    }

    public Document parseDocument(String code)
    {
        return docTemplateVersion.parseDocument(code,getMarkupParser(),false);
    }

    public String serializeDocument(Document doc,boolean resCachedNodes)
    {
        DOMRenderImpl docRender = DOMRenderImpl.createDOMRender(doc,getMIME(),getEncoding(),false);
        return serializeDocument(doc,docRender,resCachedNodes);
    }

    public DocMutationEventListenerImpl getDocMutationEventListener()
    {
        return mutationListener;
    }

    public abstract ClientDocumentImpl createClientDocumentOwner(Browser browser,ItsNatSessionImpl ownerSession);

    public DocumentFragment toDOM(String code)
    {
        return getItsNatDocumentTemplateVersion().parseFragmentToDocFragment(code,this);
    }

    public ItsNatServletImpl getItsNatServlet()
    {
        return docTemplateVersion.getItsNatServlet();
    }


    public boolean isMIME_HTML()
    {
        return getItsNatDocumentTemplateVersion().isMIME_HTML();
    }

    public boolean isDebugMode()
    {
        return getItsNatDocumentTemplateVersion().isDebugMode();
    }

    public int getClientErrorMode()
    {
        return getItsNatDocumentTemplateVersion().getClientErrorMode();
    }

    public boolean isFastLoadMode()
    {
        return getItsNatDocumentTemplateVersion().isFastLoadMode();
    }

    public boolean isLoadingPhaseAndFastLoadMode()
    {
        return isLoading() && isFastLoadMode();
    }

    public boolean isDOMInternalMode()
    {
        // En el futuro quizás sea configurable via template etc.
        return false;
    }

    public ItsNatDocumentTemplate getItsNatDocumentTemplate()
    {
        return getItsNatDocumentTemplateImpl();
    }

    public ItsNatDocumentTemplateImpl getItsNatDocumentTemplateImpl()
    {
        return getItsNatDocumentTemplateVersion().getItsNatDocumentTemplate();
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
        return doc;
    }

    public String getBindToCustomFunc()
    {
        return null; // Por defecto.
    }

    public ItsNatDocumentTemplateVersionImpl getItsNatDocumentTemplateVersion()
    {
        return docTemplateVersion;
    }

    public boolean isLoading()
    {
        return loading;
    }

    public void startLoading()
    {
        this.loading = true;

        componentMgr.startLoading();

        registerMutationListener();
    }

    public void endLoading()
    {
        this.loading = false;
    }

    public void registerMutationListener()
    {
        DocMutationEventListenerImpl mutationListener = getDocMutationEventListener();

        ItsNatDocumentInternal doc = (ItsNatDocumentInternal)getDocument();
        doc.addEventListenerInternal("DOMNodeInserted",mutationListener,false);
        doc.addEventListenerInternal("DOMNodeRemoved",mutationListener,false);
        doc.addEventListenerInternal("DOMAttrModified",mutationListener,false);
        doc.addEventListenerInternal("DOMCharacterDataModified",mutationListener,false);
    }

    public String serializeDocument()
    {
        return serializeDocument(getDocument(),true);
    }

    public UserDataMonoThreadImpl getUserData()
    {
        if (userData == null)
            this.userData = new UserDataMonoThreadImpl(); // Para ahorrar memoria si no se usa, no es necesario sincronizar pues el acceso al Document está sincronizado
        return userData;
    }

    public String[] getUserValueNames()
    {
        return getUserData().getUserDataNames();
    }

    public boolean containsUserValueName(String name)
    {
        return getUserData().containsName(name);
    }

    public Object getUserValue(String name)
    {
        return getUserData().getUserData(name);
    }

    public Object setUserValue(String name,Object value)
    {
        return getUserData().setUserData(name,value);
    }

    public Object removeUserValue(String name)
    {
        return getUserData().removeUserData(name);
    }

    public ItsNatServletRequestImpl getCurrentItsNatServletRequest()
    {
        return currentRequest;
    }

    public void setCurrentItsNatServletRequest(ItsNatServletRequestImpl currentRequest)
    {
        this.currentRequest = currentRequest;
    }

    public ClientDocumentImpl getClientDocumentOwnerImpl()
    {
        return clientDocOwner;
    }

    public ClientDocument getClientDocumentOwner()
    {
        return getClientDocumentOwnerImpl();
    }

    public boolean isInvalid()
    {
        return invalid;
    }

    public void setInvalid()
    {
        if (invalid) return; // Ya está invalidado

        setInvalidInternal();
    }

    protected void setInvalidInternal()
    {
        this.invalid = true;
    }

    public Hashtable<String,Object> getAttributeMap()
    {
        if (attributes == null) // para ahorrar memoria si no se usa
            this.attributes = new Hashtable<String,Object>();
        return attributes;
    }

    public Object getAttribute(String name)
    {
        return getAttributeMap().get(name);
    }

    public void setAttribute(String name,Object value)
    {
        getAttributeMap().put(name,value);
    }

    public Enumeration<String> getAttributeNames()
    {
        // Este método es la única razón para usar un Hashtable (el cual está sincronizado innecesariamente)
        // por seguir el patrón de ServletRequest etc
        return getAttributeMap().keys();
    }

    public void removeAttribute(String name)
    {
        getAttributeMap().remove(name);
    }

    public Object getVariable(String varName)
    {
        Object value = getAttribute(varName);
        if (value != null)
            return value;

        return getClientDocumentOwnerImpl().getItsNatSessionImpl().getVariable(varName);
    }

    public abstract void removeEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient);


    public ItsNatVariableResolver createItsNatVariableResolver()
    {
        return createItsNatVariableResolver(false);
    }

    public ItsNatVariableResolver createItsNatVariableResolver(boolean disconnected)
    {
        ItsNatDocumentImpl parent;
        if (disconnected) parent = null;
        else parent = this;
        return new ItsNatVariableResolverImpl(null,null,parent,null,null);
    }

    public abstract ElementGroupManagerImpl createElementGroupManager();

    public ElementGroupManagerImpl getElementGroupManagerImpl()
    {
        if (elemGroupMgr == null)
            this.elemGroupMgr = createElementGroupManager();
        return elemGroupMgr;
    }

    public ElementGroupManager getElementGroupManager()
    {
        return getElementGroupManagerImpl();
    }

    public ItsNatDocComponentManagerImpl getItsNatComponentManagerImpl()
    {
        return componentMgr;
    }

    public ItsNatComponentManager getItsNatComponentManager()
    {
        return getItsNatComponentManagerImpl();
    }

    public abstract ItsNatDocComponentManagerImpl createItsNatComponentManager();

    public boolean hasArtifacts()
    {
        if (artifacts == null) return false;
        return !artifacts.isEmpty();
    }

    public Map<String,Object> getArtifactMap()
    {
        if (artifacts == null)
            this.artifacts = new HashMap<String,Object>();
        return artifacts;
    }

    public void registerArtifact(String name,Object value)
    {
        Map<String,Object> artifacts = getArtifactMap();
        artifacts.put(name,value);
    }

    public Object getArtifact(String name)
    {
        if (!hasArtifacts()) return null;

        Map<String,Object> artifacts = getArtifactMap();
        return artifacts.get(name);
    }

    public Object removeArtifact(String name)
    {
        Map<String,Object> artifacts = getArtifactMap();
        return artifacts.remove(name);
    }

    public Object getArtifact(String name,boolean cascade)
    {
        Object artif = getArtifact(name);
        if (cascade && (artif == null))
            artif = getItsNatDocumentTemplateImpl().getArtifact(name,true);
        return artif;
    }

    public boolean hasCachedNodes()
    {
        return (usedTemplatesWithCachedNodes != null);
    }

    public String serializeNode(Node node)
    {
        String code = getItsNatDocumentTemplateVersion().serializeNode(node,getDOMRenderForNodes());

        code = resolveCachedNodes(code,false); // resolveEntities es false porque este método aunque sea llamado para un nodo Text no es usado el texto serializado en métodos tipo text.data = ".." o document.createTextNode(...)

        return code;
    }

    public DateFormat getDefaultDateFormat()
    {
        if (dateFormat == null)
        {
            // Primera vez:
            this.dateFormat = docTemplateVersion.getDefaultDateFormatCloned();
        }
        return dateFormat;
    }

    public void setDefaultDateFormat(DateFormat dateFormat)
    {
        this.dateFormat = dateFormat;
    }

    public NumberFormat getDefaultNumberFormat()
    {
        if (numberFormat == null)
        {
            // Primera vez:
            this.numberFormat = docTemplateVersion.getDefaultNumberFormatCloned();
        }
        return numberFormat;
    }

    public void setDefaultNumberFormat(NumberFormat numberFormat)
    {
        this.numberFormat = numberFormat;
    }

    public boolean isReferrerEnabled()
    {
        return getItsNatDocumentTemplateVersion().isReferrerEnabled();
    }

    public boolean isEventsEnabled()
    {
        if (!isScriptingEnabled())
            return false;
        return getItsNatDocumentTemplateVersion().isEventsEnabled();
    }

    public boolean isScriptingEnabled()
    {
        return scriptingEnabled;
    }

    public boolean isAutoCleanEventListeners()
    {
        return getItsNatDocumentTemplateVersion().isAutoCleanEventListeners();
    }

    public boolean isUseXHRSyncOnUnloadEvent()
    {
        return getItsNatDocumentTemplateVersion().isUseXHRSyncOnUnloadEvent();
    }

    public boolean isLoadScriptInline()
    {
        return getItsNatDocumentTemplateVersion().isLoadScriptInline();
    }

    public int getUseGZip()
    {
        return getItsNatDocumentTemplateVersion().getUseGZip();
    }

    public boolean isReferrerPushEnabled()
    {
        return getItsNatDocumentTemplateVersion().isReferrerPushEnabled();
    }

    public String getEncoding()
    {
        return getItsNatDocumentTemplateVersion().getEncoding();
    }

    public String getNamespace()
    {
        return getItsNatDocumentTemplateVersion().getNamespace();
    }

    public String getMIME()
    {
        return getItsNatDocumentTemplateVersion().getMIME();
    }

    public long getCreationTime()
    {
        return creationTime;
    }

    public boolean isUsePatternMarkupToRender()
    {
        return usePatternMarkupToRender;
    }

    public void setUsePatternMarkupToRender(boolean usePatternMarkupToRender)
    {
        this.usePatternMarkupToRender = usePatternMarkupToRender;
    }

    public boolean isJoystickMode()
    {
        return joystickMode;
    }

    public void setJoystickMode(boolean value)
    {
        this.joystickMode = value;
    }


    // EventTarget methods

    public void addEventListener(String type, EventListener listener, boolean useCapture)
    {
        addEventListener((EventTarget)getDocument(),type,listener,useCapture);
    }

    public void removeEventListener(String type, EventListener listener, boolean useCapture)
    {
        removeEventListener((EventTarget)getDocument(),type,listener,useCapture);
    }

    public boolean dispatchEvent(Event evt) throws EventException
    {
        return dispatchEvent((EventTarget)getDocument(),evt);
    }

    public abstract ClientDocumentImpl[] getAllClientDocumentsCopy();

}
