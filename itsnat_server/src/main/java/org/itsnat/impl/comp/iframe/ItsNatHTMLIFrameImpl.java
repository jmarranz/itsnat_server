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

package org.itsnat.impl.comp.iframe;

import java.util.LinkedList;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.comp.iframe.HTMLIFrameFileUpload;
import org.itsnat.comp.iframe.ItsNatHTMLIFrame;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.ItsNatHTMLElementComponentImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocImpl;
import org.itsnat.impl.comp.mgr.web.ItsNatStfulWebDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLIFrameImpl extends ItsNatHTMLElementComponentImpl implements ItsNatHTMLIFrame
{
    protected boolean enabled = true;
    protected LinkedList<HTMLIFrameFileUploadImpl> fileUploadList;

    /**
     * Creates a new instance of ItsNatHTMLIFrameImpl
     */
    public ItsNatHTMLIFrameImpl(HTMLIFrameElement element,NameValue[] artifacts,ItsNatStfulWebDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        init();
    }

    @Override
    protected void disposeEffective(boolean updateClient)
    {
        super.disposeEffective(updateClient);

        if (fileUploadList != null)
        {
            while(!fileUploadList.isEmpty())
            {
                HTMLIFrameFileUploadImpl fileUpload = fileUploadList.getFirst();
                fileUpload.dispose(); // Se quita él solo de la lista
            }
        }
    }

    public ItsNatCompNormalEventListenersByDocImpl createItsNatCompNormalEventListenersByDoc()
    {
        return new ItsNatCompNormalEventListenersByDocDefaultImpl(this);
    }

    public ItsNatCompNormalEventListenersByClientImpl createItsNatCompNormalEventListenersByClient(ClientDocumentImpl clientDoc)
    {
        return new ItsNatCompNormalEventListenersByClientDefaultImpl(this,clientDoc);
    }

    public HTMLIFrameElement getHTMLIFrameElement()
    {
        return (HTMLIFrameElement)node;
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return null;
    }

    public void bindDataModel()
    {
        // No hay modelo
    }

    public void unbindDataModel()
    {
        // No hay modelo
    }

    public Object createDefaultModelInternal()
    {
        return null; // No hay modelo
    }

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        return null;
    }

    public void initialSyncUIWithDataModel()
    {
        // no hay modelo
    }

    public Node createDefaultNode()
    {
        Document doc = getItsNatDocument().getDocument();
        HTMLIFrameElement iframe = (HTMLIFrameElement)doc.createElementNS(NamespaceUtil.XHTML_NAMESPACE,"iframe");
        return iframe;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean b)
    {
        this.enabled = b;
    }

    public HTMLIFrameFileUpload getHTMLIFrameFileUpload(HTMLInputElement elem)
    {
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocumentImpl();
        ClientDocumentStfulImpl clientDoc = itsNatDoc.getRequestingClientDocumentStful();
        return getHTMLIFrameFileUpload(clientDoc,elem);
    }

    public HTMLIFrameFileUpload getHTMLIFrameFileUpload(ClientDocument clientDoc,HTMLInputElement elem)
    {
        return new HTMLIFrameFileUploadImpl(this,elem,(ClientDocumentStfulDelegateWebImpl)((ClientDocumentStfulImpl)clientDoc).getClientDocumentStfulDelegate());
    }

    public LinkedList<HTMLIFrameFileUploadImpl> getHTMLIFrameFileUploadList()
    {
        if (fileUploadList == null)
            this.fileUploadList = new LinkedList<HTMLIFrameFileUploadImpl>(); // Así ahorramos memoria si no se usa
        return fileUploadList;
    }

    public void addHTMLIFrameFileUploadImpl(HTMLIFrameFileUploadImpl upload)
    {
        getHTMLIFrameFileUploadList().add(upload);
    }

    public void removeHTMLIFrameFileUploadImpl(HTMLIFrameFileUploadImpl upload)
    {
        getHTMLIFrameFileUploadList().remove(upload);
    }
}
