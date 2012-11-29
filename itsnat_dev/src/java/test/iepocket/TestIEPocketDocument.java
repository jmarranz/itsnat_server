/*
 * TestIEPocketDocument.java
 *
 * Created on 12 de agosto de 2007, 9:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.iepocket;

import java.io.Serializable;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.CommMode;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import test.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestIEPocketDocument implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;

    /**
     * Creates a new instance of TestIEPocketDocument
     */
    public TestIEPocketDocument(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        this.itsNatDoc = itsNatDoc;
        load(request);
    }

    public void load(ItsNatServletRequest request)
    {
        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();
        ((EventTarget)view).addEventListener("load",this,false);
        itsNatDoc.addEventListener((EventTarget)view,"unload",this,false,CommMode.XHR_SYNC);

        new TestIEPocketSetTextNode(itsNatDoc);
        new TestIEPocketSelectMultiple(itsNatDoc,request);
        new TestIEPocketInputCheckBox(itsNatDoc);
        new TestIEPocketFreeListMultiple(itsNatDoc);

        new TestSerialization(request);
    }

    public void handleEvent(Event evt)
    {
        String type = evt.getType();
        if (type.equals("load"))
        {
            itsNatDoc.addCodeToSend("alert('PROCESS LOAD HANDLER from Java');");

            testEventListenerRegistry();

            testAttributes();

            testNewElements();

            itsNatDoc.addCodeToSend("alert('END LOAD HANDLER from Java');");
        }
        else if (type.equals("unload"))
        {
            itsNatDoc.addCodeToSend("alert('END UNLOAD HANDLER from Java');");
        }
        else // click
        {
            itsNatDoc.addCodeToSend("alert('OK');");
        }
    }

    public void testEventListenerRegistry()
    {
        Document doc = itsNatDoc.getDocument();

        AbstractView view = ((DocumentView)doc).getDefaultView();
        EventListener unloadListener = new EventListenerFoo();
        // Añadimos uno nuevo
        ((EventTarget)view).addEventListener("unload",unloadListener,false);

        // Probamos a eliminar
        ((EventTarget)view).removeEventListener("unload",unloadListener,false);

        Element link = doc.getElementById("linkId");
        // Registramos varias veces para testar el registro de múltiples listeners
        EventListener clickListener1 = new EventListenerFoo();
        ((EventTarget)link).addEventListener("click",clickListener1,false);
        EventListener clickListener2 = new EventListenerFoo();
        ((EventTarget)link).addEventListener("click",clickListener2,false);

        // Eliminamos para testear si se quitan
        ((EventTarget)link).removeEventListener("click",clickListener1,false);
        ((EventTarget)link).removeEventListener("click",clickListener2,false);

        // Registramos para pruebas
        ((EventTarget)link).addEventListener("click",this,false);
    }

    public void testAttributes()
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();

        doc.getDocumentElement().setAttribute("lang","en");
        // No podemos verificar "lang" porque <html> no tiene getAttribute (otros elementos sí)

        Element head = ItsNatTreeWalker.getFirstChildElement(doc.getDocumentElement());
        head.setAttribute("lang","en");
        // Item <html> no podemos verificar


        doc.getBody().setAttribute("onunload","var evt = window.event ? window.event : event; alert('HTML ONUNLOAD ' + evt.type);");

        doc.getBody().setAttribute("bgcolor","yellow");
        itsNatDoc.addCodeToSend("var value = document.body.getAttribute('bgcolor'); if ((value != 'yellow')&&(value != '#ffff00')) alert('Bad bgcolor');");

        // Test definir atributo. El elemento <p> no tiene propiedad "align" asociada, se definirá por fuerza bruta.
        Element elem = doc.getElementById("testAttrId");
        elem.setAttribute("align","left");
        itsNatDoc.addCodeToSend("var value = document.getElementById('testAttrId').getAttribute('align'); if (value != 'left') alert('Bad align');");

        // Test eliminar atributos
        doc.getDocumentElement().removeAttribute("lang");
        head.removeAttribute("lang");

        // El getAttribute() falla cuando el atributo no existe en elementos normales (body para abajo)
        // aprovechamos ese fallo para testear su "ausencia"

        doc.getBody().removeAttribute("bgcolor");  // utilizamos minúsculas a propósito (internamente se usará bgColor)
        itsNatDoc.addCodeToSend("if (itsnat.MSIEPocketHTMLDocument) { var elem = document.body; try { elem.getAttribute('bgColor'); alert('Bad remove bgcolor'); } catch(e) { } }");

        elem.removeAttribute("align");
        itsNatDoc.addCodeToSend("if (itsnat.MSIEPocketHTMLDocument) { var elem = document.getElementById('testAttrId'); try { elem.getAttribute('align'); alert('Bad remove align'); } catch(e) {  } }");

        // Lo volvemos a poner para ver si funciona definirlo de nuevo
        elem.setAttribute("align","left");
        itsNatDoc.addCodeToSend("var value = document.getElementById('testAttrId').getAttribute('align'); if (value != 'left') alert('Bad align');");


        // Prueba de style, en teoría se genera style.cssText el cual funciona en IE Mobile
        elem.setAttribute("style","font-size:1px;");
        elem.setAttribute("style","font-size:40px;");
        elem.removeAttribute("style"); // Si quedara visualmente muy grande o muy pequeño es que no funciona

        String oldHandler;

        // Probamos si aguanta el body eliminar el onunload, cambiarlo y restaurar el original via atributo.
        Element body = doc.getBody();
        oldHandler = body.getAttribute("onunload");
        body.removeAttribute("onunload");
        body.setAttribute("onunload","MAL");
        body.setAttribute("onunload",oldHandler); // Restauramos

        // Probamos si aguanta el link eliminar el onclick, cambiarlo y restaurar el original via atributo.
        Element link = doc.getElementById("linkId");
        oldHandler = link.getAttribute("onclick");

        link.removeAttribute("onclick");
        link.setAttribute("onclick","MAL");
        link.setAttribute("onclick",oldHandler); // Restauramos
    }

    public void testNewElements()
    {
        // Probamos a añadir elementos
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Element parent = doc.getElementById("newContentId");

        Text text; HTMLTableRowElement tr; HTMLTableCellElement td;
        Element iElem,bElem;

        Element p = doc.createElement("p");
        p.setAttribute("align","right");
        text = doc.createTextNode("Must be ");
        p.appendChild(text);
        Element b = doc.createElement("b");
        p.appendChild(b);
            text = doc.createTextNode("on the right");
            b.appendChild(text);
        text = doc.createTextNode(" if ok");
        p.appendChild(text);
        parent.appendChild(p);

        // Insertamos una tabla. Hacerlo en modo "desktop" que es cuando hay más inestabilidad
        // http://xman892.blogspot.com/2007/07/internet-explorer-mobileone-column.html

        HTMLTableElement table = (HTMLTableElement)doc.createElement("table");
        table.setAttribute("border","1px");
        parent.appendChild(table);
        HTMLTableSectionElement tbody = (HTMLTableSectionElement)doc.createElement("tbody");
        table.appendChild(tbody);
        tr = (HTMLTableRowElement)doc.createElement("tr");
        tbody.appendChild(tr);
        td = (HTMLTableCellElement)doc.createElement("td");
        tr.appendChild(td);
        text = doc.createTextNode("Cell 1,1");
        td.appendChild(text);


        // Queda: <tr><td>Cell 1,1</td></tr>

        // Insertamos otra celda sobre la tabla ya en el documento
        td = (HTMLTableCellElement)doc.createElement("td");
        tr.appendChild(td);
        text = doc.createTextNode("Cell 1,2");
        td.appendChild(text);    // Usa JS appendTextNode
        // Queda: <tr><td>Cell 1,1</td><td>Cell 1,2</td></tr>

        // Añadimos una fila por clonación
        tr = (HTMLTableRowElement)tr.cloneNode(true);
        tbody.appendChild(tr); // Fila repetida
        // Queda:
        // Fila 1: <tr><td>Cell 1,1</td><td>Cell 1,2</td></tr>
        // Fila 2: <tr><td>Cell 1,1</td><td>Cell 1,2</td></tr>

        // Probamos a cambiar el texto de las dos nuevas celdas
        // de forma enrevesada para probar los métodos específicos para IE Mobile
        td = (HTMLTableCellElement)tr.getCells().item(0); // Celda 2,1
        text = (Text)td.getFirstChild(); // Texto: "Cell 1,1"
        iElem = doc.createElement("i");
        iElem.appendChild(doc.createTextNode("The "));
        td.insertBefore(iElem,text); // inserción al ppio del todo (usando JS insertAfter)
        // Fila 2: <tr><td><i>The </i>Cell 1,1</td>...

        bElem = doc.createElement("b");
        bElem.appendChild(doc.createTextNode("Cell "));
        td.insertBefore(bElem,text); // inserción entre el primero y el de texto  (usando otro caso de JS insertAfter)
        // Fila 2: <tr><td><i>The </i><b>Cell</b>Cell 1,1</td>...

        iElem = doc.createElement("i");
        iElem.appendChild(doc.createTextNode("2,1"));
        td.appendChild(iElem);
        // Fila 2: <tr><td><i>The </i><b>Cell</b>Cell 1,1<i>2,1</i></td>...

        // Eliminando ahora el texto antiguo ahora que está en medio de los dos nuevos elementos (usando JS setTextNode pero como remove)
        td.removeChild(text); // Elimina "Cell 1,1"
        // Fila 2: <tr><td><i>The </i><b>Cell</b><i>2,1</i></td>...

        td = (HTMLTableCellElement)tr.getCells().item(1); // Celda 2,2
        text = (Text)td.getFirstChild(); // Texto: "Cell 1,2"
        td.removeChild(text);
        // Fila 2: <tr><td>...</td><td></td></tr>

        iElem = doc.createElement("i");
        iElem.appendChild(doc.createTextNode("2,2"));
        td.appendChild(iElem);
        // Fila 2: <tr><td>...</td><td><i>2,2</i></td></tr>

        text = doc.createTextNode("The Cell ");
        td.insertBefore(text,iElem); // Usará el método JS insertTextNode
        // Fila 2: <tr><td>...</td><td>The Cell<i>2,2</i></td></tr>

        Comment comment = doc.createComment("Comment text"); // Para probar la inserción de comentarios
        td.appendChild(comment);

        // Prueba a eliminar una fila
        tr = (HTMLTableRowElement)tr.cloneNode(true);
        tbody.appendChild(tr);
        tbody.removeChild(tr);

        // Probamos si aguantan los TBODY, TR y TD definir atributos (se usa innerHTML usando un <span> auxiliar)
        // no nos molestamos en comprobar si los atributos están definidos realmente
        // porque eso es algo que ya se testeó antes.
        // Estos elementos son muy problemáticos sobre todo con IE en modo "Desktop"
        // necesitando a veces ocultar la tabla contenedora. Esto no es necesario
        // para la propia <table>

        tbody.setAttribute("align","left");
        tbody.setAttribute("class","foo"); // Hace necesario ocultar la tabla temporalmente
        tr = (HTMLTableRowElement)tbody.getRows().item(0);
        tr.setAttribute("align","left");
        tr.setAttribute("class","foo"); // Hace necesario ocultar la tabla temporalmente
        td = (HTMLTableCellElement)tr.getCells().item(0);
        td.setAttribute("align","left");
        td.setAttribute("class","foo"); // Hace necesario ocultar la tabla temporalmente

        // Por último vemos si aguanta la reinserción de toda la tabla
        Node tableSibling = table.getNextSibling(); // por ahora es null, pero por si cambiamos el ejemplo
        parent.removeChild(table);
        parent.insertBefore(table, tableSibling);

        // Probamos a cambiar el texto de un nodo de texto via JS setTextNode
        p = doc.createElement("p");
        text = doc.createTextNode("BAD TEXT");
        p.appendChild(text);
        parent.appendChild(p);

        text.setData("Text <ok>");  // El < y > es para probar si se toleran (y no se convierten en un nodo pues se usa innerHTML internamente)

        // Probamos a añadir nodos a un elemento tal y como <button> que no admite
        // innerHTML
        Element button = doc.createElement("button");
        parent.appendChild(button);
        text = doc.createTextNode("This is an useless ");
        button.appendChild(text);
        b = doc.createElement("b");
        button.appendChild(b);
        text = doc.createTextNode("Button");
        b.appendChild(text);
    }
}
