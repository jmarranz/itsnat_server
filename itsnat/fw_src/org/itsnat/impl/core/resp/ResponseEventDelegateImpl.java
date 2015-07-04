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

package org.itsnat.impl.core.resp;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletRequest;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.droid.BrowserDroid;
import org.itsnat.impl.core.browser.web.BrowserWeb;

/**
 *
 * @author jmarranz
 */
public abstract class ResponseEventDelegateImpl
{
    protected ResponseImpl response;
    protected String scriptId = null; // Sólo es no nulo en los modos SCRIPT y SCRIPT_HOLD

    public ResponseEventDelegateImpl(ResponseImpl response)
    {
        this.response = response;

        ServletRequest servRequest = response.getRequest().getItsNatServletRequest().getServletRequest();
        this.scriptId = servRequest.getParameter("itsnat_script_evt_id");
    }

    public static ResponseEventDelegateImpl createResponseEventDelegate(ResponseImpl response)
    {
        Browser browser = response.getClientDocument().getBrowser();
        if (browser instanceof BrowserWeb)
            return new ResponseEventDelegateWebImpl(response);
        else if (browser instanceof BrowserDroid)
            return new ResponseEventDelegateDroidImpl(response);
        return null;
    }
    
    protected void processResponseOnException(RuntimeException ex) 
    {
        if (isScriptOrScriptHoldMode())
        {
            // Modos SCRIPT y SCRIPT_HOLD

            // La situación es la siguiente: en modos SCRIPT y SCRIPT_HOLD (no AJAX) si dejamos
            // que la excepción llegue al servidor de servlets dará una respuesta de error al cliente
            // el cual obviamente no cargará/ejecutará el texto de la excepción via <script>
            // el problema es que tanto en FireFox como Opera el onload
            // no se ejecuta (MSIE no probado, en Chrome funciona) quizás porque consideran que
            // el onload sólo ha de ejecutarse cuando ciertamente el script se ha cargado, la posible
            // alternativa onerror tampoco (no tengo claro ni si existe en general).

            ex.printStackTrace();

            StringWriter writer = new StringWriter();
            PrintWriter str = new PrintWriter(writer);
            ex.printStackTrace(str);
            str.flush();

            sendPendingCode(writer.toString(),true);
        }
        else // AJAX
        {
            throw ex;
        }
    }

    public void sendPendingCode()
    {
        String code = response.getCodeToSendAndReset();
        sendPendingCode(code,false);
    }

    public boolean isScriptOrScriptHoldMode()
    {
        return (scriptId != null);
    }

    public abstract void sendPendingCode(String code,boolean error);
    

    public boolean isLoadByScriptElement()
    {
        return isScriptOrScriptHoldMode();
    }

}
