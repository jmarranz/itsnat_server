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

package org.itsnat.impl.core.scriptren.shared;

import org.itsnat.core.script.ScriptExpr;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.scriptren.jsren.ScriptReference;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderImpl
{
    public static String toLiteralStringScript(String value)
    {
        if (value == null)
            value = "null";
        else
            value = "\"" + value + "\"";
        return value;
    }    
    
    public static String toTransportableStringLiteral(String text,boolean addQuotation,Browser browser)
    {
        StringBuilder encoded = new StringBuilder(text);
        for (int i = 0; i < encoded.length(); i++)
        {
            char c = encoded.charAt(i);
            switch(c)
            {
                case '\r':  encoded.deleteCharAt(i); // Caso Windows (CR), deberá seguir un LF (\n). Lo eliminamos porque en navegadores no MSIE genera dos fines de línea, en MSIE lo que haremos será añadir un \r al procesar el \n
                            i--; // Pues el i++ añade otro más y al eliminar uno no nos hemos movido
                            break;
                case '\n':  encoded.deleteCharAt(i);
                            if (browser instanceof BrowserMSIEOld) // Importante por ejemplo cuando se añade dinámicamente el nodo de texto a un <textarea> o a un <pre> (no probado pero el problema parece que es el mismo)
                            {
                                encoded.insert(i,"\\r");
                                i += 2; // Pues hemos añadido dos caracteres
                            }
                            encoded.insert(i,"\\n");
                            i++; // Uno sólo pues el i++ del for ya añade otro más
                            break;
                case '"':   encoded.deleteCharAt(i);
                            encoded.insert(i,"\\\"");
                            i++;
                            break;
                case '\'':  if (!addQuotation) // Si la cadena se mete entre "" no hace falta "escapar" la ' 
                            {
                                encoded.deleteCharAt(i);
                                encoded.insert(i,"\\'");
                                i++;
                            }
                            break;
                case '\\':  encoded.deleteCharAt(i);
                            encoded.insert(i,"\\\\");
                            i++;
                            break;
                case '\t':  encoded.deleteCharAt(i);
                            encoded.insert(i,"\\t");
                            i++;
                            break;
                case '\f':  encoded.deleteCharAt(i); // FORM FEED
                            encoded.insert(i,"\\f");
                            i++;
                            break;
                case '\b':  encoded.deleteCharAt(i); // BACK SPACE
                            encoded.insert(i,"\\b");
                            i++;
                            break;
            }
        }

        if (addQuotation)
        {
            if (encoded.indexOf("</script>") != -1) // Raro pero puede ocurrir por ejemplo si el texto es el contenido de un comentario y se procesa por JavaScript como en BlackBerry y S60WebKit en carga o está en el valor inicial en carga de un input o similar
            {
                String encoded2 = encoded.toString().replaceAll("</script>", "</\" + \"script>");
                //String encoded2 = encoded.toString().replaceAll("</script>", "<\\/script>"); NO VALE, genera un </script> normal
                return "\"" + encoded2 + "\"";
            }
            else
                return "\"" + encoded + "\"";
        }
        else
            return encoded.toString();
    }    
    
    public static String toTransportableStringLiteral(String text,Browser browser)
    {
        return toTransportableStringLiteral(text,true,browser);
    }    
    
    public static String getTransportableCharLiteral(char c,Browser browser)
    {
        // Permite meter el caracter en código JavaScript
        if (c == '\r')  // Hay que tratarlo aparte porque toTransportableStringLiteral elimina el '\r' pues en cadenas al '\r' le sigue siempre (en Windows) el '\n' y si se envían los dos los browser no MSIE duplican los espacios pues con un '\n' ya le vale (a modo de Unix incluso en Windows). Para MSIE el proceso de un solo \n generará el \r correspondiente.
            return "'\r'";
        else
        {
            String encoded = toTransportableStringLiteral(Character.toString(c),browser);
            return "'" + encoded + "'";
        }
    }    
    
    public static String javaToScript(Object value,boolean cacheIfPossible,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // Convierte value en el adecuado código JavaScript.
        if (value == null) return "null";

        if (value instanceof Node)
            return clientDoc.getNodeReference((Node)value,cacheIfPossible,true);
        else if (value instanceof Boolean)
            return value.toString(); // Devuelve true o false en minúsculas (sin comillas)
        else if (value instanceof Character)
            return getTransportableCharLiteral(((Character)value).charValue(),clientDoc.getBrowser());
        else if (value instanceof Number)
            return value.toString();
        else if (value instanceof ScriptExpr)
            return ((ScriptExpr)value).getCode();
        else if (value instanceof ScriptReference) // Por ahora no se usa salvo en pruebas
            return ((ScriptReference)value).getCode();
        else if (value instanceof String)
            return toTransportableStringLiteral((String)value,clientDoc.getBrowser());
        else
            return value.toString();
    }    
}
