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

package org.itsnat.impl.core.mut.client;

import java.util.Map;
import org.itsnat.impl.core.browser.webkit.BrowserWebKitAndroid;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class ClientMutationEventListenerHTMLWebKitAndroidImpl extends ClientMutationEventListenerHTMLWebKitImpl
{
    public ClientMutationEventListenerHTMLWebKitAndroidImpl(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
    }

    public void postRenderAndSendMutationCode(MutationEvent mutEvent,Map context)
    {
        super.postRenderAndSendMutationCode(mutEvent,context);

        BrowserWebKitAndroid browser = (BrowserWebKitAndroid)clientDoc.getBrowser();
        if (browser.isSelectMultipleFirstOptionEverSelected())
        {
            String type = mutEvent.getType();
            if (  type.equals("DOMNodeInserted")||
                  type.equals("DOMNodeRemoved")||
                  type.equals("DOMCharacterDataModified") )
            {
                Node node = (Node)mutEvent.getTarget(); // node es el nuevo o a eliminar
                if (node.getNodeType() == Node.TEXT_NODE)
                {
                    Node parent = node.getParentNode();
                    if (DOMUtilHTML.isHTMLOptionOfSelectMultiple(parent))
                    {
                        HTMLSelectElement select = (HTMLSelectElement)parent.getParentNode();
                        fixHTMLSelectMultipleAndroid(select);
                    }
                }
                else if (node instanceof HTMLOptionElement)
                {
                    if (DOMUtilHTML.isHTMLOptionOfSelectMultiple(node))
                    {
                        HTMLSelectElement select = (HTMLSelectElement)node.getParentNode();
                        fixHTMLSelectMultipleAndroid(select);
                    }
                }
                else if (type.equals("DOMNodeInserted"))
                {
                    fixTreeHTMLSelectMultiplesAndroid(node);
                }
            }
        }
    }

    private void fixTreeHTMLSelectMultiplesAndroid(Node node)
    {
        StringBuffer code = fixTreeHTMLSelectMultiplesAndroid(node,null);

        if ((code != null) && (code.length() > 0))
            clientDoc.addCodeToSend(code.toString());
    }

    private StringBuffer fixTreeHTMLSelectMultiplesAndroid(Node node,StringBuffer code)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE) return code;

        Element elem = (Element)node;

        if (DOMUtilHTML.isHTMLSelectMultiple(elem))
        {
            return fixHTMLSelectMultipleAndroid((HTMLSelectElement)elem,code);
        }
        else
        {
            Node child = elem.getFirstChild();
            while (child != null)
            {
                code = fixTreeHTMLSelectMultiplesAndroid(child,code);
                child = child.getNextSibling();
            }
        }

        return code;
    }

    private void fixHTMLSelectMultipleAndroid(HTMLSelectElement select)
    {
        StringBuffer code = fixHTMLSelectMultipleAndroid(select,null);

        if ((code != null) && (code.length() > 0))
            clientDoc.addCodeToSend(code.toString());
    }

    private StringBuffer fixHTMLSelectMultipleAndroid(HTMLSelectElement select,StringBuffer code)
    {
        // El Android v1 r2 (y anteriores) tiene el mismo error que tuvo el iPhone
        // y es que el primer <option> se selecciona por defecto en select multiple (el atributo size no influye),
        // tanto tras cargar la página como al insertar/remover/cambiar texto de cualquier <option> via JavaScript, no necesariamente el primero.
        // El problema debe estar en que cuando se renderiza la nueva lista se selecciona
        // el primer elemento erróneamente.

        // Publiqué el error y parece que ya está solucionado aunque no se en qué versión
        // http://code.google.com/p/android/issues/detail?id=1788

        // En el caso de carga (fast load) no hay problema pues en carga el select ya está renderizado visualmente
        // antes de que se ejecute el código de ItsNat que evita los cambios del usario en el proceso de carga
        // esto indirectamente soluciona este error al poner la propiedad selected al valor apropiado.
        // El error de inserción de nuevo <option> es más complejo porque la propiedad cambia DESPUES de ejecutar
        // el script que insertó la opción, por lo que un "option.selected = false;" no hace nada
        // pues de hecho es ya false.

        // Hay una solución que inexplicablemente funciona y es eliminar
        // el select e insertarlo de nuevo usando un elemento temporal usando cloneNode y replaceChild,
        // tras la re-inserción el option.selected es true, por lo que es la oportunidad de ponerlo a false,
        // sin embargo NO FUNCIONA cuando este script es ejecutado como respuesta de una
        // petición AJAX asíncrona. Otra opción probada es la reinserción del <option> de forma similar lo anterior
        // pero igualmente en AJAX no funciona.

        // La solución que ha funcionado es con setTimeout(...,0)
        // Este setTimeout se ejecuta después de que se ejecute el script
        // el problema es que durante el script puede ocurrir cualquier cosa
        // Lo bueno es que en navegadores W3C cuando cambiamos la propiedad ponemos
        // o quitamos el atributo también por lo que el atributo del primer option en este caso
        // es el que nos sirve para saber cual es el valor que está en el servidor

        // Hay un pequeño error visual y es que al cambiar la propiedad selected
        // via JavaScript el <select> no se entera visualmente, por ello reinsertamos el <select>
        // el cual provoca un renderizado.

        // En el caso de reinserción del <select> se ha encontrado que el fallo ocurre
        // en el segundo <option> por lo que nos olvidamos de que el fallo siempre sea en el primero

        if (select.getOptions().getLength() == 0) return code;
        HTMLOptionElement firstOption = (HTMLOptionElement)select.getOptions().item(0); // Tenemos la seguridad de que hay elemento
        if (firstOption.hasAttribute("selected")) // El caso de seleccionado no da problema
            return code;

        if (code == null) code = new StringBuffer();

        String methodName = "androidFixSelectMultiple";
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindFixHTMLSelectMultipleMethod(methodName));

        String selRef = clientDoc.getNodeReference(select,true,true);

        code.append("itsNatDoc." + methodName + "(" + selRef + ");\n");

        return code;
    }

    private String bindFixHTMLSelectMultipleMethod(String methodName)
    {
        StringBuffer code = new StringBuffer();
        code.append("var func = function(select)\n");
        code.append("{\n");
        code.append("  var func = function()\n");
        code.append("  {\n");
        code.append("    var select = arguments.callee.select;\n");
        code.append("    var parentNode = select.parentNode;\n");
        code.append("    if (!parentNode) return;\n"); // Puede haber ocurrido cualquier cosa antes de ejecutarse este script incluyendo el que el select haya sido eliminado

/*
        // Reinserción y solución del bug visual (para que se re-renderice):
        code.append("    var selectClone = select.cloneNode(true);\n"); // El deep = true no es necesario pero lo ponemos para evitar un posible "parpadeo" al insertar un select vacío
        code.append("    parentNode.replaceChild(selectClone,select);\n");
        code.append("    parentNode.replaceChild(select,selectClone);\n");
*/
        code.append("    for(var i=0;i<select.options.length;i++)\n");
        code.append("    {\n");
        code.append("      var option = select.options[i];\n");
        code.append("      if (option.hasAttribute('selected')) continue;\n"); // El caso de seleccionado no da problema
        code.append("      option.selected = false;\n");
        code.append("    }\n");
        code.append("  };\n");
        code.append("  func.select = select;\n");
        code.append("  itsNatDoc.setTimeout(func,1);\n");
        code.append("};\n");
        code.append("itsNatDoc." + methodName + " = func;\n");
        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }
}
