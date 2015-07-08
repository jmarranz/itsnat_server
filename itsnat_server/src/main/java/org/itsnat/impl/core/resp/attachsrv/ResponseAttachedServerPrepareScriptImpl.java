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
public class ResponseAttachedServerPrepareScriptImpl extends ResponseAttachedServerPrepareImpl
{

    /** Creates a new instance of ResponseNormalLoadDocImpl */
    public ResponseAttachedServerPrepareScriptImpl(RequestAttachedServerPrepareImpl request)
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

        code.append("itsnat.init = function(markupOrig)\n");
        code.append("{ \n");
        code.append("  this.markupCode = escape(markupOrig); \n"); // http://xkr.us/articles/javascript/encode-compare/
        // http://support.microsoft.com/kb/208427 2,083 max len URL, hay que reservar espacio para la parte fija de la URL de ahí el valor de sliceLen
        // aunque otros navegadores soporten mayores URL hay que tener en cuenta que el servidor
        // también ha de aceptar URLs muy grandes, por lo que el criterio del IE yo creo que es el universal
        // válido para todos los casos
        code.append("  this.sliceLen = 1800;\n");
        code.append("  this.pos1 = 0;\n");
        code.append("  this.pos2 = Math.min(this.sliceLen,this.markupCode.length);\n");
        code.append("  this.scriptCount = 0;\n");
        code.append("  this.sendMarkupNext(); \n");
        code.append("}; \n");

        code.append("itsnat.contSendMarkup = function()\n");
        code.append("{ \n");
        code.append("  if (this.pos1 < this.markupCode.length) { this.sendMarkupNext(); return true; }\n");
        code.append("  else { this.endSendMarkup(); return false; } \n");
        code.append("}; \n");

        code.append("itsnat.endSendMarkup = function()\n");
        code.append("{ \n");
        code.append("  var url = \"" + url + "?itsnat_action=" + RequestImpl.ITSNAT_ACTION_ATTACH_SERVER + "&itsnat_subaction=load_doc&itsnat_client_id=" + clientDoc.getId() + "&timestamp=\" + new Date().getTime();\n");
        code.append("  this.writeScript(url);\n");
        code.append("}; \n");

        code.append("itsnat.sendMarkupNext = function()\n");
        code.append("{ \n");
        code.append("  var markupPiece = this.markupCode.substring(this.pos1,this.pos2);\n");
        // Sin embargo no podemos dejar un %NN a medias por ello para asegurarnos de que
        // no hemos cogido parcialmente un %NN vemos si el caracter actual es un % o el anterior
        // en ese caso tomamos una substring un poquito más pequeña
        code.append("  var redo = false;\n");
        code.append("  if (this.markupCode.charAt(this.pos2 - 1) == '%') { this.pos2 -= 1; redo = true; }\n");
        code.append("  if (this.markupCode.charAt(this.pos2 - 2) == '%') { this.pos2 -= 2; redo = true; }\n");
        code.append("  if (redo) markupPiece = this.markupCode.substring(this.pos1,this.pos2);\n");

        code.append("  if (typeof this.scriptGlobalCount == \"undefined\") this.scriptGlobalCount = 0;\n");
        code.append("  this.scriptGlobalCount++; "); // Para asegurar totalmente la unicidad en caso de envíos demasiado rápidos

        code.append("  var url = \"" + url + "?itsnat_action=" + RequestImpl.ITSNAT_ACTION_ATTACH_SERVER + "&itsnat_subaction=load_markup&itsnat_client_id=" + clientDoc.getId() + "&timestamp=\" + new Date().getTime() + \"-\" + this.scriptGlobalCount + \"&itsnat_markup_code=\" + markupPiece;\n");
        code.append("  this.writeScript(url);\n");
        code.append("  this.pos1 = this.pos2; this.pos2 += this.sliceLen;\n");
        code.append("  if (this.pos2 > this.markupCode.length) this.pos2 = this.markupCode.length;\n");
        code.append("}; \n");

        code.append("itsnat.writeScript = function(url)\n");
        code.append("{ \n");
        code.append("  var id = \"itsnat_script_loader_\" + this.scriptCount;\n");

        if (template.isMIME_HTML())
        {
            code.append("  document.write('<script id=\"' + id + '\" src=\"' + url + '\"><\\/script>');\n"); // Usamos src="..." en vez de src='...' porque encodeURIComponent no "escapea" el caracter: ' (no se si esto es así ahora que se usa escape pero funciona)
        }
        else
        {
            if (template.isMIME_XHTML())
            {
                code.append("var root = document.body;");
                code.append("if (!root) root = document.getElementsByTagName(\"body\")[0];"); // Caso de MIME XHTML y WebKit
            }
            else // SVG y XUL
            {
                code.append("var root = document.documentElement;");
            }

            if (template.isMIME_XHTML())
            {
                // En WebKit (probado Chrome 5 y Safari 5) no he conseguido
                // que el attachment se haga antes del evento load de la página
                code.append("var script = document.createElement(\"script\");");
                code.append("script.src = url;");
                code.append("script.id = id;");
            }
            else if(template.isMIME_SVG())
            {
                code.append("var script = document.createElementNS(root.namespaceURI,\"script\");");
                code.append("script.setAttribute(\"type\",\"application/ecmascript\");");  // Yo creo que sobra pero por si acaso
                code.append("script.setAttributeNS(\"http://www.w3.org/1999/xlink\",\"href\",url);\n");
                code.append("script.setAttribute(\"id\",id);");
            }
            else if(template.isMIME_XUL())
            {
                code.append("var script = document.createElementNS(\"http://www.w3.org/1999/xhtml\",\"script\");");
                code.append("script.src = url;\n");
                code.append("script.id = id;");
            }
            else throw new ItsNatException("Unsupported MIME: " + template.getMIME());

            code.append("root.appendChild(script);");
        }

        code.append("  this.scriptCount++;\n");
        code.append("}; \n");

        code.append("itsnat.clean = function(url)\n");
        code.append("{ \n");
        code.append("  for(var i = 0; i < this.scriptCount; i++) \n");
        code.append("    { var elem = document.getElementById(\"itsnat_script_loader_\" + i); elem.parentNode.removeChild(elem); } \n");
        code.append("}; \n");

        return code.toString();
    }
}
