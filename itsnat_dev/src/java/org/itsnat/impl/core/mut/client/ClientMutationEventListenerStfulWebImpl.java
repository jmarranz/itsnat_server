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

package org.itsnat.impl.core.mut.client;

import org.itsnat.impl.core.mut.client.web.ClientMutationEventListenerStfulWebDefaultImpl;
import org.itsnat.impl.core.mut.client.web.ClientMutationEventListenerHTMLImpl;
import java.util.LinkedList;
import org.itsnat.core.html.ItsNatHTMLEmbedElement;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ElementDocContainerWrapperImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.web.ItsNatHTMLDocumentImpl;
import org.itsnat.impl.core.domimpl.ElementDocContainer;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderAttributeImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderCharacterDataImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderNodeImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderNotAttrOrAbstractViewNodeImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLParamElement;

/**
 *
 * @author jmarranz
 */
public abstract class ClientMutationEventListenerStfulWebImpl extends ClientMutationEventListenerStfulImpl
{
    public ClientMutationEventListenerStfulWebImpl(ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        super(clientDoc);
    }

    public static ClientMutationEventListenerStfulWebImpl createClientMutationEventListenerStfulWeb(ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        ItsNatStfulDocumentImpl itsNatDoc = clientDoc.getItsNatStfulDocument();
        if (itsNatDoc instanceof ItsNatHTMLDocumentImpl)
            return ClientMutationEventListenerHTMLImpl.createClientMutationEventListenerHTML(clientDoc);
        else
            return new ClientMutationEventListenerStfulWebDefaultImpl(clientDoc);
    }

    public ClientDocumentStfulDelegateWebImpl getClientDocumentStfulDelegateWeb()
    {
        return (ClientDocumentStfulDelegateWebImpl)clientDoc;
    }
   
    protected String getDOMAttrModifiedCode(Attr attr,Element elem,int changeType)
    {
        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();          
        String code = null;
        JSRenderAttributeImpl render = JSRenderAttributeImpl.getJSRenderAttribute(attr,elem,clientDoc);
        switch(changeType)
        {
            case MutationEvent.ADDITION:
            case MutationEvent.MODIFICATION:
                code = render.setAttributeCode(attr,elem,false,clientDoc);
                break;
            case MutationEvent.REMOVAL:
                code = render.removeAttributeCode(attr,elem,clientDoc);
                break;
            // No hay más casos
        }        
        
        return code;
    }

    protected String getCharacterDataModifiedCode(CharacterData charDataNode)    
    {
        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();        
        JSRenderCharacterDataImpl render = (JSRenderCharacterDataImpl)JSRenderNodeImpl.getJSRenderNode(charDataNode,clientDoc);
        String code = render.getCharacterDataModifiedCode(charDataNode,clientDoc); 
        return code;
    }
    
    public Object getTreeDOMNodeInsertedCode(Node newNode)
    {
        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();        
        JSRenderNotAttrOrAbstractViewNodeImpl render = (JSRenderNotAttrOrAbstractViewNodeImpl)JSRenderNodeImpl.getJSRenderNode(newNode,clientDoc);
        Object code = render.getInsertNewNodeCode(newNode,clientDoc); // Puede ser null
        return code;
    }

    public Object getTreeDOMNodeRemovedCode(Node removedNode)
    {
        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();        
        JSRenderNotAttrOrAbstractViewNodeImpl render = (JSRenderNotAttrOrAbstractViewNodeImpl)JSRenderNodeImpl.getJSRenderNode(removedNode,clientDoc);
        String code = render.getRemoveNodeCode(removedNode,clientDoc);
        return code;
    }

    @Override
    public void postRenderAndSendMutationCode(MutationEvent mutEvent)
    {
        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();        
        String type = mutEvent.getType();

        if (type.equals("DOMAttrModified"))
        {
            Element elem = (Element)mutEvent.getTarget();
            Attr attr = (Attr)mutEvent.getRelatedNode();

            boolean mustCallSetSrc = false;
            ElementDocContainer elemDocCont = ElementDocContainerWrapperImpl.getElementDocContainerIfURLAttr(attr,elem);
            if (elemDocCont != null)
            {
                if (elemDocCont.getElementDocContainerWrapper().isJavaApplet())
                {
                    mustCallSetSrc = true;
                }
                else if ((clientDoc.getBrowserWeb() instanceof BrowserMSIEOld) &&
                         ((elemDocCont instanceof HTMLObjectElement) ||
                          (elemDocCont instanceof ItsNatHTMLEmbedElement))) // Posiblemente ASV 
                {
                    mustCallSetSrc = true;
                }
            }

            if (mustCallSetSrc)
                callSetSrcInPlugin(mutEvent,elem,elemDocCont);
        }
    }

    private void callSetSrcInPlugin(MutationEvent mutEvent,Element elem,ElementDocContainer elemDocCont)
    {
        // Hay varios casos:
        // 1) Applet y elem es un HTMLParamElement hijo de <applet> o <object> y el atributo es el "value" del <param name="src" value="url" />
        // Luego estamos cambiando el atributo "value" de este <param> de un <applet>
        // El cambio del src no cambia para nada el documento cargado <applet> pues
        // este src es un invento de ItsNat que sólo es útil en el applet Batik modificado para ItsNat.
        // Este applet tiene un método setSrc(url), ese método sí que es capaz de cambiar el
        // documento cargado por el applet Batik.
        // En ItsNat sólo reconocemos este applet especial Batik, en vez de tratar
        // averiguar si es el applet Batik ItsNat, lo cual es difícil porque la configuración de atributos del
        // applet tiene cierta libertad (salvo que la acotemos) y así permitimos posibles cambios
        // en el empaquetamiento del applet, llamamos a setSrc(url) con un try/catch
        // y ya está y así podemos soportar "automáticamente" otros futuros <applet> que
        // tengan un comportamiento similar también con setSrc y applets que "casualmente" (mucha casualidad) tienen este <param> especial
        // pero que no tienen el método setSrc(url).

        // 2) Applet en <embed>, en este caso el cambio del atributo src
        // o bien no funciona como es el caso de MSIE o bien da problemas
        // como es el caso de FireFox (extraño cacheado), Chrome y Safari (el antiguo documento parece que no se quita visualmente)
        // por lo que además llamamos a setSrc que soluciona todo.

        // 3) ASV o Savarese Ssrc en <object> o <embed> (sólo MSIE carga ActiveX)
        //    el cambio del atributo/propiedad src no es suficiente,
        //    ASV define setSrc(url) y Ssrc Navigate.

        StringBuilder code = new StringBuilder();

        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();        
        String refJS = clientDoc.getNodeReference(elem, true, true);

        code.append("var elem = " + refJS + ";");

        int changeType = mutEvent.getAttrChange();
        switch(changeType)
        {
            case MutationEvent.ADDITION:
            case MutationEvent.MODIFICATION:
                code.append("var value = itsNatDoc.getAttribute(elem,\"" + elemDocCont.getElementDocContainerWrapper().getURLAttrName() + "\");"); // El valor del atributo ya se definió antes en la fase de renderizado del atributo
                break;
            case MutationEvent.REMOVAL: // Raro pero por si acaso
                code.append("var value = \"\";");
                break;
            // No hay más casos
        }

        if (elem instanceof HTMLParamElement) // <applet> y <object>, en caso contrario es <embed>
            code.append("var elem = itsNatDoc.getParentNode(elem);"); // elem ahora es el <applet>

        code.append("try{");
        if (elemDocCont.getElementDocContainerWrapper().isJavaApplet())
            code.append("if (typeof elem.setSrc != \"undefined\") elem.setSrc(value);"); // Sólo soportamos el Applet Batik ItsNat que tiene este método, si fuera otro applet (muy raro llegar hasta aquí) capturamos el error y no pasa nada
        else // ASV,  Savarese Ssrc.
        {
            code.append("if (typeof elem.setSrc != \"undefined\") elem.setSrc(value);"); // ASV
            code.append("else if (typeof elem.Navigate != \"undefined\")"); // Savarese Ssrc
            code.append("  if (value == elem.LocationURL) elem.Refresh(); else elem.Navigate(value);"); // La llamada a Refresh() asegura que la request se realiza (ignora el caché) http://msdn.microsoft.com/en-us/library/aa752098%28VS.85%29.aspx
        }
        code.append("}catch(e){}\n"); // Por si acaso, probablemente es un plugin o applet desconocido por ItsNat pero que nos ha confundido

        clientDoc.addCodeToSend(code.toString());
    }


    public String getRemoveNodeFromCacheCode(LinkedList<String> idList)    
    {
        return JSRenderNodeImpl.removeNodeFromCache(idList);
    }    
    
    @Override
    public String getRemoveAllChildCode(Node node)
    {
        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();          
        JSRenderNotAttrOrAbstractViewNodeImpl render = (JSRenderNotAttrOrAbstractViewNodeImpl)JSRenderNodeImpl.getJSRenderNode(node,clientDoc);
        return render.getRemoveAllChildCode(node,clientDoc); 
    }
}
