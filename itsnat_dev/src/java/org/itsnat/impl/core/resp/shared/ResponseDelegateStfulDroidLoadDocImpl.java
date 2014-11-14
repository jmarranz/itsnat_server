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

import java.util.List;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.droid.ItsNatStfulDroidDocumentImpl;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.template.droid.ScriptCode;
import org.itsnat.impl.core.template.droid.ScriptWithSrc;

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

    public ItsNatStfulDroidDocumentImpl getItsNatStfulDroidDocument()
    {
        return (ItsNatStfulDroidDocumentImpl)getItsNatStfulDocument();
    }
    
    protected void rewriteClientUIControlProperties()
    {
        // En Android no hay autofill etc
    }

    @Override
    protected String addMarkupToTheEndOfDoc(String docMarkup, String newMarkup)
    {
        StringBuilder finalMarkup = new StringBuilder();

        int posRootTagEnd = docMarkup.lastIndexOf('<');
        String preScript = docMarkup.substring(0,posRootTagEnd);
        String posScript = docMarkup.substring(posRootTagEnd);

        finalMarkup.append(preScript);
        finalMarkup.append(newMarkup);
        finalMarkup.append(posScript);

        return finalMarkup.toString();
    }
    
    protected String getInitDocumentScriptCode(final int prevScriptsToRemove)    
    {
        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();        
        ItsNatStfulDocumentImpl itsNatDoc = getItsNatStfulDocument();        
        ItsNatSessionImpl session = clientDoc.getItsNatSessionImpl();
        String stdSessionId = session.getStandardSessionId();  
        String token = session.getToken(); 
        String sessionId = session.getId();        
        String clientId =  clientDoc.getId();
        String servletPath = delegByBrowser.getServletPathForEvents();
        int errorMode = itsNatDoc.getClientErrorMode(); 
        boolean eventsEnabled = itsNatDoc.isEventsEnabled();
        
        String attachType = null;
        if (clientDoc instanceof ClientDocumentAttachedClientImpl)
        {
            attachType = ((ClientDocumentAttachedClientImpl)clientDoc).getAttachType();
        }        
        
        return "itsNatDoc.init(\"" + stdSessionId + "\",\"" + token + "\",\"" + sessionId + "\",\"" + clientId + "\",\"" + servletPath + "\"," + errorMode + ",\"" + attachType + "\"," + eventsEnabled + ");\n"; // HACER
    }
    
    @Override     
    protected String addRequiredMarkupToTheEndOfDoc(String docMarkup)       
    {
        docMarkup = super.addRequiredMarkupToTheEndOfDoc(docMarkup);
        
        StringBuilder markup = new StringBuilder();  
        
        ItsNatStfulDroidDocumentImpl itsNatDoc = getItsNatStfulDroidDocument();           
        List<ScriptCode> scriptCodeList = itsNatDoc.getScriptCodeList();
        
        if (!scriptCodeList.isEmpty())
        {
            // Hay que tener en cuenta que los quitamos del DOM en el template pero como tenemos los scripts los enviamos "recreando" los script
            for(ScriptCode script : scriptCodeList)
            {
                if (ItsNatStfulDroidDocumentImpl.PRELOAD_SCRIPTS)
                {
                    markup.append( "<script><![CDATA[ " + script.getCode() + " ]]></script>" );
                }
                else
                {
                    if (script instanceof ScriptWithSrc)
                        markup.append( "<script src=\"" + ((ScriptWithSrc)script).getSrc() + "\"></script>" );
                    else
                        markup.append( "<script><![CDATA[ " + script.getCode() + " ]]></script>" );
                }                    
            }
        }      

        return addMarkupToTheEndOfDoc(docMarkup, markup.toString());
    }    
    
    @Override
    protected String generateFinalScriptsMarkup()
    {
        StringBuilder markup = new StringBuilder();        

        // NO CAMBIAR NI UN ESPACIO, este patrón se usa en el código cliente Android para localizar el script de carga
        markup.append( "<script id=\"itsnat_load_script\"><![CDATA[ " + getInitScriptContentCode(1) + " ]]></script>" );
        
        return markup.toString();
    }
}
