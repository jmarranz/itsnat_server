/*
 * TestXMLComponentsDocLoadListener.java
 *
 * Created on 11 de enero de 2007, 10:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.xml_comp;

import javax.swing.DefaultListModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TestXMLComponentsDocLoadListener implements ItsNatServletRequestListener
{

    /**
     * Creates a new instance of TestXMLComponentsDocLoadListener
     */
    public TestXMLComponentsDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        Element root = doc.getDocumentElement();

        // Testeamos que los componentes y el auto-build también funcionan en XML
        Element listParent = ItsNatTreeWalker.getFirstChildElement(root);

        // Los llenamos, verificando que existe un componente asociado
        addItems(listParent,itsNatDoc);

        Element listParent2 = (Element)listParent.cloneNode(true);
        root.appendChild(listParent2); // actúan los mutation events construyendo el nuevo componente, el primer hijo será el patrón
        addItems(listParent2,itsNatDoc);
    }

    public void addItems(Element listParent,ItsNatDocument itsNatDoc)
    {
        // Testeamos que los componentes y el auto-build también funcionan en XML
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatFreeListMultSel list = (ItsNatFreeListMultSel)compMgr.findItsNatComponent(listParent);
        DefaultListModel listModel = (DefaultListModel)list.getListModel();
        listModel.addElement("One");
        listModel.addElement("Two");
        listModel.addElement("Three");
    }
}
