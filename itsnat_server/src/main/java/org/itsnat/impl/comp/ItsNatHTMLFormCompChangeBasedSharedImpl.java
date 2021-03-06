/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.comp;

import java.io.Serializable;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientImpl;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.browser.web.webkit.BrowserWebKit;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.event.server.dom.domstd.ServerItsNatDOMStdEventImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLFormCompChangeBasedSharedImpl implements Serializable
{
    protected ItsNatHTMLFormCompChangeBased comp;

    public ItsNatHTMLFormCompChangeBasedSharedImpl(ItsNatHTMLFormCompChangeBased comp)
    {
        this.comp = comp;
    }

    public ItsNatHTMLFormComponentImpl getItsNatHTMLFormComponent()
    {
        return (ItsNatHTMLFormComponentImpl)comp;
    }

    public void init()
    {
    }

    public boolean isChangeEvent(String type,ClientDocumentImpl clientDoc)
    {
        if (type.equals("change"))
        {
            if (isIgnoreChangeEvent(clientDoc)) return false; // Nunca se ejecuta
            else return true;
        }
        else if (type.equals("blur"))
        {
            if (isBlurLikeChangeEvent(clientDoc)) return true; 
            else return false;
        }

        return false;
    }

    public boolean isChangeEvent(Event evt)
    {
        String type = evt.getType();
        if ((evt instanceof ServerItsNatDOMStdEventImpl) && type.equals("blur"))
            return false; // Al ser generado desde el servidor, el programador deber� lanzar primero el change y luego el blur, es decir, hacerlo BIEN, hay que tener en cuenta que con el evento del blur no habr� un par�metro "value" con el texto

        ClientDocumentImpl clientDoc = (ClientDocumentImpl)((ItsNatEvent)evt).getClientDocument();
        return isChangeEvent(type,clientDoc);
    }

    public void enableEventListenersByDoc()
    {
        ItsNatHTMLFormComponentImpl comp = getItsNatHTMLFormComponent();
        comp.enableEventListener("change");
    }

    public void enableEventListenersByClient(ItsNatCompNormalEventListenersByClientImpl clientListeners)
    {
        if (isBlurLikeChangeEvent(clientListeners.getClientDocument()))
        {
            ItsNatHTMLFormComponentImpl comp = getItsNatHTMLFormComponent();
            comp.enableEventListener("blur",clientListeners);
        }
    }

    public void processNormalEvent(Event evt)
    {
        if (isChangeEvent(evt))
        {
            // Ejecutado como respuesta al evento "change" en el navegador

            comp.handleEventOnChange(evt);

            // Oportunidad para los componentes derivados
            // de modificar el DOM en el servidor para que se manifieste
            // en el cliente pues no est� en modo "server updating from client",
            // por ejemplo para rechazar el cambio y restaurar el valor original
            // propagando al cliente.
            // El usuario puede hacer esto con un listener normal, pero necesitamos
            // ejecutar antes el posible rechazo antes de que se ejecuten los listeners
            // del usuario para que el usuario vea ya el DOM con el valor rechazado

            comp.postHandleEventOnChange(evt);
        }
    }

    public boolean isBlurLikeChangeEvent(ClientDocumentImpl clientDoc)
    {
        // ESTO ES ANTIGUO YA NO APLICA EN NAVEGADORES MODERNOS:                
        
        // En el caso de blur lanzado antes que el change hacemos que
        // el blur sea como un change actualizando el servidor
        // desde el cliente.
        
        // Es el caso de FireFox 2.0 y S60WEBKIT antiguos con WebKit 413 (en WebKit 525 funciona ya bien),
        // quiz�s se deba a que el WebKit es muy antiguo, 413 y 417 respectivamente y se decidi� cambiarlo despu�s.
        // Adem�s S60WEBKIT lanza 2 blurs cuando debe ser uno s�lo
        // El S40WebKit no tiene este problema pues empieza en 420
        // Consideramos el 420 como el primer WebKit sin este "fallo" pues
        // todos los dem�s navegadores soportados funcionan bien y tienen el 420 o mayor

        // En resumen: (webKitVersion < 420) && DOMUtilHTML.isHTMLTextAreaOrInputTextBox(formElem);        
        
        // Esto es muy �til en componentes editores "inplace"
        // pues el blur da la orden de quitar el elemento del �rbol (se quitar�a sin actualizar el servidor evitando lanzar el change).
        // Este problema ocurre (o nos importa) en <textarea> y en <input type="text|password|file"> (y en un caso especial de <select>),
        // es decir en los componentes asociados a este objeto "shared, en varios navegadores.
        // No es necesario obviamente en "hidden" pero en ese caso los eventos nunca se lanza.

        // Tambi�n se ha comprobado la necesidad de considerar blur como change en el caso de InputTextFormatted
        // cuando al editar cambia el formato (ej. en una fecha) tal que el blur
        // (que lo normal es que se ejecutara despu�s del change) cambia el formato
        // sin esto el blur restaura el formato del campo y se enviar�a despu�s via change
        // el valor en el formato inesperado (no el de edici�n). El change en estos casos es posible
        // que de error (valor rechazado) pero al menos el blur ha puesto el valor bueno, o bien el valor
        // se define dos veces.

        HTMLElement elem = comp.getHTMLElement();
        BrowserWeb browser = (BrowserWeb)clientDoc.getBrowser();
        /* if (browser.isBlurBeforeChangeEvent(elem)) 
            return true;
        */
        
        // Se ha detectado en la versi�n m�s actual de Chrome tanto en desktop como en Android en Julio de 2013, concretamente en desktop la versi�n
        // es 28.0.1500.72  y en Android  28.0.1500.94, que en un input text insertado via AJAX con un texto inicial, el eliminar TODO el texto (y perder el foco) NO dispara
        // el evento change. Ocurre en el ejemplo de input text del Feature Showcase        
        
        if ((browser instanceof BrowserWebKit) && ((BrowserWebKit)browser).isChangeEventNotFiredUseBlur(elem))           
            return true;
        
        return false;
    }

    public boolean isIgnoreChangeEvent(ClientDocumentImpl clientDoc)
    {
        return false; // Hubo muchas movidas hace tiempo, dejamos este resto por si acaso
    }

}
