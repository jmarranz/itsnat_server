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
    public static final int MAX_PAGES = 10;

    protected Map<String,TreeViewParsed> registryByMarkup = new HashMap<String, TreeViewParsed>();
    protected TreeMap<Long,TreeViewParsed> registryByTimestamp = new TreeMap<Long,TreeViewParsed>(); // Recuerda que es un SortedMap de menor a mayor por defecto

    public synchronized TreeViewParsed get(String markup)
    {
        return get(markup,true);
    }

    public synchronized TreeViewParsed get(String markup,boolean updateTimestamp)
    {
        // Si la página es generada por ItsNat y tiene scripting de carga inline no habrá dos páginas iguales porque algunos ids usados cambian en cada carga
        TreeViewParsed treeView = registryByMarkup.get(markup);
        if (treeView == null) return null;
        if (updateTimestamp)
        {
            long timestampOld = treeView.updateTimestamp();
            registryByTimestamp.remove(timestampOld);
            registryByTimestamp.put(treeView.getTimestamp(), treeView); // Así hacemos ver que esta página se está usando recientemente y no será candidata a eliminarse
        }
        return treeView;
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
        res = registryByTimestamp.put(treeView.getTimestamp(),treeView); // No me puedo creer que un usuario cargue dos páginas en menos de 1ms
        if (res != null) throw new ItsNatDroidException("Internal Error");
    }
}
