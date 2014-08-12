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

package org.itsnat.impl.core.scriptren.jsren;

import java.io.UnsupportedEncodingException;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.shared.JSAndBSRenderImpl;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderImpl
{

    /** Creates a new instance of JSRenderImpl */
    public JSRenderImpl()
    {
    }

    public static String javaToJS(Object value,boolean cacheIfPossible,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSAndBSRenderImpl.javaToScript(value, cacheIfPossible, clientDoc);
    }

    public static String toLiteralStringJS(String value)
    {
        return JSAndBSRenderImpl.toLiteralStringScript(value);
    }

    public static String toTransportableStringLiteral(String text,Browser browser)
    {
        return toTransportableStringLiteral(text,true,browser);
    }

    public static String toTransportableStringLiteral(String text,boolean addQuotation,Browser browser)
    {
        return JSAndBSRenderImpl.toTransportableStringLiteral(text, addQuotation, browser);
    }

    public static String getTransportableCharLiteral(char c,Browser browser)
    {
        return JSAndBSRenderImpl.getTransportableCharLiteral(c, browser);        
    }

    public static String encodeURIComponent(char c)
    {
        return encodeURIComponent(Character.toString(c));
    }

    public static String encodeURIComponent(String text)
    {
        return encodeURIComponent(text,true);
    }

    public static String encodeURIComponent(String text,boolean encodeSpaces)
    {
        // Sirve para codificar en el servidor preparado
        // para ser descodificado por decodeURIComponent()
        // con JavaScript, es decir emulando encodeURIComponent de JavaScript
        // Usamos URLEncoder.encode() que es lo más parecido.
        // http://xkr.us/articles/javascript/encode-compare/
        // En el caso del caracter ' URLEncoder.encode() lo "escapea" con %
        // sin embargo encodeURIComponent de JavaScript no, pero de todas formas
        // funciona el decodeURIComponent en JavaScript.


        try
        {
            text = java.net.URLEncoder.encode(text,"UTF-8");
        }
        catch(UnsupportedEncodingException ex)
        {
            throw new ItsNatException(ex);
        }

        StringBuilder textBuff = new StringBuilder(text);
        for(int i = 0; i < textBuff.length(); i++)
        {
            char c = textBuff.charAt(i);
            if (c == '+')
            {
                if (encodeSpaces)
                {
                    textBuff.deleteCharAt(i);
                    textBuff.insert(i,"%20");
                    i += 2;
                }
                else
                    textBuff.setCharAt(i,' ');
            }
        }
        return textBuff.toString();
    }

    public static String encodeURI(String text)
    {
        // NO SE USA, se usa encodeURIComponent(String)
        // pero la conservamos porque funcionaba

        // Emulamos la funcionalidad de la función
        // JavaScript encodeURI() tal que un texto codificado en el servidor
        // y preparado para meterse transportarse como texto al cliente (en cadenas literales de JavaScript por ejemplo),
        // pueda volver al original usando decodeURI() en JavaScript.
        // Para ello usamos URLEncoder.encode() muy parecida pero no exactamente igual
        // URLEncoder.encode() convierte el ' ' en '+' sin embargo
        // decodeURI() en JavaScript espera %20
        // Por otra parte URLEncoder.encode() convierte '+' en %2B el
        // cual es ignorado por decodeURI(), por ello volvemos a
        // poner como '+' el %2B
        try
        {
            text = java.net.URLEncoder.encode(text,"UTF-8");
            for(int i = 0; i < text.length(); i++)
            {
                char c = text.charAt(i);
                if (c == '+')
                {
                    text = text.substring(0,i) + "%20" + text.substring(i + 1,text.length());
                    i += 2;
                }
                else if ((c == '%') && (text.length() - i >= 3))
                {
                    // Caracteres que han sido encoded por URLEncoder pero que no codifica encodeURI
                    String charCodeStr = text.substring(i + 1, i + 3);
                    char charCode = (char)Integer.parseInt(charCodeStr,16);
                    if ((charCode == 0x21) || //  !
                        (charCode == 0x23) || //  #
                        (charCode == 0x24) || //  $
                        ((0x26 <= charCode ) && (charCode <= 0x3B)) || // & ' ( ) + , / : ;
                        (charCode == 0x3D) || //  =
                        (charCode == 0x3F) || //  ?
                        (charCode == 0x40))   //  @
                    {
                       text = text.substring(0,i) + charCode + text.substring(i + 3,text.length());
                    }
                }
            }
            return text;
        }
        catch(UnsupportedEncodingException ex)
        {
            throw new ItsNatException(ex);
        }
    }

    public static String getSetPropertyCode(Object object,String propertyName,Object value,boolean endSentence,boolean cacheIfPossible,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSAndBSRenderImpl.getSetPropertyCode(object, propertyName, value, endSentence, cacheIfPossible, clientDoc);
    }

    public static String getGetPropertyCode(Object object,String propertyName,boolean endSentence,boolean cacheIfPossible,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSAndBSRenderImpl.getGetPropertyCode(object, propertyName, endSentence, cacheIfPossible, clientDoc);
    }

}
