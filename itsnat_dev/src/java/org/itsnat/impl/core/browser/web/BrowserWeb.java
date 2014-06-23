/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.browser.web;

import java.util.Map;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.scriptren.jsren.dom.node.html.JSRenderHTMLAttributeImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.html.JSRenderHTMLElementImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.html.JSRenderHTMLTextImpl;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author jmarranz
 */
public abstract class BrowserWeb extends Browser
{
    protected transient JSRenderHTMLElementImpl jsRenderHtmlElement;
    protected transient JSRenderHTMLTextImpl jsRenderHtmlText;
    protected transient JSRenderHTMLAttributeImpl jsRenderHtmlAttr;
    
    public BrowserWeb(String userAgent)
    {
        super(userAgent);
    }
    
    public abstract boolean isMobile();   
    
    public JSRenderHTMLElementImpl getJSRenderHTMLElement()
    {
        return jsRenderHtmlElement;
    }

    public void setJSRenderHTMLElement(JSRenderHTMLElementImpl jsRenderHtmlElement)
    {
        this.jsRenderHtmlElement = jsRenderHtmlElement;
    }    
    
    public JSRenderHTMLTextImpl getJSRenderHTMLText()
    {
        return jsRenderHtmlText;
    }

    public void setJSRenderHTMLText(JSRenderHTMLTextImpl jsRenderHtmlText)
    {
        this.jsRenderHtmlText = jsRenderHtmlText;
    }    
    
    public JSRenderHTMLAttributeImpl getJSRenderHTMLAttribute()
    {
        return jsRenderHtmlAttr;
    }

    public void setJSRenderHTMLAttribute(JSRenderHTMLAttributeImpl jsRenderHtmlAttr)
    {
        this.jsRenderHtmlAttr = jsRenderHtmlAttr;
    }    
    
    public abstract boolean isDOMContentLoadedSupported();    
    
    /* El back y el forward no recargan la página desde el servidor
     * pero se ejecutan los elementos <script> dentro del <body> son ejecutados
     * y el evento load (y DOMContentLoaded antes si es soportado).
     * En este caso la página a la que se vuelve tiene el estado de antes
     * de cargar los scripts (no el que queda al dejar la página).
     */
    public abstract boolean isCachedBackForwardExecutedScripts();    
    
    /* El back y el forward no recargan la página desde el servidor
     * ¿Ya no se usa este método? parece que no.
     * De todas formas aunque no se use está bien para caracterizar bien al navegador
     */
    public abstract boolean isCachedBackForward();
    
    /*
     * Algunos navegadores (ej. FireFox 2.0 y S60WebKit) lanzan el blur antes del change
     * cuando abandonamos un text/password box o textarea que ha sido cambiado.
     */
    public abstract boolean isBlurBeforeChangeEvent(HTMLElement formElem);    
    

    /* Si el método focus() y/o blur() no debe ser llamado (y enviar eventos de forma alternativa)
     * o bien porque es ignorado o bien porque es problemático. El contexto de test es el edit inplace
     * ejecutando el focus() como una respuesta AJAX asíncrono.
     * Ocurre al menos en elementos de formulario de tipo: HTMLTextArea, HTMLInputElement o HTMLSelectElement.
     * formElem es no nulo y es un elemento HTML con método focus(), es decir HTMLSelectElement, HTMLTextAreaElement o HTMLInputElement
     */
    public abstract boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem);    
    
    /**
     * Si hay elementos que ignoran el zIndex y recibe eventos.
     */
    public abstract Map<String,String[]> getHTMLFormControlsIgnoreZIndex();   
    
    /* Si soporta opacidad aunque no sea a través de CSS opacity (caso de MSIE_OLD 6+)
     * Sólo tiene sentido en documentos X/HTML.
     */
    public abstract boolean hasHTMLCSSOpacity();
    
    /* Si es capaz de renderizar nativamente markup con namespace no X/HTML, por ejemplo SVG, MathML
     * Si la respuesta es true equivale a preguntar si soporta SVG pues al menos es siempre SVG
       el primer namespace no X/HTML (Gecko soporta también XUL y MathML).
     */
    public abstract boolean canNativelyRenderOtherNSInXHTMLDoc();
    
    /* Si al insertar un <script> no se ejecuta el código cuando dicho código se añade después del elemento, ocurría en navegadores muy antiguos, ya no, redefinir si vuelve a ocurrir */
    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        return false; 
    }                
    
    /* Un elemento <script> con código no es ejecutado cuando es insertado como tal
     * (incluído el código) ya sea el código insertado antes o después
     * de insertar el elemento <script>. Esto no ocurre en HTML por eso sólo consideramos SVG
     */
    public abstract boolean isInsertedSVGScriptNotExecuted(); 
    
    /* Idem para SVG que isTextAddedToInsertedHTMLScriptNotExecuted()
       En el caso de Opera 9 es necesario por ejemplo */
    public abstract boolean isTextAddedToInsertedSVGScriptNotExecuted();    
}
