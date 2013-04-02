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

package org.itsnat.impl.core.req.script;

import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentNoServerDocDefaultImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.resp.script.ResponseLoadScriptFrameworkImpl;

/**
 *
 * @author jmarranz
 */
public class RequestLoadScriptFrameworkImpl extends RequestLoadScriptImpl
{
    protected String scriptNameList;

    /**
     * Creates a new instance of RequestNormalLoadDocImpl
     */
    public RequestLoadScriptFrameworkImpl(String scriptNameList,ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);
        this.scriptNameList = scriptNameList;
    }

    public String getScriptNameList()
    {
        return scriptNameList;
    }

    @Override    
    public void processRequest(ClientDocumentStfulImpl clientDocStateless)
    {
        ClientDocumentNoServerDocDefaultImpl clientDoc = new ClientDocumentNoServerDocDefaultImpl(getItsNatSession());

        bindClientToRequest(clientDoc,false);  // El documento es nulo, no se vincula por tanto

        this.response = new ResponseLoadScriptFrameworkImpl(this);
        response.process();
    }

}
