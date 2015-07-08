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

import java.util.ArrayList;
import org.itsnat.core.ItsNatSession;

/**
 *
 * @author jmarranz
 */
public class FalseDatabase 
{
    // We use user session to avoid collisions with other users, in a real 
    // world this is "a data list which nobody is accessing but you in this moment".    
    protected final ItsNatSession session;
    
    public FalseDatabase(ItsNatSession session)
    {
        this.session = session;
    }
    
    public ArrayList<City> getCityList()
    {
        synchronized(session)
        {
            ArrayList<City> dataList = (ArrayList<City>)session.getAttribute("dataList");
            if (dataList != null) return dataList;
            
            dataList = new ArrayList<City>();
            dataList.add(new City(generateId(),"Madrid"));  
            dataList.add(new City(generateId(),"Sevilla"));  
            dataList.add(new City(generateId(),"Segovia"));  
            dataList.add(new City(generateId(),"Barcelona"));  
            dataList.add(new City(generateId(),"Oviedo"));  
            dataList.add(new City(generateId(),"Valencia"));   
            
            
            session.setAttribute("dataList", dataList);
            return dataList;
        }
    }
    
    public void updateCity(int index,City data)
    {    
        ArrayList<City> list = getCityList();
        list.set(index, data);
        updateCityList(list);
    }
    
    public void deleteCity(int index)
    {    
        ArrayList<City> list = getCityList();
        list.remove(index);
        updateCityList(list);
    }    
    
    public void insertCity(int index,City data)
    {    
        ArrayList<City> list = getCityList();
        list.add(index, data);
        updateCityList(list);
    }       
    
    public void updateCityList(ArrayList<City> data)
    {
        synchronized(session)
        {         
            session.setAttribute("dataList", data);
        }        
    }    
    
    
    public long generateId()
    {    
        synchronized(session)
        {
            Long idgen = (Long)session.getAttribute("idgen");
            if (idgen == null) idgen = new Long(1);
            long id = idgen;
            session.setAttribute("idgen", new Long(idgen + 1));
            return id;
        }        
    }    
    
    public void reset()
    {    
        synchronized(session)
        {
            session.removeAttribute("dataList");
            session.removeAttribute("idgen");
        }        
    }
}
