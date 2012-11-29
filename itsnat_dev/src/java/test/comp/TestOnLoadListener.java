/*
 * OnUnloadListener.java
 *
 * Created on 7 de noviembre de 2006, 18:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.ItsNatSession;
import java.lang.ref.WeakReference;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import test.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestOnLoadListener extends TestBaseHTMLDocument implements EventListener
{
    /** Creates a new instance of OnUnloadListener */
    public TestOnLoadListener(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
    }

    public static boolean isAlreadyLoaded(ItsNatHTMLDocument itsNatDoc)
    {
        /* Sirve para probar si podemos evitar que se cargue dos veces
         la misma página para la misma sesión y así testear si se puede evitar el problema
         de la "conversación". Sólo lo podemos hacer en el load de la posible
         copia porque en el Firefox al pulsar el botón reload se llama
         al servidor antes de ejecutar el unload de la página actual
         cargada por lo que sólo sabremos si es un reload o nueva carga cuando en el evento load
         del nuevo documento se comprueba que en la otra página se llamó o no al unload
         pues en reload inmediatamente después de llamar al servidor para recargar
         se ejecuta el unload de la actual (si es nueva carga no estará invalidada la original)
         En reload con el MSIE se ejecuta el unload antes de llamar al servidor.
        */

        // Inhibimos, hay navegadores que no generan el evento "unload" siempre
        if (true) return false;


        ItsNatSession session = itsNatDoc.getClientDocumentOwner().getItsNatSession();
        WeakReference docRef = (WeakReference)session.getUserValue(itsNatDoc.getItsNatDocumentTemplate().getName());
        if (docRef == null)
            return false;
        ItsNatDocument itsNatDocReg = (ItsNatDocument)docRef.get();
        if (itsNatDocReg == null)
            return false;
        if (itsNatDocReg.isInvalid()) // Ha sido descargado aunque el garbage collector no se lo ha llevado todavía
            return false;
        return true;
    }

    public static void registerToAvoidConcurrentLoad(ItsNatHTMLDocument itsNatDoc)
    {
        ItsNatSession session = itsNatDoc.getClientDocumentOwner().getItsNatSession();
        session.setUserValue(itsNatDoc.getItsNatDocumentTemplate().getName(),new WeakReference(itsNatDoc));
    }

    public void handleEvent(Event evt)
    {
        // Inhibimos, hay navegadores que no generan el evento "unload" siempre
        if (true) return;

        ItsNatDOMStdEvent itsNatEvt = (ItsNatDOMStdEvent)evt;
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatEvt.getItsNatDocument();

        if (isAlreadyLoaded(itsNatDoc))
            throw new RuntimeException("Document is already loaded by this session");
        registerToAvoidConcurrentLoad(itsNatDoc);
    }

}
