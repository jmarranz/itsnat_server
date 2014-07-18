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
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByClientImpl;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.browser.web.webkit.BrowserWebKit;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.event.server.dom.ServerItsNatDOMEventImpl;
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
            if (isIgnoreChangeEvent(clientDoc)) return false;
            else return true;
        }
        else if (type.equals("blur"))
        {
            if (isBlurIsChangeEvent(clientDoc)) return true;
            else return false;
        }

        return false;
    }

    public boolean isChangeEvent(Event evt)
    {
        String type = evt.getType();
        if ((evt instanceof ServerItsNatDOMEventImpl) && type.equals("blur"))
            return false; // Al ser generado desde el servidor, el programador deberá lanzar primero el change y luego el blur, es decir, hacerlo BIEN, hay que tener en cuenta que con el evento del blur no habrá un parámetro "value" con el texto

        ClientDocumentImpl clientDoc = (ClientDocumentImpl)((ItsNatEvent)evt).getClientDocument();
        return isChangeEvent(type,clientDoc);
    }

    public void enableEventListenersByDoc()
    {
        ItsNatHTMLFormComponentImpl comp = getItsNatHTMLFormComponent();
        comp.enableEventListener("change");
    }

    public void enableEventListenersByClient(ItsNatCompDOMListenersByClientImpl clientListeners)
    {
        if (isBlurIsChangeEvent(clientListeners.getClientDocument()))
        {
            ItsNatHTMLFormComponentImpl comp = getItsNatHTMLFormComponent();
            comp.enableEventListener("blur",clientListeners);
        }
    }

    public void processDOMEvent(Event evt)
    {
        if (isChangeEvent(evt))
        {
            // Ejecutado como respuesta al evento "change" en el navegador

            comp.handleEventOnChange(evt);

            // Oportunidad para los componentes derivados
            // de modificar el DOM en el servidor para que se manifieste
            // en el cliente pues no está en modo "server updating from client",
            // por ejemplo para rechazar el cambio y restaurar el valor original
            // propagando al cliente.
            // El usuario puede hacer esto con un listener normal, pero necesitamos
            // ejecutar antes el posible rechazo antes de que se ejecuten los listeners
            // del usuario para que el usuario vea ya el DOM con el valor rechazado

            comp.postHandleEventOnChange(evt);
        }
    }

    public boolean isBlurIsChangeEvent(ClientDocumentImpl clientDoc)
    {
        // En el caso de blur lanzado antes que el change hacemos que
        // el blur sea como un change actualizando el servidor
        // desde el cliente.
        // Esto es muy útil en componentes editores "inplace"
        // pues el blur da la orden de quitar el elemento del árbol (se quitaría sin actualizar el servidor evitando lanzar el change).
        // Este problema ocurre (o nos importa) en <textarea> y en <input type="text|password|file"> (y en un caso especial de <select>),
        // es decir en los componentes asociados a este objeto "shared, en varios navegadores.
        // No es necesario obviamente en "hidden" pero en ese caso los eventos nunca se lanza.

        // También se ha comprobado la necesidad de considerar blur como change en el caso de InputTextFormatted
        // cuando al editar cambia el formato (ej. en una fecha) tal que el blur
        // (que lo normal es que se ejecutara después del change) cambia el formato
        // sin esto el blur restaura el formato del campo y se enviaría después via change
        // el valor en el formato inesperado (no el de edición). El change en estos casos es posible
        // que de error (valor rechazado) pero al menos el blur ha puesto el valor bueno, o bien el valor
        // se define dos veces.

        HTMLElement elem = comp.getHTMLElement();
        BrowserWeb browser = (BrowserWeb)clientDoc.getBrowser();
        if (browser.isBlurBeforeChangeEvent(elem)) 
            return true;
        else if ((browser instanceof BrowserWebKit) && ((BrowserWebKit)browser).isChangeEventNotFiredUseBlur(elem))
            return true;
        return false;
    }

    public abstract boolean isIgnoreChangeEvent(ClientDocumentImpl clientDoc);

}
