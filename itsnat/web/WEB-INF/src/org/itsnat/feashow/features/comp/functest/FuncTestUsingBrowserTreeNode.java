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

package org.itsnat.feashow.features.comp.functest;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.itsnat.feashow.features.comp.lists.ListSelectionDecorator;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class FuncTestUsingBrowserTreeNode extends FeatureTreeNode implements EventListener,ListDataListener,ListSelectionListener
{
    protected ItsNatHTMLInputButton startButton;

    protected ItsNatFreeListMultSel listComp;
    protected ItsNatHTMLInputButton removeButton;
    protected ItsNatHTMLInputText itemComp;
    protected ItsNatHTMLInputText posComp;
    protected ItsNatHTMLInputButton updateButton;
    protected ItsNatHTMLInputButton insertButton;

    public FuncTestUsingBrowserTreeNode()
    {
    }

    public ItsNatFreeListMultSel getList()
    {
        return listComp;
    }

    public ItsNatHTMLInputButton getRemoveButton()
    {
        return removeButton;
    }

    public ItsNatHTMLInputText getItemInput()
    {
        return itemComp;
    }

    public ItsNatHTMLInputText getPosInput()
    {
        return posComp;
    }

    public ItsNatHTMLInputButton getUpdateButton()
    {
        return updateButton;
    }

    public ItsNatHTMLInputButton getInsertButton()
    {
        return insertButton;
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.startButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("startId");
        startButton.addEventListener("click",this);

        this.listComp = (ItsNatFreeListMultSel)compMgr.createItsNatComponentById("compId","freeListMultSel",null);

        DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();

        ListSelectionModel selModel = listComp.getListSelectionModel();
        selModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        selModel.addListSelectionListener(new ListSelectionDecorator(listComp));

        listComp.addEventListener("click",this);
        dataModel.addListDataListener(this);
        selModel.addListSelectionListener(this);

        this.removeButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("removeId");
        removeButton.addEventListener("click",this);

        this.itemComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("itemId");

        this.posComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("posId");

        this.updateButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("updateId");
        updateButton.addEventListener("click",this);

        this.insertButton = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("insertId");
        insertButton.addEventListener("click",this);
    }

    public void endExamplePanel()
    {
        this.startButton.dispose();
        this.startButton = null;

        this.listComp.dispose();
        this.listComp = null;

        this.removeButton.dispose();
        this.removeButton = null;

        this.itemComp.dispose();
        this.itemComp = null;

        this.posComp.dispose();
        this.posComp = null;

        this.updateButton.dispose();
        this.updateButton = null;

        this.insertButton.dispose();
        this.insertButton = null;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currentTarget = evt.getCurrentTarget();
        if (currentTarget == startButton.getHTMLInputElement())
        {
            new FuncTestUsingBrowserExample(this).startTest();
        }
        else
            normalBehavior(evt);
    }

    public void normalBehavior(Event evt)
    {
        EventTarget currentTarget = evt.getCurrentTarget();
        if (currentTarget == removeButton.getHTMLInputElement())
        {
            DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
            ListSelectionModel selModel = listComp.getListSelectionModel();
            if (!selModel.isSelectionEmpty())
            {
                // Selection Model is in SINGLE_INTERVAL_SELECTION mode
                int min = selModel.getMinSelectionIndex();
                int max = selModel.getMaxSelectionIndex();
                dataModel.removeRange(min,max);
            }
        }
        else if ((currentTarget == updateButton.getHTMLInputElement()) ||
                 (currentTarget == insertButton.getHTMLInputElement()))
        {
            String newItem = itemComp.getText();
            try
            {
                int pos = Integer.parseInt(posComp.getText());
                DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
                if (currentTarget == updateButton.getHTMLInputElement())
                    dataModel.setElementAt(newItem,pos);
                else
                    dataModel.insertElementAt(newItem,pos);
            }
            catch(NumberFormatException ex)
            {
                getItsNatDocument().addCodeToSend("alert('Bad Position');");
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {
                getItsNatDocument().addCodeToSend("alert('Bad Position');");
            }
        }
    }

    public void intervalAdded(ListDataEvent e)
    {
    }

    public void intervalRemoved(ListDataEvent e)
    {
    }

    public void contentsChanged(ListDataEvent e)
    {
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        int index = listComp.getSelectedIndex(); // First selected
        if (index != -1)
        {
            Object value = listComp.getListModel().getElementAt(index);
            itemComp.setText(value.toString());
            posComp.setText(Integer.toString(index));
        }
    }

}
