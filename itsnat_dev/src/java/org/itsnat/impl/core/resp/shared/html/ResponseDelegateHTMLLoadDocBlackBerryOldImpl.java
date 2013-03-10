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

package org.itsnat.impl.core.resp.shared.html;


import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseDelegateHTMLLoadDocBlackBerryOldImpl extends ResponseDelegateHTMLLoadDocW3CImpl
{
    public ResponseDelegateHTMLLoadDocBlackBerryOldImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public void dispatchRequestListeners()
    {
        super.dispatchRequestListeners();

        // Para el back/forward podríamos aplicar el document.body.onclick ... reload
        // pero el evento unload NO se lanza en BlackBerry aunque lo diga la documentación oficial.
    }

    public void processResponse()
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        if (clientDoc.isScriptingEnabled())
            if (!checkClientJavaScriptEnabled())
                return;

        super.processResponse();
    }

    public boolean checkClientJavaScriptEnabled()
    {
        // Esto lo hacemos porque de fábrica el navegador de la BlackBerry tiene
        // el JavaScript (y los alert) desactivados.
        // A partir de JDE 5.0 (Storm 2 por ejemplo) ya biene activado por defecto.
        // Ciertamente cuando se pulsa un link con un href con javascript se pregunta
        // al usuario pero si es un botón no pues el listener fue asociado via addEventListener,
        // el usuario creerá que la web no funciona.

        ItsNatServletRequestImpl request = getResponseLoadDoc().getItsNatServletResponse().getItsNatServletRequestImpl();
        if (request.getHeader("accept").indexOf("application/x-javascript") == -1)
        {
            StringBuilder docMarkup = new StringBuilder();

            docMarkup.append("<html><head></head><body>");
            docMarkup.append("<h2>This page requires JavaScript and is disabled in your device</h2>");
            docMarkup.append("<p>To enable do the following:<p> ");
            docMarkup.append("<p><code>Options->Browser Configuration->Enable 'Support JavaScript' and 'Allow JavaScript popups'->Save Options</code></p>");
            docMarkup.append("<p>Then press <code>Refresh</code> to load again</p>");
            docMarkup.append("</body></html>");

            getResponseLoadDoc().sendMarkupToClient(docMarkup.toString());

            return false;
        }
        else return true;
    }

    @Override
    public String serializeDocument()
    {
        // Todo esto se debe a una tontería de la BlackBerry, los comentarios
        // SON FILTRADOS cuando se carga la página en la BlackBerry Bold (JDE 4.6)
        // seguramente para ahorrar memoria. Sin embargo cuando son insertados
        // via DOM o via innerHTML NO son filtrados y se reflejan en el árbol DOM.
        // La estrategia podría ser evitar enviar comentarios al cliente y/o
        // filtrarlos en el cálculo de paths. Sin embargo como esto sólo ocurre
        // en carga, hacemos el siguiente HACK: sustituimos los comentarios
        // por <style name="itsnat_fake_comm"></style> tal que luego
        // un script los sustituya por los comentarios originales.
        // He comprobado que los <style></style> es como si no existieran visualmente,
        // no hace falta display:none, la alternativa es <span> pero si se insertaran
        // dentro del <head> (caso de comentarios en el head) los pone debajo del <body>
        // distorsionando el cálculo de paths, los <style> valen en <head> y en <body>,
        // y en aquellos lugares en donde no vale son filtrados (se hace en la normalización del documento)
        // Esta técnica es tolerante respecto al futuro si dejaran de filtrarse.

        ResponseDelegateHTMLLoadDocFixFilteredCommentsImpl filter = new ResponseDelegateHTMLLoadDocFixFilteredCommentsImpl(this);
        filter.preSerializeDocument();

        String docMarkup = super.serializeDocument();

        docMarkup = filter.postSerializeDocument(docMarkup);

        return docMarkup;
    }
}
