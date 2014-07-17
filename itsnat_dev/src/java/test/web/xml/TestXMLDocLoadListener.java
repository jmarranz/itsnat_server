/*
 * TestXMLDocLoadListener.java
 *
 * Created on 11 de enero de 2007, 10:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.xml;

import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TestXMLDocLoadListener implements ItsNatServletRequestListener
{

    /** Creates a new instance of TestXMLDocLoadListener */
    public TestXMLDocLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        Element cdListElem = ItsNatTreeWalker.getLastChildElement(doc.getDocumentElement());

        ItsNatDocFragmentTemplate docFragDesc = request.getItsNatServlet().getItsNatDocFragmentTemplate("test_xml_fragment");
        DocumentFragment fragment = docFragDesc.loadDocumentFragment(itsNatDoc);
        doc.getDocumentElement().insertBefore(fragment,cdListElem);

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList discList = factory.createElementList(cdListElem,true);

        addCD("Help","The Beatles",new String[] {"A Hard Day's Night","Let It Be"},discList);
        addCD("Making Movies","Dire Straits",new String[] {"Tunnel Of Love","Romeo & Juliet"},discList);

        // Para testear serialización de namespaces y createElementNS
        // En FireFox usar el "view source" porque tiene algún bug la visualización del XML

        String NAMESPACE = "http://prefix.org/prefix";
        Element parent = doc.createElement("parent");
        parent.setAttribute("xmlns:prefix",NAMESPACE);
        //String value = parent.getAttribute("xmlns:prefix");
        doc.getDocumentElement().appendChild(parent);
        Element child = doc.createElementNS(NAMESPACE,"child");
        parent.appendChild(child);
        child = doc.createElementNS(NAMESPACE,"prefix:child"); // Si fuera distinto de "prefix" daría error el navegador
        parent.appendChild(child);
    }

    public void addCD(String title,String artist,String[] songs,ElementList discList)
    {
        Element cdElem = discList.addElement();
        Element titleElem = ItsNatTreeWalker.getFirstChildElement(cdElem);
        ItsNatDOMUtil.setTextContent(titleElem,title);
        Element artistElem = ItsNatTreeWalker.getNextSiblingElement(titleElem);
        ItsNatDOMUtil.setTextContent(artistElem,artist);
        Element songsElem = ItsNatTreeWalker.getNextSiblingElement(artistElem);

        ItsNatDocument itsNatDoc = discList.getItsNatDocument();
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList songList = factory.createElementList(songsElem,true);
        for(int i = 0; i < songs.length; i++)
        {
            songList.addElement(songs[i]);
        }
    }
}
