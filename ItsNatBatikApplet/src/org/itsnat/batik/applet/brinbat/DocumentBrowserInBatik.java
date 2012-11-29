package org.itsnat.batik.applet.brinbat;

import netscape.javascript.JSObject;

/**
 *
 * @author jmarranz
 */
public class DocumentBrowserInBatik extends ObjectBrowserInBatik
{
    public DocumentBrowserInBatik(JSObject jsRef)
    {
        super(jsRef);
    }

    public ItsNatDocBrowserInBatik getItsNatDoc()
    {
         JSObject jsRes = (JSObject)call("getItsNatDoc",new Object[0]);
         return new ItsNatDocBrowserInBatik(jsRes);
    }
}
