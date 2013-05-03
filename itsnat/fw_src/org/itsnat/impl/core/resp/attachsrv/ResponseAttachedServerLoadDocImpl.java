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

import javax.servlet.ServletResponse;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedServerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.jsren.JSRenderImpl;
import org.itsnat.impl.core.req.attachsrv.RequestAttachedServerLoadDocImpl;
import org.itsnat.impl.core.req.norm.RequestNormalLoadDocAttachedServerImpl;
import org.itsnat.impl.core.resp.ResponseJavaScript;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletResponseImpl;
import org.itsnat.impl.core.servlet.ServletResponseAttachedServer;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateAttachedServerImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseAttachedServerLoadDocImpl extends ResponseAttachedServerImpl implements ResponseJavaScript
{
     /** Creates a new instance of ResponseNormalLoadDocImpl */
    public ResponseAttachedServerLoadDocImpl(RequestAttachedServerLoadDocImpl request)
    {
        super(request);
    }

    public RequestAttachedServerLoadDocImpl getRequestAttachedServerLoadDoc()
    {
        return (RequestAttachedServerLoadDocImpl)request;
    }

    public ClientDocumentAttachedServerImpl getClientDocumentAttachedServer()
    {
        // Lo obtenemos del RequestAttachedServerLoadDocImpl y no llamando
        // getClientDocument() que devuelve el actual del ItsNatServletRequest
        // pues hay que tener en cuenta que hay doble
        // ClientDocument porque hay doble proceso request/response
        // De esta manera tenemos la seguridad de que devuelve el correcto.
        return getRequestAttachedServerLoadDoc().getClientDocumentAttachedServer();
    }

    public void processResponse()
    {
        // El ItsNatServletRequestImpl y el ItsNatServletResponseImpl serán los mismos
        // por lo que en ellos quedará definido el clientDoc en las dos fases del request
        // Modificamos temporalmente el ServletResponse con un wrapper para el que el Writer y el OutputStream
        // recojan la salida como una cadena, pues dicha cadena la tenemos que meter
        // en un document.write en código JavaScript.

        ClientDocumentAttachedServerImpl clientDocAS = getClientDocumentAttachedServer();

        ItsNatStfulDocumentTemplateAttachedServerImpl template = clientDocAS.getItsNatStfulDocumentTemplateAttachedServer();

        ItsNatServletResponseImpl itsNatResponse = getItsNatServletResponse();
        ItsNatServletRequestImpl itsNatRequest = itsNatResponse.getItsNatServletRequestImpl();

        ServletResponse responseOriginal = itsNatResponse.getServletResponse();
        ServletResponseAttachedServer servResWrapper = itsNatResponse.createServletResponseAttachedServer();
        itsNatResponse.setServletResponse(servResWrapper);

        RequestNormalLoadDocAttachedServerImpl delegRequest = new RequestNormalLoadDocAttachedServerImpl(template,getRequestAttachedServerLoadDoc(),itsNatRequest);
        itsNatRequest.setRequest(delegRequest); // Para que al procesar el request el ItsNatServletRequestImpl esté correctamente conectado a request de carga normal no al attached y el código del usuario obtenga el ClientDocument normal por ejemplo
        delegRequest.process(null);
        ClientDocumentStfulImpl clientDocNormal = delegRequest.getClientDocumentStful();
        Browser browser = clientDocNormal.getBrowser();

        itsNatResponse.setServletResponse(responseOriginal); // restauramos

        // Los objetos ItsNatServletRequestImpl y ItsNatServletResponseImpl quedan vinculados
        // a los objetos RequestImpl y ResponseImpl de la carga normal y está bien así

        String resultMarkup = servResWrapper.getString(delegRequest.getResponse().getEncoding());

        StringBuilder code = new StringBuilder();
        code.append("itsnat.clean();\n");
        code.append("try{ delete window.itsnat; }catch(ex){}\n"); // Ya ha cumplido su función

        boolean useOnLoad = clientDocAS.isOnLoadHanderUsed();

        // Sabemos que el markup son dos <script> con el código inline
        if (template.isMIME_HTML() && !useOnLoad)
        {
            String resultMarkupStringLiteral = JSRenderImpl.toTransportableStringLiteral(resultMarkup, true, browser);
            code.append( "document.write(" + resultMarkupStringLiteral + ");\n" );
        }
        else // Caso de useOnLoad o no HTML MIME
        {
            // Es un poco absurdo volver al DOM pero es lo mejor
            // document.write no vale.
            ItsNatStfulDocumentImpl itsNatDoc = clientDocNormal.getItsNatStfulDocument();
            DocumentFragment docFrag = itsNatDoc.toDOM(resultMarkup);

            if (template.isMIME_XHTML() || (template.isMIME_HTML() && useOnLoad))
            {
                code.append("var root = document.body;\n");
                code.append("if (!root) root = document.getElementsByTagName(\"body\")[0];\n"); // Caso de MIME XHTML y WebKit
            }
            else // SVG y XUL
            {
                code.append("var root = document.documentElement;\n");
            }

            Element script = (Element)docFrag.getFirstChild();
            while(script != null)
            {
                CharacterData text = (CharacterData)script.getFirstChild();
                String scriptCode = text.getData(); // Si es un nodo Text puede tener el CDATA como parte del texto
                String prefix = "<![CDATA[";
                String suffix = "]]>";
                if (scriptCode.startsWith(prefix))
                    scriptCode = MarkupTemplateVersionImpl.removePrefixSuffix(scriptCode,prefix,suffix);
                String scriptCodeLiteral = JSRenderImpl.toTransportableStringLiteral(scriptCode, true, browser);

                if (template.isMIME_HTML() && useOnLoad)
                {
                    code.append("var script = document.createElement(\"script\");\n");
                    code.append("script.setAttribute(\"id\",\"" + script.getAttribute("id") + "\");\n");
                    if (clientDocAS.getBrowser() instanceof BrowserMSIEOld)
                    {
                        // Esto es porque haciendo script.text = ... da error al insertar (se cae el MSIE)
                        // yo creo que la culpa la tiene la eliminación del <script> desde código
                        // de dentro del <script> insertado via appendChild (no ocurre si es cargado via markup o via document.write())
                        // El evento onload de la página no es ejecutado.
                        code.append("root.appendChild(script);");
                        code.append("eval(" + scriptCodeLiteral + ");\n");
                    }
                    else
                    {
                        code.append("script.appendChild(document.createTextNode(" + scriptCodeLiteral + "));");
                        code.append("root.appendChild(script);");
                    }
                }
                else if(template.isMIME_XHTML() || (template.isMIME_HTML() && useOnLoad))
                {
                    code.append("var script = document.createElement(\"script\");");
                    code.append("script.setAttribute(\"id\",\"" + script.getAttribute("id") + "\");");
                    code.append("script.appendChild(document.createCDATASection(" + scriptCodeLiteral + "));");
                    code.append("root.appendChild(script);");
                }
                else if(template.isMIME_SVG())
                {
                    code.append("var script = document.createElementNS(root.namespaceURI,\"script\");");
                    code.append("script.setAttribute(\"type\",\"application/ecmascript\");");  // Yo creo que sobra pero por si acaso
                    code.append("script.setAttribute(\"id\",\"" + script.getAttribute("id") + "\");");
                    code.append("script.appendChild(document.createCDATASection(" + scriptCodeLiteral + "));");
                    code.append("root.appendChild(script);");
                }
                else if(template.isMIME_XUL())
                {
                    code.append("var script = document.createElementNS(\"http://www.w3.org/1999/xhtml\",\"script\");");
                    code.append("script.setAttribute(\"id\",\"" + script.getAttribute("id") + "\");");
                    code.append("script.appendChild(document.createCDATASection(" + scriptCodeLiteral + "));");
                    code.append("root.appendChild(script);");
                }
                else throw new ItsNatException("Unsupported MIME: " + template.getMIME());


                script = (Element)script.getNextSibling();
            }

        }

        writeResponse(code.toString());
    }

    public boolean isLoadByScriptElement()
    {
        return true;
    }
}
