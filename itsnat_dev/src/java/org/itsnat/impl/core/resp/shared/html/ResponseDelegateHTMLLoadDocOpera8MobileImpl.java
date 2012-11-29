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

package org.itsnat.impl.core.resp.shared.html;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.browser.opera.BrowserOpera8Mobile;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementOpera8MobileImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateHTMLLoadDocOpera8MobileImpl extends ResponseDelegateHTMLLoadDocOperaImpl
{
    protected static final String FAKE_BUTTON_TAGNAME = "itsnatfixbutton";

    public ResponseDelegateHTMLLoadDocOpera8MobileImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public String getJavaScriptDocumentName()
    {
        return "Opera8MobileHTMLDocument";
    }

    protected String getInitDocumentAndLoadJSCode(final int prevScriptsToRemove)
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        BrowserOpera8Mobile browser = (BrowserOpera8Mobile)clientDoc.getBrowser();
        if (browser.isInitialScriptElementNotInDOM())
        {
            StringBuffer code = new StringBuffer();

            // Hacemos un "Delayed Init":

            // Es el caso de Opera Mobile 8.60 UIQ 3.0, este navegador no soporta
            // DOMContentLoaded por lo que usamos un setTimeout, al parecer
            // setTimeout(func,0) se ejecuta antes que el evento load lo cual es
            // totalmente deseable. Esto no es seguro en otras versiones y en WinCE
            // (al parecer depende si se llega por un link o con un reload)
            // pero afortunadamente no necesitamos esto en los demás casos.

            code.append( "\n" );
            code.append( "var initListener = function ()\n" );
            code.append( "{\n" );

            code.append( super.getInitDocumentAndLoadJSCode(prevScriptsToRemove) );

            code.append( "};\n" );
            code.append( "window.setTimeout(initListener,0);\n" );

            return code.toString();
        }
        else return super.getInitDocumentAndLoadJSCode(prevScriptsToRemove);
    }

    public void dispatchRequestListeners()
    {
        boolean serializeBefore = isSerializeBeforeDispatching();
        if (serializeBefore) // Caso fastLoad=false y remote control
            fixInputCheckBoxElementsBlurFocus();

        super.dispatchRequestListeners();

        if (!serializeBefore)
            fixInputCheckBoxElementsBlurFocus();

        fixLoadEvent(); // Da igual si se llama antes o después dispatchRequestListeners() pues el código es muy independiente y se envía como código "fixDOM"
        fixBackButton(); // Debe añadirse lo más último posible pues registra un listener "unload" que debe ejecutarse el último
    }

    public void fixInputCheckBoxElementsBlurFocus()
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        if (!clientDoc.isScriptingEnabled()) return;

        BrowserOpera8Mobile browser = (BrowserOpera8Mobile)clientDoc.getBrowser();
        if (!browser.isInputCheckBoxSurplusFocusBlur())
            return;

        HTMLDocument doc = (HTMLDocument)clientDoc.getItsNatDocumentImpl().getDocument();
        JSRenderHTMLElementOpera8MobileImpl.fixTreeHTMLInputCheckBoxFocusBlur(doc.getBody(), clientDoc);
    }

    protected void fixBackButton()
    {
        // Opera Mobile 8 ejecuta el evento load y los scripts cuando se vuelve
        // a la página PERO NO SIEMPRE (no en todas las circunstancias).
        // No puede aplicarse a Opera Mini 4 no recibe clicks en BODY y en general esta técnica no funciona.

        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        if (!clientDoc.isScriptingEnabled()) return;

        StringBuffer code = new StringBuffer();
        code.append("var listener = function () ");
        code.append("{");
        code.append("  document.body.onclick = function () { window.location.reload(true); }; ");
        code.append("};");
        code.append("window.addEventListener('unload',listener,false);");
        clientDoc.addCodeToSend(code);
    }

    protected void fixLoadEvent()
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        if (!clientDoc.isScriptingEnabled()) return;

        BrowserOpera8Mobile browser = (BrowserOpera8Mobile)clientDoc.getBrowser();
        if (!browser.isLoadEventNotEverFired()) return;

        // Este código lo insertamos al ppio de todo usando addFixDOMCodeToSend
        // porque este código no está sujeto al "delayed init" usado en Opera Mobile 8
        // que supone un setTimeout y que seguramente es la causa del problema.
        // Curiosamente se resuelve registrando un evento "load" inútil
        // en tiempo de carga pero antes y fuera del handler del setTimeout.
        // Gracias a esto conseguimos que los referrers funcionen (por ejemplo)

        String code = "window.addEventListener(\"load\",function(){},false);\n";
        addFixDOMCodeToSend(code);
    }

    public String serializeDocument()
    {
        // Opera Mobile 8.65 (supongo que también 8.6) tiene un error estúpido
        // en el parseado del markup de carga con los <button> bajo <p>
        // En teoría <p> no puede tener nodos "box" hijo (por ejemplo <div>)
        // el caso es que Opera Mobile 8.65 se hace un lio y considera <button>
        // un elemento de este tipo (por sí mismo NO lo es aunque sus hijos sí pueden serlo),
        // y lo saca del <p> poniéndolo como nodo siguiente. El asunto es más
        // grave pues los nodos consecutivos TAMBIÉN son sacados.
        // Ejemplo: <p>Hello<br /><button>Button</button><br />Byte</p>
        // queda en el DOM como:
        // <p>Hello<br /></p><button>Button</button><br />Byte
        // El asunto es más complicado aún pues esto no lo hace siempre, pues en:
        // <p><span><button>Button</button></span></p> NO ocurre.
        // Lo curioso es que incluso en este caso lo que sigue a <button> es sacado fuera del <p>
        // Curiosamente esto no ocurre cuando el <button> es insertado via innerHTML
        // o métodos DOM.
        // La solución por tanto será la siguiente: cambiaremos temporalmente los
        // <button> por <itsnatfixbutton> (NO usar "_") serializaremos
        // y haremos replaceChild en el cliente de los nodos <itsnatfixbutton>.

        // Este caso es un poco complicado porque el DOM a serializar contiene
        // nodos cacheados que a su vez posiblemente tengan <button> conflictivos,
        // si no consideramos los <button> no visibles éstos pueden fastidiar
        // "indirectamente" el DOM en el cliente de las zonas dinámicas que nos interesan.
        // Aunque en teoría la presencia de un <button> impide el cacheamiento
        // por ser un elemento de formulario, esto podría cambiar quizás con un atributo
        // itsnat:nocache="false" que obligara a cachear dicho <button>
        // La estrategia es la siguiente: solucionamos los <button> visibles
        // a través de cambios en el DOM, serializamos y si siguen existiendo
        // <button> pues reharemos el DOM a partir de la cadena y haremos
        // lo mismo de nuevo, tarea mucho más lenta obviamente.

        boolean sentCodeFixButtons = false;

        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
        Document doc = itsNatDoc.getDocument();

        Map buttons = processTreeButtonElements(doc);

        String docMarkup = super.serializeDocument();

        if (buttons != null) // Si es null es porque los "<button" estaban en comentarios o bien no dentro de <p>
        {
            restoreButtonElements(buttons);
            addCodeToSendFixButtons();
            sentCodeFixButtons = true;
        }

        // Ahora procesamos los <button> que pudieran existir en los nodos cacheados

        // Intentamos evitar un costoso parseado si no hay ningun <button>
        if (docMarkup.indexOf("<button") == -1)
            return docMarkup; // No hay ningún "<button>" o "<button "

        Document docTmp = itsNatDoc.parseDocument(docMarkup);
        // No es necesario normalizar ya fue normalizado el documento original que dio lugar a la cadena

        buttons = processTreeButtonElements(docTmp);

        if (buttons != null) // Si es null es porque los "<button" estaban en comentarios o bien no dentro de <p>
        {
            // Para serializar de nuevo no es necesario resolver nodos cacheados pues partimos del string que se envía al cliente
            docMarkup = itsNatDoc.serializeDocument(docTmp,false);

            // No hace falta restaurar el DOM pues es un documento temporal que se pierde

            if (!sentCodeFixButtons) // Hay que evitar enviarlo dos veces, el código está preparado para procesar los button procesados en las dos fases
                addCodeToSendFixButtons();
        }

        return docMarkup;
    }

    protected Map processTreeButtonElements(Document doc)
    {
        // Sólo procesamos los <button> que estén bajo <p> el problema es
        // que el NodeList se modifica cuando se quita del árbol un elemento
        // del NodeList (que es lo que queremos) sin embargo no todos van
        // estar bajo <p>, por lo que lo mejor es usar una lista auxiliar.

        LinkedList buttonListToProcess = null;
        LinkedList buttonList = DOMUtilInternal.getChildElementListWithTagNameNS(doc,NamespaceUtil.XHTML_NAMESPACE,"button",true);
        if (buttonList != null)
        {
            for(Iterator it = buttonList.iterator(); it.hasNext(); )
            {
                HTMLButtonElement elem = (HTMLButtonElement)it.next();
                if (!isButtonUnderParagraph(elem)) continue;

                if (buttonListToProcess == null)
                    buttonListToProcess = new LinkedList();
                buttonListToProcess.add(elem);
            }
        }

        if (buttonListToProcess == null) return null;

        Map buttonMap = new HashMap();
        for(Iterator it = buttonListToProcess.iterator(); it.hasNext(); ) // Cuando se procesan se eliminan del documento por lo que automáticamente se quitan del NodeList
        {
            HTMLButtonElement elem = (HTMLButtonElement)it.next();
            processButtonElement(elem,buttonMap,doc);
        }

        return buttonMap;
    }

    protected boolean isButtonUnderParagraph(HTMLButtonElement elem)
    {
        // Sólo aquellos que están bajo un <p>
        Element parent = ItsNatTreeWalker.getParentElement(elem); // El primer padre no puede ser nulo (será BODY si acaso)
        do
        {
            if ((parent instanceof HTMLElement) &&
                 parent.getLocalName().equals("p"))
                return true;
            parent = ItsNatTreeWalker.getParentElement(parent);
        }
        while(parent != null);
        return false;
    }

    protected static Element processButtonElement(HTMLButtonElement elem,Map buttonMap,Document doc)
    {
        DocumentFragment content = DOMUtilInternal.extractChildrenToDocFragment(elem);
        Element fakeElem = doc.createElementNS(NamespaceUtil.XHTML_NAMESPACE,FAKE_BUTTON_TAGNAME);
        fakeElem.appendChild(content);
        if (elem.hasAttributes())
        {
            NamedNodeMap attribs = elem.getAttributes();
            while(attribs.getLength() > 0)
            {
                Attr attr = (Attr)attribs.item(0);
                elem.removeAttributeNode(attr); // Necesario sino da error
                fakeElem.setAttributeNode(attr);
            }
        }
        elem.getParentNode().replaceChild(fakeElem, elem);

        buttonMap.put(fakeElem, elem);

        return fakeElem; // Ha reemplazado a elem
    }

    protected static void restoreButtonElements(Map buttonMap)
    {
        // Restauramos el estado anterior del DOM
        for(Iterator it = buttonMap.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)it.next();
            Element fakeElem = (Element)entry.getKey();
            HTMLButtonElement originalElem = (HTMLButtonElement)entry.getValue();
            DocumentFragment content = DOMUtilInternal.extractChildrenToDocFragment(fakeElem);
            originalElem.appendChild(content);
            if (fakeElem.hasAttributes())
            {
                NamedNodeMap attribs = fakeElem.getAttributes();
                while(attribs.getLength() > 0)
                {
                    Attr attr = (Attr)attribs.item(0);
                    fakeElem.removeAttributeNode(attr); // Necesario sino da error
                    originalElem.setAttributeNode(attr);
                }
            }
            fakeElem.getParentNode().replaceChild(originalElem, fakeElem);
        }
    }

    protected void addCodeToSendFixButtons()
    {
        StringBuffer code = new StringBuffer();

        // Metemos una función para evitar dejar variables globales
        code.append("var func = function()\n");
        code.append("{\n");
        code.append("  var fakeButtonList = document.getElementsByTagName(\"" + FAKE_BUTTON_TAGNAME + "\");\n");

        // Hacemos una copia porque un NodeList según el estándar es una colección viva por tanto
        // si quitamos los elementos del documento puede cambiar (de hecho es así en S60WebKit 5th v0.9)
        // y no queremos eso.
        // Evitamos en los posible el acceso a la propiedad length que parece que da errores aleatorios
        code.append("  var fakeButtonLen = fakeButtonList.length;\n");
        code.append("  var fakeButtonArr = new Array(fakeButtonLen);\n");
        code.append("  for(var i=0;i < fakeButtonLen; i++) fakeButtonArr[i] = fakeButtonList[i]; \n");
        code.append("  fakeButtonList = null;\n"); // para ahorrar memoria
        code.append("  for(var i=0;i < fakeButtonLen; i++)\n");
        code.append("  {\n");
        code.append("    var fakeButton = fakeButtonArr[i];\n");
        code.append("    var button = document.createElement('button');\n");
        code.append("    while(fakeButton.hasAttributes() > 0)\n");
        code.append("       button.setAttributeNode(fakeButton.removeAttributeNode(fakeButton.attributes.item(0)));\n");
        code.append("    while(fakeButton.hasChildNodes() > 0)\n");
        code.append("       button.appendChild(fakeButton.childNodes.item(0));\n");
        code.append("    fakeButton.parentNode.replaceChild(button,fakeButton);\n");
        code.append("  }\n");
        code.append("};\n");
        code.append("func();\n");
        code.append("func = null;\n"); // No se necesita más, para liberar memoria

        // Insertamos AL PRINCIPIO para que el código generado por el usuario (que va después)
        // vea ya los button correctamente en el DOM
        addFixDOMCodeToSend(code.toString());
    }

}

