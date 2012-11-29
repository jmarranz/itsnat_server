/*
 * TestNoScriptDocument.java
 *
 * Created on 9 de agosto de 2007, 17:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.noscript;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.http.ItsNatHttpSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.ElementCSSInlineStyle;

/**
 *
 * @author jmarranz
 */
public class TestNoScriptDocument
{
    protected ItsNatDocument itsNatDoc;
    protected ItsNatHTMLSelectMult listComp;

    /**
     * Creates a new instance of TestNoScriptDocument
     */
    public TestNoScriptDocument(ItsNatServletRequest itsNatRequest)
    {
        this.itsNatDoc = itsNatRequest.getItsNatDocument();

        try
        {

            ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();

            this.listComp = (ItsNatHTMLSelectMult)componentMgr.addItsNatComponentById("listId");
            ListSelectionModel selModel = listComp.getListSelectionModel();
            selModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

            ServletRequest request = itsNatRequest.getServletRequest();
            String action = null;

            if (request.getParameter("remove") != null)
                action = "remove";
            else if (request.getParameter("insert") != null)
                action = "insert";
            else if (request.getParameter("update") != null)
                action = "update";

            if (action == null)
                firstTime(itsNatRequest);
            else
                processAction(action,itsNatRequest);

            int selectedIndex = listComp.getSelectedIndex();
            if (selectedIndex != -1)
            {
                ItsNatHTMLInputText itemComp = (ItsNatHTMLInputText)componentMgr.addItsNatComponentById("itemId");
                itemComp.setText(listComp.getListModel().getElementAt(listComp.getSelectedIndex()).toString());

                ItsNatHTMLInputText posComp = (ItsNatHTMLInputText)componentMgr.addItsNatComponentById("posId");
                posComp.setText(Integer.toString(listComp.getSelectedIndex()));
            }
        }
        finally
        {
            saveDocumentToSession(itsNatRequest);
        }
    }

    public void firstTime(ItsNatServletRequest itsNatRequest)
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

    public void processAction(String action,ItsNatServletRequest itsNatRequest)
    {
        ItsNatDocument itsNatDocPrev = loadDocumentFromSession(itsNatRequest);

        ItsNatHTMLSelectMult prevListComp = (ItsNatHTMLSelectMult)itsNatDocPrev.getItsNatComponentManager().findItsNatComponentById("listId");
        DefaultListModel model = (DefaultListModel)prevListComp.getListModel();
        prevListComp.dispose(); // to disconnect the data model from the old markup

        listComp.setListModel(model);  // Reusing the data model

        setSelection(itsNatRequest);

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
            int pos;
            try
            {
                pos = Integer.parseInt(request.getParameter("pos"));
                if (action.equals("insert"))
                    model.insertElementAt(item,pos);
                else
                    model.setElementAt(item,pos);
            }
            catch(NumberFormatException ex)
            {
                setErrorMessage("Bad Position");
            }
            catch(ArrayIndexOutOfBoundsException ex)
            {
                setErrorMessage("Bad Position");
            }
        }
    }

    public int[] setSelection(ItsNatServletRequest itsNatRequest)
    {
        ServletRequest request = itsNatRequest.getServletRequest();
        String[] selectedParam = request.getParameterValues("list");
        if (selectedParam == null) return new int[0];

        int[] selected = new int[selectedParam.length];
        for(int i = 0; i < selected.length; i++)
            selected[i] = listComp.indexOf(selectedParam[i]);

        ListModel model = listComp.getListModel();
        boolean[] state = new boolean[model.getSize()];
        for(int i = 0; i < selected.length; i++)
            state[selected[i]] = true;

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

        return selected;
    }

    public void saveDocumentToSession(ItsNatServletRequest itsNatRequest)
    {
        ItsNatHttpSession itsNatSession = (ItsNatHttpSession)itsNatRequest.getItsNatSession();
        HttpSession session = itsNatSession.getHttpSession();
        session.setAttribute("previous_doc",itsNatDoc);
    }

    public ItsNatDocument loadDocumentFromSession(ItsNatServletRequest itsNatRequest)
    {
        ItsNatHttpSession itsNatSession = (ItsNatHttpSession)itsNatRequest.getItsNatSession();
        HttpSession session = itsNatSession.getHttpSession();
        ItsNatDocument itsNatDocPrev = (ItsNatDocument)session.getAttribute("previous_doc");
        session.removeAttribute("previous_doc"); // No longer available
        return itsNatDocPrev;
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

    public void setErrorMessage(String msg)
    {
        Document doc = itsNatDoc.getDocument();
        Element errorElem = doc.getElementById("errorId");
        ItsNatDOMUtil.setTextContent(errorElem,msg);
        ElementCSSInlineStyle errorCSS = (ElementCSSInlineStyle)errorElem;
        CSSStyleDeclaration style = errorCSS.getStyle();
        style.removeProperty("display"); // makes visible
    }
}
