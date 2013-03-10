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

package org.itsnat.impl.core.resp.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.itsnat.impl.core.resp.shared.bybrow.ResponseDelegStfulLoadDocByBrowserImpl;
import org.itsnat.impl.core.resp.shared.otherns.ResponseDelegateOtherNSLoadDocImpl;
import org.itsnat.impl.core.resp.shared.html.ResponseDelegateHTMLLoadDocImpl;
import org.itsnat.impl.core.resp.*;
import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.browser.webkit.BrowserWebKit;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientCometImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientTimerImpl;
import org.itsnat.impl.core.doc.ItsNatHTMLDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatOtherNSDocumentImpl;
import org.itsnat.impl.core.clientdoc.SVGWebInfoImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.jsren.dom.node.JSRenderPropertyImpl;
import org.itsnat.impl.core.listener.domstd.OnLoadBackForwardListenerImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerImpl;
import org.itsnat.impl.res.core.js.LoadScriptImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLTextAreaElement;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseDelegateStfulLoadDocImpl extends ResponseDelegateStfulImpl
{
    protected static final String scriptLoadTimeStamp = Long.toString(System.currentTimeMillis());
    protected StringBuilder fixDOMCode; // Código JavaScript que se ejecutará al comienzo de todo con la finalidad de arreglar el árbol DOM cliente antes de que se accedan a los nodos. NO debería accederse a los nodos usando paths ItsNat o accediendo a la cache
    protected ResponseDelegStfulLoadDocByBrowserImpl delegByBrowser;
    protected LinkedList<String> scriptFilesToLoad;
    
    /**
     * Creates a new instance of ResponseDelegateStfulLoadDocImpl
     */
    public ResponseDelegateStfulLoadDocImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);

        this.delegByBrowser = ResponseDelegStfulLoadDocByBrowserImpl.createResponseDelegStfulLoadDocByBrowser(this);
    }

    public static ResponseDelegateStfulLoadDocImpl createResponseDelegateStfulLoadDoc(ResponseLoadStfulDocumentValid responseParent)
    {
        ItsNatStfulDocumentImpl itsNatDoc = responseParent.getItsNatStfulDocument();
        if (itsNatDoc instanceof ItsNatHTMLDocumentImpl)
            return ResponseDelegateHTMLLoadDocImpl.createResponseDelegateHTMLLoadDoc(responseParent);
        else if (itsNatDoc instanceof ItsNatOtherNSDocumentImpl)
            return ResponseDelegateOtherNSLoadDocImpl.createResponseDelegateOtherNSLoadDoc(responseParent);
        else
            return null; // No se llega nunca, futuros tipos
    }

    public ResponseLoadStfulDocumentValid getResponseLoadStfulDocumentValid()
    {
        return (ResponseLoadStfulDocumentValid)responseParent;
    }

    public ResponseLoadDocImpl getResponseLoadDoc()
    {
        return (ResponseLoadDocImpl)responseParent;
    }

    public void addFixDOMCodeToSend(String code)
    {
        getFixDOMCodeToSend().append(code);
    }

    public boolean hasFixDOMCodeToSend()
    {
        return (fixDOMCode != null);
    }

    public StringBuilder getFixDOMCodeToSend()
    {
        if (fixDOMCode == null) this.fixDOMCode = new StringBuilder();
        return fixDOMCode;
    }

    public void addScriptFileToLoad(String code)
    {
        getScriptFilesToLoad().add(code);
    }

    public boolean hasScriptFilesToLoad()
    {
        return (scriptFilesToLoad != null);
    }

    public LinkedList<String> getScriptFilesToLoad()
    {
        if (scriptFilesToLoad == null) this.scriptFilesToLoad = new LinkedList<String>();
        return scriptFilesToLoad;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return responseParent.getItsNatStfulDocument();
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return getResponseLoadStfulDocumentValid().getClientDocumentStful();
    }

    public boolean isSerializeBeforeDispatching()
    {
        return getResponseLoadStfulDocumentValid().isSerializeBeforeDispatching();
    }

    public void processResponse()
    {
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        DocMutationEventListenerImpl mutListener = itsNatDoc.getDocMutationEventListener();

        String docMarkup = null;

        boolean serializeBefore = isSerializeBeforeDispatching();
        if (serializeBefore) // Caso fastLoad=false y remote control
        {
            mutListener.setEnabled(false); // Desactivamos totalmente pues hay muchos casos en los que antes de serializar necesitamos hacer cambios temporales en el DOM que no queremos que generen código JavaScript
            preSerializeDocument();
            docMarkup = serializeDocument();
            mutListener.setEnabled(true); // Restauramos

            rewriteClientUIControlProperties();
        }

        dispatchRequestListeners();

        if (!serializeBefore) // Caso fast load
        {
            // En caso Fast Load DEBE ejecutarse después de los listeners del
            // usuario porque el estado del DOM servidor en carga es el resultado
            // de ejecutar dichos listeners en el servidor que deben también
            // ejecutarse en JavaScript.
            rewriteClientUIControlProperties();

            mutListener.setEnabled(false); // Desactivamos totalmente pues hay muchos casos en los que antes de serializar necesitamos hacer cambios temporales en el DOM que no queremos que generen código JavaScript
            preSerializeDocument();
            docMarkup = serializeDocument();
            mutListener.setEnabled(true); // Restauramos
        }

        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        if (clientDoc.isScriptingEnabled())
        {
            String scriptsMarkup = generateFinalScriptsMarkup();
            if (getResponseLoadStfulDocumentValid().isOnlyReturnMarkupOfScripts())
                docMarkup = scriptsMarkup;
            else
                docMarkup = addScriptMarkupToDocMarkup(docMarkup,scriptsMarkup);
        }

        getResponseLoadDoc().sendMarkupToClient(docMarkup);
    }

    protected void dispatchRequestListeners()
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        Browser browser = clientDoc.getBrowser();
        if (clientDoc.canReceiveALLNormalEvents() &&
            browser.isCachedBackForwardExecutedScripts())
        {
            // Opera 9 y BlackBerryOld tienen este problema
            // Opera 9 soporta DOMContentLoaded
            // el cual se envía *antes* del evento load, es importante porque Opera
            // no recarga la página en un back/forward y la emisión de este evento
            // puede provocar la recarga de la página, así evitamos un fallo con load que es un evento
            // más normal que pueda usar el usuario.
            // Acerca de DOMContentLoad: http://developer.mozilla.org/en/docs/Gecko-Specific_DOM_Events
            // Alternativas: http://dean.edwards.name/weblog/2005/02/order-of-events/

            ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
            Document doc = itsNatDoc.getDocument();

            OnLoadBackForwardListenerImpl listener = new OnLoadBackForwardListenerImpl();
            ParamTransport[] extraParam = OnLoadBackForwardListenerImpl.createExtraParams();
            String eventType;
            EventTarget target;
            if (browser.isClientWindowEventTarget())
            {
                if (browser.isDOMContentLoadedSupported())
                    eventType = "DOMContentLoaded";
                else
                    eventType = "load";
                target = (EventTarget)((DocumentView)doc).getDefaultView();
            }
            else
            {
                eventType = "SVGLoad";
                target = (EventTarget)doc.getDocumentElement();
            }

            clientDoc.addEventListener(target,eventType,listener,false,clientDoc.getCommMode(),extraParam,null,-1,null);
        }

        getResponseLoadDoc().dispatchRequestListeners();

    }

    protected void rewriteClientUIControlProperties()
    {
        // Tratamos de revertir las acciones del usuario antes de se cargue el
        // script de inicio y el autofill de algunos navegadores.

        // El form autofill y los clicks del usuario sobre los controles form
        // pueden cambiar el estado de estos antes de que se ejecute el script
        // de carga y por tanto no generar eventos de cambio, por ello en el script
        // de carga revertimos cualquier posible cambio.

        // Para verificar que el comportamiento es correcto usar un visor remoto
        // para confirmar que el estado del servidor (lo que se ve en el visor remoto)
        // es el mismo que lo que se ve en la página observada.

        // En el caso de control remoto en modo readonly en teoría el usuario
        // NO debe tocar los controles, sin embargo reescribimos los controles
        // también en el readonly pues está también el form autofill/autocomplete, por ejemplo
        // en Safari 3 desde una página control remote cambias de página via URL y
        // vuelves via "back" y el form auto fill actúa poniendo los forms al estado
        // inicial cuando se cargo la página control remoto.
        // Hay otra razón aunque solo aplica a los <textarea>:
        // el nodo de texto hijo del textarea es el que se usa para el valor inicial
        // del textarea, NO el atributo "value". Al cargar la página en control remoto
        // el atributo value tiene el valor del actual estado del control sin embargo en tiempo de carga
        // dicho atributo es ignorado en favor del nodo de texto hijo. Por lo tanto
        // necesitamos definir explícitamente la propiedad "value".

        final ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        if (!clientDoc.isScriptingEnabled())
            return;

        // Revertir cambios hechos a través de JavaScript y que el navegador ha podido memorizar para el autofill, por ejemplo la etiqueta de un botón que cambia
        boolean revertJSChanges = delegByBrowser.getRevertJSChanges();

        String code = rewriteClientUIControlProperties(revertJSChanges);
        clientDoc.addCodeToSend(code);
    }

    public String rewriteClientUIControlProperties(boolean revertJSChanges)
    {
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        Document doc = itsNatDoc.getDocument();
        StringBuilder code = new StringBuilder();
        Element elem = doc.getDocumentElement();
        do
        {
            rewriteClientUIControlProperties(elem,revertJSChanges,code);

            elem = ItsNatTreeWalker.getNextElement(elem);
        }
        while(elem != null);

        return code.toString();
    }

    protected abstract void rewriteClientUIControlProperties(Element elem,boolean revertJSChanges,StringBuilder code);


    protected boolean rewriteClientHTMLUIControlProperties(Element elem,boolean revertJSChanges,StringBuilder code)
    {
        // Actualizamos la propiedad "value" en todos los tipos de control que lo utilizan
        // cuando revertJSChanges = true, porque en caso de revertir el autofill de formularios por parte del browser
        // si se cambió el "value" en una carga anterior de la página dicho
        // value queda cacheado, y el navegador tenderá a restaurar el valor antiguo (ej, un botón que cambió de nombre en la página antigua)
        // En el caso de cambios del usuario (revertJSChanges = false), el usuario
        // no puede cambiar el atributo "value" de ciertos controles tal y como botones
        // no merece la pena por tanto "reescribir" el value pues además contiene texto que hay que enviar por la red.
        // No consideramos el posible efecto de JavaScript cliente no ItsNat (suponemos siempre control por parte del servidor).
        // Sólo procesaremos obviamente los formularios que no fueron cacheados

        // FireFox 3.5, Opera 9.6 , Chrome 2 y Safari 3.1 al menos (versiones previas quizás también)
        // admiten XHTML dentro de SVG.
        // Admiten controles pero regular (con fallos visuales).
        // http://starkravingfinkle.org/blog/2007/07/firefox-3-svg-foreignobject/
        // http://starkravingfinkle.org/blog/wp-content/uploads/2007/07/foreignobject-text.svg
        // Y por supuesto XUL también admite XHTML embebido

        // Devolvemos true si es un elemento XHTML

        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();

        if (!(elem instanceof HTMLElement))
            return false; // No es un elemento XHTML, puede haber también elementos SVG etc

        if (elem instanceof HTMLSelectElement)
        {
            HTMLSelectElement select = (HTMLSelectElement)elem;
            code.append( "var elem = " + clientDoc.getNodeReference(elem,true,true) + ";\n" );

            // El uso de HTMLSelectElement.getOptions() es terriblemente ineficiente
            // Toleramos la presencia de <optgroup>
            // Los <option> dentro de un <optgroup> se manifiestan en la colección JavaScript "options"
            LinkedList<Node> options = DOMUtilInternal.getChildElementListWithTagNameNS(select,NamespaceUtil.XHTML_NAMESPACE,"option",true);
            if (options != null)
            {
                int i = 0;
                for(Iterator<Node> it = options.iterator(); it.hasNext(); i++)
                {
                    HTMLOptionElement optElem = (HTMLOptionElement)it.next();
                    String opref = "elem.options[" + i + "]";
                    processUIControlProperty(optElem,opref,"selected",code,clientDoc);
                    if (revertJSChanges)
                        processUIControlProperty(optElem,opref,"value",code,clientDoc);
                }
            }
        }
        else if (elem instanceof HTMLInputElement)
        {
            String type = elem.getAttribute("type");
            if (type.equals("checkbox") || type.equals("radio"))
            {
                processUIControlProperty(elem,"checked",code,clientDoc);
                if (revertJSChanges)
                    processUIControlProperty(elem,"value",code,clientDoc);
            }
            else if (type.equals("text") || type.equals("password"))
                processUIControlProperty(elem,"value",code,clientDoc);
            else
            {   // Casos: submit reset file hidden image button (en el caso de file no hay problema, el render sabe que no hay que hacer nada)
                if (revertJSChanges)
                    processUIControlProperty(elem,"value",code,clientDoc);
            }
        }
        else if (elem instanceof HTMLTextAreaElement)
        {
            rewriteClientHTMLTextAreaProperties((HTMLTextAreaElement)elem,code);
        }

        return true;
    }

    protected abstract void rewriteClientHTMLTextAreaProperties(HTMLTextAreaElement elem,StringBuilder code);

    protected void processUIControlProperty(Element elem,String attrName,StringBuilder code,ClientDocumentStfulImpl clientDoc)
    {
        code.append( "var elem = " + clientDoc.getNodeReference(elem,true,true) + ";\n" );
        code.append( JSRenderPropertyImpl.renderUIControlProperty(elem,"elem",attrName,clientDoc) );
    }

    protected void processUIControlProperty(Element elem,String elemVarName,String attrName,StringBuilder code,ClientDocumentStfulImpl clientDoc)
    {
        code.append( JSRenderPropertyImpl.renderUIControlProperty(elem,elemVarName,attrName,clientDoc) );
    }

    protected String serializeDocument()
    {
        return getResponseLoadDoc().serializeDocument();
    }

    protected void preSerializeDocument()
    {
        getResponseLoadStfulDocumentValid().preSerializeDocumentStful();
    }

    protected String getLoadCodeUsingDocument()
    {
        // Llamar después de serializar el markup así en el proceso de serializar el documento hay una última oportunidad de generar código JavaScript
        return getResponseLoadDoc().getCodeToSendAndReset();
    }

    protected String generateFinalScriptsMarkup()
    {
        StringBuilder scriptsMarkup = new StringBuilder();

        LinkedList<String> list = new LinkedList<String>();
        list.add(LoadScriptImpl.ITSNAT);

        delegByBrowser.fillFrameworkScriptFileNamesOfBrowser(list);

        if (hasScriptFilesToLoad())
            list.addAll(getScriptFilesToLoad());

        StringBuilder fileNameList = new StringBuilder();
        int i = 0;
        for(Iterator<String> it = list.iterator(); it.hasNext(); i++)
        {
            String fileName = it.next();
            if (i > 0) fileNameList.append(',');
            fileNameList.append(fileName);
        }

        int prevScriptsToRemove = 0; // prevScriptsToRemove es para eliminar los <script> del framework antes de ejecutar el resto del código para que no influya

        prevScriptsToRemove++;
        scriptsMarkup.append( generateFrameworkScriptElement(fileNameList.toString(),prevScriptsToRemove) );

        prevScriptsToRemove++; // Incrementamos en 1 porque siempre hay que eliminar el <script> con el código de carga
        scriptsMarkup.append( generateInitScriptElementCode(prevScriptsToRemove) );

        return scriptsMarkup.toString();
    }

    public String getServletPath()
    {
        ResponseLoadDocImpl parent = getResponseLoadDoc();
        boolean useAbsoluteURL = false;
        if (getClientDocumentStful().getBrowser().isNeededAbsoluteURL())
            useAbsoluteURL = true;  // El browser lo pide
        else if (getResponseLoadStfulDocumentValid().isNeededAbsoluteURL())
            useAbsoluteURL = true;  // El tipo de proceso lo pide

        ItsNatServletRequestImpl itsNatRequest = parent.getRequestLoadDoc().getItsNatServletRequest();
        return itsNatRequest.getServletPath(useAbsoluteURL, true);
    }

    protected Element createFrameworkScriptElement(String jsFileNameList,final int loaderScriptNum)
    {
        Element scriptElem = createScriptElement(loaderScriptNum);
        if (getResponseLoadStfulDocumentValid().isInlineLoadFrameworkScripts())
            loadFrameworkScriptElementInline(scriptElem,jsFileNameList);
        else
            loadFrameworkScriptElementWithURL(scriptElem,jsFileNameList);
        return scriptElem;
    }

    protected void loadFrameworkScriptElementWithURL(Element scriptElem,String jsFileNameList)
    {
        StringBuilder jsPathFile = new StringBuilder();
        String servletPath = getServletPath();
        jsPathFile.append(servletPath + "?itsnat_action=load_script&itsnat_file=" + jsFileNameList + "&");
        jsPathFile.append("time=" + scriptLoadTimeStamp); // Evita el problema de la caché del MSIE que no actualiza el archivo .js ante cambios del mismo salvo haciendo "reload/actualizar", así se genera un URL único al cargar la aplicación, por otra parte el número no cambia durante la vida de la aplicación por lo que el archivo es cacheado por MSIE, si se cambia el .js deberá pararse/recargarse la aplicación web lo cual sólo es necesario en tiempo de desarrollo de ItsNat, al recargarse esta clase el scriptLoadTimeStamp se actualiza
        setScriptURLAttribute(scriptElem,jsPathFile.toString());
    }

    protected void loadFrameworkScriptElementInline(Element scriptElem,String jsFileNameList)
    {
        String code = loadScriptList(jsFileNameList);
        setScriptContent(scriptElem,code);
    }
    
    public String generateFrameworkScriptElement(String jsFileNameList,final int loaderScriptNum)
    {
        Element scriptElem = createFrameworkScriptElement(jsFileNameList,loaderScriptNum);
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        return itsNatDoc.serializeNode(scriptElem);
    }

    protected abstract String getJavaScriptMIME();
    protected abstract void setScriptURLAttribute(Element scriptElem,String url);
    protected abstract void setScriptContent(Element scriptElem,String code);

    protected abstract String getDocumentNamespace();

    protected Element createScriptElement(final int loaderScriptNum)
    {
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        Document doc = itsNatDoc.getDocument();

        Element script = doc.createElementNS(getDocumentNamespace(),"script");

        DOMUtilInternal.setAttribute(script,"type",getJavaScriptMIME());
        DOMUtilInternal.setAttribute(script,"id","itsnat_load_script_" + Integer.toString(loaderScriptNum));

        return script;
    }

    protected String generateInitScriptElementCode(final int prevScriptsToRemove)
    {
        Element scriptElem = createInitScriptElement(prevScriptsToRemove);
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        return itsNatDoc.serializeNode(scriptElem);
    }

    protected Element createInitScriptElement(final int prevScriptsToRemove)
    {
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();

        // Hacemos un truco para evitar que se definan variables globales
        // al ejecutar el script de carga, pues las variables dentro del <script>
        // declaradas como var myvar = ... son inevitablemente globales (asociadas a window)
        // Así evitamos enlazar a window objetos que serán temporales
        // El truco de meter la función entre paréntesis lo usa por ejemplo SVGWeb
        // Más info sobre memory leaks en MSIE:
        // http://msdn.microsoft.com/en-us/library/bb250448%28VS.85%29.aspx

        // Esto es por ejemplo un gran problema para Batik como applet:
        // http://old.nabble.com/Cache-entry-not-found---slows-drawing-display-td21562669.html
        // http://www.nabble.com/FW%3A-Strange-applet-delay-revisited-to21494010.html
        // http://old.nabble.com/attachment/21502224/0/disable-rhino-class-loader.patch
        // Este problema se puede ver en la consola de los applets poniendo un nivel de rastreo 2 ("red")
        // (obviamente sin aplicar la solución).
        // Estas declaraciones de paquetes globales que se citan en Internet
        // se realizan en el constructor de RhinoInterpreter.java
        // evitando variables globales minimizamos el problema

        StringBuilder code = new StringBuilder();

        Browser browser = getClientDocumentStful().getBrowser();
        boolean enclosing = browser.isFunctionEnclosingByBracketSupported();

        if (enclosing)
            code.append("(function(){\n");

        code.append( getInitScriptContentCode(prevScriptsToRemove) + "\n" );

        if (enclosing)
            code.append("})();"); // Así la propia función tampoco no es global

        Element scriptElem = createScriptElement(prevScriptsToRemove);

        boolean loadScriptInline = itsNatDoc.isLoadScriptInline();
        if (loadScriptInline)
        {
            setScriptContent(scriptElem,code.toString());
        }
        else
        {
            ClientDocumentStfulImpl clientDoc = getClientDocumentStful();

            clientDoc.setScriptLoadCode(code.toString());

            StringBuilder url = new StringBuilder();
            url.append(getServletPath());
            url.append("?itsnat_action=load_script&itsnat_file=initial");
            url.append("&itsnat_client_id=" + clientDoc.getId());
            setScriptURLAttribute(scriptElem,url.toString());
        }

        return scriptElem;
    }

    protected String getInitScriptContentCode(final int prevScriptsToRemove)
    {
        StringBuilder code = new StringBuilder();

        // Llamamos antes de llamar a getInitDocumentAndLoadJSCode pues es la última oportunidad de enviar código "FixDOM" antes de iniciar el documento ItsNat
        code.append( getPreInitDocumentJSCode() );

        code.append( getInitDocumentAndLoadJSCode(prevScriptsToRemove) );

        return code.toString();
    }

    protected String getPreInitDocumentJSCode()
    {
        StringBuilder code = new StringBuilder();

        if (hasFixDOMCodeToSend())
            code.append( getFixDOMCodeToSend() );

        String byBrowserCode = delegByBrowser.getOnInitScriptContentCodeFixDOMCode();
        if (byBrowserCode != null)
            code.append( byBrowserCode );

        return code.toString();
    }

    protected String getInitDocumentAndLoadJSCode(final int prevScriptsToRemove)
    {
        StringBuilder code = new StringBuilder();

        code.append( getInitJSDocumentCode(prevScriptsToRemove) );

        code.append( getLoadCodeUsingDocument() );

        return code.toString();
    }

    protected String getInitJSDocumentCode(final int prevScriptsToRemove)
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();

        Browser browser = clientDoc.getBrowser();
        int browserType = browser.getTypeCode();
        int browserSubType = browser.getSubTypeCode();
        ItsNatSessionImpl itsNatSession = clientDoc.getItsNatSessionImpl();
        String token = itsNatSession.getToken();
        String sessionId = itsNatSession.getId();
        String clientId = clientDoc.getId();
        String servletPath = delegByBrowser.getServletPathForEvents();

        int errorMode = itsNatDoc.getClientErrorMode();
        StringBuilder code = new StringBuilder();

        String attachType = null;
        if (clientDoc instanceof ClientDocumentAttachedClientImpl)
        {
            if (clientDoc instanceof ClientDocumentAttachedClientTimerImpl)
                attachType = "attach_timer";
            else if (clientDoc instanceof ClientDocumentAttachedClientCometImpl)
                attachType = "attach_comet";
            else
                attachType = "attach_none";
        }

        boolean usePost = true;
        boolean xhrSyncSup = true;
        if (browser instanceof BrowserWebKit)
        {
            BrowserWebKit webKit = (BrowserWebKit)browser;
            usePost = webKit.isXHRPostSupported();
            xhrSyncSup = webKit.isXHRSyncSupported();
        }

        code.append( "\n");
        code.append( "var win = " + getWindowReference() + ";\n");

        SVGWebInfoImpl svgWeb = clientDoc.getSVGWebInfo();
        boolean svgweb = (svgWeb != null);
        if (svgweb)
        {
            boolean msie = (browser instanceof BrowserMSIEOld);
            int metaPos = -1;
            if (msie)
            {
                if (!svgWeb.hasMetaForceFlash()) throw new ItsNatException("Element <meta name='svg.render.forceflash' ..> is required when SVGWeb is used in MSIE");
                metaPos = svgWeb.getMetaForceFlashPos();
            }

            code.append( "itsnat_fix_svgweb(win," + msie + "," + metaPos + ");\n");
        }

        code.append( "var itsnat = " + delegByBrowser.getJSMethodInitName() + "(win);\n");

        if (svgweb)
        {
            boolean msie = (browser instanceof BrowserMSIEOld);
            code.append( "itsnat_init_svgweb(win,itsnat," + msie + ");\n");
        }

        code.append( "var itsNatDoc = new itsnat." + getJavaScriptDocumentName() + "();\n" );
        code.append( "itsNatDoc.init(document,win," + browserType + "," + browserSubType + ",\"" + token + "\",\"" + sessionId + "\",\"" + clientId + "\",\"" + servletPath + "\"," + usePost + ",\"" + attachType + "\"," + errorMode + "," + xhrSyncSup + "," + prevScriptsToRemove + ");\n" );

        if (svgweb)
            code.append( "itsnat_extend_doc_svgweb(itsNatDoc);\n");

        return code.toString();
    }

    protected String getWindowReference()
    {
        // Se redefine en AdobeSVG
        return "window";
    }

    public static String loadScriptList(String scriptNameList)
    {
        StringBuilder code = new StringBuilder();
        String[] scriptNameArray = scriptNameList.split(",");
        for(int i = 0; i < scriptNameArray.length; i++)
        {
            String scriptName = scriptNameArray[i];
            loadScript(scriptName,code);
        }
        return code.toString();
    }

    public static void loadScript(String scriptName,StringBuilder code)
    {
        LoadScriptImpl.checkFileName(scriptName);
        try
        {
            InputStream input = LoadScriptImpl.class.getResourceAsStream(scriptName);
            Reader reader = new InputStreamReader(input);
            char[] cbuf = new char[1024*10];
            int count = reader.read(cbuf);
            while(count != -1)
            {
                code.append(cbuf, 0, count);
                count = reader.read(cbuf);
            }
            reader.close();
        }
        catch(IOException ex)
        {
            throw new ItsNatException("INTERNAL ERROR",ex);
        }
    }


    protected abstract String getJavaScriptDocumentName();

    protected abstract String addScriptMarkupToDocMarkup(String docMarkup,String codeToAdd);
}
