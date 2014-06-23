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

import org.itsnat.impl.core.browser.web.BrowserAdobeSVG;
import org.itsnat.impl.core.browser.web.BrowserBatik;
import org.itsnat.impl.core.browser.web.BrowserGecko;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.browser.web.opera.BrowserOpera;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderElementImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatModalLayerClientDocSVGImpl extends ItsNatModalLayerClientDocImpl
{
    public ItsNatModalLayerClientDocSVGImpl(ItsNatModalLayerSVGImpl parentComp,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        super(parentComp,clientDoc);
    }

    public void initModalLayer()
    {
        // No hay z-index en SVG, el orden depende del orden de inserción
        // que afortunadamente coincide con nuestra forma de trabajar con los
        // modal layers
        // http://wiki.svg.org/Rendering_Order

        Element layerElem = parentComp.getElement();
        // No se necesita: int zIndex = parentComp.getZIndex();
        String background = parentComp.getBackground();
        float opacity = parentComp.getOpacity();

        // Ejemplo: <rect x="0" y="0" width="100%" height="100%" style="fill-opacity:0.2; fill:black;" />

        StringBuilder code = new StringBuilder();

        String elemLayerRef = clientDoc.getNodeReference(layerElem,true,true);

        code.append( "var elem = " + elemLayerRef + ";\n" );
        code.append( "elem.setAttribute('x','0');\n" );
        code.append( "elem.setAttribute('y','0');\n" );
        code.append( "elem.setAttribute('width','100%');\n" );
        code.append( "elem.setAttribute('height','100%');\n" );

        StringBuilder styleCode = new StringBuilder();
        styleCode.append("fill-opacity:" + opacity + ";");
        if (background != null)
            styleCode.append("fill:" + background + ";");
        code.append( "elem.setAttribute('style','" + styleCode.toString() + "');\n" );

        if (clientDoc.getBrowserWeb() instanceof BrowserBatik)
        {
            // En Batik el <rect> a nivel de markup debe tener definido obligatoriamente el width
            // y el height, al ser insertado via DOM no da error pero el estado es incorrecto
            // tal que ignora los posteriores setAttribute.
            // Como el elemento en este punto ya está insertado lo que hacemos
            // es reinsertarlo de nuevo.
            code.append( "var elemClone = elem.cloneNode(false);");
            code.append( "elem.parentNode.replaceChild(elemClone,elem);");
            code.append( "elemClone.parentNode.replaceChild(elem,elemClone);");
        }

        if (needsRedimension())
        {
            String methodName = "initModalLayerSVG";
            if (!clientDoc.isClientMethodBounded(methodName))
                code.append(bindInitModalLayerMethod(methodName));

            code.append("itsNatDoc." + methodName + "(elem);\n");
        }

        clientDoc.addCodeToSend(code.toString());
    }

    private String bindInitModalLayerMethod(String methodName)
    {
        // Afortunadamente la actualización de layers a las nuevas dimensiones
        // de la ventana es muy sencilla en SVG y en los navegadores testeados:
        // FireFox 3, Opera 9.6, Safari 3.1 y Chrome
        // pues en estos navegadores NO hay scroll por lo que la dimensión del
        // documento coincide con la dimensión de la ventana.
        // Por ello si se cambia la ventana con un simple recálculo
        // respecto a los valores 100% es suficiente.
        // Sin embargo en FireFox y Opera esto NO es necesario porque al redimensionar
        // se recalcula el tamaño del layer. Esto no ocurre en navegadores WebKit (Safari y Chrome)
        // en estos si el layer se mostró al ppio con valores width y height al 100% y se hace
        // más grande la ventana no cambia el tamaño.

        StringBuilder code = new StringBuilder();
        code.append("var func = function (elem)\n");
        code.append("{\n");
        code.append("  var listener = function ()\n");
        code.append("  {\n");
        code.append("    var elem = arguments.callee.elem;\n");
        code.append( "   elem.setAttribute('width','1px');\n" );
        code.append( "   elem.setAttribute('height','1px');\n" );
        code.append( "   elem.setAttribute('width','100%');\n" );
        code.append( "   elem.setAttribute('height','100%');\n" );

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

    public boolean needsRedimension()
    {
        return (getTimeout() > 0);
    }

    public int getTimeout()
    {
        int timeout = super.getTimeout();
        if (timeout <= 0) return -1;

        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();
        BrowserWeb browser = clientDoc.getBrowserWeb();
        if ((browser instanceof BrowserGecko)||
            (browser instanceof BrowserOpera)||
            (browser instanceof BrowserAdobeSVG)|| // ASV 
            (browser instanceof BrowserBatik)) // En Batik applet no hay redimensionamiento porque no es normal cambiar las dimensiones del applet
            return -1; // No es necesario el redimensionamiento, se redimensiona automáticamente cuando cambia el cuadro (gracias a los valores "100%")

        return timeout;
    }

    public void preRemoveLayer()
    {
        if (getTimeout() >= 0)
        {
            Element element = parentComp.getElement();
            String elemLayerRef = clientDoc.getNodeReference(element,true,true);
            clientDoc.addCodeToSend("itsNatDoc.clearTimeout(" + elemLayerRef + ".itsNatModalLayerTimer);");
        }
    }

    protected void renderShowHide(Element elem,String elemVarName,boolean hide,StringBuilder code,JSRenderElementImpl render)
    {
        if (NamespaceUtil.isSVGElement(elem))
        {
            // El <script> de SVG no tiene objeto style, al menos en FireFox
            // y ASV3. Da igual lo que pase en los demás (Opera, Chrome etc) no
            // tiene sentido ocultar un <script>
            String localName = elem.getLocalName();
            if (localName.equals("script")) return;

            ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();
            BrowserWeb browser = clientDoc.getBrowserWeb();
            if (browser instanceof BrowserAdobeSVG)
            {
                // El <foreignObject> tampoco tiene objeto style en ASV3
                // este elemento ES visual pero como no está reconocido
                // en ASV3 los elementos hijo son tratados como elementos SVG
                // desconocidos sin visualización. Por tanto evitamos que de error.
                // De hecho la propiedad "display" no funciona en ASV3 (se ignora)
                // pero al menos no da error.
                if (localName.equals("foreignObject"))
                    return;
            }
        }

        super.renderShowHide(elem, elemVarName, hide, code, render);
    }
}
