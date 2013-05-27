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
package org.itsnat.feashow.features.stless.comp;


import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.ItsNatEventDOMStateless;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class StlessFreeListExampleInitialDocument implements EventListener,ListDataListener
{
    protected FalseDatabase db;
    protected ItsNatHTMLDocument itsNatDoc; 
    protected ItsNatFreeListMultSel listComp;  
    protected ItsNatHTMLInputText itemComp;  
    protected ItsNatHTMLInputText posComp;  
    
    public StlessFreeListExampleInitialDocument(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)  
    {  
        this.itsNatDoc = itsNatDoc;
        this.db = new FalseDatabase(itsNatDoc.getClientDocumentOwner().getItsNatSession());        
        load(request);
    }  
  
    public ItsNatDocument getItsNatDocument()
    {
        return itsNatDoc;
    }
    
    public ItsNatFreeListMultSel getItsNatFreeListMultSel()
    {
        return listComp;
    }

    public ItsNatHTMLInputText getItemItsNatHTMLInputText()
    {
        return itemComp;
    }
    
    public ItsNatHTMLInputText getPosItsNatHTMLInputText()
    {
        return posComp;
    }    
    
    public void load(ItsNatServletRequest request)  
    {         
        ItsNatDocument itsNatDoc = getItsNatDocument();        

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();  
  
        this.listComp = (ItsNatFreeListMultSel)compMgr.createItsNatComponentById("compId","freeListMultSel",null);  
        listComp.setItsNatListCellRenderer(new StlessListCellRenderer());
  
        List<City> cityList = db.getCityList();
        
        DefaultListModel dataModel = (DefaultListModel)listComp.getListModel(); 
        for(City city : cityList)
            dataModel.addElement(city);        
  
        dataModel.addListDataListener(this); 
        
        ListSelectionModel selModel = listComp.getListSelectionModel();  
        selModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);  
  
        selModel.addListSelectionListener(new StlessListSelectionListener(this));  

        this.itemComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("itemId");    
        this.posComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("posId");  

        if (itsNatDoc.isStateless())
        {
            String[] selList = (String[])request.getServletRequest().getParameterValues("currentSelection");           
            if (selList != null)
            {
                for(String selected : selList)
                {
                    int index = Integer.parseInt(selected);
                    selModel.addSelectionInterval(index, index);
                }
            }
            itsNatDoc.addEventListener(this);            
        }
    }  
  
    public void handleEvent(Event evt)  
    {  
        ItsNatEventDOMStateless itsNatEvt = (ItsNatEventDOMStateless)evt;
        String action = (String)itsNatEvt.getExtraParam("action");
        
        try
        {
            if ("remove".equals(action))
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
                else
                {
                    itsNatDoc.addCodeToSend("alert('None selected');");
                }
            }
            else if ("select".equals(action))        
            {
                ListSelectionModel selModel = listComp.getListSelectionModel();

                boolean selected = Boolean.valueOf((String)itsNatEvt.getExtraParam("selected"));            
                int index = Integer.parseInt((String)itsNatEvt.getExtraParam("index")); 
                if (!selected) selModel.addSelectionInterval(index, index);
                else selModel.removeSelectionInterval(index, index);
            }
            else if ("update".equals(action))        
            {
                String name = (String)itsNatEvt.getExtraParam("name");              
                int index = Integer.parseInt((String)itsNatEvt.getExtraParam("index")); 

                DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();             
                City city = (City)dataModel.getElementAt(index);
                city.setName(name);
                dataModel.setElementAt(city,index);              
            }       
            else if ("insert".equals(action))        
            {
                String name = (String)itsNatEvt.getExtraParam("name");              
                int index = Integer.parseInt((String)itsNatEvt.getExtraParam("index")); 

                DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();             
                City city = new City(db.generateId(),name);
                dataModel.insertElementAt(city,index);               
            }                
            else if ("reset".equals(action))        
            {
                db.reset();    
                itsNatDoc.addCodeToSend("window.location.reload();");
            }
        }
        catch(NumberFormatException ex)  
        {  
            getItsNatDocument().addCodeToSend("alert('Bad position number');");  
        }  
        catch(ArrayIndexOutOfBoundsException ex)  
        {  
            getItsNatDocument().addCodeToSend("alert('Position out of range');");  
        }         
    }  
  
    public void intervalAdded(ListDataEvent e)  
    {         
        int index0 = e.getIndex0();
        int index1 = e.getIndex1();        
        
        for(int i = index0; i <= index1; i++)
        {
            DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();                 
            City city = (City)dataModel.getElementAt(i);  
            db.insertCity(i,city);
        }        
    }  
  
    public void intervalRemoved(ListDataEvent e)  
    {     
        int index0 = e.getIndex0();
        int index1 = e.getIndex1();        
        
        for(int i = index0; i <= index1; i++)
            db.deleteCity(i);        
    }  
  
    public void contentsChanged(ListDataEvent e)  
    {  
        int type = e.getType();  
        switch(type)  
        {  
            case ListDataEvent.INTERVAL_ADDED:   
                intervalAdded(e);
                break;  
            case ListDataEvent.INTERVAL_REMOVED:             
                intervalRemoved(e);
                break; 
            case ListDataEvent.CONTENTS_CHANGED:  
                int index0 = e.getIndex0();
                DefaultListModel dataModel = (DefaultListModel)listComp.getListModel();                 
                City city = (City)dataModel.getElementAt(index0);        
                db.updateCity(index0,city);                
                break;  
        }          

    }  

}
