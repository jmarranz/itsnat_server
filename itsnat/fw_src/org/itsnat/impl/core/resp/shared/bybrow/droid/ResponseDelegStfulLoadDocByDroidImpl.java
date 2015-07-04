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
package org.itsnat.impl.core.resp.shared.bybrow.droid;

import org.itsnat.impl.core.resp.shared.ResponseDelegateStfulDroidLoadDocImpl;
import org.itsnat.impl.core.resp.shared.bybrow.web.ResponseDelegStfulLoadDocByBrowserImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegStfulLoadDocByDroidImpl extends ResponseDelegStfulLoadDocByBrowserImpl
{
    public ResponseDelegStfulLoadDocByDroidImpl(ResponseDelegateStfulDroidLoadDocImpl parent)
    {
        super(parent);
    }

    public String getOnInitScriptContentCodeFixDOMCode()
    {
        return null;
    }

}
