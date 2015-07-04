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

package org.itsnat.impl.core.doc;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domimpl.ElementDocContainer;
import org.w3c.dom.Attr;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLParamElement;

/**
 *
 * @author jmarranz
 */
public class HTMLObjectElementWrapperImpl extends ElementDocContainerWrapperImpl
{

    public HTMLObjectElementWrapperImpl(HTMLObjectElement elem)
    {
        super((ElementDocContainer)elem);
    }

    public HTMLObjectElement getHTMLObjectElement()
    {
        return (HTMLObjectElement)elem;
    }

    public String getURL()
    {
        if (isJavaApplet())
        {
            HTMLParamElement srcParam = getHTMLParamElementWithSrc();
            if (srcParam == null) return "";
            return srcParam.getValue();
        }
        else
            return getHTMLObjectElement().getData();
    }

    public void setURL(String url)
    {
        if (isJavaApplet())
        {
            HTMLParamElement srcParam = getHTMLParamElementWithSrc();
            if (srcParam == null) throw new ItsNatException("INTERNAL ERROR");
            srcParam.setValue(url);
        }
        else getHTMLObjectElement().setData(url);
    }

    public boolean isJavaApplet()
    {
        String type = getElement().getAttribute("type");
        return type.equals("application/x-java-applet");
    }

    public String getURLAttrName()
    {
        // Si es un applet los atributos "data" y "src" no pintan nada en applets.
        // Es el <param name="src" value="url"> el que vale en el caso de
        // applet Batik.
        if (isJavaApplet())
            return "value";
        else
            return "data"; // ASV, Savarese Ssrc ...
    }

    public boolean isURLAttribute(Attr attr)
    {
        String attrName = getURLAttrName();
        if (isJavaApplet())
        {
            HTMLParamElement srcParam = getHTMLParamElementWithSrc();
            return (attr.getOwnerElement() == srcParam) &&
                    attrName.equals(attr.getName());
        }
        else
        {
            return attrName.equals(attr.getName());
        }
    }

    public void setURL(String url,ClientDocumentStfulImpl clientDoc)
    {
        String oldURL = getURL();

        super.setURL(url,clientDoc);

        if ((clientDoc.getBrowser() instanceof BrowserMSIEOld) &&
             !isJavaApplet())
        {
            // En el caso al menos de Adobe SVG Viewer (en Internet Explorer desktop) el atributo/propiedad "src" es usado
            // en Internet Explorer para especificar el URL pero curiosamente
            // éste sólo es detectado cuando se inserta el <object> via DOM (appendChild/insertBefore)
            // no en carga en el markup, tampoco cuando el <object> es insertado
            // dentro de un innerHTML. El atributo "data" (el estándar) es ignorado.
            // Lo curioso es que una vez insertado, exista o no el atributo src, existe
            // la propiedad src con el valor introducido ya sea via atributo src
            // o via <param name="src"...>

            // Si el atributo "src" está presente y es igual a "data" es que existe
            // la intencionalidad de que esté sincronizado con data.

            HTMLObjectElement elem = getHTMLObjectElement();
            if (elem.getAttribute("src").equals(oldURL)) // Si no fuera igual que el valor del "data" del object (el estándar) antes de cambiarlo sería sospechoso, quizás un intento de engañarnos o bien no es el caso de uso de plugin de Adobe SVG Viewer
                elem.setAttribute("src",url);

            // Igualmente cuando el "src" es también ignorado (por ejemplo en markup en carga)
            // el URL hay que indicarlo en un <param name="src" value="url">, por lo tanto si detectamos
            // dicho parámetro tenemos que poner el valor del URL en dicho parámetro.
            // http://joliclic.free.fr/html/object-tag/en/object-svg.html
            HTMLParamElement param = getHTMLParamElementWithSrc();
            if ((param != null) && param.getValue().equals(oldURL)) // Si el valor del <param> no fuera igual que el valor del "data" del object (el estándar) antes de cambiarlo sería sospechoso, quizás un intento de engañarnos o bien no es el caso de uso de plugin de Adobe SVG Viewer
                param.setValue(url);
        }
    }
}
