/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.script.ScriptUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import test.shared.BrowserUtil;

/**
 *
 * @author jmarranz
 */
public class TestUCWEBAnchorHRef implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;

    public TestUCWEBAnchorHRef(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;

        if (!BrowserUtil.isUCWEBWinMobile(request))
            return;

        // Usamos el evento load porque añadimos, accedemos (se cachea por tanto)
        // y eliminamos un nodo, lo cual no es correcto si estamos en fastMode = true
        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();
        ((EventTarget)view).addEventListener("load",this,false);
    }

    public void handleEvent(Event evt)
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Element anchor = doc.createElement("a");
        // A propósito no ponemos un href para testear qué hace el render
        Element body = doc.getBody();
        body.appendChild(anchor);

        ScriptUtil scrUtil = itsNatDoc.getScriptUtil();
        String ref = scrUtil.getNodeReference(anchor);

        StringBuffer code = new StringBuffer();
        code.append("var elem = " + ref + ";");

        code.append("var href = elem.getAttribute('href');");
        code.append("if (href == null) document.body.innerHTML = 'ERROR TestUCWEBAnchorHRef';");
        code.append("if (href.indexOf('javascript:document.getItsNatDoc().fixHTMLAnchorHRefProcess') != 0) document.body.innerHTML = 'ERROR TestUCWEBAnchorHRef';");
        itsNatDoc.addCodeToSend(code.toString());

        // Ahora lo cambiamos y ponemos un "javascript:var i = 0;"
        anchor.setAttribute("href","javascript:var i = 0;");

        code = new StringBuffer();
        code.append("var href = elem.getAttribute('href');");
        code.append("if (href == null) document.body.innerHTML = 'ERROR TestUCWEBAnchorHRef';");
        code.append("if (href.indexOf('javascript:document.getItsNatDoc().fixHTMLAnchorHRefProcess') != 0) document.body.innerHTML = 'ERROR TestUCWEBAnchorHRef';");
        code.append("if (href.indexOf('var i = 0;') == -1) document.body.innerHTML = 'ERROR TestUCWEBAnchorHRef';");
        itsNatDoc.addCodeToSend(code.toString());

        // Ahora lo removemos, esto no supone la remoción del atributo, sólo quita la parte del usuario
        anchor.removeAttribute("href");

        code = new StringBuffer();
        code.append("var href = elem.getAttribute('href');");
        code.append("if (href == null) document.body.innerHTML = 'ERROR TestUCWEBAnchorHRef';");
        code.append("if (href.indexOf('javascript:document.getItsNatDoc().fixHTMLAnchorHRefProcess') != 0) document.body.innerHTML = 'ERROR TestUCWEBAnchorHRef';");
        code.append("if (href.indexOf('var i = 0;') != -1) document.body.innerHTML = 'ERROR TestUCWEBAnchorHRef';"); // Ahora NO debe de estar
        itsNatDoc.addCodeToSend(code.toString());

        // Fin del test
        body.removeChild(anchor);
    }
}
