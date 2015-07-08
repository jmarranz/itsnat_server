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

package org.itsnat.impl.core.resp.attachsrv;

import org.itsnat.impl.core.browser.web.webkit.BrowserWebKit;
import org.itsnat.impl.core.req.attachsrv.RequestAttachedServerImpl;
import org.itsnat.impl.core.resp.ResponseImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseAttachedServerImpl extends ResponseImpl
{

    /** Creates a new instance of ResponseNormalLoadDocImpl */
    public ResponseAttachedServerImpl(RequestAttachedServerImpl request)
    {
        super(request);
    }

    public RequestAttachedServerImpl getRequestAttachedServer()
    {
        return (RequestAttachedServerImpl)request;
    }

    protected boolean isAsyncScriptLoaded()
    {
        return (getClientDocument().getBrowser() instanceof BrowserWebKit);
    }
}
