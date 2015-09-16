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

package org.itsnat.impl.core.browser.web;

import java.util.Map;
import org.itsnat.impl.core.scriptren.jsren.node.html.JSRenderHTMLElementImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.w3c.JSRenderHTMLElementMSIE9Impl;
import org.w3c.dom.html.HTMLElement;

/**
 * IE 9 y superiores
 * 
 * IE 11 user agent (no tiene MSIE):
 *  Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko
 * 
 * Edge User Agent (no tiene ni MSIE ni Trident, como se puede ver desde la v12):
 *  Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240
 * 
 * http://msdn.microsoft.com/en-us/ie/ff468705.aspx
 * 
 * http://www.useragentstring.com/pages/Internet%20Explorer/
 * 
 * IE 11 puede tener por primera vez un user agent que NO INCLUYE "MSIE", pero si incluye "Trident":
 *      Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko
        Mozilla/5.0 (compatible, MSIE 11, Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko
 * 
 * @author jmarranz
 */
public class BrowserMSIE9Up extends BrowserW3C
{
    protected int version; // Por ahora no lo usamos, será 9 o mayor (futuras versiones

    /** Creates a new instance of BrowserMSIE9 */
    public BrowserMSIE9Up(String userAgent,int version)
    {
        super(userAgent);

        this.browserType = MSIE_9;
        this.version = version;
    }

    @Override
    public boolean isMobile()
    {
        return false;
    }

    public int getVersion()
    {
        return version;
    }
    
    @Override
    public boolean hasBeforeUnloadSupportHTML()
    {
        return true;
    }     
    
    @Override
    public boolean isReferrerReferenceStrong()
    {
        return true;  // Estudiar aunque true es la opción más segura
    }

    @Override
    public boolean isCachedBackForward()
    {
        return false;
    }

    @Override
    public boolean isCachedBackForwardExecutedScripts()
    {
        return false;
    }

    @Override
    public boolean isDOMContentLoadedSupported()
    {
        return true;
    }

    @Override
    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
         // ESTUDIAR, esta es la opción más conservadora, copiado de MSIE < 9
        return false; // DOMUtilHTML.isHTMLTextAreaOrInputTextBox(formElem);
    }

    @Override
    public Map<String,String[]> getHTMLFormControlsIgnoreZIndex()
    {
        return null;
    }
    

    @Override
    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return true; 
    }

    @Override
    public boolean isInsertedSVGScriptNotExecuted()
    {
        return true; // Revisar en la versión final de IE 9
    }

    @Override
    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        return true; // Revisar en la versión final de IE 9
    }

    @Override
    public JSRenderHTMLElementImpl getJSRenderHTMLElementSingleton() 
    {
        return getJSRenderHTMLElementSingletonStatic(); 
    }    
    
    public static JSRenderHTMLElementImpl getJSRenderHTMLElementSingletonStatic() 
    {
        return JSRenderHTMLElementMSIE9Impl.SINGLETON; 
    }      
}
