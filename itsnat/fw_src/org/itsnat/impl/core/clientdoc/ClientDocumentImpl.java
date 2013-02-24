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

package org.itsnat.impl.core.clientdoc;

import java.util.LinkedList;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatSession;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.*;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.util.HasUniqueId;
import org.itsnat.impl.core.util.UniqueId;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public abstract class ClientDocumentImpl extends ItsNatUserDataImpl implements ClientDocument,HasUniqueId
{
    protected UniqueId idObj;
    protected ItsNatSessionImpl session;
    protected long creationTime = System.currentTimeMillis();
    protected boolean invalid = false;
    protected long lastEventTime = creationTime;
    protected Browser browser;
    
    /**
     * Creates a new instance of ClientDocumentImpl
     */
    public ClientDocumentImpl(Browser browser,ItsNatSessionImpl session)
    {
        super(false);

        this.idObj = session.getUniqueIdGenerator().generateUniqueId("cd"); // cd = client document
        this.session = session;   
        this.browser = browser;
    }

    public abstract boolean isScriptingEnabled();

    public Browser getBrowser()
    {
        return browser;
    }

    public ItsNatSession getItsNatSession()
    {
        return session;
    }

    public ItsNatSessionImpl getItsNatSessionImpl()
    {
        return session;
    }

    public String getId()
    {
        return idObj.getId();
    }

    public UniqueId getUniqueId()
    {
        return idObj;
    }

    public boolean isInvalid()
    {
        return invalid;
    }

    public void setInvalid()
    {
        if (invalid) return; // Ya está invalidado

        setInvalidInternal();
    }

    protected void setInvalidInternal()
    {
        this.invalid = true;
    }

    public abstract ItsNatDocumentImpl getItsNatDocumentImpl();


    public ItsNatDocument getItsNatDocument()
    {
        return getItsNatDocumentImpl();
    }

    public long getCreationTime()
    {
        return creationTime;
    }

    public long getLastRequestTime()
    {
        return lastEventTime;
    }

    public abstract String getCodeToSendAndReset();

    public abstract void registerInSession();

    // Este métodos NO HACER públicos
    public abstract void addMutationEventListener(EventTarget target,EventListener listener,boolean useCapture,int commMode,String preSendCode,long eventTimeout,String bindToListener);
    public abstract void addEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener);
    public abstract void addUserEventListener(EventTarget target,String name,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout,String bindToListener);
    public abstract void removeEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,boolean updateClient);

    public abstract void getGlobalEventListenerList(LinkedList<EventListener> list);
}
