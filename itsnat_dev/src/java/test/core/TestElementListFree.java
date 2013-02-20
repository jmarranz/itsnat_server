/*
 * TestElementListFree.java
 *
 * Created on 11 de julio de 2007, 11:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import java.util.ListIterator;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.domutil.ElementListFree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestElementListFree
{

    /**
     * Creates a new instance of TestElementListFree
     */
    public TestElementListFree(ItsNatHTMLDocument itsNatDoc)
    {
        test(itsNatDoc);
    }

    public void test(ItsNatHTMLDocument itsNatDoc)
    {
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById("testElementListFreeId");

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();

        // Modo master
        ElementListFree elemList = factory.createElementListFree(parent,true);

        testElementListFree(elemList,itsNatDoc);

        // Testeamos el modo master = false (slave)

        elemList = factory.createElementListFree(parent,false);

        testElementListFree(elemList,itsNatDoc); // Idem que el modo master

        // Testeamos de nuevo para ver si funciona de verdad el modo slave
        // la lista está vacía en este momento

        parent.appendChild(doc.createElement("span"));
        parent.appendChild(doc.createElement("span"));
        parent.appendChild(doc.createElement("span"));

        elemList = factory.createElementListFree(parent,false);

        TestUtil.checkError(elemList.size() == 3);

        TestUtil.checkError(elemList.getElementAt(2) != null);
    }

    public void testElementListFree(ElementListFree elemList,ItsNatHTMLDocument itsNatDoc)
    {
        TestUtil.checkError(elemList.size() == 0);  // Se supone vacía

        Document doc = itsNatDoc.getDocument();

        elemList.addElement(doc.createElement("span"));
        elemList.addElement(doc.createElement("input"));
        elemList.insertElementAt(1,doc.createElement("div"));

        // Orden resultante: <span> <div> <input>
        TestUtil.checkError(elemList.getListElementInfoAt(2).getIndex() == 2); // Para testear que se ha actualizado de posición el <input>
        if (elemList.isMaster())
        {
            // La misma instancia
            TestUtil.checkError(elemList.getListElementInfoAt(2) == elemList.getListElementInfoAt(2));
        }
        else
        {
            // Diferentes instancias
            TestUtil.checkError(elemList.getListElementInfoAt(2) != elemList.getListElementInfoAt(2));
        }

        elemList.moveElement(0,1,1); // Desplaza las dos primeras filas a ser las dos finales
        TestUtil.checkError(elemList.getElementAt(0).getTagName().equals("input"));
        TestUtil.checkError(elemList.getElementAt(1).getTagName().equals("span"));
        TestUtil.checkError(elemList.getElementAt(2).getTagName().equals("div"));
        TestUtil.checkError(elemList.getListElementInfoAt(1).getIndex() == 1);

        elemList.moveElement(1,2,0); // Las deja como estaba
        TestUtil.checkError(elemList.getElementAt(0).getTagName().equals("span"));
        TestUtil.checkError(elemList.getElementAt(1).getTagName().equals("div"));
        TestUtil.checkError(elemList.getElementAt(2).getTagName().equals("input"));

        int count = 1;
        for(ListIterator<Element> it = elemList.listIterator(count); it.hasNext(); )
        {
            Element elem = it.next();
            count++;
            TestUtil.checkError(it.nextIndex() == count);
        }

        TestUtil.checkError(count == elemList.size());

        count = elemList.size();
        for(ListIterator<Element> it = elemList.listIterator(count); it.hasPrevious(); )
        {
            Element elem = it.previous();
            count--;
            TestUtil.checkError(it.previousIndex() == count - 1);
        }

        TestUtil.checkError(count == 0);

        for(ListIterator<Element> it = elemList.listIterator(); it.hasNext(); )
        {
            Element elem = it.next();
            it.set(doc.createElement("div"));
        }

        for(int i = 0; i < elemList.size(); i++)
        {
            TestUtil.checkError(elemList.getElementAt(i).getTagName().equals("div"));
        }

        for(ListIterator<Element> it = elemList.listIterator(elemList.size()); it.hasPrevious(); )
        {
            Element elem = it.previous();
            it.set(doc.createElement("input"));
        }

        for(int i = 0; i < elemList.size(); i++)
        {
            TestUtil.checkError(elemList.getElementAt(i).getTagName().equals("input"));
        }


        count = 0;
        for(ListIterator<Element> it = elemList.listIterator(); it.hasNext(); )
        {
            Element elem1 = it.next();
            Element elem2 = it.previous();
            Element elem3 = it.next();

            count++;

            // Esto también se cumple con una lista normal (ej. LinkedList)
            TestUtil.checkError(elem1 == elem2);
            TestUtil.checkError(elem2 == elem3);
        }

        TestUtil.checkError(count == elemList.size());

        count = elemList.size();
        for(ListIterator<Element> it = elemList.listIterator(); it.hasNext(); )
        {
            Element newElem = doc.createElement("input");
            it.add(newElem);
            it.next();
        }

        TestUtil.checkError((count * 2) == elemList.size());

        count = elemList.size();
        for(ListIterator<Element> it = elemList.listIterator(); it.hasNext(); )
        {
            Element newElem = doc.createElement("input");
            it.add(newElem);
            TestUtil.checkError(it.previous() == newElem);
            TestUtil.checkError(it.next() == newElem);
            it.next();
        }

        TestUtil.checkError((count * 2) == elemList.size());


        for(ListIterator<Element> it = elemList.listIterator(); it.hasNext(); )
        {
            Element elem = it.next();
            it.remove();
        }

        TestUtil.checkError(elemList.size() == 0);  // La dejamos vacía
    }

}
