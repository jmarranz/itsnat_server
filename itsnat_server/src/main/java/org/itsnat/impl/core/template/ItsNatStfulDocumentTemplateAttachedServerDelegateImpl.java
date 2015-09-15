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

package org.itsnat.impl.core.template;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.req.attachsrv.RequestAttachedServerLoadDocImpl;
import org.itsnat.impl.core.req.norm.RequestNormalLoadDocAttachedServerImpl;
import org.itsnat.impl.core.req.norm.RequestNormalLoadDocImpl;
import org.itsnat.impl.core.servlet.ItsNatServletContextImpl;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDocumentTemplateAttachedServerDelegateImpl extends MarkupTemplateDelegateImpl
{
    protected boolean templateAlreadyUsed = false;

    public ItsNatStfulDocumentTemplateAttachedServerDelegateImpl(ItsNatStfulDocumentTemplateAttachedServerImpl parent)
    {
        super(parent);
    }

    @Override
    public boolean isItsNatTagsAllowed()
    {
        // No tiene sentido usar <itsnat:include> <itsnat:comment> etc
        return false;
    }

    @Override
    public MarkupSourceImpl getMarkupSource(RequestNormalLoadDocImpl request)
    {
        RequestAttachedServerLoadDocImpl parentRequest =
                ((RequestNormalLoadDocAttachedServerImpl)request).getParentRequestAttachedServerLoadDoc();
        String clientMarkup = parentRequest.getClientMarkup();
        return new MarkupSourceStringMarkupImpl(clientMarkup);
    }

    @Override
    public Object getSource()
    {
        return null;
    }

    @Override
    public MarkupTemplateVersionImpl getNewestMarkupTemplateVersion(MarkupSourceImpl source,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        // Cada request es siempre un MarkupTemplateVersionImpl nuevo.
        this.templateAlreadyUsed = true;

        long currentTimeStamp = source.getCurrentTimestamp(request,response);
        InputSource inputSource = source.createInputSource(request,response);
        ItsNatStfulDocumentTemplateVersionImpl templateVer =
                (ItsNatStfulDocumentTemplateVersionImpl)parent.createMarkupTemplateVersion(inputSource,currentTimeStamp,request,response);

        ItsNatServletContextImpl context = parent.getItsNatServletImpl().getItsNatServletContextImpl();
        if (context.isSessionReplicationCapable()) // Sólo lo salvamos cuando es estrictamente necesario pues ocupa bastante memoria pues contiene el markup original en texto
            templateVer.setMarkupSourceStringMarkup((MarkupSourceStringMarkupImpl)source);

        return templateVer;
    }

    @Override
    public MarkupTemplateVersionImpl getNewestMarkupTemplateVersion(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        throw new ItsNatException("INTERNAL ERROR");
    }

    @Override
    public boolean isTemplateAlreadyUsed()
    {
        return templateAlreadyUsed;
    }

    @Override
    public boolean canVersionBeSharedBetweenDocs()
    {
        return false; // Sólo estará asociado a un documento
    }
}
