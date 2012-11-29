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

package org.itsnat.impl.comp.button.normal;

import org.itsnat.comp.button.normal.ItsNatHTMLInputSubmit;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.w3c.dom.html.HTMLInputElement;

/**
 * La principal finalidad de este componente es permitir cancelar
 * el envío del formulario al pulsar el botón
 * Aunque un tipo "reset" o "submit" generen un reset o submit ante un click dicho evento
 * no es recogido por el propio botón, es decir onreset y onsubmit no son llamados
 * (es el <form> el que lo recibe)
 *
 * @author jmarranz
 */
public class ItsNatHTMLInputSubmitImpl extends ItsNatHTMLInputButtonTextImpl implements ItsNatHTMLInputSubmit
{
    /**
     * Creates a new instance of ItsNatHTMLInputButtonTextImpl
     */
    public ItsNatHTMLInputSubmitImpl(HTMLInputElement element,NameValue[] artifacts,ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        init();
    }

    public String getExpectedType()
    {
        return "submit";
    }

}
