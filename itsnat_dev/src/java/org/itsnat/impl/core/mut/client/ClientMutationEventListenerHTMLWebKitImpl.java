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

package org.itsnat.impl.core.mut.client;

import java.util.Map;
import org.itsnat.impl.core.browser.webkit.BrowserWebKit;
import org.itsnat.impl.core.browser.webkit.BrowserWebKitAndroid;
import org.itsnat.impl.core.browser.webkit.BrowserWebKitMoto;
import org.itsnat.impl.core.browser.webkit.BrowserWebKitS40;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public abstract class ClientMutationEventListenerHTMLWebKitImpl extends ClientMutationEventListenerHTMLImpl
{
    public ClientMutationEventListenerHTMLWebKitImpl(ClientDocumentStfulImpl clientDoc)
    {
        super(clientDoc);
    }

    public static ClientMutationEventListenerHTMLWebKitImpl createClientMutationEventListenerHTMLWebKit(ClientDocumentStfulImpl clientDoc)
    {
        BrowserWebKit browser = (BrowserWebKit)clientDoc.getBrowser();
        if (browser instanceof BrowserWebKitS40)
            return new ClientMutationEventListenerHTMLWebKitS40Impl(clientDoc);
        else if (browser instanceof BrowserWebKitAndroid)
            return new ClientMutationEventListenerHTMLWebKitAndroidImpl(clientDoc);
        else if (browser instanceof BrowserWebKitMoto)
            return new ClientMutationEventListenerHTMLWebKitMotoImpl(clientDoc);
        else
            return new ClientMutationEventListenerHTMLWebKitDefaultImpl(clientDoc);
    }

}
