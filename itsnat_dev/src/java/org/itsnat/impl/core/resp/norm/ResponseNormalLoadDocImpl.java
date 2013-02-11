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

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import java.util.Iterator;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.req.attachsrv.RequestAttachedServerLoadDocImpl;
import org.itsnat.impl.core.req.norm.RequestNormalLoadDocImpl;
import org.itsnat.impl.core.resp.attachsrv.ResponseAttachedServerLoadDocImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseNormalLoadDocImpl extends ResponseNormalLoadDocBaseImpl
{

    /**
     * Creates a new instance of ResponseNormalLoadDocImpl
     */
    public ResponseNormalLoadDocImpl(RequestNormalLoadDocImpl request)
    {
        super(request);
    }

    public static ResponseNormalLoadDocImpl createResponseNormalLoadDoc(RequestNormalLoadDocImpl request)
    {
        ItsNatDocumentImpl itsNatDoc = request.getItsNatDocument();
        if (itsNatDoc instanceof ItsNatStfulDocumentImpl)
            return ResponseNormalLoadStfulDocImpl.createResponseNormalLoadStfulDoc(request);
        else
            return new ResponseNormalLoadXMLDocImpl(request);
    }

    public RequestNormalLoadDocImpl getRequestNormalLoadDoc()
    {
        return (RequestNormalLoadDocImpl)request;
    }

    public ResponseAttachedServerLoadDocImpl getParentResponseAttachedServerLoadDoc()
    {
        RequestAttachedServerLoadDocImpl parentRequest = getRequestNormalLoadDoc().getParentRequestAttachedServerLoadDoc();
        if (parentRequest == null) return null; // Es lo normal
        return parentRequest.getResponseAttachedServerLoadDoc(); // no nulo
    }

    public void process()
    {
        ClientDocumentImpl clientDoc = getClientDocument();
        clientDoc.registerInSession(); // Pasa a ser accesible el documento (aunque no se puede tocar por otros hilos hasta que se libere el lock)
        
        ItsNatDocumentImpl itsNatDoc = getItsNatDocument();
        try
        {
            itsNatDoc.startLoading();
           
            super.process();
        }
        finally
        {
            itsNatDoc.endLoading();
        }
    }

    public void dispatchRequestListeners()
    {
        // Antes de despachar a los listeners normales, le damos una oportunidad
        // al referrer (si existe) de modificar nuestro documento

        ItsNatDocumentImpl itsNatDoc = getItsNatDocument();
        if (itsNatDoc.isReferrerPushEnabled()) // permitimos que el referrer, si existe, nos escriba en nuestro document o nos pase nuevos parámetros
        {
            ItsNatServletRequestImpl itsNatRequest = itsNatResponse.getItsNatServletRequestImpl();
            ItsNatStfulDocumentImpl itsNatDocRef = itsNatRequest.getItsNatStfulDocumentReferrer();
            if (itsNatDocRef != null) // Si referrer no hubiera estado activado devolvería null
            {
                synchronized(itsNatDocRef) // por si acaso aunque en teoría la página "fue abandonada"
                {
                    itsNatDocRef.dispatchReferrerRequestListeners(itsNatRequest,itsNatResponse);
                }
            }
        }

        ItsNatDocumentTemplateImpl docTemplate = itsNatDoc.getItsNatDocumentTemplateImpl();
        ItsNatServletImpl itsNatServlet = docTemplate.getItsNatServletImpl();
        Iterator<ItsNatServletRequestListener> iterator;

        iterator = itsNatServlet.getItsNatServletRequestListenerIterator();
        itsNatResponse.dispatchItsNatServletRequestListeners(iterator);

        iterator = docTemplate.getItsNatServletRequestListenerIterator();
        itsNatResponse.dispatchItsNatServletRequestListeners(iterator);
    }

}
