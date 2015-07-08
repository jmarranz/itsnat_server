package org.itsnat.batik.applet.brinbat;

import netscape.javascript.JSObject;

/**
 *
 * @author jmarranz
 */
public class ObjectBrowserInBatik
{
    protected JSObject jsRef;

    public ObjectBrowserInBatik(JSObject jsRef)
    {
         this.jsRef = jsRef;
    }

    public JSObject getJSObject()
    {
        return jsRef;
    }

    protected Object call(String name,Object[] params)
    {
        //Object[] jsParams = params;
        
        
        Object[] jsParams;
        if (params != null)
        {
            if (params.length == 0) jsParams = params;
            else
            {
                jsParams = new Object[params.length];
                for(int i = 0; i < params.length; i++)
                {
                    Object param = params[i];
                    if (param != null)
                    {
                        // Este es un intento de hacer lo más interoperable posible Batik y el browser
                        if (param instanceof ObjectBrowserInBatik)
                            jsParams[i] = ((ObjectBrowserInBatik)param).getJSObject();
                        else
                            jsParams[i] = param;
                    }
                }
            }
        }
        else jsParams = new Object[0];

        return jsRef.call(name,jsParams);
    }    
}
