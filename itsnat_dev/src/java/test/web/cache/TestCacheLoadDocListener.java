/*
 * TestCacheLoadDocListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.cache;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLDivElement;
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestCacheLoadDocListener implements ItsNatServletRequestListener
{

    /**
     * Creates a new instance of TestCacheLoadDocListener
     */
    public TestCacheLoadDocListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();

        testCachedNodes(itsNatDoc);
    }

    public void testCachedNodes(ItsNatDocument itsNatDoc)
    {
        // El nodo está cacheado en el servidor, pero en el cliente
        // se ve un "lorem ipsum..." largo (más de 100 letras)
        if (itsNatDoc.getItsNatDocumentTemplate().isOnLoadCacheStaticNodes())
        {
            HTMLDivElement node = (HTMLDivElement)itsNatDoc.getDocument().getElementById("bigCachedTextNodeId");
            Text textNode = (Text)node.getFirstChild();
            String data = textNode.getData();
            boolean res;
            res = data.startsWith("${");
            TestUtil.checkError(res);
            res = data.endsWith("}");
            TestUtil.checkError(res);
        }
    }
}
