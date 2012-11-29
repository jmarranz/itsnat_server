/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.comp.lists;

import javax.swing.DefaultListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.comp.list.ItsNatListMultSel;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.feashow.features.comp.shared.Person;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class PersonRenderer implements ItsNatListCellRenderer
{
    public PersonRenderer()
    {
    }

    public static ItsNatHTMLInputText getInputText(String id,Node containerNode,ItsNatList list)
    {
        Element firstName = ItsNatDOMUtil.getElementById(id,containerNode);
        ItsNatComponentManager compMgr = list.getItsNatComponentManager();
        return (ItsNatHTMLInputText)compMgr.findItsNatComponent(firstName);
    }

    public void renderListCell(ItsNatList list, int index, Object value, boolean isSelected, boolean hasFocus, Element cellContentElem, boolean isNew)
    {
        ItsNatListMultSel listMult = (ItsNatListMultSel)list;

        final DefaultListModel listModel = (DefaultListModel)listMult.getListModel();
        final Person person = (Person)listModel.getElementAt(index);

        if (isNew)
        {
            ItsNatDocument itsNatDoc = listMult.getItsNatDocument();

            if (!itsNatDoc.getItsNatDocumentTemplate().isAutoBuildComponents())
            {
                ItsNatComponentManager compMgr = listMult.getItsNatComponentManager();
                compMgr.buildItsNatComponents(cellContentElem);
            }

            final ItsNatHTMLInputText firstNameComp = getInputText("firstNameId",cellContentElem,listMult);
            final ItsNatHTMLInputText lastNameComp = getInputText("lastNameId",cellContentElem,listMult);

            firstNameComp.setText(person.getFirstName());
            lastNameComp.setText(person.getLastName());

            DocumentListener firstNameListener = new DocumentListener()
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

            DocumentListener lastNameListener = new DocumentListener()
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
        else
        {
            ItsNatHTMLInputText firstNameComp = getInputText("firstNameId",cellContentElem,listMult);
            ItsNatHTMLInputText lastNameComp = getInputText("lastNameId",cellContentElem,listMult);

            firstNameComp.setText(person.getFirstName());
            lastNameComp.setText(person.getLastName());
        }
    }

    public void unrenderListCell(ItsNatList list,int index,Element cellContentElem)
    {
        ItsNatDocument itsNatDoc = list.getItsNatDocument();
        if (!itsNatDoc.getItsNatDocumentTemplate().isAutoBuildComponents())
        {
            ItsNatComponentManager compMgr = list.getItsNatComponentManager();
            compMgr.removeItsNatComponents(cellContentElem,true);
        }
    }
}
