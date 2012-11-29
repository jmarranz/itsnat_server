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

package org.itsnat.feashow.features.comp.degraded;


import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.http.ItsNatHttpSession;

public class DisabledEventsAndCompListener
{
    protected ItsNatServletRequest itsNatRequest;
    protected ItsNatHTMLSelectMult listComp;

    public DisabledEventsAndCompListener(ItsNatServletRequest itsNatRequest)
    {
        this.itsNatRequest = itsNatRequest;

        try
        {
            ItsNatDocument itsNatDoc = itsNatRequest.getItsNatDocument();
            ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

            this.listComp = (ItsNatHTMLSelectMult)compMgr.addItsNatComponentById("listId");
            ListSelectionModel selModel = listComp.getListSelectionModel();
            selModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            String action = itsNatRequest.getServletRequest().getParameter("action");
            if (action == null)
                firstTime();
            else
                processAction(action);

            int selectedIndex = listComp.getSelectedIndex();
            if (selectedIndex != -1)
            {
                ItsNatHTMLInputText itemComp = (ItsNatHTMLInputText)compMgr.addItsNatComponentById("itemId");
                itemComp.setText(listComp.getListModel().getElementAt(listComp.getSelectedIndex()).toString());

                ItsNatHTMLInputText posComp = (ItsNatHTMLInputText)compMgr.addItsNatComponentById("posId");
                posComp.setText(Integer.toString(listComp.getSelectedIndex()));
            }
        }
        finally
        {
            saveDocumentToSession();
        }
    }

    public ItsNatDocument getItsNatDocument()
    {
        return itsNatRequest.getItsNatDocument();
    }

    public ItsNatHTMLSelectMult getListComponent()
    {
        return listComp;
    }

    public void firstTime()
    {
        DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();
        dataModel.addElement("Madrid");
        dataModel.addElement("Sevilla");
        dataModel.addElement("Segovia");
        dataModel.addElement("Barcelona");
        dataModel.addElement("Oviedo");
        dataModel.addElement("Valencia");

        ListSelectionModel selModel = listComp.getListSelectionModel();
        selModel.setSelectionInterval(2,3);
    }

    public void processAction(String action)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        DisabledEventsAndCompListener docPrev = loadDocumentFromSession();
        //ItsNatDocument itsNatDocPrev = docPrev.getItsNatDocument();

        ItsNatHTMLSelectMult prevListComp = docPrev.getListComponent();
        DefaultListModel model = (DefaultListModel)prevListComp.getListModel();
        ListSelectionModel oldSelModel = prevListComp.getListSelectionModel();
        prevListComp.dispose(); // to disconnect data model and selection models from the old markup (removing internal listeners)

        listComp.setListModel(model);// Reusing the data model
        // The selection model object may be reused but the current selection state will be lost,
        // so we reuse the selection state saved before
        boolean[] selection = getSelection(model.size(),oldSelModel);
        setSelection(selection); // set the current selection state

        if (action.equals("remove")) // remove selected
        {
            ListSelectionModel selModel = listComp.getListSelectionModel();
            int size = model.getSize();
            for(int i = size - 1; i >= 0; i--) // Removing from last to first to avoid index changes
            {
                if (selModel.isSelectedIndex(i))
                    model.removeElementAt(i);
            }
        }
        else if (action.equals("insert") || action.equals("update"))
        {
            ServletRequest request = itsNatRequest.getServletRequest();

            String item = request.getParameter("item");
            try
            {
                int pos = Integer.parseInt(request.getParameter("pos"));
                if (action.equals("insert"))
                    model.insertElementAt(item,pos);
                else
                    model.setElementAt(item,pos);
            }
            catch(NumberFormatException ex)
            {
                itsNatDoc.addCodeToSend("alert('Bad Position');");
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {
                itsNatDoc.addCodeToSend("alert('Bad Position');");
            }
        }
        else // Selection changed
        {
            setSelectionFromClient();
        }
    }
    public void setSelectionFromClient()
    {
        ServletRequest request = itsNatRequest.getServletRequest();
        String selectedParam = request.getParameter("selected");
        String[] selectedStr;
        if (!selectedParam.equals(""))
            selectedStr = selectedParam.split(",");
        else
            selectedStr = new String[0];

        int[] selected = new int[selectedStr.length];
        for(int i = 0; i < selected.length; i++)
            selected[i] = Integer.parseInt(selectedStr[i]);

        ListModel model = listComp.getListModel();
        boolean[] state = new boolean[model.getSize()];
        for(int i = 0; i < selected.length; i++)
            state[selected[i]] = true;

        setSelection(state);
    }

    public boolean[] getSelection(int size,ListSelectionModel selModel)
    {
        boolean[] state = new boolean[size];
        for(int i = 0; i < state.length; i++)
            state[i] = selModel.isSelectedIndex(i);
        return state;
    }

    public void setSelection(boolean[] state)
    {
        ListSelectionModel selModel = listComp.getListSelectionModel();
        for(int i = 0; i < state.length; i++)
        {
            boolean newState = state[i];
            if (newState != selModel.isSelectedIndex(i))
            {
                if (newState)
                    addSelectionIntervalJKD14(selModel,i,i);
                else
                    selModel.removeSelectionInterval(i,i);
            }
        }
    }

    public void saveDocumentToSession()
    {
        ItsNatHttpSession itsNatSession = (ItsNatHttpSession)itsNatRequest.getItsNatSession();
        HttpSession session = itsNatSession.getHttpSession();
        session.setAttribute("previous_doc",this);
    }

    public DisabledEventsAndCompListener loadDocumentFromSession()
    {
        ItsNatHttpSession itsNatSession = (ItsNatHttpSession)itsNatRequest.getItsNatSession();
        HttpSession session = itsNatSession.getHttpSession();
        DisabledEventsAndCompListener docPrev = (DisabledEventsAndCompListener)session.getAttribute("previous_doc");
        session.removeAttribute("previous_doc"); // No longer available
        return docPrev;
    }

    public static void addSelectionIntervalJKD14(ListSelectionModel selModel,int first,int end)
    {
        int mode = selModel.getSelectionMode();
        if (mode == ListSelectionModel.SINGLE_INTERVAL_SELECTION)
        {
            for(int i = first - 1; (i >= 0) && selModel.isSelectedIndex(i); i--)
                first = i;

            int max = selModel.getMaxSelectionIndex();
            for(int i = end + 1; (i <= max) && selModel.isSelectedIndex(i); i++)
                end = i;
        }

        selModel.addSelectionInterval(first,end);
    }
}
