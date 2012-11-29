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

package org.itsnat.impl.core.req.shared;

import org.itsnat.impl.core.servlet.ItsNatServletContextImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.doc.BoundElementDocContainerImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.req.RequestLoadDocImpl;

/**
 *
 * @author jmarranz
 */
public class RequestDelegateLoadDocImpl
{
    protected RequestLoadDocImpl parent;

    public RequestDelegateLoadDocImpl(RequestLoadDocImpl parent)
    {
        this.parent = parent;
    }

    public ItsNatSessionImpl getBoundParentItsNatSession()
    {
        String stdSessionParentId = parent.getAttrOrParam("itsnat_jsessionid_parent");
        if (stdSessionParentId != null)
        {
            ItsNatServletContextImpl context = parent.getItsNatServletRequest().getItsNatServletContext();
            return context.getItsNatSessionByStandardId(stdSessionParentId); // Puede ser null (no encuentra la sesión, raro)
        }
        else
        {
            return parent.getItsNatSession(); // La sesión del padre es la misma que la del hijo
        }
    }

    public BoundElementDocContainerImpl getBoundElementDocContainer(ItsNatStfulDocumentImpl parentItsNatDoc)
    {
        // A día de hoy no consideramos otra forma de vincular a un documento
        // padre diferente al mecanismo de iframes/objects/embeds.
        String elementId = parent.getAttrOrParamExist("itsnat_element_parent_id");
        String secNumber = parent.getAttrOrParamExist("itsnat_element_sec_num");

        // No provocaremos error si la vinculación falla, el programador
        // puede detectar que algo ha ido mal si el método ItsNatDocument.getParentItsNatDocument()
        // devuelve null

        synchronized(parentItsNatDoc) // No necesitamos sincronizar a su vez los padres porque lo que hacemos sólo afecta a este documento y el documento hijo todavía no está sincronizado
        {
            // Puede devolver null pero es raro.
            return parentItsNatDoc.getBoundElementDocContainer(elementId,Integer.parseInt(secNumber));
        }
    }
}
