/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.core;

import org.w3c.dom.events.Event;
import test.shared.*;
import java.io.Serializable;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.NodeCacheRegistryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 * El motivo de este test es comprobar que el evento load ha sido
 * recibido en un ejemplo de attached server, pues en XHTML y SVG en WebKit
 * el evento load se emite antes de realizarse el attachment
 * por lo que tenemos que forzarlo.
 *
 * @author jmarranz
 */
public class TestIgnoreIntrusiveNodes implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;

    public TestIgnoreIntrusiveNodes(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;

        // Este test conviente que sea el primer test pues necesitamos acceder a <body> por primera vez para
        // probar que el cálculo de paths resiste a nodos intrusivos, de todas formas no importa que no lo
        // sea pues forzamos que no haya cacheo.

        testIntrusiveNodesBetweenHeadAndBody(request);

        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();
        ((EventTarget)view).addEventListener("load",this,false);
    }
    
    public void testIntrusiveNodesBetweenHeadAndBody(ItsNatServletRequest request)
    {
        // Insertamos un nodo intrusivo antes de <body> y vemos si rompe o no el cálculo de paths
        // (no debería)

        StringBuffer code = new StringBuffer();
        code.append("try{");
        code.append("  var body = document.body;");
        code.append("  if (!body) body = document.getElementsByTagName(\"body\")[0];"); // WebKits en MIME XHTML
        code.append("  var style = document.createElement(\"style\");");
        code.append("  style.id = 'testIgnoreIntrusiveNodesId';");
        code.append("  document.documentElement.insertBefore(style,body);");
        code.append("}catch(e){");
        if (BrowserUtil.isUCWEB(request))
            code.append("document.body.innerHTML = 'ERROR TestIgnoreIntrusiveNodes 1';");
        else
            code.append("alert('ERROR TestIgnoreIntrusiveNodes 1'); throw 'ERROR';");
        code.append("}");
        itsNatDoc.addCodeToSend(code.toString());
        code = null;

        // Esto lo hacemos para asegurar que al obtener la referencia la orden de cacheado
        // que se emite antes, si falla por alguna razón, sea dentro de nuestro try/catch de control para enterarnos
        
        code = new StringBuffer();
        code.append("try{");
        itsNatDoc.addCodeToSend(code.toString());
        code = null;
        
        // Es necesario acceder a las tripas de ItsNat para asegurarnos que no está cacheado
        // pues necesitamos forzar la búsqueda del <body> via path tras la inserción del intruso,
        // de otra manera este test no sirve para nada
        HTMLElement body = itsNatDoc.getHTMLDocument().getBody();
        ClientDocumentStfulImpl clientDoc = (ClientDocumentStfulImpl)itsNatDoc.getClientDocumentOwner();
        clientDoc.removeNodeFromCacheAndSendCode(body);
        // Nos aseguramos
        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCache();
        if (nodeCache.getId(body) != null) throw new RuntimeException("Unexpected Error");

        String refBody = itsNatDoc.getScriptUtil().getNodeReference(body);

        code = new StringBuffer();
        code.append("  var elem = " + refBody + ";" );
        code.append("  if (elem.tagName.toLowerCase() != \"body\")");
        code.append("  {");
        if (BrowserUtil.isUCWEB(request))
            code.append("  document.body.innerHTML = 'ERROR TestIgnoreIntrusiveNodes Head-Body 2';");
        else
            code.append("  alert('ERROR TestIgnoreIntrusiveNodes Head-Body 2'); throw 'ERROR';");
        code.append("  }");
        code.append("  document.documentElement.removeChild(document.getElementById('testIgnoreIntrusiveNodesId'));");
        itsNatDoc.addCodeToSend(code.toString());
        code = null;
        

        code = new StringBuffer();
        code.append("}catch(e){");
        if (BrowserUtil.isUCWEB(request))
            code.append("document.body.innerHTML = 'ERROR TestIgnoreIntrusiveNodes Head-Body 3';");
        else
            code.append("alert('ERROR TestIgnoreIntrusiveNodes Head-Body 3'); throw 'ERROR';");
        code.append("}");
        
        itsNatDoc.addCodeToSend(code.toString());       
    }


    public void handleEvent(Event evt)
    {
        ItsNatServletRequest request = ((ItsNatEvent)evt).getItsNatServletRequest();
        testIntrusiveNodesEndOfHeadOrBody("head",request);
        testIntrusiveNodesEndOfHeadOrBody("body",request);
    }


    public void testIntrusiveNodesEndOfHeadOrBody(String tagName,ItsNatServletRequest request)
    {
        // Insertamos un nodo intrusivo al final de <parentNode> o <body> y vemos si rompe o no el cálculo de paths
        // (no debería)

        StringBuffer code = new StringBuffer();
        code.append("try{");
        code.append("  var parentNode = document.getElementsByTagName(\"" + tagName + "\")[0];");
        code.append("  var style = document.createElement(\"style\");");
        code.append("  style.id = 'testIgnoreIntrusiveNodesId';");
        code.append("  parentNode.appendChild(style);");
        code.append("}catch(e){");
        if (BrowserUtil.isUCWEB(request))
            code.append("document.body.innerHTML = 'ERROR TestIgnoreIntrusiveNodes EndOfHeadOrBody 1';");
        else
            code.append("alert('ERROR TestIgnoreIntrusiveNodes EndOfHeadOrBody 1'); throw 'ERROR';");
        code.append("}");
        itsNatDoc.addCodeToSend(code.toString());
        code = null;

        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Element parentNode = (Element)doc.getElementsByTagName(tagName).item(0);
        Element validChildElem1 = doc.createElement("input"); // Para que si algo falla sea muy visible en el documento (por defecto suele ser un "text")
        parentNode.appendChild(validChildElem1);
        Element validChildElem2 = doc.createElement("textarea"); // Para que si algo falla sea muy visible en el documento (por defecto suele ser un "text")
        parentNode.insertBefore(validChildElem2,validChildElem1);

        ClientDocumentStfulImpl clientDoc = (ClientDocumentStfulImpl)itsNatDoc.getClientDocumentOwner();
        // Es necesario acceder a las tripas de ItsNat para asegurarnos que está cacheado
        NodeCacheRegistryImpl nodeCache = clientDoc.getNodeCache();
        if (nodeCache.getId(validChildElem1) == null)  // DEBE estar cacheado aunque esté recién insertado, en eso consiste la protección
            throw new RuntimeException("Unexpected Error");
        if (nodeCache.getId(validChildElem2) == null)  // DEBE estar cacheado aunque esté recién insertado, en eso consiste la protección
            throw new RuntimeException("Unexpected Error");

        String refValidChildElem1 = itsNatDoc.getScriptUtil().getNodeReference(validChildElem1);
        String refValidChildElem2 = itsNatDoc.getScriptUtil().getNodeReference(validChildElem2);

        code = new StringBuffer();
        code.append("try{");
        code.append("  var elem1 = " + refValidChildElem1 + ";" );
        code.append("  var elem2 = " + refValidChildElem2 + ";" );
        code.append("  if ((elem1.tagName.toLowerCase() != \"input\")||(elem2.tagName.toLowerCase() != \"textarea\"))");
        code.append("  {");
        if (BrowserUtil.isUCWEB(request))
            code.append("  document.body.innerHTML = 'ERROR TestIgnoreIntrusiveNodes EndOfHeadOrBody 2';");
        else
            code.append("  alert('ERROR TestIgnoreIntrusiveNodes EndOfHeadOrBody 2'); throw 'ERROR';");
        code.append("  }");
        code.append("  parentNode.removeChild(document.getElementById('testIgnoreIntrusiveNodesId'));");
        code.append("}catch(e){");
        if (BrowserUtil.isUCWEB(request))
            code.append("document.body.innerHTML = 'ERROR TestIgnoreIntrusiveNodes EndOfHeadOrBody 3';");
        else
            code.append("alert('ERROR TestIgnoreIntrusiveNodes EndOfHeadOrBody 3'); throw 'ERROR';");
        code.append("}");

        itsNatDoc.addCodeToSend(code.toString());

        parentNode.removeChild(validChildElem1);
        parentNode.removeChild(validChildElem2);
    }

}
