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
import org.itsnat.impl.core.servlet.ItsNatServletImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDocumentTemplateAttachedServerImpl extends ItsNatStfulDocumentTemplateImpl
{

    /** Creates a new instance of ItsNatStfulDocumentTemplateImpl */
    public ItsNatStfulDocumentTemplateAttachedServerImpl(String name,String mime,ItsNatServletImpl servlet)
    {
        super(name,mime,null,servlet);

        this.fastLoadMode = false; // No tiene sentido que sea true pues el template inicial ya está renderizado en el cliente

        // Podríamos imponer que el modo events (stateful) sea obligatorio, pero quien sabe
        // aunque no haya eventos el documento del cliente ha llegado al servidor
        // y eso puede ser útil aunque sea con el fin de copiar la web del cliente.
        // Algo similar con "scripting enable", el documento del cliente
        // llega de todas formas al servidor.
    }

    public void setFastLoadMode(boolean fastLoadMode)
    {
        if (fastLoadMode) throw new ItsNatException("Fast load mode has no sense in attached server mode");

        super.setFastLoadMode(false);
    }

    public MarkupTemplateDelegateImpl createMarkupTemplateDelegate(MarkupSourceImpl source)
    {
        // source es necesariamente nulo
        if (source != null) throw new ItsNatException("INTERNAL ERROR");
        return new ItsNatStfulDocumentTemplateAttachedServerDelegateImpl(this);
    }

    public ItsNatStfulDocumentTemplateVersionImpl getNewestItsNatStfulDocumentTemplateVersion(MarkupSourceStringMarkupImpl source,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        return (ItsNatStfulDocumentTemplateVersionImpl)delegate.getNewestMarkupTemplateVersion(source,request,response);
    }
}
