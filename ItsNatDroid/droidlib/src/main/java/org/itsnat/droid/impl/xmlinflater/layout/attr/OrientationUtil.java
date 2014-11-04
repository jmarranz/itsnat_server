package org.itsnat.droid.impl.xmlinflater.layout.attr;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 28/07/14.
 */
public class OrientationUtil
{
    public static final Map<String, Integer> valueMap = new HashMap<String, Integer>( 2 );
    static
    {
        valueMap.put("horizontal", 0);
        valueMap.put("vertical", 1);
    }
}
