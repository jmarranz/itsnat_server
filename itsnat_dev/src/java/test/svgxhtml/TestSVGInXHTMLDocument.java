/*
 * TestSVGInXHTMLLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.svgxhtml;


import java.io.Serializable;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.list.ItsNatListUI;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestSVGInXHTMLDocument implements EventListener,ListSelectionListener,Serializable
{
    protected Element containerElem;
    protected ItsNatHTMLDocument itsNatDoc;
    protected ItsNatHTMLButton buttonBigger;
    protected ItsNatHTMLButton buttonAdd;
    protected ItsNatHTMLButton testNamespacesButton;
    protected ItsNatHTMLButton reinsertButton;
    protected ItsNatFreeListMultSel circleList;

    /**
     * Creates a new instance of TestSVGInXHTMLLoadListener
     */
    public TestSVGInXHTMLDocument(ItsNatServletRequest request)
    {
        this.itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();

        load(request);
    }

    public void load(ItsNatServletRequest request)
    {
        Document doc = itsNatDoc.getDocument();
        this.containerElem = doc.getElementById("containerId");

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        //Element listParentElem = itsNatDoc.getDocument().getElementById("circleListId");
        this.circleList = (ItsNatFreeListMultSel)compMgr.addItsNatComponentById("circleListId","freeListMultSel",null);
        circleList.setItsNatListCellRenderer(new CircleRenderer());
        circleList.setItsNatListCellEditor(null); // No se visualizar el <input> metido en un elemento svg incluso aunque se añada el namespace
        circleList.getListSelectionModel().addListSelectionListener(this);

        DefaultListModel model = (DefaultListModel)circleList.getListModel();
        model.addElement(new Circle(50));

        this.buttonBigger = (ItsNatHTMLButton)compMgr.findItsNatComponentById("biggerCircleId");
        buttonBigger.addEventListener("click",this);

        this.buttonAdd = (ItsNatHTMLButton)compMgr.findItsNatComponentById("addCircleId");
        buttonAdd.addEventListener("click",this);

        this.testNamespacesButton = (ItsNatHTMLButton)compMgr.findItsNatComponentById("testNamespacesId");
        testNamespacesButton.addEventListener("click",this);

        this.reinsertButton = (ItsNatHTMLButton)compMgr.findItsNatComponentById("reinsertId");
        reinsertButton.addEventListener("click",this);

        decorate(0,circleList.getListModel().getSize() - 1);

/*
    <P:svg id="test1Id" P:PruebA="Valor"  xmlns:P="http://www.w3.org/2000/svg"  itsnat:nocache="true" version="1.1">
      <P:text x="10" font-size="20" y="20" fill="blue">Some text</P:text>
      <P:Prueba id="test1childId"></P:Prueba>
    </P:svg>

   <span id="test2Id" xmlns:P="http://prueba.org/prueba" P:PruebA="Valor" itsnat:nocache="true" />
 
   <span xmlns:P="http://prueba.org/prueba" itsnat:nocache="true">
        <span id="test3Id" P:PruebA="Valor" />
   </span>

   <P:Parent id="test4Id" itsnat:ignorens="true" xmlns:P="http://prueba.org/prueba" itsnat:nocache="true">
        <P:Child id="test4childId"  P:PruebA="Valor" />
   </P:Parent>
*/

        StringBuffer code = new StringBuffer();
        ScriptUtil util = itsNatDoc.getScriptUtil();

        code.append( "try{" );

        code.append( "var elem = null;" );
        code.append( "var attr = null;" );

        Element testElem1 = doc.getElementById("test1Id");
        code.append( "elem = " + util.getNodeReference(testElem1) + ";" );
        code.append( "attr = elem.getAttributeNodeNS('http://www.w3.org/2000/svg','PruebA');");
        code.append( "if (attr == null) alert('WRONG test 1'); ");
        code.append( "else if (attr.name != 'P:PruebA') alert('WRONG test 1 (2)'); ");

        Element testChildElem1 = doc.getElementById("test1childId");
        code.append( "elem = " + util.getNodeReference(testChildElem1) + ";" );
        code.append( "if (elem.namespaceURI != 'http://www.w3.org/2000/svg') alert('WRONG test child 1'); ");
        code.append( "if (elem.tagName != 'P:Prueba') alert('WRONG test child 1 (2)'); ");

        Element testElem2 = doc.getElementById("test2Id");
        code.append( "elem = " + util.getNodeReference(testElem2) + ";" );
        code.append( "attr = elem.getAttributeNodeNS('http://prueba.org/prueba','PruebA');");
        code.append( "if (attr == null) alert('WRONG test 2'); ");
        code.append( "else if (attr.name != 'P:PruebA') alert('WRONG test 2 (2)'); ");

        Element testElem3 = doc.getElementById("test3Id");
        code.append( "elem = " + util.getNodeReference(testElem3) + ";" );
        code.append( "attr = elem.getAttributeNodeNS('http://prueba.org/prueba','PruebA');");
        code.append( "if (attr == null) alert('WRONG test 2'); ");
        code.append( "else if (attr.name != 'P:PruebA') alert('WRONG test 2 (2)'); ");

        ItsNatDocumentTemplate template = itsNatDoc.getItsNatDocumentTemplate();
        String mime = template.getMIME();
        if (mime.equals("text/html")) // testeamos el itsnat:ignorens que sólo tiene sentido en text/html
        {
            Element testElem4 = doc.getElementById("test4Id");
            code.append( "elem = " + util.getNodeReference(testElem4) + ";" );
            code.append( "if (elem.namespaceURI == 'http://prueba.org/prueba') alert('WRONG test 4'); ");
            code.append( "if (elem.tagName.toLowerCase() != 'p:parent') alert('WRONG test 4 (2)'); ");

            Element testChildElem4 = doc.getElementById("test4childId");
            code.append( "elem = " + util.getNodeReference(testChildElem4) + ";" );
            code.append( "if (elem.namespaceURI == 'http://prueba.org/prueba') alert('WRONG test 4 child'); ");
            code.append( "if (elem.tagName.toLowerCase() != 'p:child') alert('WRONG test 4 child (2)'); ");

            code.append( "attr = elem.getAttributeNode('p:prueba');");
            code.append( "if (attr == null) alert('WRONG test 4 child (3)'); ");
            code.append( "else if (attr.name.toLowerCase() != 'p:prueba') alert('WRONG test 4 (3)'); ");
        }

        code.append( "}catch(ex){ alert('WRONG test (maybe namespaces not supported)'); }" );

        itsNatDoc.addCodeToSend(code);
    }

    public void handleEvent(Event evt)
    {
        ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
        ItsNatDocument itsNatDoc = itsNatEvt.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == buttonBigger.getHTMLButtonElement())
        {
            Element circleParent = doc.getElementById("circleId");

            Element circleElem =  ItsNatTreeWalker.getFirstChildElement(circleParent);

            int radio = Integer.parseInt(circleElem.getAttribute("r"));
            radio += 20;
            circleElem.setAttribute("r",Integer.toString(radio));
        }
        else if (currTarget == buttonAdd.getHTMLButtonElement())
        {
            // Al menos hay uno
            DefaultListModel model = (DefaultListModel)circleList.getListModel();
            int len = model.getSize();
            int cx = ((Circle)model.getElementAt(len - 1)).getCx();
            model.addElement(new Circle(cx + 70));
        }
        else if (currTarget == testNamespacesButton.getHTMLButtonElement())
        {
            Element elem = doc.createElementNS("http://www.w3.org/2000/svg","svg");
            elem.setAttribute("id","newElemWithNamespaceId");
            doc.getDocumentElement().appendChild(elem);

            String code = "var elem = document.getElementById('newElemWithNamespaceId'); \n";
            code += "var ok = (elem.tagName == 'svg') && (elem.namespaceURI == 'http://www.w3.org/2000/svg') && (elem.tagName == 'svg'); \n";
            code += "if (ok) alert('OK'); \n";
            code += "else alert('WRONG!! (normal when namespaces are not supported)'); \n";
            itsNatDoc.addCodeToSend(code);

            doc.getDocumentElement().removeChild(elem);
        }
        else if (currTarget == reinsertButton.getHTMLButtonElement())
        {
            DocumentFragment docFrag = doc.createDocumentFragment();
            while(containerElem.hasChildNodes())
                docFrag.appendChild(containerElem.getFirstChild());

            containerElem.appendChild(docFrag);

            load(((ItsNatEvent)evt).getItsNatServletRequest());

            itsNatDoc.addCodeToSend("alert('OK, almost no visual change expected (removed list of circles)');");
        }
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        int first = e.getFirstIndex();
        int last = e.getLastIndex();
        decorate(first,last);
    }

    public void decorate(int first,int last)
    {
        ListSelectionModel selModel = circleList.getListSelectionModel();
        ItsNatListUI compUI = circleList.getItsNatListUI();
        for(int i = first; i <= last; i++)
        {
            Element elem = compUI.getElementAt(i);
            boolean selected = selModel.isSelectedIndex(i);
            if (selected)
                elem.setAttribute("fill","#ff0000");
            else
                elem.setAttribute("fill","#0000ff");
        }
    }
}
