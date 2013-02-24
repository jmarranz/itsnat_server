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

package org.itsnat.impl.comp.layer;

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserBlackBerryOld;
import org.itsnat.impl.core.browser.BrowserMSIE9;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.browser.opera.BrowserOpera;
import org.itsnat.impl.core.browser.webkit.BrowserWebKit;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatModalLayerClientDocHTMLImpl extends ItsNatModalLayerClientDocImpl
{
    public ItsNatModalLayerClientDocHTMLImpl(ItsNatModalLayerHTMLImpl comp,ClientDocumentStfulImpl clientDoc)
    {
        super(comp,clientDoc);
    }

    public ItsNatModalLayerHTMLImpl getItsNatModalLayerHTML()
    {
        return (ItsNatModalLayerHTMLImpl)parentComp;
    }

    public void initModalLayer()
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        Browser browser = clientDoc.getBrowser();

        Element layerElem = parentComp.getElement();
        int zIndex = parentComp.getZIndex();
        String background = parentComp.getBackground();
        float opacity = parentComp.getOpacity();

        // La no definición de la propiedad background supone usar los valores
        // por defecto, y lo normal es que background-color sea "transparent" por defecto.
        String backgroundProp;
        if ( (background != null) &&
             (browser.hasHTMLCSSOpacity() || (opacity >= (float)0.5)) )
            backgroundProp = background;
        else
        {
            if (background == null) // Transparente (valor por defecto normal)
            {
                // Vemos qué casos el fondo transparente no es válido
                if ((browser instanceof BrowserMSIEOld)||(browser instanceof BrowserMSIE9))
                {
                    // El fondo transparente ignora el z-index, los elementos por debajo son pulsables,
                    // evitamos así esto.
                    backgroundProp = "white";
                    opacity = 0;
                }
                else
                    backgroundProp = null;
            }
            else backgroundProp = background;
        }

        StringBuilder code = new StringBuilder();
        String elemLayerRef = clientDoc.getNodeReference(layerElem,true,true);
        code.append( "var elem = " + elemLayerRef + ";\n" );


        {
            StringBuilder styleCode = new StringBuilder();
            styleCode.append( "position:absolute; top:0px; left:0px; width:1px; height:1px; margin:0px; padding:0px; border:0px; " ); // border:1px red solid; para testear
            styleCode.append( "z-index:" + zIndex + "; " );
            if (browser.hasHTMLCSSOpacity()) // Evitamos poner la opacidad si no se soporta, por ej. en Opera Mobile 9.7 beta hay un bug que hace que se oculte el nodo si opacity < 1
            {
                styleCode.append( "opacity:" + opacity + "; " );

                if (browser instanceof BrowserMSIEOld)
                {
                    // Por script sería: http://msdn.microsoft.com/en-us/library/ms532847(VS.85).aspx#Scripting_Filters
                    int opInt = (int)(100*opacity);
                    styleCode.append( "filter:alpha(opacity=" + opInt + "); " ); // Equivale en CSS a: filter:alpha(opacity=" + opInt + ") La sintaxis:  elem.style.filter.opacity = .. sólo funciona si el filtro ya está presente
                }
            }

            if (backgroundProp != null)
                 styleCode.append( "background:" + backgroundProp + "; " );
            else
            {
                if (browser instanceof BrowserOpera)
                {
                    // El color transparente por sí solo no respeta el z-index, hay que "ayudar"
                    // Probado en Opera 9.63 y Mobile v9.5, 9.7 y 9.8 (la v10). Podemos arreglarlo con una imagen transparente.
                    styleCode.append( "background-image:url('data:image/gif;base64,R0lGODlhCgAKAIAAAP///////yH5BAEKAAEALAAAAAAKAAoAAAIIjI+py+0PYysAOw==');");
                }
            }

            // code.append( "elem.style.cssText = \"" + styleCode.toString() + "\";\n" );

            // En el caso de MSIE el cliente sabe que hay que utilizar cssText
            code.append("itsNatDoc.setAttribute(elem,\"style\",\"" + styleCode.toString() + "\");\n");
        }

        // Aparentemente podríamos poner el layer con width:100% y height:100%
        // tal que se redimensionara automáticamente cuando cambiara la ventana,
        // sin embargo no es así, este 100% representa el área visible sin contar
        // la parte oculta y accesible usando el scroll, por lo que el recalculado
        // es inevitable.

        // El timer es para cuando se redimensiona la ventana por el usuario
        // y se recoloca el HTML y también para el siguiente caso: aunque en
        // teoría el HTML que se añada después para que se vea "encima" del ModalLayer
        // puede cambiar las dimensiones de la página, como tenemos un timer
        // no hay problema, se corregirá sólo.

        // Lo de "style.width = '1px';" y "style.height = '1px';" tiene el siguiente
        // sentido: nuestra capa modal busca tapar lo que hay "debajo" para ello
        // se calcula la dimensión máxima de la página y la capa se redimensiona para ello.
        // El problema es que si hay redimensionamiento o bien porque el usuario cambia
        // el tamaño de la ventana, o porque nuevo HTML es añadido (posiblemente con posicionamiento
        // absoluto), el teoría la dimensión "natural" de la página cambiaría sin embargo la propia
        // capa modal ahora influye en el dimensionamiento, por eso haciendo
        // width/height = '1px'; antes de obtener la nueva dimensión conseguimos
        // que htmlElem.scrollWidth/scrollHeight se calculen sin contar con la capa modal.

        String methodName = "initModalLayerHTML";
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindInitModalLayerMethod(methodName));

        code.append("itsNatDoc." + methodName + "(elem);\n");

        clientDoc.addCodeToSend(code.toString());
    }


    protected String bindInitModalLayerMethod(String methodName)
    {
        // Disminuyemos poco a poco el tamaño del layer hasta que sea menor que
        // el tamaño del contenido de la página y entonces ajustamos de nuevo
        // así evitamos el parpadeo que queda muy feo en dispositivos móviles sin opacidad
        // (el modal layer es negro y opaco en mis tests)

        Browser browser = clientDoc.getBrowser();

        StringBuilder code = new StringBuilder();
        code.append("var func = function (elem)\n");
        code.append("{\n");
        code.append("  var listener = function ()\n");
        code.append("  {\n");
        code.append("    var elem = arguments.callee.elem;\n");
        code.append("    var maxW = 1;\n");
        code.append("    var maxH = 1;\n");
        code.append("    var style = elem.style;\n");
        code.append("    while(true)\n");
        code.append("    {\n");
        if (browser instanceof BrowserBlackBerryOld)
        {
            code.append("  var currW = elem.offsetWidth + elem.offsetLeft*2;\n");
            code.append("  var currH = elem.offsetHeight + elem.offsetTop*2;\n");
            // BlackBerry no se entera de pequeños cambios y los offsetWidth/Height no cambian por eso hacemos una reducción mucho mayor y no he detectado parpadeo
            code.append("  currW = parseInt((currW*3)/4);\n");
            code.append("  currH = parseInt((currH*3)/4);\n");
        }
        else
        {
            code.append("  var currW = elem.scrollWidth;\n");
            code.append("  var currH = elem.scrollHeight;\n");
            code.append("  currW = parseInt((currW*49)/50);\n");
            code.append("  currH = parseInt((currH*49)/50);\n");
        }

        code.append("      style.width =  currW + 'px';\n");
        code.append("      style.height = currH + 'px';\n");

    if ((browser instanceof BrowserMSIEOld)||(browser instanceof BrowserWebKit))
    {
        // Hay un bug en MSIE (v6 al menos), el scrollWith/scrollHeight del HTML coinciden
        // siempre erróneamente con el offsetWidth/offsetHeight. Esto no ocurre con el BODY
        // http://connect.microsoft.com/IE/feedback/ViewFeedback.aspx?FeedbackID=364101
        // Hacemos código que permita que funcione bien en versiones futuras "correctas".
        // Parecido ocurre en Safari 3.1 (aplicamos a todos los WebKit en general)
        code.append("      var top1 = itsNatDoc.doc.documentElement;\n");
        code.append("      var top2 = itsNatDoc.getVisualRootElement();\n"); // Devuelve el <body>. No podemos usar document.body porque hay que tener en cuenta que en XHTML y al menos en Safari document.body no está definido
        code.append("      maxW = Math.max(top1.scrollWidth,top2.scrollWidth);\n");
        code.append("      maxH = Math.max(top1.scrollHeight,top2.scrollHeight);\n");
    }
    else if (browser instanceof BrowserBlackBerryOld)
    {
        code.append("      var top = itsNatDoc.doc.documentElement;\n");
        code.append("      maxW = top.offsetWidth + top.offsetLeft*2;\n");
        code.append("      maxH = top.offsetHeight + top.offsetTop*2;\n");
    }
    else
    {
        code.append("      var top = itsNatDoc.doc.documentElement;\n");
        code.append("      maxW = top.scrollWidth;\n");
        code.append("      maxH = top.scrollHeight;\n");
    }

        code.append("      if ((currW<maxW)&&(currH<maxH)) break; \n");
        code.append("    }\n");
        code.append("    style.width  = maxW + 'px';\n");
        code.append("    style.height = maxH + 'px';\n");

    int timeout = getTimeout();
    if (timeout > 0)
    {
        code.append("    elem.itsNatModalLayerTimer = itsNatDoc.setTimeout(arguments.callee," + timeout + ");\n");
    }
        code.append("  };\n");
        code.append("  listener.elem = elem;\n");
        code.append("  listener();\n");
        code.append("};\n");

        code.append("itsNatDoc." + methodName + " = func;\n");

        clientDoc.bindClientMethod(methodName);

        return code.toString();
     }

    public void preRemoveLayer()
    {
        if (getTimeout() > 0)
        {
            Element element = parentComp.getElement();
            String elemLayerRef = clientDoc.getNodeReference(element,true,true);
            clientDoc.addCodeToSend("itsNatDoc.clearTimeout(" + elemLayerRef + ".itsNatModalLayerTimer);");
        }
    }

}
