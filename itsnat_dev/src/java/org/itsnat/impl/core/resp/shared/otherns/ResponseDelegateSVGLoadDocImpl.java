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

package org.itsnat.impl.core.resp.shared.otherns;

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserAdobeSVG;
import org.itsnat.impl.core.browser.BrowserBatik;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.browser.opera.BrowserOpera;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseDelegateSVGLoadDocImpl extends ResponseDelegateOtherNSLoadDocImpl
{

    /**
     * Creates a new instance of ResponseDelegateOtherNSLoadDocImpl
     */
    public ResponseDelegateSVGLoadDocImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public static ResponseDelegateSVGLoadDocImpl createResponseDelegateSVGLoadDoc(ResponseLoadStfulDocumentValid response)
    {
        Browser browser = response.getClientDocumentStful().getBrowser();
        if (browser instanceof BrowserMSIEOld)
            return new ResponseDelegateSVGLoadDocMSIEOldImpl(response);
        else if (browser instanceof BrowserAdobeSVG)
            return new ResponseDelegateSVGLoadDocAdobeSVGImpl(response);
        else if (browser instanceof BrowserBatik)
            return new ResponseDelegateSVGLoadDocBatikImpl(response);
        else if (browser instanceof BrowserOpera)
            return new ResponseDelegateSVGLoadDocOperaImpl(response);
        else
            return new ResponseDelegateSVGLoadDocDefaultImpl(response);
    }

    public String getJavaScriptMIME()
    {
        return "text/ecmascript";
    }

    public void setScriptURLAttribute(Element scriptElem,String url)
    {
        DOMUtilInternal.setAttribute(scriptElem,"xmlns:xlink","http://www.w3.org/1999/xlink");
        DOMUtilInternal.setAttribute(scriptElem,"xlink:href",url);
    }


    public String getDocumentNamespace()
    {
        return NamespaceUtil.SVG_NAMESPACE;
    }

}
