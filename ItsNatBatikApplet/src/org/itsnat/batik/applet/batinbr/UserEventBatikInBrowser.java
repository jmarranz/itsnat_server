
package org.itsnat.batik.applet.batinbr;

import org.mozilla.javascript.Scriptable;

/**
 *
 * @author jmarranz
 */
public class UserEventBatikInBrowser extends ObjectBatikInBrowser
{
    protected ItsNatDocBatikInBrowser parent;
    protected Scriptable jsRef;

    public UserEventBatikInBrowser(ItsNatDocBatikInBrowser parent,Scriptable jsRef)
    {
        this.parent = parent;
        this.jsRef = jsRef;
    }

    @Override
    public Scriptable getScriptable()
    {
        return jsRef;
    }

    @Override
    public ItsNatSVGOMDocBatikInBrowser getItsNatSVGOMDocBatikInBrowser()
    {
        return parent.getItsNatSVGOMDocBatikInBrowser();
    }

    public void setExtraParam(String name,Object value)
    {
        callJSMethodFromBrowser("setExtraParam",new Object[]{ name,value });
    }
}
