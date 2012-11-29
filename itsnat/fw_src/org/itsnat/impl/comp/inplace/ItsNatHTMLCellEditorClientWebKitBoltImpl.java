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

package org.itsnat.impl.comp.inplace;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLCellEditorClientWebKitBoltImpl extends ItsNatCellEditorClientImpl
{
    protected static final ItsNatHTMLCellEditorClientWebKitBoltImpl SINGLETON = new ItsNatHTMLCellEditorClientWebKitBoltImpl();

    public ItsNatHTMLCellEditorClientWebKitBoltImpl()
    {
    }

    public void registerEventListeners(ItsNatCellEditorImpl parent,ClientDocumentStfulImpl clientDoc)
    {
        super.registerEventListeners(parent,clientDoc);

        // La técnica que usamos en Bolt es similar a la de Opera Mini
        // puesto que tampoco recibe clicks a nivel de documento (ni con window, ni con body).
        // A pesar de que sí tiene evento blur éste solo se lanza si hemos clickado el elemento
        // por ejemplo entrando en el editor del input text, por lo que necesitamos también
        // el poder quitar el editor al pulsar fuera del control.
        // Bolt sí admite posicionamiento absoluto pero a la vez los controles form ignoran
        // el z-index por lo que un layer puesto encima nos sirve para capturar clicks fuera del control
        // y a la vez no impide que el control sea accedido.
        // Hay una pega y es que el área que recibe eventos en Bolt suele ser
        // el que ocupan los textos y el área de imágenes. Por ello usamos
        // como layer una imagen transparente 10px x 10px embebida usando un URL tipo "data".
        // http://www.websiteoptimization.com/speed/tweak/inline-images/
        // El base64 de esta imagen se obtuvo con:
        // http://www.motobit.com/util/base64-decoder-encoder.asp
        // el cual quita ya la cabecera del GIF.

        StringBuffer code = new StringBuffer();
        String method = "boltInitInplaceEditor";
        if (!clientDoc.isClientMethodBounded(method))
        {
            code.append( "var func = function (editor)" );
            code.append( "{" );
            code.append( "  var topElem = itsNatDoc.doc.documentElement;" );
            code.append( "  var layer = itsNatDoc.doc.createElement('img');" );
            code.append( "  var zIndex = 999999;");
            code.append( "  layer.setAttribute('style','position:absolute; z-index:' + zIndex + '; margin:0; padding:0; top:0; left:0; width:' + topElem.scrollWidth + 'px; height:' + topElem.scrollHeight + 'px;');" ); // Para testeo:  border:3px solid blue;
            code.append( "  layer.src='data:image/gif;base64,R0lGODlhCgAKAIAAAP///////yH5BAEKAAEALAAAAAAKAAoAAAIIjI+py+0PYysAOw=='; ");
            code.append( "  itsNatDoc.getVisualRootElement().appendChild(layer);" );
            code.append( "  editor.style.position = 'absolute';" );
            code.append( "  editor.style.zIndex = zIndex + 1;" );
            code.append( "  var listener = function (evt)" );
            code.append( "  {" );
            code.append( "    if (evt.target != editor)" );
            code.append( "    {" );
            code.append( "       var evtTmp = itsNatDoc.doc.createEvent('HTMLEvents');" );
            code.append( "       evtTmp.initEvent('blur',true,true);" );
            code.append( "       editor.dispatchEvent(evtTmp);" );
            code.append( "    }" );
            code.append( "  };" );
            code.append( "  layer.setAttribute('onclick','');" ); // Esto es para evitar el error de Opera Mini de los listeners-atributos
            code.append( "  layer.addEventListener('click',listener,true);" );
            code.append( "  var listener = function (evt)" );
            code.append( "  {" );
            code.append( "    itsNatDoc.getVisualRootElement().removeChild(layer);" );
            code.append( "  };" );
            code.append( "  editor.addEventListener('DOMNodeRemovedFromDocument',listener,false);" );
            code.append( "};" );
            code.append( "itsNatDoc." + method + " = func;\n" );
            clientDoc.bindClientMethod(method);
        }
        code.append( "itsNatDoc." + method + "(nodeEditor);\n" );

        clientDoc.addCodeToSend(code.toString());

        // No es necesario código de "desregistro" porque si el editor
        // se ha eliminado por otra vía el editor.dispatchEvent() no hace nada y el registro de DOMNodeRemovedFromDocument
        // obviamente se pierde (pues el elemento en el cliente no se reutiliza de acuerdo con las reglas de ItsNat aunque no sea así en el servidor)
        // y al ejecutarse el mutation se elimina el layer de utilidad en cualquier caso.

    }
}
