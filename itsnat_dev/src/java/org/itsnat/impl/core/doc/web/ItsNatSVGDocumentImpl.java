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
import org.itsnat.impl.comp.mgr.web.ItsNatSVGDocComponentManagerImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.template.otherns.ItsNatSVGDocumentTemplateVersionImpl;
import org.w3c.dom.Document;

/**
 * Editor XUL online:
 * http://ted.mielczarek.org/code/mozilla/xuledit/xuledit.xul
 *
 * @author jmarranz
 */
public class ItsNatSVGDocumentImpl extends ItsNatOtherNSDocumentImpl
{

    /** Creates a new instance of ItsNatSVGDocumentImpl */
    public ItsNatSVGDocumentImpl(Document doc,ItsNatSVGDocumentTemplateVersionImpl docLoader,Browser browser,String requestURL,ItsNatSessionImpl parentSession,boolean stateless)
    {
        super(doc,docLoader,browser,requestURL,parentSession,stateless);

        if (docLoader.isGeneratedDocumentFake(browser))
            this.scriptingEnabled = false; // MSIE no tiene ni idea de SVG, para ello están los plugins (ASV)
    }

    public ItsNatDocComponentManagerImpl createItsNatComponentManager()
    {
        return new ItsNatSVGDocComponentManagerImpl(this);
    }
}
