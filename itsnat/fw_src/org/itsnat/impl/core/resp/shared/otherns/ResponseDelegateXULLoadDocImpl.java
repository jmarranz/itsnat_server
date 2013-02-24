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

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateXULLoadDocImpl extends ResponseDelegateOtherNSLoadDocImpl
{

    /**
     * Creates a new instance of ResponseDelegateXULLoadDocImpl
     */
    public ResponseDelegateXULLoadDocImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public String getJavaScriptMIME()
    {
        return "application/x-javascript";
    }

    public void setScriptURLAttribute(Element scriptElem,String url)
    {
        DOMUtilInternal.setAttribute(scriptElem,"src",url);
    }

    protected void rewriteClientUIControlProperties(Element elem,boolean revertJSChanges,StringBuilder code)
    {
        boolean res = rewriteClientHTMLUIControlProperties(elem,revertJSChanges,code);
        if (res) return; // Era un elemento XHTML ya procesado

        // La lista de elementos y propiedades ha sido obtenida de JSRenderXULProperty
        // Como no sabemos lo suficiente de XUL por ahora ignoramos revertJSChanges
        // Hay que estudiar si por ejemplo listbox tiene una colección tipo "listitems"
        // al igual que "options" en HTML select con el fin de optimizar un poco.

        if (NamespaceUtil.isXULElement(elem))
        {
            ClientDocumentStfulImpl clientDoc = getClientDocumentStful();

            String localName = elem.getLocalName();
            if ("button".equals(localName))
            {
                processUIControlProperty(elem,"checkState",code,clientDoc);
                processUIControlProperty(elem,"checked",code,clientDoc);
            }
            else if ("checkbox".equals(localName))
            {
                processUIControlProperty(elem,"checked",code,clientDoc);
            }
            else if ("colorpicker".equals(localName))
            {
                processUIControlProperty(elem,"color",code,clientDoc);
            }
            else if ("datepicker".equals(localName))
            {
                processUIControlProperty(elem,"value",code,clientDoc);
            }
            else if ("listitem".equals(localName))
            {
                processUIControlProperty(elem,"selected",code,clientDoc);
            }
            else if ("menulist".equals(localName))
            {
                processUIControlProperty(elem,"label",code,clientDoc);
            }
            else if ("menuitem".equals(localName))
            {
                processUIControlProperty(elem,"checked",code,clientDoc);
            }
            else if ("prefpane".equals(localName))
            {
                processUIControlProperty(elem,"selected",code,clientDoc);
            }
            else if ("progressmeter".equals(localName))
            {
                processUIControlProperty(elem,"value",code,clientDoc);
            }
            else if ("radio".equals(localName))
            {
                processUIControlProperty(elem,"selected",code,clientDoc);
            }
            else if ("richlistitem".equals(localName))
            {
                processUIControlProperty(elem,"selected",code,clientDoc);
            }
            else if ("textbox".equals(localName))
            {
                processUIControlProperty(elem,"value",code,clientDoc);
            }
            else if ("tab".equals(localName))
            {
                processUIControlProperty(elem,"selected",code,clientDoc);
            }
            else if ("timepicker".equals(localName))
            {
                processUIControlProperty(elem,"value",code,clientDoc);
            }
            else if ("toolbarbutton".equals(localName))
            {
                processUIControlProperty(elem,"checkState",code,clientDoc);
                processUIControlProperty(elem,"checked",code,clientDoc);
            }
        }
    }


    public String getDocumentNamespace()
    {
        return NamespaceUtil.XUL_NAMESPACE;
    }

}
