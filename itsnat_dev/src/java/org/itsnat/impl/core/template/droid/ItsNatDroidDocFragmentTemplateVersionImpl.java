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

package org.itsnat.impl.core.template.droid;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.template.MarkupTemplateVersionDelegateImpl;
import org.itsnat.impl.core.template.ItsNatDocFragmentTemplateVersionImpl;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatDroidDocFragmentTemplateVersionImpl extends ItsNatDocFragmentTemplateVersionImpl
{
    /**
     * Creates a new instance of ItsNatDroidDocFragmentTemplateVersionImpl
     */
    public ItsNatDroidDocFragmentTemplateVersionImpl(ItsNatDroidDocFragmentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        super(docTemplate,source,timeStamp,request,response);
        
        this.templateDoc = null; // Para que no se vuelva a usar y para salvar memoria
    }

    
    public Element getContainerElement()
    {
        // El nodo padre puede ser cualquiera, por ejemplo <root>
        // son los nodos hijos los que constituyen el fragmento
        // Hay que recordar que tras el crear el DocumentFragment el root queda vacío.        
        return getDocument().getDocumentElement();       
    }    

    protected MarkupTemplateVersionDelegateImpl createMarkupTemplateVersionDelegate()
    {
        return new StfulDroidTemplateVersionDelegateImpl(this);
    }

}
