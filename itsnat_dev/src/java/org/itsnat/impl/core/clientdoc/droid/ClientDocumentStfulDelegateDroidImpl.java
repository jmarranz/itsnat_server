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

package org.itsnat.impl.core.clientdoc.droid;

import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.core.browser.droid.BrowserDroid;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class ClientDocumentStfulDelegateDroidImpl extends ClientDocumentStfulDelegateImpl
{
    public ClientDocumentStfulDelegateDroidImpl(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
    }
    
    public BrowserDroid getBrowserDroid()
    {
        return (BrowserDroid)clientDoc.getBrowser();
    }    
    
    public ScriptUtil createScriptUtil()    
    {
        return null; // HACER
    }

    @Override
    public boolean dispatchEvent(EventTarget target, Event evt, int commMode, long eventTimeout) throws EventException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
