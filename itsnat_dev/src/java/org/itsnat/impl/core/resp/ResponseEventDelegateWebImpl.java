/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.resp;

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.browser.web.webkit.BrowserWebKit;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.scriptren.jsren.JSRenderImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseEventDelegateWebImpl extends ResponseEventDelegateImpl
{
    public ResponseEventDelegateWebImpl(ResponseImpl response)
    {
        super(response);
    }
    
    @Override
    public void sendPendingCode(String code,boolean error)
    {
        if (isScriptOrScriptHoldMode())
        {
            // Modos SCRIPT y SCRIPT_HOLD

            Browser browser = response.getClientDocument().getBrowser();

            StringBuilder codeBuff = new StringBuilder();

            codeBuff.append("var elem = document.getElementById(\"" + scriptId + "\");\n"); // elem es el <script> cargador del script
            codeBuff.append("if (elem != null)"); // elem puede ser null cuando hay un timeout en el cliente y se ha eliminado el <script> y por alguna razón (extraña) se ha cargado y ejecutado el script (REVISAR)
            codeBuff.append("{\n");

            codeBuff.append("  elem.executed = true;\n");
            if (error)
            {
                codeBuff.append("  elem.error = true;\n");
                codeBuff.append("  elem.code = " + JSRenderImpl.toTransportableStringLiteral(code,browser) + ";\n");
            }
            else
            {
                if (browser instanceof BrowserMSIEOld)
                {
                    // Esto es porque al eliminar en el cliente el <script> la función JavaScript
                    // contenida se invalida
                    codeBuff.append("  elem.code = " + JSRenderImpl.toTransportableStringLiteral(code,browser) + ";\n");
                }
                else
                {
                    codeBuff.append("  elem.code = function (event,itsNatDoc)\n"); // Los mismos parámetros que processRespValid
                    codeBuff.append("   {\n");
                    codeBuff.append( code );
                    codeBuff.append("   };\n");
                }
            }

            codeBuff.append("}\n");

            code = codeBuff.toString();
        }

        if (code.length() == 0)
        {   // Este caso obviamente sólo se dará en eventos AJAX
            // por si acaso lo hacemos también con eventos SCRIPT
            ClientDocumentImpl clientDoc = response.getClientDocument();
            Browser browser = clientDoc.getBrowser();
            if ((browser instanceof BrowserWebKit) &&
                ((BrowserWebKit)browser).isAJAXEmptyResponseFails())
            {
                code = "          ";
            }
        }
        
        response.writeResponse(code);
    }
    
}
