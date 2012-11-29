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

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import org.apache.xml.serialize.ElementState;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.HTMLSerializer;
import org.apache.xml.serialize.HTMLdtd;
import org.itsnat.core.ItsNatException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ItsNatXercesHTMLSerializerOld extends HTMLSerializer
{
    protected boolean docFragment;
    protected boolean doingComment;
    protected boolean doingContent;

    protected String fUserXHTMLNamespace = null; // Es privado en la clase base, su valor debe ser null
    protected boolean _xhtml; // Es privado en la clase base, lo necesitamos porque redefinimos un método

    public ItsNatXercesHTMLSerializerOld(boolean xhtml,Writer writer, OutputFormat format,boolean docFragment)
    {
        super(xhtml,format);

        // Aunque admite serialización para XHTML, no es suficientemente correcta
        // usar sólo para serializar HTML
        if (xhtml) throw new ItsNatException("INTERNAL ERROR");

        setOutputCharStream(writer);
        this.docFragment = docFragment;

        this._xhtml = xhtml;
    }

    /**
     * La razón de este método y de los métodos comment() y content()
     * es la siguiente:
     * Cuando se serializa un comentario en solitario se usa el método
     * que usa un DocFragment, el problema es que en este caso
     * isDocumentState() devuelve true erróneamente ejecutándose
       de forma indeseada el siguiente código en el método comment():

       if ( isDocumentState() ) {
            if ( _preRoot == null )
                _preRoot = new Vector();
            _preRoot.addElement( fStrBuffer.toString() );
        }  else ...

        El devolver false en isDocumentState() no es la solución pues
        el método content() que es ejecutado durante comment() y ANTES
        del código anterior, necesita que isDocumentState() devuelva true
        para funcionar correctamente.
        Por tanto detectamos que hemos entrado en el proceso del comentario
        y que ya hemos llamado a content() en ese caso isDocumentState()
        devuelve false.
     */
    protected boolean isDocumentState()
    {
        if (docFragment && doingComment && !doingContent)
            return false;
        return super.isDocumentState();
    }

    public void comment( String text )
        throws IOException
    {
        this.doingComment = true;
        super.comment(text);
        this.doingComment = false;
    }

    protected ElementState content()
        throws IOException
    {
        this.doingContent = true;
        ElementState state = super.content();
        this.doingContent = false;
        return state;
    }

    /* Redefinimos este método totalmente porque no tenemos más remedio, para
       evitar un pequeño problema en atributos de elementos con namespace
       no XHTML (SVG etc) que se ponen en minúsculas en el caso de documentos HTML

     * Otros dos métodos startElement que hacen algo similar al parecer no son llamados.
     */
    protected void serializeElement( Element elem )
        throws IOException
    {
        Attr         attr;
        NamedNodeMap attrMap;
        int          i;
        Node         child;
        ElementState state;
        boolean      preserveSpace;
        String       name;
        String       value;
        String       tagName;

        tagName = elem.getTagName();
        state = getElementState();
        if ( isDocumentState() ) {
            // If this is the root element handle it differently.
            // If the first root element in the document, serialize
            // the document's DOCTYPE. Space preserving defaults
            // to that of the output format.
            if ( ! _started )
                startDocument( tagName );
        } else {
            // For any other element, if first in parent, then
            // close parent's opening tag and use the parnet's
            // space preserving.
            if ( state.empty )
                _printer.printText( '>' );
            // Indent this element on a new line if the first
            // content of the parent element or immediately
            // following an element.
            if ( _indenting && ! state.preserveSpace &&
                 ( state.empty || state.afterElement ) )
                _printer.breakLine();
        }
        preserveSpace = state.preserveSpace;

        // Do not change the current element state yet.
        // This only happens in endElement().

        // XHTML: element names are lower case, DOM will be different
        _printer.printText( '<' );
        if ( _xhtml )
            _printer.printText( tagName.toLowerCase(Locale.ENGLISH) );
        else
            _printer.printText( tagName );
        _printer.indent();

        // Lookup the element's attribute, but only print specified
        // attributes. (Unspecified attributes are derived from the DTD.
        // For each attribute print it's name and value as one part,
        // separated with a space so the element can be broken on
        // multiple lines.
        attrMap = elem.getAttributes();
        if ( attrMap != null ) {
            for ( i = 0 ; i < attrMap.getLength() ; ++i ) {
                attr = (Attr) attrMap.item( i );
                // ORIGINAL quitado para ItsNat: name = attr.getName().toLowerCase(Locale.ENGLISH);
                name = attr.getName(); // NUEVO para ItsNat. Así evitamos poner en minúsculas atributos que pueden tener mayúsculas en namespaces no HTML tal y como SVG
                value = attr.getValue();
                if ( attr.getSpecified() ) {
                    _printer.printSpace();
                    if ( _xhtml ) {
                        // XHTML: print empty string for null values.
                        if ( value == null ) {
                            _printer.printText( name );
                            _printer.printText( "=\"\"" );
                        } else {
                            _printer.printText( name );
                            _printer.printText( "=\"" );
                            printEscaped( value );
                            _printer.printText( '"' );
                        }
                    } else {
                        // HTML: Empty values print as attribute name, no value.
                        // HTML: URI attributes will print unescaped
                        if ( value == null ) {
                            value = "";
                        }
                        if ( !_format.getPreserveEmptyAttributes() && value.length() == 0 )
                            _printer.printText( name );
                        else if ( HTMLdtd.isURI( tagName, name ) ) {
                            _printer.printText( name );
                            _printer.printText( "=\"" );
                            _printer.printText( escapeURI( value ) );
                            _printer.printText( '"' );
                        } else if ( HTMLdtd.isBoolean( tagName, name ) )
                            _printer.printText( name );
                        else {
                            _printer.printText( name );
                            _printer.printText( "=\"" );
                            printEscaped( value );
                            _printer.printText( '"' );
                        }
                    }
                }
            }
        }
        if ( HTMLdtd.isPreserveSpace( tagName ) )
            preserveSpace = true;

        // If element has children, or if element is not an empty tag,
        // serialize an opening tag.
        // ORIGINAL quitado para ItsNat: if ( elem.hasChildNodes() || ! HTMLdtd.isEmptyTag( tagName ) ) {
        if ( elem.hasChildNodes() || ! isEmptyTag( tagName ) ) {  // NUEVO para ItsNat
            // Enter an element state, and serialize the children
            // one by one. Finally, end the element.
            state = enterElementState( null, null, tagName, preserveSpace );

            // Prevents line breaks inside A/TD
            if ( tagName.equalsIgnoreCase( "A" ) || tagName.equalsIgnoreCase( "TD" ) ) {
                state.empty = false;
                _printer.printText( '>' );
            }

            // Handle SCRIPT and STYLE specifically by changing the
            // state of the current element to CDATA (XHTML) or
            // unescaped (HTML).
            if ( tagName.equalsIgnoreCase( "SCRIPT" ) ||
                 tagName.equalsIgnoreCase( "STYLE" ) ) {
                if ( _xhtml ) {
                    // XHTML: Print contents as CDATA section
                    state.doCData = true;
                } else {
                    // HTML: Print contents unescaped
                    state.unescaped = true;
                }
            }
            child = elem.getFirstChild();
            while ( child != null ) {
                serializeNode( child );
                child = child.getNextSibling();
            }
            endElementIO( null, null, tagName );
        } else {
            _printer.unindent();
            // XHTML: Close empty tag with ' />' so it's XML and HTML compatible.
            // HTML: Empty tags are defined as such in DTD no in document.
            if ( _xhtml )
                _printer.printText( " />" );
            else
                _printer.printText( '>' );
            // After element but parent element is no longer empty.
            state.afterElement = true;
            state.empty = false;
            if ( isDocumentState() )
                _printer.flush();
        }
    }

    public static boolean isEmptyTag(String tagName)
    {
        boolean res = HTMLdtd.isEmptyTag( tagName );
        if (res) return res;
        // Estudiamos ahora elementos introducidos en HTML 5 que son vacíos y que no están
        // en HTMLdtd. http://www.whatwg.org/specs/web-apps/current-work/ ("Content model: Empty")

        // Esto es importante porque de otra manera por ejemplo un <embed /> es serializado
        // como <embed></embed>, los navegadores W3C no tienen problema con ésto pero a MSIE se le atraganta
        // y considera </embed> como un elemento desconocido. El problema de <embed> es que sí
        // es válido en MSIE 6.
        // Esto no soluciona el problema de los elementos de HTML 5 con contenido que son desconocidos
        // para MSIE 6, pues el cierre </tag> se considerará de todas formas como un elemento más (desconocido). Al menos
        // resolvemos el <embed> y ya que estamos los demás tipo "empty".
        String tagNameUpper = tagName.toUpperCase();
        if ("WBR".equals(tagNameUpper) || "EMBED".equals(tagNameUpper) || "SOURCE".equals(tagNameUpper) ||
            "TRACK".equals(tagNameUpper) || "KEYGEN".equals(tagNameUpper) || "COMMAND".equals(tagNameUpper) ||
            "DEVICE".equals(tagNameUpper))
            return true;
        return false;
    }
}
