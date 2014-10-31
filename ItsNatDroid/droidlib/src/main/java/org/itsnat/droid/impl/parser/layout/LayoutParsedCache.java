package org.itsnat.droid.impl.parser.layout;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.model.layout.LayoutParsed;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jmarranz on 27/10/14.
 */
public class LayoutParsedCache
{
    public static final int MAX_PAGES = 10;

    protected Map<String,LayoutParsed> registryByMarkup = new HashMap<String, LayoutParsed>();
    protected TreeMap<Long,LayoutParsed> registryByTimestamp = new TreeMap<Long,LayoutParsed>(); // Recuerda que es un SortedMap de menor a mayor por defecto

    public synchronized LayoutParsed get(String markup)
    {
        return get(markup,true);
    }

    public synchronized LayoutParsed get(String markup,boolean updateTimestamp)
    {
        // Si la página es generada por ItsNat y tiene scripting de carga inline no habrá dos páginas iguales porque algunos ids usados cambian en cada carga
        LayoutParsed layoutParsed = registryByMarkup.get(markup);
        if (layoutParsed == null) return null;
        if (updateTimestamp)
        {
            long timestampOld = layoutParsed.updateTimestamp();
            registryByTimestamp.remove(timestampOld);
            registryByTimestamp.put(layoutParsed.getTimestamp(), layoutParsed); // Así hacemos ver que esta página se está usando recientemente y no será candidata a eliminarse
        }
        return layoutParsed;
    }

    public synchronized void put(String markup,LayoutParsed layoutParsed)
    {
        if (registryByMarkup.size() == MAX_PAGES)
        {
            Iterator<Map.Entry<Long,LayoutParsed>> it = registryByTimestamp.entrySet().iterator();
            Map.Entry<Long,LayoutParsed> oldestItem = it.next();
            it.remove();
            registryByMarkup.remove(oldestItem.getValue());
        }
        LayoutParsed res;
        res = registryByMarkup.put(markup,layoutParsed);
        if (res != null) throw new ItsNatDroidException("Internal Error");
        res = registryByTimestamp.put(layoutParsed.getTimestamp(), layoutParsed); // No me puedo creer que un usuario cargue dos páginas en menos de 1ms
        if (res != null) throw new ItsNatDroidException("Internal Error");
    }
}
