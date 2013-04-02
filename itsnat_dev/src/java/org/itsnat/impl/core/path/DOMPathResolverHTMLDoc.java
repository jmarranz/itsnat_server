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

package org.itsnat.impl.core.path;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.SVGWebInfoImpl;
import org.itsnat.impl.core.doc.ItsNatHTMLDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author jmarranz
 */
public class DOMPathResolverHTMLDoc extends DOMPathResolver
{
    /** Creates a new instance of DOMPathResolverHTMLDoc */
    public DOMPathResolverHTMLDoc(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
    }

    public ItsNatHTMLDocumentImpl getItsNatHTMLDocument()
    {
        return (ItsNatHTMLDocumentImpl)clientDoc.getItsNatStfulDocument();
    }

    @Override
    protected Node getChildNodeFromStrPos(Node parentNode,String posStr)
    {
        // Ver comentarios en getNodeChildPosition(Node)
        ItsNatHTMLDocumentImpl itsNatDoc = getItsNatHTMLDocument();
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        if (parentNode == doc.getDocumentElement()) // <html>
        {
            if (posStr.equals("bo"))
                return doc.getBody();
            else if (posStr.equals("he"))
                return DOMUtilHTML.getHTMLHead(doc);
        }
        return super.getChildNodeFromStrPos(parentNode,posStr);
    }

    @Override
    protected String getNodeChildPosition(Node node)
    {
        // Esto lo hacemos para ser tolerantes con los elementos
        // que introducen algunas extensiones de los navegadores entre los elementos
        // <head> y <body> y probablemente scripts JS que intentan no ser muy intrusivos.
        // Por ejemplo FireBug <=1.5 inserta un elemento con id "_firebugConsole",
        // parece que la v1.6 ya no http://code.google.com/p/fbug/source/detail?r=6418
        // (nota: usar DOM Inspector pues FireBug no muestra sus nodos auxiliares)
        // Delicious Tools o AdBlock en Chrome insertaban un <style> entre <head> y <body>
        // en el caso de AdBlock afortunadamente después de <body> actualmente y Delicious Tools parece que ya no (?)
        // Otra alternativa sería eliminarlos en tiempo de carga pero por ej. la consola de FireBug es útil.
        HTMLDocument doc = getItsNatHTMLDocument().getHTMLDocument();
        if (node.getParentNode() == doc.getDocumentElement()) // <html>
        {
            if (node == doc.getBody())  // Primero porque es el más habitual y no hay que "buscar"
                return "bo";
            else if (node == DOMUtilHTML.getHTMLHead(doc))
                return "he";
        }
        return super.getNodeChildPosition(node);
    }
    
    public boolean isFilteredInClient(Node node)
    {
        SVGWebInfoImpl svgWebInfo = clientDoc.getSVGWebInfo();
        if (svgWebInfo == null)
        {
            return false;
        }
        else
        {
            // SVGWeb definido:
            // En teoría hasta después del evento onload no tiene sentido
            // (no funciona) acceder a los elementos SVG procesados por SVGWeb
            // por lo que este método suponemos que ha sido llamado tras el evento
            // onload cuando ya claramente se sabe si la página contiene SVGWeb
            // (y objeto SVGWebInfo está determinado, lo cual se hace en carga).

            // Los comentarios no son soportados (no reflejados en el DOM)
            // en un trozo de SVG procesado por SVGWeb
            if (node instanceof Comment)
                return svgWebInfo.isSVGNodeProcessedBySVGWebFlash(node);
            else
                return false;
        }
    }    
}
