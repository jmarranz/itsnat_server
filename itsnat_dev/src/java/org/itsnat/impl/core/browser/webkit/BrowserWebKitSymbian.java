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

package org.itsnat.impl.core.browser.webkit;

import java.util.Map;

/*
 */

public abstract class BrowserWebKitSymbian extends BrowserWebKit
{
    public BrowserWebKitSymbian(String userAgent,int browserSubType)
    {
        super(userAgent,browserSubType);
    }

    public boolean isMobile()
    {
        return true;
    }

    public boolean hasBeforeUnloadSupportHTML()
    {
        return false;
    }

    public Map<String,String[]> getHTMLFormControlsIgnoreZIndex()
    {
        return null;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return false;
    }

}
