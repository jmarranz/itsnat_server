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

import java.util.LinkedList;
import java.util.Map;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.scriptren.jsren.dom.node.html.JSRenderHTMLElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLInputElement;

/**
 * Caso por ejemplo de MSIE v6, la versión 7 ya soluciona el problema de los HTML Select y el z-index.
 * http://blogs.msdn.com/ie/archive/2006/01/17/514076.aspx
 *
 * Android (v1.0 r2) etc tienen también este problema
 *
 * @author jmarranz
 */
public class ItsNatModalLayerClientDocHTMLHideFormElemImpl extends ItsNatModalLayerClientDocHTMLImpl
{
    public ItsNatModalLayerClientDocHTMLHideFormElemImpl(ItsNatModalLayerHTMLImpl comp,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        super(comp,clientDoc);
    }

    @Override
    public void initModalLayer()
    {
        // Ocultamos los HTML selects, input etc
        showHideHTMLFormElements(false);

        super.initModalLayer();
    }

    @Override
    public void postRemoveLayer()
    {
        super.postRemoveLayer();

        // Mostrar los HTML selects, input etc
        showHideHTMLFormElements(true);
    }

    private void showHideHTMLFormElements(boolean show)
    {
        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();

        ItsNatModalLayerHTMLImpl parentComp = getItsNatModalLayerHTML();
        ItsNatModalLayerImpl prevComp = parentComp.getPreviousItsNatModalLayer();
        int zIndex1 = prevComp != null? prevComp.getZIndex() : Integer.MIN_VALUE; // El z-index puede ser negativo
        int zIndex2 = parentComp.getZIndex();

        StringBuilder code = new StringBuilder();

        String methodName = "modalLayerShowHTMLFormCtrls";
        if (!clientDoc.isClientMethodBounded(methodName))
            code.append(bindModalLayerShowFormCtrlsMethod(methodName,clientDoc));

        BrowserWeb browser = clientDoc.getBrowserWeb();
        Map<String,String[]> localNames = browser.getHTMLFormControlsIgnoreZIndex();
        Document doc = clientDoc.getItsNatStfulDocument().getDocument();

        for(Map.Entry<String,String[]> entry : localNames.entrySet())
        {
            String localName = entry.getKey();
            String[] types = entry.getValue();

            LinkedList<Node> elemList = DOMUtilInternal.getChildElementListWithTagNameNS(doc,NamespaceUtil.XHTML_NAMESPACE,localName,true);
            if (elemList != null)
            {
                for(Node node : elemList)
                {
                    Element elem = (Element)node;
                    if ((types != null) && (elem instanceof HTMLInputElement))
                    {
                        boolean found = false;
                        String type = elem.getAttribute("type");
                        if (type.equals("")) type = "text";
                        for(int i = 0; i < types.length; i++)
                            if (types[i].equals(type)) { found = true; break; }
                        if (!found) continue;
                    }
                    code.append("var elem = " + clientDoc.getNodeReference(elem, true, true) + ";\n");
                    code.append("itsNatDoc." + methodName + "(elem," + show + "," + zIndex1 + "," + zIndex2 + ");\n");
                }
            }
        }

        clientDoc.addCodeToSend(code);
    }

    private static String bindModalLayerShowFormCtrlsMethod(String methodName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        BrowserWeb browser = clientDoc.getBrowserWeb();
        JSRenderHTMLElementImpl render = JSRenderHTMLElementImpl.getJSRenderHTMLElement(browser); 

        StringBuilder code = new StringBuilder();

        // Necesitamos enviar antes los métodos usados luego dentro de la función
        code.append( render.bindBackupAndSetStylePropertyMethod(clientDoc) );
        code.append( render.bindRestoreBackupStylePropertyMethod(clientDoc) );

        code.append( "var func = function (elem,show,zIndex1,zIndex2)" );
        code.append( "{" );
        //code.append( "  try{" ); // No se cual es la razón pero este try/catch es necesario para evitar que Opera Mobile 9.7 beta (y creo que 9.5 beta también) de errores imprevisibles (NO ESTA CLARO POR ESO LO HE QUITADO)

        code.append( "  var zIndex=0,zIndexMax=0;" );
        code.append( "  var parentNode = elem;" );
        code.append( "  while(parentNode.nodeType == 1)" );  // 1 == Node.ELEMENT_NODE
        code.append( "  {" );
        code.append( "    var style = " + render.getCurrentStyleObject("this","parentNode",clientDoc) + ";" );
        code.append( "    zIndex = style.zIndex;" );
        code.append( "    zIndex = parseInt(zIndex);" ); // zIndex normalmente (en W3C) es una string y puede ser el valor "auto" (parseInt devuelve NaN)
        code.append( "    if (isNaN(zIndex)) zIndex = 0;" );
        code.append( "    if (zIndex > zIndexMax) zIndexMax = zIndex;" );
        code.append( "    parentNode = parentNode.parentNode;" );
        code.append( "  }" );
        code.append( "  if ((zIndexMax < zIndex1)||(zIndexMax >= zIndex2)) return;" ); // zIndexMax debe estar en el conjunto [zIndex1,zIndex2)

        code.append( "  if (show)"); // Si el elemento no fue oculto no pasa nada, el código de restauración del backup detecta que no hay backup de la propiedad
        code.append( "  {" );
        code.append( "    " + render.getRestoreBackupStyleProperty("elem","visibility",clientDoc) );
        code.append( "  }" );
        code.append( "  else" );
        code.append( "  {" );
        code.append( "    " + render.getBackupAndSetStyleProperty("elem","visibility","hidden",clientDoc) );
        code.append( "  }" );
        //code.append( "  }catch(e){ }" );
        code.append( "};" );
        code.append("itsNatDoc." + methodName + " = func;\n");

        clientDoc.bindClientMethod(methodName);

        return code.toString();
    }
}
