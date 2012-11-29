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

package org.itsnat.impl.core.markup.parse;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.cyberneko.html.HTMLElements;
import org.cyberneko.html.HTMLTagBalancer;

/**
 *
 * @author jmarranz
 */
public class ItsNatNekoHTMLTagBalancerImpl extends HTMLTagBalancer
{
    public final HTMLElements.Element EMBED_DESC = new HTMLElements.Element(HTMLElements.EMBED, "EMBED",HTMLElements.Element.EMPTY, HTMLElements.BODY, null);
    public final HTMLElements.Element BASEFONT_DESC = new HTMLElements.Element(HTMLElements.BASEFONT, "BASEFONT", HTMLElements.Element.EMPTY, HTMLElements.HEAD, null);
    public final HTMLElements.Element KEYGEN_DESC = new HTMLElements.Element(HTMLElements.KEYGEN, "KEYGEN", HTMLElements.Element.EMPTY, HTMLElements.BODY, null);

    public final HTMLElements.Element SOURCE_DESC = new HTMLElements.Element(HTMLElements.UNKNOWN, "SOURCE", HTMLElements.Element.EMPTY, HTMLElements.BODY, null);  // Debería estar bajo <audio> o <video> pero si lo dejamos así (body) evitamos cambios imprevistos en el estándar
    public final HTMLElements.Element TRACK_DESC = new HTMLElements.Element(HTMLElements.UNKNOWN, "TRACK", HTMLElements.Element.EMPTY, HTMLElements.BODY, null);
    public final HTMLElements.Element COMMAND_DESC = new HTMLElements.Element(HTMLElements.UNKNOWN, "COMMAND", HTMLElements.Element.EMPTY, HTMLElements.BODY, null);
    public final HTMLElements.Element DEVICE_DESC = new HTMLElements.Element(HTMLElements.UNKNOWN, "DEVICE", HTMLElements.Element.EMPTY, HTMLElements.BODY, null);

    public ItsNatNekoHTMLTagBalancerImpl()
    {
    }

    public void emptyElement(QName elem, XMLAttributes attrs, Augmentations augs)
        throws XNIException
    {
        // Esto lo hacemos porque NekoHTML desde la versión 1.9.8
        // (ver http://nekohtml.sourceforge.net/changes.html)
        // se pasa de listo y decide que los elementos "container" tal y como
        // <p> o <div> no pueden ponerse como <p /> o <div /> (debería ponerse <p></p> y <div></div>)
        // ignorando el cerramiento y por tanto metiendo los elementos que siguen dentro
        // del tag y cerrando según el criterio de auto-balanceado de tags de NekoHTML.
        // Este comportamiento nos fastidia pues nos interesa tener elementos vacíos
        // que luego llenamos via DOM y nos obliga a recordar si ha de ponerse
        // como <p></p> o no, lo cual es un problema para un programador final.

        startElement(elem, attrs, augs);
        endElement(elem, augs);
    }

    protected HTMLElements.Element getElement(final QName elementName)
    {
        // NekoHTML considera EMBED con flags 0 el cual admite nodos hijo, el elemento <embed> no es estándar HTML 4,
        // pero en navegadores antiguos pudo admitir elementos hijo de forma similar a <object> o <applet>,
        // sin embargo actualmente no se considera con contenido más aún cuando está siendo
        // revitalizado para HTML 5.
        // http://msdn.microsoft.com/en-us/library/ms535245%28VS.85%29.aspx "This element does not require a closing tag"
        // http://www.w3.org/TR/html5/text-level-semantics.html#the-embed-element "Content model: Empty."
        // http://www.w3schools.com/html5/tag_embed.asp
        // http://www.html-reference.com/EMBED.asp
        // El problema es que NekoHTML le da como "flags" el valor 0 el cual admite contenido
        // y si se encuentra con un <embed ... > sin cerrar "/>" o con </embed>, mete los nodos siguientes dentro y añade un cierre,
        // es preciso evitar esto.
        // Idem con BASEFONT: http://www.w3.org/TR/REC-html40/sgml/loosedtd.html#basefont
        // Idem con KEYGEN reintroducido en HTML 5: http://www.w3schools.com/html5/tag_keygen.asp

        // De acuerdo con la lista de tags nuevos de HTML 5: http://www.w3schools.com/html5/html5_reference.asp
        // y considerando los que no están: http://www.whatwg.org/specs/web-apps/current-work/)
        // lo siguientes son tags vacíos ("Content model: Empty") que hay que considerar pues son tags desconocidos
        // y por defecto NekoHTML los considera  con contenido y si no se cierra bien con "/>" o con </tag> se meterían
        // los siguientes tags dentro:

        // SOURCE: http://www.whatwg.org/specs/web-apps/current-work/#the-source-element
        // TRACK: http://www.whatwg.org/specs/web-apps/current-work/#the-track-element
        // COMMAND: http://www.whatwg.org/specs/web-apps/current-work/#the-command
        // DEVICE: http://www.whatwg.org/specs/web-apps/current-work/#devices

        // No hay más pues por ejemplo WBR curiosamente ya lo incluye bien NekoHTML

        HTMLElements.Element elem = super.getElement(elementName);
        if (elem != HTMLElements.NO_SUCH_ELEMENT) // Definidos en NekoHTML pero mal (son vacíos)
        {
            if ("EMBED".equals(elem.name))
                return EMBED_DESC;
            else if ("BASEFONT".equals(elem.name))
                return BASEFONT_DESC;
            else if ("KEYGEN".equals(elem.name))
                return KEYGEN_DESC;
            return elem;
        }
        else // HTMLElements.NO_SUCH_ELEMENT  Desconocidos para NekoHTML pero que deben ser vacíos
        {
            String name = elementName.rawname.toUpperCase();
            if ("SOURCE".equals(name))
                return SOURCE_DESC;
            else if ("TRACK".equals(name))
                return TRACK_DESC;
            else if ("COMMAND".equals(name))
                return COMMAND_DESC;
            else if ("DEVICE".equals(name))
                return DEVICE_DESC;
            return elem; // Devolvemos NO_SUCH_ELEMENT que se tratará como un elemento tipo BLOCK
        }
    }
}
