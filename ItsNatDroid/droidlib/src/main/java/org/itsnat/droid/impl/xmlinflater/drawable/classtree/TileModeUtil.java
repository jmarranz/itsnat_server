package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 28/07/14.
 */
public class TileModeUtil
{
    public static final Map<String,Integer> valueMap = new HashMap<String,Integer>( 4 );
    static
    {
        valueMap.put("disabled", -1);
        valueMap.put("clamp", 0);
        valueMap.put("repeat",1);
        valueMap.put("mirror",2);
    }
}
