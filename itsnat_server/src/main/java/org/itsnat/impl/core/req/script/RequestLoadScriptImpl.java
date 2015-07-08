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

import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.req.RequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.resp.script.ResponseLoadScriptImpl;

/**
 *
 * @author jmarranz
 */
public abstract class RequestLoadScriptImpl extends RequestImpl
{
    /**
     * Creates a new instance of RequestNormalLoadDocImpl
     */
    public RequestLoadScriptImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);
    }

    public static RequestLoadScriptImpl createRequestLoadScript(ItsNatServletRequestImpl itsNatRequest)
    {
        // Carga del script de iniciación del document (el <script> del final del documento)
        // Puede ser al cargar una página "normal" o "remote"
        String scriptNameList = itsNatRequest.getAttrOrParamExist("itsnat_file");
        if (scriptNameList.equals("initial"))
            return new RequestLoadScriptInitialImpl(itsNatRequest);
        else
            return new RequestLoadScriptFrameworkImpl(scriptNameList,itsNatRequest);
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocumentReferrer()
    {
        return null;
    }

    public ResponseLoadScriptImpl getResponseLoadScript()
    {
        return (ResponseLoadScriptImpl)response;
    }

    protected boolean isMustNotifyEndOfRequestToSession()
    {
        // Así nos ahorramos en la carga de la página dos serializaciones inútiles,
        // tantas como scripts de carga
        return false;
    }

}

