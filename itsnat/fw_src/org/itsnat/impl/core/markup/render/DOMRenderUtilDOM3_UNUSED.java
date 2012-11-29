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

package org.itsnat.impl.core.markup.render;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

/**
 *
 * @author jmarranz
 */
public class DOMRenderUtilDOM3_UNUSED
{

    /**
     * Creates a new instance of DOMRenderUtilDOM3_UNUSED
     */
    public DOMRenderUtilDOM3_UNUSED()
    {
    }

    public static String serialize(Document doc)
    {
        return serialize(doc,doc);
    }

    public static String serialize(Node node)
    {
        return serialize(node,node.getOwnerDocument());
    }

    public static String serialize(Node node,Document doc)
    {
        // POR AHORA NO SE USA (es un borrador para pruebas de todas formas)
        // PUES NO VALE PARA HTML porque es un serializador únicamente XML
        // Por ejemplo: elementos como <SCRIPT> los termina como <SCRIPT /> lo cual no vale en HTML antiguo (ni siquiera en navegadores nuevos)
        // http://mail-archives.apache.org/mod_mbox/xerces-j-users/200403.mbox/%3COF25B896FA.78EFE5E5-ON85256E68.0012EE41-85256E68.0017D161@ca.ibm.com%3E

        DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();
        if (!(node instanceof Document) || (node instanceof HTMLDocument))
        {
            lsSerializer.getDomConfig().setParameter("xml-declaration", Boolean.FALSE);
        }
        return lsSerializer.writeToString(node);
    }
}
