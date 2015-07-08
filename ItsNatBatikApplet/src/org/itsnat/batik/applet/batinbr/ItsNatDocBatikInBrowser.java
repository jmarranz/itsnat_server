
package org.itsnat.batik.applet.batinbr;

import org.mozilla.javascript.Scriptable;

/**
 *
 * @author jmarranz
 */
public class ItsNatDocBatikInBrowser extends ObjectBatikInBrowser
{
    protected ItsNatSVGOMDocBatikInBrowser doc;

    public ItsNatDocBatikInBrowser(ItsNatSVGOMDocBatikInBrowser doc)
    {
        this.doc = doc;
    }

    @Override
    public ItsNatSVGOMDocBatikInBrowser getItsNatSVGOMDocBatikInBrowser()
    {
        return doc;
    }

    @Override
    public Scriptable getScriptable()
    {
        // En tiempo de creación de este objeto la referencia JS a itsNatDoc
        // no está todavía disponible, por eso la obtenemos cuando la necesitamos
        return doc.getItsNatDocScriptable();
    }

    public UserEventBatikInBrowser createUserEvent(String name)
    {
        Scriptable jsUserEvent = (Scriptable)callJSMethodFromBrowser("createUserEvent",new Object[]{ name });
        return new UserEventBatikInBrowser(this,jsUserEvent);
    }

    public void dispatchUserEvent(Object targetNode,Object evt)
    {
        callJSMethodFromBrowser("dispatchUserEvent",new Object[]{ targetNode, evt });
    }

    public void fireUserEvent(Object targetNode,String name)
    {
        callJSMethodFromBrowser("fireUserEvent",new Object[]{ targetNode, name });
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
