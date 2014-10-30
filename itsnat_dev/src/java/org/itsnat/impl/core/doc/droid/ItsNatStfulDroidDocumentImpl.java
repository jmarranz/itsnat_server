/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.doc.droid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletContext;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.mgr.droid.ItsNatStfulDroidDocComponentManagerImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.DefaultElementGroupManagerImpl;
import org.itsnat.impl.core.domutil.ElementGroupManagerImpl;
import org.itsnat.impl.core.listener.droid.ItsNatDroidEventListenerWrapperImpl;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerImpl;
import org.itsnat.impl.core.mut.doc.droid.DocMutationEventListenerStfulDroidImpl;
import org.itsnat.impl.core.registry.droid.ItsNatDroidEventListenerRegistryImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.droid.ItsNatStfulDroidDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.droid.ScriptCode;
import org.itsnat.impl.core.template.droid.ScriptCodeInline;
import org.itsnat.impl.core.template.droid.ScriptLocalFile;
import org.itsnat.impl.core.template.droid.ScriptURI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDroidDocumentImpl extends ItsNatStfulDocumentImpl
{
    protected ItsNatDroidEventListenerRegistryImpl droidEvtListenerRegistry;
    protected List<ScriptCode> scriptCodeList = new LinkedList<ScriptCode>();
    
    public ItsNatStfulDroidDocumentImpl(Document doc, ItsNatDocumentTemplateVersionImpl docTemplate, Browser browser, String requestURL, ItsNatSessionImpl ownerSession, boolean stateless)
    {
        super(doc, docTemplate, browser, requestURL, ownerSession, stateless);
        
        extractScriptElements();
    }

    public ItsNatStfulDroidDocumentTemplateVersionImpl getItsNatStfulDroidDocumentTemplateVersion()
    {
        return (ItsNatStfulDroidDocumentTemplateVersionImpl)docTemplateVersion;
    }    
    
    @Override
    public Element getVisualRootElement()
    {
        return getDocument().getDocumentElement();
    }

    @Override
    public boolean isNewNodeDirectChildOfContentRoot(Node newNode)
    {
        Node parentNode = newNode.getParentNode();
        if (parentNode == null) return false; // No ocurre nunca pero por si acaso
        Document doc = getDocument();
        return (parentNode.getParentNode() == doc.getDocumentElement());
    }

    @Override
    public DocMutationEventListenerImpl createInternalMutationEventListener()
    {
        return new DocMutationEventListenerStfulDroidImpl(this);
    }

    @Override
    public ElementGroupManagerImpl createElementGroupManager()
    {
        return new DefaultElementGroupManagerImpl(this);
    }

    @Override
    public ItsNatDocComponentManagerImpl createItsNatComponentManager()
    {
        return new ItsNatStfulDroidDocComponentManagerImpl(this);
    }
    
    public int removeAllPlatformEventListeners(EventTarget target,boolean updateClient)
    {
        if (!hasDroidEventListeners()) return 0;

        return getDroidEventListenerRegistry().removeAllItsNatDroidEventListeners(target,updateClient);
    }

    public ItsNatDroidEventListenerWrapperImpl getDroidEventListenerById(String listenerId)
    {
        if (!hasDroidEventListeners()) return null;

        return getDroidEventListenerRegistry().getItsNatDroidEventListenerById(listenerId);
    }    
    
    public void addPlatformEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        addDroidEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
    }        
    
    public void addDroidEventListener(EventTarget nodeTarget,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        getDroidEventListenerRegistry().addItsNatDroidEventListener(nodeTarget,type,listener,useCapture,commMode,extraParams,preSendCode,eventTimeout,bindToCustomFunc);
    }    
    
    public void removePlatformEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        removeDroidEventListener(target,type,listener,useCapture, updateClient);
    }
    
    public void removeDroidEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient)
    {
        getDroidEventListenerRegistry().removeItsNatDroidEventListener(target,type,listener,useCapture,updateClient);
    }    
    
    public void addMutationEventListener(EventTarget target,EventListener listener,boolean useCapture,int commMode,String preSendCode,long eventTimeout,String bindToCustomFunc)
    {
        throw new ItsNatException("Mutation events are not supported in Droid ItsNat documents");
    }
    
    public void addMutationEventListener(EventTarget nodeTarget,EventListener mutationListener,boolean useCapture)
    {
        throw new ItsNatException("Mutation events are not supported in Droid ItsNat documents");
    }

    public void removeMutationEventListener(EventTarget target,EventListener listener,boolean useCapture)
    {
        throw new ItsNatException("Mutation events are not supported in Droid ItsNat documents");
    }

    public boolean hasDroidEventListeners()
    {
        if (droidEvtListenerRegistry == null)
            return false;
        return !droidEvtListenerRegistry.isEmpty();
    }

    public ItsNatDroidEventListenerRegistryImpl getDroidEventListenerRegistry()
    {
        if (droidEvtListenerRegistry == null) // Evita instanciar si no se usa, caso de servir XML
            this.droidEvtListenerRegistry = new ItsNatDroidEventListenerRegistryImpl(this,null);
        return droidEvtListenerRegistry;
    }
    
    public void renderPlatformEventListeners(ClientDocumentAttachedClientImpl clientDoc)    
    {
        if (hasDroidEventListeners())
            getDroidEventListenerRegistry().renderItsNatNormalEventListeners(clientDoc);        
    }    

    protected void extractScriptElements()
    {
        // Recuerda que el <script> principal nunca llega a ser DOM pues se mete como cadena en la string del documento serializado
        NodeList scriptElemList = doc.getElementsByTagName("script");
        int len = scriptElemList.getLength();
        if (len > 0)
        {
            ServletContext servContext = getItsNatDocumentTemplateImpl().getItsNatServletImpl().getServlet().getServletConfig().getServletContext();
            for(int i = 0; i < len; i++)
            {
                Element scriptElem = (Element)scriptElemList.item(i);                
                addScript(scriptElem,servContext);
            }

            // Los eliminamos del DOM, para que no interfieran
            while(scriptElemList.getLength() > 0)
            {
                Element scriptElem = (Element)scriptElemList.item(0);
                scriptElem.getParentNode().removeChild(scriptElem);
            }
        }
    }    
    
    public List<String> getScriptCodeList()
    {
        List<String> res = new LinkedList<String>();
        for(ScriptCode item : scriptCodeList)
            res.add(item.getCode());
        return res;
    }    
    
    public void addScript(Element scriptElem)
    {
        ServletContext servContext = getItsNatDocumentTemplateImpl().getItsNatServletImpl().getServlet().getServletConfig().getServletContext();    
        addScript(scriptElem,servContext);
    }    
        
    private void addScript(Element scriptElem,ServletContext servContext)
    {
        ScriptCode scriptCodeItem = null;
        String src = scriptElem.getAttribute("src");
        if (!"".equals(src))
        {    
            URI uri = null;
            try { uri = new URI(src); }
            catch (URISyntaxException ex) { throw new ItsNatException(ex);  }

            String scheme = uri.getScheme();
            if (scheme == null)
                scriptCodeItem = new ScriptLocalFile(src,servContext);                        
            else if (scheme.equals("http") || scheme.equals("https"))                 
                scriptCodeItem = new ScriptURI(uri);
            else
                throw new ItsNatException("Scheme not allowed or supported: " + scheme); // para evitar el uso de "file:" etc
        }
        else
        {
            String code = DOMUtilInternal.getTextContent(scriptElem, true);
            scriptCodeItem = new ScriptCodeInline(code);
        }
        scriptCodeList.add(scriptCodeItem);
    }    
}
