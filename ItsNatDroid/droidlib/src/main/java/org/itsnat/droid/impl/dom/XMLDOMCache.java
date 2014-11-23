package org.itsnat.droid.impl.dom;

import org.itsnat.droid.ItsNatDroidException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jmarranz on 27/10/14.
 */
public class XMLDOMCache<T extends XMLDOM>
{
    public static final int MAX_PAGES = 10;

    protected Map<String,T> registryByMarkup = new HashMap<String, T>();
    protected TreeMap<Long,T> registryByTimestamp = new TreeMap<Long,T>(); // Recuerda que es un SortedMap de menor a mayor por defecto

    public synchronized T get(String markup)
    {
        return get(markup,true);
    }

    public synchronized T get(String markup,boolean updateTimestamp)
    {
        // Si la página es generada por ItsNat y tiene scripting de carga inline no habrá dos páginas iguales porque algunos ids usados cambian en cada carga
        T xmlDOM = registryByMarkup.get(markup);
        if (xmlDOM == null) return null;
        if (updateTimestamp)
        {
            long timestampOld = xmlDOM.updateTimestamp();
            registryByTimestamp.remove(timestampOld);
            registryByTimestamp.put(xmlDOM.getTimestamp(), xmlDOM); // Así hacemos ver que esta página se está usando recientemente y no será candidata a eliminarse
        }
        return xmlDOM;
    }

    public synchronized void put(String markup,T xmlDOM)
    {
        if (registryByMarkup.size() == MAX_PAGES)
        {
            Iterator<Map.Entry<Long,T>> it = registryByTimestamp.entrySet().iterator();
            Map.Entry<Long,T> oldestItem = it.next();
            it.remove();
            registryByMarkup.remove(oldestItem.getValue());
        }
        T res;
        res = registryByMarkup.put(markup,xmlDOM);
        if (res != null) throw new ItsNatDroidException("Internal Error");
        res = registryByTimestamp.put(xmlDOM.getTimestamp(), xmlDOM); // No me puedo creer que un usuario cargue dos páginas en menos de 1ms
        if (res != null) throw new ItsNatDroidException("Internal Error");
    }
}
