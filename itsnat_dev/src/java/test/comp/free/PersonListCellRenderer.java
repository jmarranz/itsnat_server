/*
 * PersonListCellRenderer.java
 *
 * Created on 2 de marzo de 2007, 17:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.free;

import java.io.Serializable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatVariableResolver;
import org.w3c.dom.Element;
import test.comp.*;


/**
 *
 * @author jmarranz
 */
public class PersonListCellRenderer implements ItsNatListCellRenderer,Serializable
{

    /** Creates a new instance of PersonListCellRenderer */
    public PersonListCellRenderer()
    {
    }

    public void renderListCell(ItsNatList list, int index, Object value, boolean isSelected, boolean cellHasFocus,Element cellElem,boolean isNew)
    {
        if (!isNew) return;

        // Sólo es llamado una vez al añadir la persona

        ItsNatFreeListMultSel comp = (ItsNatFreeListMultSel)list;
        ItsNatDocument itsNatDoc = comp.getItsNatDocument();
        ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver(true);
        resolver.setLocalVariable("firstNameId","firstName_" + index);
        resolver.setLocalVariable("lastNameId","lastName_" + index);
        resolver.resolve(cellElem);

        ItsNatComponentManager componentMgr = comp.getItsNatComponentManager();
        if (!itsNatDoc.getItsNatDocumentTemplate().isAutoBuildComponents())
            componentMgr.buildItsNatComponents(cellElem);

        final ItsNatHTMLInputText firstNameComp = (ItsNatHTMLInputText)componentMgr.findItsNatComponentById("firstName_" + index);
        final ItsNatHTMLInputText lastNameComp = (ItsNatHTMLInputText)componentMgr.findItsNatComponentById("lastName_" + index);

        final Person person = (Person)value;

        firstNameComp.setText(person.getFirstName());

        DocumentListener firstNameListener = new DocumentListenerSerializable()
        {
            public void insertUpdate(DocumentEvent e)
            {
                update();
            }

            public void removeUpdate(DocumentEvent e)
            {
                update();
            }

            public void changedUpdate(DocumentEvent e)
            {
                update();
            }

            public void update()
            {
                person.setFirstName(firstNameComp.getText());
            }

        };

        firstNameComp.getDocument().addDocumentListener(firstNameListener);



        lastNameComp.setText(person.getLastName());

        DocumentListener lastNameListener = new DocumentListenerSerializable()
        {
            public void insertUpdate(DocumentEvent e)
            {
                update();
            }

            public void removeUpdate(DocumentEvent e)
            {
                update();
            }

            public void changedUpdate(DocumentEvent e)
            {
                update();
            }

            public void update()
            {
                person.setLastName(lastNameComp.getText());
            }

        };

        lastNameComp.getDocument().addDocumentListener(lastNameListener);
    }

    public void unrenderListCell(ItsNatList list,int index,Element cellContentElem)
    {
        ItsNatDocument itsNatDoc = list.getItsNatDocument();
        if (!itsNatDoc.getItsNatDocumentTemplate().isAutoBuildComponents())
        {
            // En modo auto build se destruyen automáticamente
            ItsNatComponentManager componentMgr = list.getItsNatComponentManager();
            componentMgr.removeItsNatComponents(cellContentElem,true);
        }

    }
}
