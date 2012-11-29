package org.itsnat.batik.applet.brinbat;

import javax.swing.JApplet;
import netscape.javascript.JSObject;
import org.apache.batik.script.rhino.WindowWrapper;
import org.mozilla.javascript.Context;

/**
 *
 * @author jmarranz
 */
public class WindowBrowserInBatik extends ObjectBrowserInBatik
{
    public WindowBrowserInBatik(WindowWrapper winWrapper,JApplet applet)
    {
         super(JSObject.getWindow(applet));

        // El objetivo es hacer window.top o window.parent desde el documento Batik
        // y acceder al window del browser.
         
        // Cuando devolvemos un objeto Java normal desde un método Java
        // llamado por código JavaScript en Batik, el propio Rhino al parecer
        // envuelve el objeto en un Scriptable usando el WrapFactory por defecto (llamado también si es
        // necesario por Context.javaToJS).
        // En nuestro caso nosotros estamos asociando propiedades a valores directamente
        // en Java por lo que no hay el recubrimiento automático, por eso llamamos
        // a Context.javaToJS(...).
        // Podríamos no recubrir con javaToJS y funcionaría igual excepto un detalle,
        // el typeof NO funciona, si es recubierto el typeof devuelve "object".

        Object browserWinJS = Context.javaToJS(this, winWrapper);
        winWrapper.put("top",   winWrapper, browserWinJS);
        winWrapper.put("parent",winWrapper, browserWinJS);
    }

    public DocumentBrowserInBatik getDocument()
    {
        // En Batik el método getDocument() es llamado al leer la propiedad "document"
        // en window.
         JSObject jsRes = (JSObject)jsRef.getMember("document");
         return new DocumentBrowserInBatik(jsRes);
    }    
}
