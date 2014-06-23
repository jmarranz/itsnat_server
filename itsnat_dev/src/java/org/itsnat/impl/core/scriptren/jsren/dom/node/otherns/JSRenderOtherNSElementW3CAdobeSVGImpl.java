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

package org.itsnat.impl.core.scriptren.jsren.dom.node.otherns;

import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
 *
 * @author jmarranz
 */
public class JSRenderOtherNSElementW3CAdobeSVGImpl extends JSRenderOtherNSElementW3CSVGPluginImpl
{
    public static final JSRenderOtherNSElementW3CAdobeSVGImpl SINGLETON = new JSRenderOtherNSElementW3CAdobeSVGImpl();

    /**
     * Creates a new instance of JSRenderOtherNSElementW3CImpl
     */
    public JSRenderOtherNSElementW3CAdobeSVGImpl()
    {
    }

    public String getEncoding(ItsNatStfulDocumentImpl itsNatDoc)
    {
        // En ASV v6 el parseXML no tolera el <?xml...> y además no es necesario
        // porque al pasar el "document" como parámetro toda información que falte (el encoding)
        // la tendrá el documento.
        return null;
    }
}
