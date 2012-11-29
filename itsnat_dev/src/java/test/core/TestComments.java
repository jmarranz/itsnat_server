/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpSession;
import org.itsnat.core.script.ScriptUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jmarranz
 */
public class TestComments
{
    protected ItsNatHTMLDocument itsNatDoc;

    public TestComments(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;

        /* Testeamos que aunque se hayan filtrado los comentarios en carga
         en navegadores tal y como BlackBerry y algún S60WebKit,
         fueron de nuevo re-insertados. En otros navegadores este test no
         tiene interés.
        */

        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        if (userAgent.indexOf(" IEMobile") != -1) // Pocket IE no soporta comentarios por lo que ScriptUtil.getNodeRefenrence() dará error (pues los comentarios no son usados en el cálculo de paths)
            return;

        Document doc = itsNatDoc.getDocument();

        StringBuffer code = new StringBuffer();

        code.append("try {");

        Element table = doc.getElementById("testCommentId");
        testChildren(table,code);

        Element tbody = ItsNatTreeWalker.getFirstChildElement(table);
        testChildren(tbody,code);

        Element tr = ItsNatTreeWalker.getFirstChildElement(tbody);
        testChildren(tr,code);

        code.append("} catch(e) { alert('ERROR TestComments'); throw 'ERROR'; }");

        itsNatDoc.addCodeToSend(code.toString());
    }

    public void testChildren(Element elem,StringBuffer code)
    {
        ScriptUtil util = itsNatDoc.getScriptUtil();
        NodeList children = elem.getChildNodes();
        int len = children.getLength();
        for(int i = 0; i < len; i++)
        {
            Node child = children.item(i);
            int nodeType = child.getNodeType();
            if (nodeType == Node.TEXT_NODE) continue;
            String ref = util.getNodeReference(child);

            code.append("var node = " + ref + ";");
            code.append("if (node.nodeType != " + nodeType + ") { alert('ERROR TestComments'); throw 'ERROR'; }");
        }
    }
}
