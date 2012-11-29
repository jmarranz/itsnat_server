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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.CodeToSendListener;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.event.CodeToSendEventImpl;
import org.itsnat.impl.core.event.CodeToSendListenersImpl;
import org.itsnat.impl.core.jsren.dom.node.InnerMarkupCodeImpl;
import org.itsnat.impl.core.listener.WaitForEventListenerImpl;

/**
 *
 * @author jmarranz
 */
public class CodeToSendRegistryImpl implements Serializable
{
    protected transient LinkedList codeToSend = new LinkedList(); // NO se serializa pues de otra manera estaríamos enviando el mismo código desde varias JVM
    protected ClientDocumentImpl clientDoc;
    protected CodeToSendListenersImpl codeToSendListeners;
    protected boolean enabledSendCode = true;

    public CodeToSendRegistryImpl(ClientDocumentImpl clientDoc)
    {
        this.clientDoc = clientDoc;
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        this.codeToSend = new LinkedList();

        in.defaultReadObject();
    }

    public boolean isSendCodeEnabled()
    {
        ItsNatDocumentImpl itsNatDoc = clientDoc.getItsNatDocumentImpl();
        if ((itsNatDoc != null) && !itsNatDoc.isSendCodeEnabled()) return false;
        return enabledSendCode;
    }

    public void disableSendCode()
    {
        this.enabledSendCode = false;
    }

    public void enableSendCode()
    {
        this.enabledSendCode = true;
    }

    public void addCodeToSendListener(CodeToSendListener listener)
    {
        getCodeToSendListeners().addCodeToSendListener(listener);
    }

    public void removeCodeToSendListener(CodeToSendListener listener)
    {
        getCodeToSendListeners().removeCodeToSendListener(listener);
    }

    public boolean hasCodeToSendListeners()
    {
        if (codeToSendListeners == null)
            return false;
        return codeToSendListeners.hasCodeToSendListeners();
    }

    public CodeToSendListenersImpl getCodeToSendListeners()
    {
        if (codeToSendListeners == null)
            this.codeToSendListeners = new CodeToSendListenersImpl(this);
        return codeToSendListeners;
    }

    public Object getLastCodeToSend()
    {
        if (codeToSend.isEmpty()) return null;
        return getLastCodeToSend(codeToSend.getLast());
    }

    protected Object getLastCodeToSend(Object codeFragment)
    {
        if (codeFragment instanceof CodeListImpl)
            return ((CodeListImpl)codeFragment).getLast(this);  // NO puede estar vacío, no tiene sentido        
        else
            return codeFragment;
    }

    public void addCodeToSend(Object code)
    {
        addCodeToSend(codeToSend.size(),code);
    }

    /* Por ahora no se necesita hacerla pública */
    private void addCodeToSend(int index,Object code)
    {
        if (code == null) return; // Nada que hacer

        // code ha de ser o String o JSCodeFragmentImpl
        if (!clientDoc.isScriptingEnabled())
            throw new ItsNatException("Scripting is disabled",this);

        if (!isSendCodeEnabled())
            throw new ItsNatException("Send Code is disabled",this);

        CodeToSendEventImpl event = null;
        if (hasCodeToSendListeners())
        {
            event = getCodeToSendListeners().preProcessCodeToSend(code);
            code = event.getCode();
            if (code == null) return; // Ha sido rechazado
        }

        codeToSend.add(index,code);

        if (event != null)
            getCodeToSendListeners().postProcessCodeToSend(event);
    }

    public String getCodeToSendAndReset()
    {
        if (codeToSend.isEmpty()) return "";

        StringBuffer code = new StringBuffer();
        for(Iterator it = codeToSend.iterator(); it.hasNext(); )
        {
            Object codeFragment = it.next();

            if (codeFragment instanceof WaitForEventListenerImpl)
                break; // Hasta que no se reciba el evento no se envían al cliente los siguientes (la marca también la dejamos)

            code.append( codeToString(codeFragment) );

            it.remove(); // Lo eliminamos
        }

        return code.toString();
    }

    protected String codeToString(Object codeFragment)
    {
        if (codeFragment instanceof InnerMarkupCodeImpl)
        {
            return ((InnerMarkupCodeImpl)codeFragment).render(clientDoc);
        }
        else if (codeFragment instanceof CodeListImpl)
        {
            return ((CodeListImpl)codeFragment).codeToString(this);
        }
        else
            return codeFragment.toString();
    }

    public void removeWaitForEventListener(WaitForEventListenerImpl listener)
    {
        codeToSend.remove(listener);
    }
}
