
package org.itsnat.batik.applet.brinbat;

import netscape.javascript.JSObject;

/**
 *
 * @author jmarranz
 */
public class ItsNatDocBrowserInBatik extends ObjectBrowserInBatik
{
    public ItsNatDocBrowserInBatik(JSObject jsRef)
    {
         super(jsRef);
    }

    public UserEventBrowserInBatik createUserEvent(String name)
    {
        JSObject jsRes = (JSObject)call("createUserEvent", new Object[]{ name });
        return new UserEventBrowserInBatik(jsRes);
    }

    public void dispatchUserEvent(Object targetNode,Object evt)
    {
        // Yo creo que targetNode apenas será el document o el window
        // del browser contenedor porque los demás métodos de document
        // no están exportados
        UserEventBrowserInBatik userEvt = (UserEventBrowserInBatik)evt;
        call("dispatchUserEvent",new Object[]{ targetNode, userEvt.getJSObject() });
    }

    public void fireUserEvent(Object targetNode,String name)
    {
        // Leer notas en dispatchUserEvent
        call("fireUserEvent",new Object[]{ targetNode, name });
    }

    public void addEventMonitor(Object listener)
    {
        // Es también público pero no merece la pena implementar
        // pues es un caso de uso MUY RARO.
        RuntimeException ex = new RuntimeException("Not implemented");
        ex.printStackTrace();
        throw ex;
    }

    public void removeEventMonitor(Object listener)
    {
        // Es también público pero no merece la pena implementar
        // pues es un caso de uso MUY RARO.
        RuntimeException ex = new RuntimeException("Not implemented");
        ex.printStackTrace();
        throw ex;
    }
    
    public void setEnableEventMonitors(boolean enable)
    {
        // Es también público pero no merece la pena implementar
        // pues es un caso de uso MUY RARO.
        RuntimeException ex = new RuntimeException("Not implemented");
        ex.printStackTrace();
        throw ex;
    }

}
