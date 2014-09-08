package org.itsnat.droid.impl.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 5/09/14.
 */
public class MapLightAndRealPerformTest
{
    public static void test()
    {
        // Este test muestra que incluso en un conjunto de elementos muy pequeño tal y como 5 elementos, tiene MUCHO más rendimiento
        // un HashMap que un LinkedList y significativamente más que un ArrayList
        // Resultados en varios intentos en ms:
        // LinkedList:  2346,2423,2386
        // ArrayList:   565,654,553
        // real HashMap: 78,75,71
        // Los resultados son muy parecidos (un poco más rápido) si en vez de un LinkedList se usa un ArrayList


        String[] keys = new String[5];
        for (int i = 0; i < keys.length; i++)
            keys[i] = "key_" + i;

        MapLight<String, Integer> mapLight = new MapLight<String, Integer>();
        Map<String, Integer> hashMap = new HashMap<String, Integer>();

        for (int i = 0; i < keys.length; i++)
        {
            mapLight.put(keys[i], i);
            hashMap.put(keys[i], i);
        }

        int COUNT = 100000;
        boolean testMapLight = true;
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++)
        {
            for (int j = 0; j < keys.length; j++)
            {
                int res;
                if (testMapLight)
                {
                    res = mapLight.get(keys[j]);
                }
                else
                {
                    res = hashMap.get(keys[j]);
                }
                if (res != j) throw new RuntimeException("Unexpected");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("LAPSE: " + (end - start));
    }
}
