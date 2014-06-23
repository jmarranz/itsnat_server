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

package org.itsnat.impl.core.resp.shared;

import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateStfulDroidLoadDocImpl extends ResponseDelegateStfulLoadDocImpl
{
    public ResponseDelegateStfulDroidLoadDocImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    protected void rewriteClientUIControlProperties()
    {
        // En Android no hay autofill etc
    }

    @Override
    protected String addScriptMarkupToDocMarkup(String docMarkup, String scriptsMarkup)
    {
        // REVISAR
        StringBuilder finalMarkup = new StringBuilder();

        int posRootTagEnd = docMarkup.lastIndexOf('<');
        String preScript = docMarkup.substring(0,posRootTagEnd);
        String posScript = docMarkup.substring(posRootTagEnd);

        finalMarkup.append(preScript);
        finalMarkup.append(scriptsMarkup);
        finalMarkup.append(posScript);

        return finalMarkup.toString();
    }
    
    protected String getInitDocumentScriptCode(final int prevScriptsToRemove)    
    {
        ItsNatSessionImpl session = getClientDocumentStful().getItsNatSessionImpl();
        String sessionId = session.getStandardSessionId();       
        String clientId =  getClientDocumentStful().getId();
        return "itsNatDoc.init(\"" + sessionId + "\",\"" + clientId + "\");"; // HACER
    }
    
    @Override
    protected String generateFinalScriptsMarkup()
    {
        return "<script><![CDATA[ " + getInitScriptContentCode(1) + " ]]></script>";
    }
}
