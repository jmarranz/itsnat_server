/*
 * TestSVGInXHTMLLoadListener.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.svgxhtml;


import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import test.shared.Shared;
import test.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestSVGInXHTMLASVDocument implements EventListener
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element objectContainerElem;
    protected Element svgContainerElem;
    protected Element svgElem;
    protected ElementList circleList;
    protected Element textElem;
    protected Element selectedCircle;
    protected Element objectContainerReference;
    protected Element svgContainerReference;
    protected Element addCircleElem;
    protected Element removeCircleElem;
    protected Element biggerCircleElem;
    protected Element smallerCircleElem;
    protected Element addRemoveTextElem;
    protected Element changeTextElem;
    protected Element reinsertElem;
    protected Element subtreeInsertElem;

    /**
     * Creates a new instance of TestSVGInXHTMLLoadListener
     */
    public TestSVGInXHTMLASVDocument(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();

        load(request,response);
    }

    public void load(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        Document doc = itsNatDoc.getDocument();

        loadSVGPart();

        addCircle(); // Para demostrar que funciona en tiempo de carga e incluso con fastLoad=false

        this.objectContainerReference = doc.getElementById("objectContainerReferenceId");
        this.svgContainerReference = doc.getElementById("svgContainerReferenceId");

        this.addCircleElem = doc.getElementById("addCircleId");
        ((EventTarget)addCircleElem).addEventListener("click",this,false);

        this.removeCircleElem = doc.getElementById("removeCircleId");
        ((EventTarget)removeCircleElem).addEventListener("click",this,false);

        this.biggerCircleElem = doc.getElementById("biggerCircleId");
        ((EventTarget)biggerCircleElem).addEventListener("click",this,false);

        this.smallerCircleElem = doc.getElementById("smallerCircleId");
        ((EventTarget)smallerCircleElem).addEventListener("click",this,false);

        this.textElem = doc.getElementById("textId");

        this.addRemoveTextElem = doc.getElementById("addRemoveTextId");
        ((EventTarget)addRemoveTextElem).addEventListener("click",this,false);

        this.changeTextElem = doc.getElementById("changeTextId");
        ((EventTarget)changeTextElem).addEventListener("click",this,false);

        this.reinsertElem = doc.getElementById("reinsertId");
        ((EventTarget)reinsertElem).addEventListener("click",this,false);

        this.subtreeInsertElem = doc.getElementById("testSubTreeInsertionId");
        ((EventTarget)subtreeInsertElem).addEventListener("click",this,false);

        Shared.setRemoteControlLink(request,response);

        new TestSerialization(request);
    }

    public void loadSVGPart()
    {
        Document doc = itsNatDoc.getDocument();

        this.objectContainerElem = doc.getElementById("objectContainerId");
        this.svgContainerElem = doc.getElementById("svgContainerId");

        this.svgElem = doc.getElementById("svgId");
        ParamTransport[] params = new ParamTransport[]
            { new CustomParamTransport("offsetX","event.getNativeEvent().offsetX"),
              new CustomParamTransport("offsetY","event.getNativeEvent().offsetY") };
        itsNatDoc.addEventListener(((EventTarget)svgElem),"click",this,false,params);

        ElementGroupManager egm = itsNatDoc.getElementGroupManager();
        Element circleListElem = doc.getElementById("circleListId");
        this.circleList = egm.createElementList(circleListElem,false);
    }

    public void handleEvent(Event evt)
    {
        EventTarget target = evt.getTarget();
        if (target == svgElem) // MSIE o fuera de los círculos
        {
            ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
            if ("undefined".equals(itsNatEvt.getExtraParam("offsetX")))
                return; // No MSIE y fuera de los círculos

            String offsetXStr = (String)itsNatEvt.getExtraParam("offsetX");
            int x = Integer.parseInt(offsetXStr);
            String offsetYStr = (String)itsNatEvt.getExtraParam("offsetY");
            int y = Integer.parseInt(offsetYStr);
            itsNatDoc.addCodeToSend("alert(" + x + " + ',' + " + y + ");");
            for(int i = circleList.getLength() - 1; i >= 0; i--)
            {
                // The last circle has a greater SVG "z-index"
                Element circleElem = circleList.getElementAt(i);
                if (clickedCircle(x,y,circleElem))
                {
                    selectCircle(circleElem);
                    break;
                }
            }
        }
        else if ("circle".equals(((Node)target).getLocalName()))
        {
            selectCircle((Element)target);
        }
        else if (target == addCircleElem)
        {
            Element lastElem = circleList.getLastElement();
            if (lastElem == null)
            {
                circleList.addElement(); // Will based on the pattern
            }
            else
            {
                Element newCircle = circleList.addElement();
                int cx = Integer.parseInt(lastElem.getAttribute("cx"));
                cx += 100;
                newCircle.setAttribute("cx",Integer.toString(cx));
            }
        }
        else if (target == removeCircleElem)
        {
            if (!circleList.isEmpty())
            {
                Element circle = circleList.removeElementAt(circleList.getLength() - 1);
                if (selectedCircle == circle) this.selectedCircle = null;
            }
        }
        else if (target == biggerCircleElem)
        {
            if (selectedCircle != null)
            {
                int r = Integer.parseInt(selectedCircle.getAttribute("r"));
                r += 20;
                selectedCircle.setAttribute("r",Integer.toString(r));
            }
        }
        else if (target == smallerCircleElem)
        {
            if (selectedCircle != null)
            {
                int r = Integer.parseInt(selectedCircle.getAttribute("r"));
                r -= 20;
                if (r < 0) r = 10;
                selectedCircle.setAttribute("r",Integer.toString(r));
            }
        }
        else if (target == addRemoveTextElem)
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
        else if (target == changeTextElem)
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
        else if (target == reinsertElem)
        {
            HTMLDocument doc = itsNatDoc.getHTMLDocument();
            Node parent = doc.getBody();
            boolean inserted = (objectContainerElem.getParentNode() != null) &&
                               (svgContainerElem.getParentNode() != null);

            if (inserted) // Están insertados, eliminamos
            {
                selectCircle(null);
                parent.removeChild(objectContainerElem);
                parent.removeChild(svgContainerElem);
                ItsNatDOMUtil.setTextContent(reinsertElem,"Reinsertar");
            }
            else // Está eliminado alguno, reinsertamos
            {
                parent.insertBefore(objectContainerElem,objectContainerReference);
                parent.insertBefore(svgContainerElem,svgContainerReference);
                loadSVGPart();
                ItsNatDOMUtil.setTextContent(reinsertElem,"Eliminar");
            }
        }
        else if (target == subtreeInsertElem)
        {
            // La finalidad de este test es el de probar el setInnerXML con
            // un subtree SVG para ser post-procesado después por ASV

            // La lista de círculos no tiene listeners
            Element circleListElem = circleList.getParentElement();
            Element parentNode = (Element)circleListElem.getParentNode();
            Element circleListElemClone = (Element)circleListElem.cloneNode(false);
            parentNode.replaceChild(circleListElemClone,circleListElem);
            parentNode.replaceChild(circleListElem,circleListElemClone);

            itsNatDoc.addCodeToSend("alert('OK (must see no visual change)');");
        }
        else itsNatDoc.addCodeToSend("alert('UNEXPECTED');");
    }

    public void addCircle()
    {
        Element lastElem = circleList.getLastElement();
        if (lastElem == null) lastElem = circleList.getChildPatternElement();
        Element newCircle = circleList.addElement();
        int cx = Integer.parseInt(lastElem.getAttribute("cx"));
        cx += 100;
        newCircle.setAttribute("cx",Integer.toString(cx));
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
