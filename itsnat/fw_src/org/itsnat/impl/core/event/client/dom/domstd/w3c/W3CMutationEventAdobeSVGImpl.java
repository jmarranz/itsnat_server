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

package org.itsnat.impl.core.event.client.dom.domstd.w3c;

import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.Element;

/**
 * Sólo tiene sentido en Adobe SVG Viewer v6, la v3 no soporta
 * mutation events.
 *
 * @author jmarranz
 */
public class W3CMutationEventAdobeSVGImpl extends W3CMutationEventImpl
{
    protected Short attrChange;

    /**
     * Creates a new instance of W3CMutationEventImpl
     */
    public W3CMutationEventAdobeSVGImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);
    }

    public short getAttrChange()
    {
        // Redefinimos porque el atributo "attrChange" NO se define
        // en ASV v6 (la v3 ni siquiera tiene mutation events),
        // lo tenemos que deducir nosotros en el cliente parcialmente
        // y aquí rematamos.
        // Notas: el atributo prevValue suele ser erróneo y tanto
        // el prevValue como newValue no son correctos en el caso REMOVAL
        // afortunadamente podemos vivir con ello
        if (attrChange == null)
        {
            short attrChangeVal = super.getAttrChange();
            if (attrChangeVal == MODIFICATION)
            {
                // En el cliente no podemos distinguir entre MODIFICATION y ADDITION
                Element elem = (Element)getTarget();
                if (!elem.hasAttribute(getAttrName()))
                    attrChangeVal = ADDITION; // No es una modificación pues el atributo no está en el servidor
            }
            this.attrChange = new Short(attrChangeVal);
        }

        return attrChange.shortValue();
    }

}
