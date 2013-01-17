
package org.itsnat.impl.res.core.js;

import org.itsnat.core.ItsNatException;

public class LoadScriptImpl
{
    public static final String ITSNAT = "itsnat.js";
    public static final String ITSNAT_MSIE_OLD = "itsnat_msie_old.js";
    public static final String ITSNAT_W3C = "itsnat_w3c.js";
    public static final String ITSNAT_SVGWEB = "itsnat_svgweb.js";
    public static final String ITSNAT_OPERA_8_MOBILE = "itsnat_opera_8_mobile.js";
    public static final String ITSNAT_MOTOWEBKIT = "itsnat_motowebkit.js";
    public static final String ITSNAT_FIX_OTHERNS_IN_HTML = "itsnat_fix_otherns_in_html.js";

    public static void checkFileName(String fileName)
    {
        if (!fileName.equals(ITSNAT) &&
            !fileName.equals(ITSNAT_MSIE_OLD) &&
            !fileName.equals(ITSNAT_W3C) &&
            !fileName.equals(ITSNAT_SVGWEB) &&
            !fileName.equals(ITSNAT_OPERA_8_MOBILE) &&
            !fileName.equals(ITSNAT_MOTOWEBKIT) &&
            !fileName.equals(ITSNAT_FIX_OTHERNS_IN_HTML) )
           throw new ItsNatException("Security Violation Attempt");
    }
}
