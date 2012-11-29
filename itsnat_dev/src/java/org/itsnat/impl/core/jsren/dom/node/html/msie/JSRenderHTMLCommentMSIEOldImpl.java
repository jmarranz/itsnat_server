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

package org.itsnat.impl.core.jsren.dom.node.html.msie;

import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.browser.BrowserMSIEPocket;
import org.itsnat.impl.core.jsren.dom.node.JSRenderCommentImpl;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderHTMLCommentMSIEOldImpl extends JSRenderCommentImpl
{

    /** Creates a new instance of JSRenderHTMLCommentMSIEOldImpl */
    public JSRenderHTMLCommentMSIEOldImpl()
    {
    }

    public static JSRenderHTMLCommentMSIEOldImpl getJSRenderHTMLCommentMSIEOld(BrowserMSIEOld browser)
    {
        if (browser instanceof BrowserMSIEPocket)
            return JSRenderHTMLCommentMSIEPocketImpl.SINGLETON;
        else
            return JSRenderHTMLCommentMSIE6Impl.SINGLETON;
    }
}
