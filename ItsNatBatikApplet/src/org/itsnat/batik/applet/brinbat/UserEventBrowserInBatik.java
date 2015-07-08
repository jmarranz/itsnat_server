
package org.itsnat.batik.applet.brinbat;

import netscape.javascript.JSObject;

/**
 *
 * @author jmarranz
 */
public class UserEventBrowserInBatik extends ObjectBrowserInBatik
{
    public UserEventBrowserInBatik(JSObject jsRef)
    {
        super(jsRef);
    }
    
    public void setExtraParam(String name,Object value)
    {
        call("setExtraParam",new Object[]{ name,value });
    }
}
