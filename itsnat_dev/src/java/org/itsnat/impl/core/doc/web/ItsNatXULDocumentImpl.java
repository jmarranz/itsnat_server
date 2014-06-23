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

package org.itsnat.impl.core.doc.web;

import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.comp.mgr.web.ItsNatXULDocComponentManagerImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.template.otherns.ItsNatXULDocumentTemplateVersionImpl;
import org.w3c.dom.Document;

/**
 *
 * @author jmarranz
 */
public class ItsNatXULDocumentImpl extends ItsNatOtherNSDocumentImpl
{

    /** Creates a new instance of ItsNatOtherNSDocumentImpl */
    public ItsNatXULDocumentImpl(Document doc,ItsNatXULDocumentTemplateVersionImpl docLoader,Browser browser,String requestURL,ItsNatSessionImpl parentSession,boolean stateless)
    {
        super(doc,docLoader,browser,requestURL,parentSession,stateless);
    }

    public ItsNatDocComponentManagerImpl createItsNatComponentManager()
    {
        return new ItsNatXULDocComponentManagerImpl(this);
    }
}
