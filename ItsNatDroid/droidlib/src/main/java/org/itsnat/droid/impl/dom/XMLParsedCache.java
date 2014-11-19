package org.itsnat.droid.impl.dom;

import org.itsnat.droid.ItsNatDroidException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jmarranz on 27/10/14.
 */
public class XMLParsedCache<T extends XMLParsed>
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
        T xmlParsed = registryByMarkup.get(markup);
        if (xmlParsed == null) return null;
        if (updateTimestamp)
        {
            long timestampOld = xmlParsed.updateTimestamp();
            registryByTimestamp.remove(timestampOld);
            registryByTimestamp.put(xmlParsed.getTimestamp(), xmlParsed); // Así hacemos ver que esta página se está usando recientemente y no será candidata a eliminarse
        }
        return xmlParsed;
    }

    public synchronized void put(String markup,T xmlParsed)
    {
        if (registryByMarkup.size() == MAX_PAGES)
        {
            Iterator<Map.Entry<Long,T>> it = registryByTimestamp.entrySet().iterator();
            Map.Entry<Long,T> oldestItem = it.next();
            it.remove();
            registryByMarkup.remove(oldestItem.getValue());
        }
        T res;
        res = registryByMarkup.put(markup,xmlParsed);
        if (res != null) throw new ItsNatDroidException("Internal Error");
        res = registryByTimestamp.put(xmlParsed.getTimestamp(), xmlParsed); // No me puedo creer que un usuario cargue dos páginas en menos de 1ms
        if (res != null) throw new ItsNatDroidException("Internal Error");
    }
}
