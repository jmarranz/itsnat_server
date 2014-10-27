package org.itsnat.droid.impl.parser;

import org.itsnat.droid.ItsNatDroidException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jmarranz on 27/10/14.
 */
public class TreeViewParsedCache
{
    //Seguir: usando la clase antes de crear un nuevo TreeViewParsed

    public static final int MAX_PAGES = 10;

    protected Map<String,TreeViewParsed> registryByMarkup = new HashMap<String, TreeViewParsed>();
    protected TreeMap<Long,TreeViewParsed> registryByTimestamp = new TreeMap<Long,TreeViewParsed>(); // Recuerda que es un SortedMap de menor a mayor por defecto

    public synchronized TreeViewParsed get(String markup)
    {
        return registryByMarkup.get(markup);
    }

    public synchronized void put(String markup,TreeViewParsed treeView)
    {
        if (registryByMarkup.size() == MAX_PAGES)
        {
            Iterator<Map.Entry<Long,TreeViewParsed>> it = registryByTimestamp.entrySet().iterator();
            Map.Entry<Long,TreeViewParsed> oldestItem = it.next();
            it.remove();
            registryByMarkup.remove(oldestItem.getValue());
        }
        TreeViewParsed res;
        res = registryByMarkup.put(markup,treeView);
        if (res != null) throw new ItsNatDroidException("Internal Error");
        res = registryByTimestamp.put(treeView.getTimestamp(),treeView);
        if (res != null) throw new ItsNatDroidException("Internal Error");
    }
}
