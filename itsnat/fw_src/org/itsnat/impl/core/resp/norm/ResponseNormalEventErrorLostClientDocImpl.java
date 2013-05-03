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

package org.itsnat.impl.core.resp.norm;

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.listener.domstd.OnLoadBackForwardListenerImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseNormalEventErrorLostClientDocImpl extends ResponseNormalEventErrorImpl
{
    protected String lostClientId; // No se usa

    /**
     * Creates a new instance of ResponseNormalEventErrorLostClientDocImpl
     */
    public ResponseNormalEventErrorLostClientDocImpl(RequestNormalEventImpl request,String lostClientId)
    {
        super(request);

        this.lostClientId = lostClientId;
    }

    public void processEvent()
    {
        // Antes hemos comprobado que no es un evento unload y que la sesión
        // no es la que se ha perdido.

        /*
         Casos:
         1) Ocurre en Opera 9 y BlackBerryOld pudiendo ser un back o forward volviendo
         a una página que salió haciendo un unload y que por tanto
         ya no existe (la sesión está bien), el problema es que el navegador no recarga la página del servidor
         sino que usa la caché y no recarga la página desde el servidor,
         pero en lo demás es igual generando un evento load,
         de hecho añadimos un evento load especial precisamente
         para solucionar este problema (de que se vuelva a una página
         en el cliente eliminada en el servidor). En los demás navegadores
         esto no ocurre porque se recarga un documento nuevo.

         Si es otro tipo de evento puede ser un evento generado por JavaScript
         como parte del handler onload de la propia página (que se ejecutó antes del listener que
         envía al servidor el evento) y que también es escuchado también desde el servidor.
         También puede ser el caso de navegador que no emite el evento load al volver via back
         (típico de algunos browser móviles) y el usuario simplemente ha pulsado algo que emite evento al servidor.
         Por eso no chequeamos el tipo de evento.

         2) También ocurre cuando se invalidó en el servidor por código el documento (sin necesariamente cerrar la ventana)
         por alguna razón (el programador puede llamar a setInvalid del documento)
         el framework lo hace por ejemplo cuando hay demasiados documentos abiertos.

         Son dos situaciones diferentes, en Opera nos interesa recargar la página
         para conseguir que funcione el back/forward, en el otro caso no hay esa excusa
         y si queremos podemos provocar un error (suponemos evento unload o beforeunload excluidos)
         */

        RequestNormalEventImpl request = getRequestNormalEvent();
        Browser browser = getClientDocumentWithoutDocumentDefault().getBrowser();

        if (browser.isCachedBackForwardExecutedScripts())
        {
            // Esperamos "con seguridad" el evento especial load/DOMContentLoaded marcado con "itsnat_check_reload"
            // con la finalidad de recargar la página, cualquier otro evento lo ignoraremos (no reload)
            // para evitar varios reload, por ejemplo podría ser el caso de un evento "load" normal, dicho evento
            // ha podido enviarse después del evento especial pero antes de que el JavaScript de recarga tenga efecto,
            // evitamos por tanto generar otro JavaScript de recarga
            // El problema que introduce es que este reload añade una nueva
            // entrada a la caché del navegador.
            boolean loadBackForwardEvent = OnLoadBackForwardListenerImpl.isLoadBackForwardEvent(request);
            if (loadBackForwardEvent)
            {
                sendReload();
                return;
            }
            else if (request.isLoadEvent())
            {
                // No es un evento load tipo back/forward, lo ignoramos pues antes o después viene el que recarga la página
                return;
            }
            // Si llegamos aquí es un evento cualquiera como click, no sabemos exactamente qué ha pasado, delegaremos
            // a los global listeners para que decidan
        }

        if (request.isLoadEvent())
        {
            // No sabemos qué ha pasado ni que caso de navegador es, lo que
            // parece claro es que la página cliente quiere cargarse
            // Una posible explicación es que la página contenga iframes
            // y que al cargar los iframes hayamos superado el número de
            // documentos abiertos máximo por sesión y que por tanto
            // este evento load llega cuando el documento ha sido invalidado.
            // La solución es aumentar el número de documentos por sesión.
            // Para cualquier otro caso de posible cacheo no previsto, lo
            // propio es recargar.
            sendReload();
            return;
        }

        // Es un evento cualquiera de una página perdida (invalidada en el servidor)
        // Damos una oportunidad a los listeners globales a recargar la página
        // o a redireccionar a otra o lo que sea

        if (!processGlobalListeners())
        {
            // No hay global listeners:
            // Podríamos lanzar un error, pero queda más elegante recargar la página
            sendReload();
        }
    }

    public void sendReload()
    {
        Browser browser = getClientDocumentWithoutDocumentDefault().getBrowser();
        getItsNatServletResponse().addCodeToSend(OnLoadBackForwardListenerImpl.getReloadCode(browser));
    }
}
