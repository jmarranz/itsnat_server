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

package org.itsnat.impl.core.resp.attachsrv;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedServerImpl;
import org.itsnat.impl.core.req.RequestImpl;
import org.itsnat.impl.core.req.attachsrv.RequestAttachedServerPrepareImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateAttachedServerImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseAttachedServerPrepareFormImpl extends ResponseAttachedServerPrepareImpl
{

    /** Creates a new instance of ResponseNormalLoadDocImpl */
    public ResponseAttachedServerPrepareFormImpl(RequestAttachedServerPrepareImpl request)
    {
        super(request);
    }

    public String genSendMarkupCodeByMethod()
    {
        StringBuilder code = new StringBuilder();

        ClientDocumentAttachedServerImpl clientDoc = getClientDocumentAttachedServer();
        ItsNatStfulDocumentTemplateAttachedServerImpl template = clientDoc.getItsNatStfulDocumentTemplateAttachedServer();

        ItsNatServletRequestImpl itsNatRequest = getRequest().getItsNatServletRequest();
        String url = itsNatRequest.getServletPath(true, true); // Devuelve un URL absoluto sin la query

        boolean useOnLoad = clientDoc.isOnLoadHanderUsed();

        code.append("itsnat.init = function(markupOrig)\n");
        code.append("{ \n");

        code.append("  var url = \"" + url + "?itsnat_action=" + RequestImpl.ITSNAT_ACTION_ATTACH_SERVER + "&itsnat_subaction=load_markup&itsnat_client_id=" + clientDoc.getId() + "&timestamp=\" + new Date().getTime();\n"); // El timestamp es simplemente para evitar cacheos

        if (template.isMIME_HTML())
        {
            // Haciendo document.write nos ahorramos el añadir usando document.body que puede no estar definido si es MIME XHTML en WebKit etc
            // En esta primera fase sí podemos usar document.write() independientemente de lo que
            // diga el valor de useOnLoad, es si se ejecuta el onload cuando no se puede
            code.append("  document.write('<iframe id=\"itsnat_iframe_loader\" name=\"itsnat_iframe_loader\" src=\"about:blank\" style=\"display:none\" onload=\"" + (useOnLoad ? "itsnat.endSendMarkup();" : "") + "\"></iframe>');\n");
            code.append("  document.write('<form id=\"itsnat_form_loader\" action=\"' + url + '\" target=\"itsnat_iframe_loader\" method=\"post\">');\n");
            code.append("  document.write(' <input id=\"itsnat_markup_code\" name=\"itsnat_markup_code\" type=\"hidden\" />');\n");
            code.append("  document.write('</form>');\n");
            code.append("  var iframe = document.getElementById(\"itsnat_iframe_loader\");\n");
            code.append("  var form = document.getElementById(\"itsnat_form_loader\");\n");
            code.append("  var input = document.getElementById(\"itsnat_markup_code\");\n");
        }
        else
        {
            if (template.isMIME_XHTML())
            {
                code.append("var root = document.body;\n");
                code.append("if (!root) root = document.getElementsByTagName(\"body\")[0];\n"); // Caso de MIME XHTML y WebKit
                code.append("var iframe = document.createElement(\"iframe\");\n");
                code.append("var form = document.createElement(\"form\");\n");
                code.append("var input = document.createElement(\"input\");\n");
            }
            else if(template.isMIME_SVG() || template.isMIME_XUL())
            {
                code.append("var root = document.documentElement;\n");
                code.append("var iframe = document.createElementNS(\"http://www.w3.org/1999/xhtml\",\"iframe\");\n");
                code.append("var form = document.createElementNS(\"http://www.w3.org/1999/xhtml\",\"form\");\n");
                code.append("var input = document.createElementNS(\"http://www.w3.org/1999/xhtml\",\"input\");\n");
            }
            else throw new ItsNatException("Unsupported MIME: " + template.getMIME());

            code.append("iframe.id = \"itsnat_iframe_loader\";\n");
            code.append("iframe.name = \"itsnat_iframe_loader\";\n");
            code.append("iframe.src = \"about:blank\";\n");
            code.append("iframe.style.display = \"none\";\n");
            code.append("iframe.onload = " + (useOnLoad ? "function () { itsnat.endSendMarkup(); }" : "null") + ";\n");
            code.append("root.appendChild(iframe);\n");

            code.append("form.id = \"itsnat_form_loader\";\n");
            code.append("form.action = url;\n");
            code.append("form.target = \"itsnat_iframe_loader\";\n");
            code.append("form.method = \"post\";\n");
            code.append("form.appendChild(input);\n");
            code.append("root.appendChild(form);\n");

            code.append("input.type = \"hidden\";\n");
            code.append("input.id = \"itsnat_markup_code\";\n");
            code.append("input.name = \"itsnat_markup_code\";\n");
        }

        code.append("  this.iframe = iframe;\n");
        code.append("  input.value = markupOrig;\n");
        code.append("  form.submit();\n");
        code.append("  form.parentNode.removeChild(form);\n"); // Elimina también el input

        if (!useOnLoad)  // Si no se usa onload podemos llamar aquí directamente
            code.append("this.endSendMarkup();\n");

        code.append("}; \n");

        code.append("itsnat.endSendMarkup = function()\n");
        code.append("{ \n");
        code.append("  var url = \"" + url + "?itsnat_action=" + RequestImpl.ITSNAT_ACTION_ATTACH_SERVER + "&itsnat_subaction=load_doc&itsnat_client_id=" + clientDoc.getId() + "&timestamp=\" + new Date().getTime();\n"); // El timestamp es simplemente para evitar cacheos
        code.append("  var id = \"itsnat_script_loader\";\n");

        if (template.isMIME_HTML() && !useOnLoad)  // Tras el onload el documento está cargado y document.write() ya no vale
        {
            code.append("document.write('<script id=\"' + id + '\" src=\"' + url + '\"><\\/script>');\n"); // Usamos src="..." en vez de src='...' porque encodeURIComponent no "escapea" el caracter: ' (no se si esto es así ahora que se usa escape pero funciona)
            code.append("var script = document.getElementById(id); \n");
        }
        else  // O bien es ejecutado via onload=... o bien no es MIME text/html
        {
            if (template.isMIME_XHTML() || (template.isMIME_HTML() && useOnLoad))
            {
                code.append("var root = document.body;\n");
                code.append("if (!root) root = document.getElementsByTagName(\"body\")[0];\n"); // Caso de MIME XHTML y WebKit
            }
            else // SVG y XUL
            {
                code.append("var root = document.documentElement;\n");
            }

            if (template.isMIME_XHTML() || (template.isMIME_HTML() && useOnLoad))
            {
                code.append("var script = document.createElement(\"script\");\n");
                code.append("script.src = url;\n");
                code.append("script.id = id;\n");
            }
            else if(template.isMIME_SVG())
            {
                code.append("var script = document.createElementNS(root.namespaceURI,\"script\");\n");
                code.append("script.setAttribute(\"type\",\"application/ecmascript\");\n");  // Yo creo que sobra pero por si acaso
                code.append("script.setAttributeNS(\"http://www.w3.org/1999/xlink\",\"href\",url);\n");
                code.append("script.setAttribute(\"id\",id);\n");
            }
            else if(template.isMIME_XUL())
            {
                code.append("var script = document.createElementNS(\"http://www.w3.org/1999/xhtml\",\"script\");\n");
                code.append("script.src = url;\n");
                code.append("script.id = id;\n");
            }
            else throw new ItsNatException("Unsupported MIME: " + template.getMIME());

            code.append("root.appendChild(script);\n");
        }

        code.append("  this.script = script;\n");
        code.append("}; \n");

        code.append("itsnat.clean = function(url)\n");
        code.append("{ \n");
        code.append("  this.iframe.parentNode.removeChild(this.iframe);\n");
        code.append("  this.script.parentNode.removeChild(this.script); \n");
        code.append("}; \n");

        return code.toString();
    }
}
