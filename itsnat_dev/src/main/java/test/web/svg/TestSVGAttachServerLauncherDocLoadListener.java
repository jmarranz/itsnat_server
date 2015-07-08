/*
 * TestCoreDocLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.svg;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;

/**
 *
 * @author jmarranz
 */
public class TestSVGAttachServerLauncherDocLoadListener implements ItsNatServletRequestListener
{

    /**
     * Creates a new instance of TestCoreDocLoadListener
     */
    public TestSVGAttachServerLauncherDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();

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

        StringBuilder code = new StringBuilder();
        code.append("<script><![CDATA[");

        code.append("var doc_name = \"test_svg_attached_server\";");
        code.append("var proto = window.location.protocol;");
        code.append("var host = window.location.host;");
        code.append("var path = window.location.pathname;");
        code.append("if (path.indexOf(\"/\") != 0) path = \"/\" + path;"); // IE 9
        code.append("var address = host + path;");
        code.append("var url = proto + \"//\" + address + \"?itsnat_action=attach_server&itsnat_doc_name=\" + doc_name + \"&itsnat_method=" + method + ( method.equals("form")? "&itsnat_timeout=" + timeout : "" ) + "&timestamp=\" + new Date().getTime();");

        code.append("var root = document.documentElement;");
//        code.append("var script = document.createElementNS(\"http://www.w3.org/1999/xhtml\",\"script\");");
//        code.append("script.src = url;");
        code.append("var script = document.createElementNS(root.namespaceURI,\"script\");");
        code.append("script.setAttributeNS(\"http://www.w3.org/1999/xlink\",\"href\",url);\n");
        code.append("root.appendChild(script);");

        code.append("]]></script>");

        DocumentFragment frag = itsNatDoc.toDOM(code.toString());
        Document doc = itsNatDoc.getDocument();
        doc.getDocumentElement().appendChild(frag);
    }

}
