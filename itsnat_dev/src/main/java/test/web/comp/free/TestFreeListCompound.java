/*
 * TestFreeListCompound.java
 *
 * Created on 2 de marzo de 2007, 13:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLElement;
import test.web.shared.TestBaseHTMLDocument;
import test.web.comp.Person;

/**
 *
 * @author jmarranz
 */
public class TestFreeListCompound extends TestBaseHTMLDocument implements EventListener //,ListDataListener
{
    protected ItsNatFreeListMultSel comp;

    /**
     * Creates a new instance of TestFreeListCompound
     */
    public TestFreeListCompound(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        load();
    }

    public void load()
    {
        List<Person> model = new ArrayList<Person>();
        model.add(new Person("John","Smith"));
        model.add(new Person("Bill","Clinton"));

        Document doc = itsNatDoc.getDocument();
        HTMLElement parentElem = (HTMLElement)doc.getElementById("freeListCompoundId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.comp = (ItsNatFreeListMultSel)componentMgr.findItsNatComponent(parentElem);

        DefaultListModel dataModel = new DefaultListModel();

        //dataModel.addListDataListener(this); // Añadimos antes pues queremos que se llame después del interno (renderizado) pues el primero que se llama es el último

        comp.setListModel(dataModel);

        ItsNatListCellRenderer renderer = new PersonListCellRenderer();
        comp.setItsNatListCellRenderer(renderer);
        comp.setItsNatListCellEditor(null);

        for(int i = 0; i < model.size(); i++)
        {
            dataModel.addElement(model.get(i));
        }

        ItsNatHTMLButton addButton = (ItsNatHTMLButton)componentMgr.findItsNatComponentById("addItemFreeListCompoundId");
        addButton.addEventListener("click",this);

        ItsNatHTMLButton removeButton = (ItsNatHTMLButton)componentMgr.findItsNatComponentById("removeItemFreeListCompoundId");
        removeButton.addEventListener("click",this);
    }


    public void handleEvent(Event evt)
    {
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLElementComponent comp = (ItsNatHTMLElementComponent)componentMgr.findItsNatComponent((Node)evt.getCurrentTarget());
        if (comp instanceof ItsNatFreeListMultSel)
            handleEvent(evt,(ItsNatFreeListMultSel)comp);
        else
            handleEvent(evt,(ItsNatHTMLButton)comp);
    }

    public void handleEvent(Event evt,ItsNatFreeListMultSel comp)
    {
        // Nada que hacer
    }

    public void handleEvent(Event evt,ItsNatHTMLButton button)
    {
        String id = button.getHTMLElement().getAttribute("id");
        if (id.startsWith("add"))
            addNewRow();  // Lo hacemos aparte via botón para que no influya en la selección de celdas
        else
            removeLastRow();
    }

    public void addNewRow()
    {
        DefaultListModel model = (DefaultListModel)comp.getListModel();
        model.addElement(new Person("First Name","Last Name")); // Para ver si se manifiesta en el navegador y genera un evento ListDataEvent
    }

    public void removeLastRow()
    {
        DefaultListModel model = (DefaultListModel)comp.getListModel();
        int size = model.getSize();
        if (size > 0)
            model.removeElementAt(size - 1);
    }

}
