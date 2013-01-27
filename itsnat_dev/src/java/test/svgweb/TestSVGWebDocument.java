/*
 * TestSVGWebDocument.java
 *
 * Created on 11 de febrero de 2008, 18:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.svgweb;

import java.io.Serializable;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import test.shared.BrowserUtil2;
import test.shared.EventListenerSerial;
import test.shared.Shared;
import test.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestSVGWebDocument implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected boolean opera;
    protected Element svgContainerElem;
    protected Element svgElem;
    protected Element circleListElem;
    protected ElementList circleList;
    protected Element textElem;
    protected Element selectedCircle;
    protected Element svgContainerReference;
    protected Element addCircleElem;
    protected Element removeCircleElem;
    protected Element biggerCircleElem;
    protected Element smallerCircleElem;
    protected Element addRemoveTextElem;
    protected Element changeTextElem;
    protected Element subtreeInsertElem;
    protected Element reinsertElem;
    protected HTMLInputElement useSVGLoadElem;

    /** Creates a new instance of TestSVGWebDocument */
    public TestSVGWebDocument(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();
        this.opera = BrowserUtil2.isOpera(request);

        Document doc = itsNatDoc.getDocument();
        this.svgContainerReference = doc.getElementById("svgContainerReferenceId");

        this.addCircleElem = doc.getElementById("addCircleId");
        ((EventTarget)addCircleElem).addEventListener("click",this,false);

        this.removeCircleElem = doc.getElementById("removeCircleId");
        ((EventTarget)removeCircleElem).addEventListener("click",this,false);

        this.biggerCircleElem = doc.getElementById("biggerCircleId");
        ((EventTarget)biggerCircleElem).addEventListener("click",this,false);

        this.smallerCircleElem = doc.getElementById("smallerCircleId");
        ((EventTarget)smallerCircleElem).addEventListener("click",this,false);

        this.addRemoveTextElem = doc.getElementById("addRemoveTextId");
        ((EventTarget)addRemoveTextElem).addEventListener("click",this,false);

        this.changeTextElem = doc.getElementById("changeTextId");
        ((EventTarget)changeTextElem).addEventListener("click",this,false);

        this.subtreeInsertElem = doc.getElementById("testSubTreeInsertionId");
        ((EventTarget)subtreeInsertElem).addEventListener("click",this,false);

        this.reinsertElem = doc.getElementById("reinsertId");
        ((EventTarget)reinsertElem).addEventListener("click",this,false);

        this.useSVGLoadElem = (HTMLInputElement)doc.getElementById("useSVGLoadId");
        itsNatDoc.addEventListener((EventTarget)useSVGLoadElem,"click",null,false,new NodePropertyTransport("checked",boolean.class));

        // Necesario iniciar en el evento onLoad porque los
        // <script type="image/svg+xml"> están presentes inicialmente
        AbstractView view = ((DocumentView)doc).getDefaultView();
        EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                onLoad(evt);
            }
        };
        ((EventTarget)view).addEventListener("SVGLoad",listener,false);

        Shared.setRemoteControlLink(request,response);

        new TestSerialization(request);
    }

    public void onLoad(Event evt)
    {
        loadSVGPart(evt);

        checkSVGWebMSIEExtraNodes();

        checkComments();

        if (opera)
            itsNatDoc.addCodeToSend("alert('Dynamic script insertion does not work in Opera and SVGWeb');");
        else
            new TestSVGWebScriptInsertion(itsNatDoc,svgElem).testAddScripts();

        // Testeamos el atributo "style" que está inicialmente con valores exagerados
        // Hay que tener en cuenta que style.cssText no funciona en SVGWeb y
        // en MSIE SVGWeb lo usa internamente para sus fines (es decir, no tocar).
        textElem.removeAttribute("style");
        textElem.setAttribute("style","font-size:20; fill:#000000;");
    }

    public void checkSVGWebMSIEExtraNodes()
    {
        // Sólo tiene interés en MSIE
        // Chequeamos que los elementos auxiliares que necesita el MSIE
        // están debajo del <div> que reemplaza el <meta>
        StringBuffer code = new StringBuffer();
        code.append("var elem = document.getElementById('__ie__svg__onload');");
        code.append("if ((elem != null)&&(elem.parentNode.tagName != 'DIV')) alert('ERROR SVGWeb');\n");
        code.append("var elem = document.getElementById('__htc_container');");
        code.append("if ((elem != null)&&(elem.parentNode.tagName != 'DIV')) alert('ERROR SVGWeb');\n");
        itsNatDoc.addCodeToSend(code.toString());
    }

    public void checkComments()
    {
        // Comprobamos que el añadir/cambiar/eliminar un comentario no es problema
        // porque no se puede en SVGWeb (no soportado), ItsNat los filtra

        Document doc = itsNatDoc.getDocument();
        Comment comm = doc.createComment("Some comment");
        svgElem.appendChild(comm);
        comm.setData("New comment");
        svgElem.removeChild(comm);
    }

    public void loadSVGPart(Event evt)
    {
        Document doc = itsNatDoc.getDocument();

        this.svgContainerElem = doc.getElementById("svgContainerId");

        this.svgElem = doc.getElementById("svgId");

        ElementGroupManager egm = itsNatDoc.getElementGroupManager();
        this.circleListElem = doc.getElementById("circleListId");
        this.circleList = egm.createElementList(circleListElem, false);
        itsNatDoc.addEventListener(((EventTarget)circleListElem),"click",this,false);

        this.textElem = doc.getElementById("textId");
        Node text = textElem.getLastChild();
        if (text instanceof Text)
            ((Text)text).setData("HELLO");
        else
            textElem.appendChild(doc.createTextNode("HELLO"));

        // Para testear si funciona el removeEventListener:
        // NO HACEMOS ESTE TEST, en dracolisk parece que removeEventListener no
        // funciona muy bien, quizás es porque es inmediatamente seguido al addEventListener
        // o es un problema de identidad del registro del listener,
        // el caso es que removeEventListener elimina los dos listeners que se añaden
        // al objeto no sólo el inmediatamente anterior.
        // No se si tiene que ver: http://code.google.com/p/svgweb/issues/detail?id=528#c0
        EventListener listener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                // NO debe de ejecutarse
                itsNatDoc.addCodeToSend("alert('ERROR addSVGEventListeners');");
            }
        };
        //((EventTarget)circleListElem).addEventListener("click",listener,false);
        //((EventTarget)circleListElem).removeEventListener("click",listener,false);
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == circleListElem)
        {
            EventTarget target = evt.getTarget();
            if ((target instanceof Element) &&
                ((Element)target).getLocalName().equals("circle"))
            {
                selectCircle((Element)target);
            }
        }
        else if (currTarget == addCircleElem)
        {
            Element lastElem = circleList.getLastElement();
            if (lastElem == null)
            {
                circleList.addElement(); // Will based on the pattern
            }
            else
            {
/*
                String code = new String();
                code += "var elem = " + itsNatDoc.getScriptUtil().getNodeReference(circleList.getParentElement()) + ";";
                code += "var t1 = new Date().getTime();";
                code += "for(var i = 0; i < 1000; i++) {";
                code += "  var child = document.createElementNS('http://www.w3.org/2000/svg','circle');";
                code += "  elem.appendChild(child); ";
                code += "}";
                code += "var t2 = new Date().getTime();";
                code += "alert(t2 - t1);";
                itsNatDoc.addCodeToSend(code);
*/

                Element newCircle = circleList.addElement();
                int cx = Integer.parseInt(lastElem.getAttribute("cx"));
                cx += 100;
                newCircle.setAttribute("cx",Integer.toString(cx));
            }
        }
        else if (currTarget == removeCircleElem)
        {
            if (!circleList.isEmpty())
            {
                Element circle = circleList.removeElementAt(circleList.getLength() - 1);
                if (selectedCircle == circle) this.selectedCircle = null;
            }
        }
        else if (currTarget == biggerCircleElem)
        {
            if (selectedCircle != null)
            {
                int r = Integer.parseInt(selectedCircle.getAttribute("r"));
                r += 20;
                selectedCircle.setAttribute("r",Integer.toString(r));

/*
                String code = new String();
                code += "var elem = " + itsNatDoc.getScriptUtil().getNodeReference(selectedCircle) + ";";
                code += "var t1 = new Date().getTime();";
                code += "var value;";
                code += "for(var i = 0; i < 10000; i++)";
                code += "  value = elem.getAttribute('r');";
                code += "var t2 = new Date().getTime();";
                code += "alert((t2 - t1) + ' ' + value);";
                itsNatDoc.addCodeToSend(code);
*/
            }
        }
        else if (currTarget == smallerCircleElem)
        {
            if (selectedCircle != null)
            {
                int r = Integer.parseInt(selectedCircle.getAttribute("r"));
                r -= 20;
                if (r < 0) r = 10;
                selectedCircle.setAttribute("r",Integer.toString(r));
            }
        }
        else if (currTarget == addRemoveTextElem)
        {
            Node text = textElem.getLastChild();
            if (text instanceof Text)
            {
                textElem.removeChild(text);
            }
            else
            {
                Document doc = itsNatDoc.getDocument();
                text = doc.createTextNode("SOME TEXT");
                textElem.appendChild(text);
            }
        }
        else if (currTarget == changeTextElem)
        {
            Node text = textElem.getLastChild();
            if (text instanceof Text)
            {
                String data = ((Text)text).getData();
                if (data.equals("")) data = "SOME TEXT";
                else data = "";
                ((Text)text).setData(data);
            }
            else itsNatDoc.addCodeToSend("alert('Add Text Before');");
        }
        else if (currTarget == subtreeInsertElem)
        {
            // La finalidad de este test es el de probar el setInnerXML con
            // un subtree SVG para ser post-procesado por SVGWeb

            // La lista de círculos no tiene listeners
            Element circleListElem = circleList.getParentElement();
            Element parentNode = (Element)circleListElem.getParentNode();
            Element circleListElemClone = (Element)circleListElem.cloneNode(false);
            parentNode.replaceChild(circleListElemClone,circleListElem);
            parentNode.replaceChild(circleListElem,circleListElemClone);

            // Al reinsertar se pierde el listener, restauramos:
            itsNatDoc.addEventListener(((EventTarget)circleListElem),"click",this,false);

            // Idem para testear la inserción de un nodo de texto via setInnerXML
            Element gElem = (Element)textElem.getParentNode();
            parentNode = (Element)gElem.getParentNode();
            Element gElemClone = (Element)gElem.cloneNode(false);
            parentNode.replaceChild(gElemClone,gElem);
            parentNode.replaceChild(gElem,gElemClone);

            itsNatDoc.addCodeToSend("alert('OK (must see no visual change)');");
        }
        else if (currTarget == reinsertElem)
        {
            HTMLDocument doc = itsNatDoc.getHTMLDocument();
            Node parent = doc.getBody();
            boolean inserted = (svgContainerElem.getParentNode() != null);

            if (inserted) // Está insertado, eliminamos
            {
                selectCircle(null);
                parent.removeChild(svgContainerElem);
                ItsNatDOMUtil.setTextContent(reinsertElem,"Reinsertar");
            }
            else // Está eliminado alguno, reinsertamos
            {
                parent.insertBefore(svgContainerElem,svgContainerReference);

                if (useSVGLoadElem.getChecked())
                {
                    EventListener listener = new EventListenerSerial()
                    {
                        public void handleEvent(Event evt)
                        {
                            loadSVGPart(evt);
                        }
                    };
                    // Evitamos registrar para todos los clientes, pues en control remoto completo significa que
                    // se recibe el evento tantas veces como cliente y eso no tiene sentido.
                    // por ello usamos el ClientDocumentImpl pues todavía no son públicos
                    // los métodos ClientDocumentImpl.addEventListener...
                    ClientDocumentImpl clientDoc = (ClientDocumentImpl)((ItsNatEvent)evt).getClientDocument();
                    clientDoc.addEventListener((EventTarget)svgElem,"SVGLoad",listener,false);
                }
                else
                {
                    loadSVGPart(evt);
                }

                ItsNatDOMUtil.setTextContent(reinsertElem,"Eliminar");
            }
        }
        else itsNatDoc.addCodeToSend("alert('UNEXPECTED');");
    }

    public void selectCircle(Element circleElem)
    {
        if (selectedCircle != null)
            selectedCircle.setAttribute("fill","#FF0000");
        if (circleElem != null)
            circleElem.setAttribute("fill","#00FF00");
        this.selectedCircle = circleElem;
    }

    public boolean clickedCircle(int x,int y,Element circle)
    {
        int xc = Integer.parseInt(circle.getAttribute("cx"));
        int yc = Integer.parseInt(circle.getAttribute("cy"));
        int r = Integer.parseInt(circle.getAttribute("r"));
        return (xc - r <= x)&&(x <= xc + r)&&(yc - r <= y)&&(y <= yc + r);
    }

}
