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

package org.itsnat.impl.core.req.norm;

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.BoundElementDocContainerImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocSynchronizerImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.req.attachsrv.RequestAttachedServerLoadDocImpl;
import org.itsnat.impl.core.req.shared.RequestDelegateLoadDocImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalLoadDocImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateImpl;


/**
 *
 * @author jmarranz
 */
public abstract class RequestNormalLoadDocImpl extends RequestNormalLoadDocBaseImpl
{
    protected ItsNatDocumentTemplateImpl docTemplate;
    protected RequestDelegateLoadDocImpl delegate;
    
    /**
     * Creates a new instance of RequestNormalLoadDocImpl
     */
    public RequestNormalLoadDocImpl(ItsNatDocumentTemplateImpl docTemplate,ItsNatServletRequestImpl itsNatRequest,boolean stateless)
    {
        super(itsNatRequest,stateless);

        this.docTemplate = docTemplate;
        this.delegate = new RequestDelegateLoadDocImpl(this);
    }

    public ResponseNormalLoadDocImpl getResponseNormalLoadDoc()
    {
        return (ResponseNormalLoadDocImpl)response;
    }

    @Override    
    public void processRequest(ClientDocumentStfulImpl clientDocStateless)
    {

/*
HttpServletRequest request = (HttpServletRequest)itsNatRequest.getServletRequest();
Enumeration enumera = request.getHeaderNames();
while(enumera.hasMoreElements())
{
    String name = (String)enumera.nextElement();
    String value = request.getHeader(name);
    System.out.println(name + ":" + value);
    //value = "";
}
System.out.println();
 */

        final ItsNatDocumentImpl itsNatDoc = docTemplate.loadItsNatDocument(this);

        ItsNatStfulDocumentImpl parentItsNatDoc = bindParentItsNatDocument(itsNatDoc); // Necesario antes de sincronizar el hijo
        if (parentItsNatDoc == null) // Debe ser null porque no tiene sentido que un iframe/object/embed/applet vinculado al padre reciba el referrer de la página anterior pues en este caso el iframe no se ha cargado por navegación sino a través del documento padre
        {
            if (!isStateless())
                setItsNatStfulDocumentReferrer( getItsNatSession().getReferrer().popItsNatStfulDocument() );  // Aunque este documento no sea AJAX podría recibir un referrer de un documento anterior AJAX
        }

        ItsNatDocSynchronizerImpl syncTask = new ItsNatDocSynchronizerImpl()
        {
            protected void syncMethod()
            {
                processThreadSync(itsNatDoc);
            }
        };
        syncTask.exec(itsNatDoc);
    }

    public void processThreadSync(ItsNatDocumentImpl itsNatDoc)
    {
        final ClientDocumentImpl clientDoc = itsNatDoc.getClientDocumentOwnerImpl();

        bindClientToRequest(clientDoc);

        try
        {
            this.response = ResponseNormalLoadDocImpl.createResponseNormalLoadDoc(this);
            response.process();
        }
        catch(RuntimeException ex)
        {
            itsNatDoc.setInvalid();
            throw ex;
        }
        finally
        {
            unbindRequestFromDocument();
        }
    }

    public ItsNatStfulDocumentImpl bindParentItsNatDocument(ItsNatDocumentImpl itsNatDoc)
    {
        BoundElementDocContainerImpl bindInfo = getBoundElementDocContainer();
        if (bindInfo == null) return null;

        ItsNatStfulDocumentImpl parentItsNatDoc = bindInfo.getItsNatStfulDocument();
        synchronized(parentItsNatDoc) // Yo creo que no es necesario sincronizar pero por si acaso. Afortunadamente itsNatDoc NO está sincronizado
        {
            bindInfo.setContentItsNatDocument(itsNatDoc);
        }

        return parentItsNatDoc;
    }

    public BoundElementDocContainerImpl getBoundElementDocContainer()
    {
        ItsNatSessionImpl sessionParent = delegate.getBoundParentItsNatSession();
        if (sessionParent == null) return null;
        
        String docParentId = getAttrOrParam("itsnat_doc_parent_id");
        if (docParentId == null) return null; // No tiene documento padre.

        ItsNatStfulDocumentImpl parentItsNatDoc = sessionParent.getItsNatStfulDocumentById(docParentId);
        if (parentItsNatDoc == null) return null;

        return delegate.getBoundElementDocContainer(parentItsNatDoc);
    }

    public abstract RequestAttachedServerLoadDocImpl getParentRequestAttachedServerLoadDoc();
}
