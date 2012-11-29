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

package org.itsnat.impl.core.resp.shared.html;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.itsnat.impl.core.browser.BrowserBlackBerryOld;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateHTMLLoadDocBlackBerryOld2Impl extends ResponseDelegateHTMLLoadDocBlackBerryOldImpl
{
    public ResponseDelegateHTMLLoadDocBlackBerryOld2Impl(ResponseLoadStfulDocumentValid responseParent)
    {
        super(responseParent);
    }


    public String serializeDocument()
    {
        BrowserBlackBerryOld browser = (BrowserBlackBerryOld)getClientDocumentStful().getBrowser();
        if (browser.isHTMLInputFileValueBuggy())
        {
            // No podemos permitir que haya elementos <input type="file" value=".." />
            // pues la simpre presencia del atributo "value" en el markup provoca una excepción en BlackBerry
            // (JDE 4.6 y 4.7).

            // Este caso es un poco complicado porque el DOM a serializar contiene
            // nodos cacheados que a su vez posiblemente tengan <input type="file"> conflictivos,
            // si no consideramos los <input type="file"> no visibles éstos pueden fastidiar
            // provocar el error de BlackBerry
            // Aunque en teoría la presencia de un <input> impide el cacheamiento
            // por ser un elemento de formulario, esto podría cambiar quizás con un atributo
            // itsnat:nocache="false" que obligara a cachear dicho <input>
            // La estrategia es la siguiente: solucionamos los <input type="file"> visibles
            // a través de cambios en el DOM, serializamos y si siguen existiendo
            // <input type="file"> pues reharemos el DOM a partir de la cadena y haremos
            // lo mismo de nuevo, tarea mucho más lenta obviamente.

            ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();
            Document doc = itsNatDoc.getDocument();
            Map attributes = processTreeInputFileElements(doc);

            String docMarkup = super.serializeDocument();

            if (attributes != null)
                restoreAttributes(attributes);

            // Ahora procesamos los <input type="file"> que estuvieran en nodos cacheados
            // Intentamos evitar un costoso parseado si no hay ningun <input type="file">
            if (docMarkup.indexOf(" type=\"file\"") == -1)  // type="file" es la forma estándar en la que se serializa via DOM este atributo. Si un <input> no tiene "type" el valor por defecto es "text" no "file"
                return docMarkup; // No hay ningún <input type="file">


            Document docTmp = itsNatDoc.parseDocument(docMarkup);
            // No es necesario normalizar ya fue normalizado el documento original que dio lugar a la cadena
            attributes = processTreeInputFileElements(docTmp);
            // Como el documento temporal se pierde no es necesario restaurar nada

            // Para serializar de nuevo no es necesario resolver nodos cacheados
            if (attributes != null) // Hemos cambiado algo
            {
                docMarkup = itsNatDoc.serializeDocument(docTmp,false);
                // NO es necesario restaurar el DOM pues es un documento temporal
            }

            return docMarkup;
        }
        else // No necesario, solucionado en JDE v5
        {
            return super.serializeDocument();
        }
    }

    protected Map processTreeInputFileElements(Document doc)
    {
        Map attributes = null;
        LinkedList elems = DOMUtilInternal.getChildElementListWithTagNameNS(doc,NamespaceUtil.XHTML_NAMESPACE,"input",true);
        if (elems != null)
        {
            for(Iterator it = elems.iterator(); it.hasNext(); )
            {
                HTMLInputElement elem = (HTMLInputElement)it.next();
                if (DOMUtilHTML.isHTMLInputFile(elem) && elem.hasAttribute("value"))
                {
                    if (attributes == null) attributes = new HashMap();
                    processInputFileElement(elem,attributes,doc);
                }
            }
        }
        return attributes;
    }

    public static void processInputFileElement(Element elem,Map attributes,Document doc)
    {
        // Salvamos el atributo para restaurarlo después
        Attr attr = elem.getAttributeNode("value"); // Es seguro que no es nulo
        attributes.put(elem,attr);

        elem.removeAttribute("value");
    }

    public static void restoreAttributes(Map attributes)
    {
        // Restauramos el estado anterior del DOM
        for(Iterator it = attributes.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)it.next();
            Element elem = (Element)entry.getKey();
            Attr attr = (Attr)entry.getValue();
            elem.setAttributeNode(attr);
        }
    }

}

