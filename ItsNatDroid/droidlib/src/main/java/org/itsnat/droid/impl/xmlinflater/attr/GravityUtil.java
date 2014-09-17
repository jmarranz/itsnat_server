package org.itsnat.droid.impl.xmlinflater.attr;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 28/07/14.
 */
public class GravityUtil
{
    public static final Map<String,Integer> valueMap = new HashMap<String,Integer>( 14 );

    static
    {
        // Merece la pena un Map, de otra manera son muchos if anidados y poco rendimiento

        // http://developer.android.com/reference/android/widget/LinearLayout.LayoutParams.html#attr_android:layout_gravity
        // http://developer.android.com/reference/android/widget/LinearLayout.html#attr_android:gravity
        // http://developer.android.com/reference/android/widget/TextView.html#attr_android:gravity
        // http://developer.android.com/reference/android/widget/FrameLayout.html#attr_android:foregroundGravity

        valueMap.put("top",0x30);
        valueMap.put("bottom", 0x50);
        valueMap.put("left", 0x03);
        valueMap.put("right", 0x05);
        valueMap.put("center_vertical", 0x10);
        valueMap.put("fill_vertical", 0x70);
        valueMap.put("center_horizontal", 0x01);
        valueMap.put("fill_horizontal", 0x07);
        valueMap.put("center", 0x11);
        valueMap.put("fill", 0x77);
        valueMap.put("clip_vertical", 0x80);
        valueMap.put("clip_horizontal", 0x08);
        valueMap.put("start", 0x00800003);
        valueMap.put("end", 0x00800005);

        // En el caso del atributo FrameLayout android:foregroundGravity, los casos start y end no aplican según los javadoc pero mi impresión es que es un olvido, de todas formas el programador obediente no debería usar entonces esos casos "no documentados" en el javadoc

    }
}
