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
package org.itsnat.impl.core.resp.norm;

import java.io.IOException;
import java.io.Writer;
import org.itsnat.impl.core.req.norm.RequestNormal;
import org.itsnat.impl.core.req.norm.RequestNormalLoadDocBaseImpl;
import org.itsnat.impl.core.resp.ResponseLoadDocImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseNormalLoadDocBaseImpl extends ResponseLoadDocImpl implements ResponseNormal
{
    public ResponseNormalLoadDocBaseImpl(RequestNormalLoadDocBaseImpl request)
    {
        super(request);
    }

    public RequestNormalLoadDocBaseImpl getRequestNormalLoadDocBase()
    {
        return (RequestNormalLoadDocBaseImpl)request;
    }

    public RequestNormal getRequestNormal()
    {
        return (RequestNormal)request;
    }

    @Override
    protected Writer initWriter() throws IOException 
    {
        if (getRequestNormalLoadDocBase().isStateless())
            return null;
        return super.initWriter(); 
    }    
    
    @Override    
    protected void prepareResponse()    
    {
        if (getRequestNormalLoadDocBase().isStateless())
            return; // Las cabeceras etc se definen en la fase del evento
        super.prepareResponse();         
    }
}
