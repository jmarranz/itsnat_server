package org.itsnat.droid.impl.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 26/11/14.
 */
public class MimeUtil
{
    // http://www.sitepoint.com/web-foundations/mime-types-complete-list/
    public static final String MIME_ANDROID_LAYOUT = "android/layout";
    public static final String MIME_BEANSHELL = "text/beanshell";   // Inventado obviamente
    public static final String MIME_JSON = "application/json";
    public static final String MIME_XML = "text/xml"; // No usamos application/xml aunque sea más correcto, pues en otro lugar al detectar "text/XXX" lo convertimos a texto
    public static final String MIME_PNG = "image/png";
    //public static final String MIME_PNG_9 = "image/png9"; // Inventada, si no se necesitara realmente eliminarla
    public static final String MIME_JPEG = "image/jpg"; // Válido en BitmapDrawable
    public static final String MIME_GIF = "image/gif"; // Válido en BitmapDrawable

    public static final Map<String,String> MIME_BY_EXT = new HashMap<String,String>(); // Como sólo se lee puede usarse en multihilo
    static
    {
        // http://www.sitepoint.com/web-foundations/mime-types-complete-list/
        MIME_BY_EXT.put("xml", MIME_XML);
        MIME_BY_EXT.put("png", MIME_PNG);
        //MIME_BY_EXT.put("9.png",MIME_PNG);
        MIME_BY_EXT.put("jpg", MIME_JPEG);
        MIME_BY_EXT.put("jpe", MIME_JPEG);
        MIME_BY_EXT.put("jpeg",MIME_JPEG);
        MIME_BY_EXT.put("gif", MIME_GIF);

        // No es necesario "bs" ni "json" estos no se acceden remótamente, en el caso de .bs sí pero sabemos que es un script beanshell da igual la extensión
    }

    public static boolean isMIMEImage(String resourceMime)
    {
        return (MimeUtil.MIME_PNG.equals(resourceMime) ||
                MimeUtil.MIME_JPEG.equals(resourceMime) ||
                MimeUtil.MIME_GIF.equals(resourceMime));
    }
}
