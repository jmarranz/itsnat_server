/*
 * TestCoreDocLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestCoreAttachServerLauncherDocLoadListener implements ItsNatServletRequestListener
{

    /**
     * Creates a new instance of TestCoreDocLoadListener
     */
    public TestCoreAttachServerLauncherDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();

        if (!itsNatDoc.getItsNatDocumentTemplate().isFastLoadMode())
            throw new RuntimeException("NO TIENE SENTIDO");

        if (itsNatDoc.getItsNatDocumentTemplate().isScriptingEnabled())
            throw new RuntimeException("NO TIENE SENTIDO");

        String method = request.getServletRequest().getParameter("_method");
        if (method == null) throw new RuntimeException("Missing _method");
        String timeout = null;
        if (method.equals("form"))
        {
            timeout = request.getServletRequest().getParameter("_timeout");
            if (timeout == null) throw new RuntimeException("Missing _timeout");
        }

        ItsNatDocumentTemplate template = itsNatDoc.getItsNatDocumentTemplate();
        String mime = template.getMIME();

        StringBuilder code = new StringBuilder();
        code.append("<script>");

        code.append("var doc_name = \"test_core_attached_server\";");
        code.append("var proto = window.location.protocol;");
        code.append("var host = window.location.host;");
        code.append("var path = window.location.pathname;");
        if (mime.equals("text/html"))
            code.append("if (path.indexOf(\"/\") != 0) path = \"/\" + path;"); // bug de IE 9 sólo en MIME text/html
        code.append("var address = host + path;");
        //code.append("var address = \"www.innowhere.com:8080/itsnat_dev/ItsNatServletExample\";");
        code.append("var url = proto + \"//\" + address + \"?itsnat_action=attach_server&itsnat_doc_name=\" + doc_name + \"&itsnat_method=" + method + ( method.equals("form")? "&itsnat_timeout=" + timeout : "" ) + "&timestamp=\" + new Date().getTime();");
        if (mime.equals("text/html"))
        {
            code.append("document.write(\"<script src='\" + url + \"'><\\/script>\");");
        }
        else // XHTML
        {
            code.append("var body = document.body;");
            code.append("if (!body) body = document.getElementsByTagName(\"body\")[0];");
            code.append("var script = document.createElement(\"script\");");
            code.append("script.src = url;");
            code.append("body.appendChild(script);");

            //code.append("new Image().src = \"?itsnat_action=prueba\";");
        }

        code.append("</script>");

        DocumentFragment frag = itsNatDoc.toDOM(code.toString());
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        doc.getBody().appendChild(frag);
    }

}
