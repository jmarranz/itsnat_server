

import org.apache.batik.script.rhino.RhinoInterpreterFixed;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.batik.bridge.WindowUtil;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.script.Window;
import org.apache.batik.script.rhino.WindowWrapper;
import org.apache.batik.script.rhino.WindowWrapperUtil;
import org.itsnat.batik.applet.ItsNatSVGOMDocumentBatik;
import org.itsnat.batik.applet.JSVGCanvasApplet;
import org.itsnat.batik.applet.XMLHttpRequest;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Ponemos esta clase en el package root porque al ser llamado un método
 * desde JavaScript supone una referencia global tipo:
 * Packages.pkg1.pkg2....NombreClase.metodo(params);
 * Y como inevitablemente hay un importPackage por defecto Rhino intenta
 * resolver todos los nombres pkg1, pkg2 a modo de variables globales lo cual
 * supone llamadas al servidor inútiles.
 * 
 * @author jmarranz
 */
public class ItsNatBatikAppletScriptUtil
{
    public ItsNatBatikAppletScriptUtil()
    {
    }

    public static void fix(Object someScope)
    {
        // Este método es llamado desde código JavaScript de Batik
        // someScope serí un Scriptable si se llama desde JavaScript
        // doc debe ser la referencia "document" que sabemos que el documento DOM Java

        WindowWrapper winWrapper = (WindowWrapper)ScriptableObject.getTopLevelScope((Scriptable)someScope);
        SVGOMDocument svgDoc = (SVGOMDocument)WindowWrapperUtil.getWindow(winWrapper).getBridgeContext().getDocument();

        JSVGCanvasApplet canvas = JSVGCanvasApplet.getJSVGCanvasApplet(svgDoc);
        canvas.setWindowWrapper(winWrapper);

        fixInterpreter(winWrapper,svgDoc);

        registerXMLHttpRequest(winWrapper);

        canvas.createWrapperDocuments();

        ItsNatSVGOMDocumentBatik batikDoc = canvas.getItsNatSVGOMDocumentBatik();
        // Cambiamos "document" por el nuestro que exporta getItsNatDoc() via Java para obtener
        // el objeto gestor del documento de ItsNat.
        // Podríamos no recubrir con javaToJS y funcionaría igual excepto un detalle,
        // el typeof NO funciona, si es recubierto el typeof devuelve "object".

        Object batikDocJS = Context.javaToJS(batikDoc, winWrapper);
        winWrapper.put("document",winWrapper, batikDocJS);

        // Los cambios en el contexto son importantes, este método
        // es llamado desde el <script> de carga de ItsNat, afortunadamente
        // este es el último porque he comprobado que siguientes <script> darán
        // error a nivel de Batik porque la ejecución de los <script> en carga
        // se realiza desde un bucle y las modificaciones que hacemos aquí
        // fastidian dicho blucle. Otros <script> introducidos dinámicamente
        // no tendrán este problema.
    }

    protected static void registerXMLHttpRequest(WindowWrapper winWrapper)
    {
        // Registramos la clase XMLHttpRequest como clase ScriptableObject
        // De esta manera se comporta como un objeto JavaScript.
        // en el scope top, es decir en window, tal que podamos
        // hacer new window.XMLHttpRequest() como si fuera un objeto nativo.
        // El método defineClass en script que se suele indicar en la documentación
        // de Rhino no vale pues es para el modo consola y el importClass
        // parece que es para objetos Java no para objetos Java que registrados
        // como ScriptableObject.
        // Como someScope puede ser cualquier Scriptable pues a partir del mismo
        // obtenemos el scope del window.

        // La fuente de "inspiración" fue:
        // http://markmail.org/message/e4cnt3usou2wc3a4
        // "However, it seems to me that all you really want is the XMLHttpRequest object -
        // in which case you can just define that object in particular. All you have to do
        // is include E4XUtils.jar to your compile and add code like this:
        // Scriptable thescope = ScriptableObject.getTopLevelScope(sobj); if(thescope==null) { System.out.println("unable to obtain scope"); }else{ ScriptableObject.defineClass(thescope, xmlhttp.XMLHttpRequest.class); }
        // where 'sobj' is the ScriptableObject in which the script is evaluated."
        // El E4XUtils.jar se obtiene del ejemplo: http://www.ibm.com/developerworks/library/ws-ajax1/
        // cuyo XMLHttpRequest es el que usamos nosotros, aunque cambiado.

        defineClass(winWrapper,XMLHttpRequest.class);
    }

    protected static void defineClass(Scriptable scope,Class cls)
    {
        try { ScriptableObject.defineClass(scope,cls); }
        catch(Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
    }

    protected static RhinoInterpreterFixed fixInterpreter(WindowWrapper winWrapper,AbstractDocument doc)
    {
        // Solucionamos el problema de que en RhinoInterpreter el atributo
        // rhinoClassLoader es nulo porque estamos ejecutando un applet firmado
        // y el método getAccessControlContext() no tiene en cuenta este caso y da error al ser llamado.
        // Este problema está resuelto en la futura versión 1.8 (lo he comprobado en la snapshot)
        // Este fallo se maniesta por ejemplo en los métodos de window tal y como parseXML (usado por ItsNat),
        // getURL, postURL etc.
        // http://old.nabble.com/Batik-Applet-and-getURL-XMLHttpRequest--td18105942.html

        // Nota: si se quiere capturar posibles excepciones Java desde JavaScript, hacer
        // un try{ }catch(e) { if (e.javaException) ... } en donde javaException es el objeto Exception Java
        // si se hace e.javaException.printStackTrace(); saldrá por la consola.

        Window window = WindowWrapperUtil.getWindow(winWrapper);

        URL url = null;
        try { url = new URL(doc.getDocumentURI()); } // La inspección del código fuente de Batik muestra que el RhinoInterpreter es creado usando este URL
        catch(MalformedURLException ex) { ex.printStackTrace(); new RuntimeException(ex); }

        RhinoInterpreterFixed interpreter = RhinoInterpreterFixed.createRhinoInterpreterFixed(url);
        WindowUtil.changeInterpreter(window,interpreter);
        return interpreter;
    }
}
