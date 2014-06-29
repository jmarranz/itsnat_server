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

package org.itsnat.impl.core.scriptren.jsren.dom.node;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderCharacterDataAliveImpl extends JSRenderCharacterDataImpl
{
    /** Creates a new instance of JSCDATASectionRender */
    public JSRenderCharacterDataAliveImpl()
    {
    }

    public boolean isScriptContent(Node newNode)
    {
        Node parentNode = newNode.getParentNode();
        return ((parentNode instanceof Element) && parentNode.getLocalName().equals("script"));
    }

    @Override
    protected String getInsertCompleteNodeCode(Node newNode,String newNodeCode,ClientDocumentStfulDelegateImpl clientDoc)
    {
        if (isScriptContent(newNode))
        {
            // Este método es llamado desde el mutation event listener al insertar
            // un nodo de texto, por lo que el <script> suponemos ya insertado
            // en el documento (en el cliente), es decir, no estamos en el proceso
            // de render de la inserción del elemento padre. Esto es importante
            // porque la solución a este problema se realiza en el render del elemento.
            // En este contexto lo que hacemos es reinsertar el <script>

            // Sólo consideramos la inserción y no el caso de modificar el texto del script
            // porque tanto en FireFox, Safari y Chrome sólo se ejecuta UNA vez el contenido del
            // <script> (no es el caso de Opera), es como si el elemento quedara marcado como "ejecutado"
            // ni siquiera reinsertando se ejecuta o eliminando el contenido y volviéndolo a añadir (o con otro código diferente),
            // Lo anterior es positivo en nuestra solución, pues si una versión más moderna del navegador soluciona
            // este problema y seguimos aplicando la reinserción, la reinserción no ejecutará el script de nuevo.
            // Afortunadamente la reinserción nos sirve a nosotros en el caso de S60WebKit.
            // Si la solución de la reinserción no funciona la única alternativa es el eval

            ClientDocumentStfulDelegateWebImpl clientDocWeb = (ClientDocumentStfulDelegateWebImpl)clientDoc;
            
            StringBuilder code = new StringBuilder();
            if (newNodeCode.startsWith("itsNatDoc.doc.createTextNode(") || // Es una llamada a createTextNode (no es directamente un valor)
                newNodeCode.startsWith("itsNatDoc.doc.createCDATASection("))
                code.append( "var textNode = " + newNodeCode + ";\n" );
            else
            {
                if (newNode instanceof Text)
                    code.append( "var textNode = itsNatDoc.doc.createTextNode(" + newNodeCode + ");\n" ); // Este sí se llamará
                else
                    code.append( "var textNode = itsNatDoc.doc.createCDATASection(" + newNodeCode + ");\n" );
            }

            code.append( super.getInsertCompleteNodeCode(newNode,"textNode",clientDoc) );

            Element script = (Element)newNode.getParentNode();

            JSRenderElementImpl elemRender = JSRenderElementImpl.getJSRenderElement(script, clientDocWeb);

            if (elemRender.isInsertedScriptNotExecuted(script,clientDoc))
            {
                code.append( "eval(textNode.data);\n" );
            }
            else if (elemRender.isTextAddedToInsertedScriptNotExecuted(script,clientDocWeb))
            {
                String method = "fixScriptTextNode";
                if (!clientDocWeb.isClientMethodBounded(method))
                {
                    code.append( "var func = function (textNode)" );
                    code.append( "{" );
                    code.append( "  var elem = textNode.parentNode;\n" );  // El elemento <script>
                    code.append( "  var elemClone = elem.cloneNode(false);\n" );
                    code.append( "  elem.parentNode.replaceChild(elemClone,elem);\n" );
                    code.append( "  elemClone.parentNode.replaceChild(elem,elemClone);\n" );
                    code.append( "};" );
                    code.append( "itsNatDoc." + method + " = func;\n" );
                    clientDocWeb.bindClientMethod(method);
                }

                code.append( "itsNatDoc." + method + "(textNode);\n" );
            }

            return code.toString();
        }
        else return super.getInsertCompleteNodeCode(newNode,newNodeCode,clientDoc);
    }
}
