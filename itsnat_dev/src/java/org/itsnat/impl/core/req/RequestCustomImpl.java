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

package org.itsnat.impl.core.req;

import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;

/**
 *
 * @author jmarranz
 */
public class RequestCustomImpl extends RequestImpl implements ContainsItsNatStfulDocumentReferrer
{
    protected ItsNatStfulDocumentImpl itsNatDocReferrer;
    protected boolean stateless;
    
    /**
     * Creates a new instance of RequestNormalImpl
     */
    public RequestCustomImpl(ItsNatServletRequestImpl itsNatRequest,boolean stateless)
    {
        super(itsNatRequest);
        this.stateless = stateless;
    }

    public static RequestCustomImpl createRequestCustom(ItsNatServletRequestImpl itsNatRequest,boolean stateless)
    {
        return new RequestCustomImpl(itsNatRequest,stateless);
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocumentReferrer()
    {
        return itsNatDocReferrer;
    }

    public void setItsNatStfulDocumentReferrer(ItsNatStfulDocumentImpl itsNatDocReferrer)
    {
        this.itsNatDocReferrer = itsNatDocReferrer;
    }

    @Override    
    public void processRequest(ClientDocumentStfulImpl clientDocStateless)
    {
        // Casi siempre (¿siempre?) un request custom será una carga de página, aunque en teoría
        // podría ser una request AJAX a mano.
        // No usamos un response para que no se inicie el Writer se escriban cabeceras etc
        // pues se trata de que el programador haga lo que quiera casi como un request
        // normal de servlet, de hecho lo normal será que redireccione a ItsNatServlet.processRequest(...)
        
        // También tiene derecho al referrer, aunque si fuera una request AJAX a mano podría ser una metedura de pata pues el uso habitual de referrers es navegar entre páginas,
        // como lo habitual es redireccionar la request a una normal de carga del documento y como en esta
        // fase no vamos a redefinir el referrer pues no hay documento y hay riesgo de que sea una request AJAX
        // lo que hacemos es "pedir" el referrer pero NO hacer un popItsNatStfulDocument() que lo perdería
        // así, si se redirecciona a una request normal de carga, dicha request hará el popItsNatStfulDocument()
        // volviendo a definir el referrer en la request
        if (!stateless)
            setItsNatStfulDocumentReferrer( getItsNatSession().getReferrer().getItsNatStfulDocument() );
       
        ItsNatServletImpl itsNatServlet = itsNatRequest.getItsNatServletImpl();
        itsNatServlet.dispatchItsNatServletRequestListeners(itsNatRequest);
    }

    protected boolean isMustNotifyEndOfRequestToSession()
    {
        if (stateless)
            return false;        
        // Devolvemos true porque el referrer cambia en la sesión pero no hay otra
        // razón pues lo normal es que el request se reenvíe vía
        // ItsNatServlet.processRequest(ServletRequest request, ServletResponse response)
        // el cual creará un nuevo objetos request el cual serializaría la sesión si fuera necesario
        return true;
    }
}
