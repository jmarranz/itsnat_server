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

package org.itsnat.impl.comp.android.view;

import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.comp.android.graphics.drawable.ClipDrawable;
import org.itsnat.comp.android.graphics.drawable.Drawable;
import org.itsnat.comp.android.view.View;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.ItsNatElementComponentImpl;
import org.itsnat.impl.comp.android.graphics.drawable.ClipDrawableImpl;
import org.itsnat.impl.comp.android.graphics.drawable.DrawableImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocImpl;
import org.itsnat.impl.comp.mgr.droid.ItsNatStfulDroidDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.droid.ItsNatStfulDroidDocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ViewImpl extends ItsNatElementComponentImpl implements View
{   
    /** Creates a new instance of CheckBoxImpl */
    public ViewImpl(Element element,NameValue[] artifacts,ItsNatStfulDroidDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);
    }

    public Document getDocument()
    {
        return getItsNatDocument().getDocument();
    }
    
    public ItsNatStfulDroidDocumentImpl getItsNatStfulDroidDocument()            
    {
        return (ItsNatStfulDroidDocumentImpl)getItsNatDocumentImpl();
    }
    
    @Override
    public Object createDefaultStructure()
    {
        return null;
    }    

    @Override
    public ItsNatCompNormalEventListenersByDocImpl createItsNatCompNormalEventListenersByDoc()
    {
        return new ItsNatCompNormalEventListenersByDocDefaultImpl(this);
    }

    @Override
    public ItsNatCompNormalEventListenersByClientImpl createItsNatCompNormalEventListenersByClient(ClientDocumentImpl clientDoc)
    {
        return new ItsNatCompNormalEventListenersByClientDefaultImpl(this,clientDoc);
    }

    @Override
    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return null;
    }

    @Override
    public ParamTransport[] getInternalParamTransports(String type, ClientDocumentImpl clientDoc)
    {
        return null;
    }

    @Override
    public void bindDataModel()
    {
    }

    @Override
    public void unbindDataModel()
    {
    }

    @Override
    public void initialSyncUIWithDataModel()
    {
    }

    @Override
    public Object createDefaultModelInternal()
    {
        return null;
    }

    protected String getNodeReference()
    {
        return getItsNatDocument().getScriptUtil().getNodeReference(getElement());
    }
    
    @Override
    public Node createDefaultNode()
    {
        return getDocument().createElement(getClassName());
    }
    
    public String getAndroidNamespacePrefix()
    {
        ItsNatStfulDroidDocumentImpl itsNatDoc = getItsNatStfulDroidDocument();
        return itsNatDoc.getItsNatStfulDroidDocumentTemplateVersion().getAndroidNamespacePrefix();
    }    
    
    public abstract String getClassName();

    @Override
    public <T extends Drawable> T getBackground(Class<T> clasz)     
    {
        return (T)getDrawable(clasz,"getBackground");
    }
    
    @Override
    public <T extends Drawable> T getForeground(Class<T> clasz)    
    {
        return (T)getDrawable(clasz,"getForeground");
    }   
    
    private Drawable getDrawable(Class clasz,String methodCalled)
    {
        if (ClipDrawable.class.equals(clasz))
            return new ClipDrawableImpl(this,methodCalled);
        return null;
    }    
}
