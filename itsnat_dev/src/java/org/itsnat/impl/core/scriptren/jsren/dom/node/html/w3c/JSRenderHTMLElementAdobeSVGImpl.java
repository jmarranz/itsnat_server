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

package org.itsnat.impl.core.scriptren.jsren.dom.node.html.w3c;

import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementAdobeSVGImpl extends JSRenderHTMLElementSVGPluginImpl
{
    public final static JSRenderHTMLElementAdobeSVGImpl SINGLETON = new JSRenderHTMLElementAdobeSVGImpl();

    /** Creates a new instance of JSRenderHTMLElementAdobeSVGImpl */
    public JSRenderHTMLElementAdobeSVGImpl()
    {
    }

    @Override    
    public String getCurrentStyleObject(String itsNatDocVar,String elemName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // window.getComputedStyle no está definido
        // Afortunadamente este método sólo se utiliza en documentos HTML
        return elemName + ".style"; // Por poner algo.
    }
}

