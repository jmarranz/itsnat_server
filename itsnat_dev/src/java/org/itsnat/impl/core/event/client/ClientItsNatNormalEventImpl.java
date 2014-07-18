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

package org.itsnat.impl.core.event.client;

import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatNormalEvent;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.itsnat.impl.core.util.MiscUtil;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ClientItsNatNormalEventImpl extends ClientItsNatEventStfulImpl implements ItsNatNormalEvent
{
    protected ItsNatNormalEventListenerWrapperImpl listenerWrapper;

    /**
     * Creates a new instance of ClientItsNatNormalEventImpl
     */
    public ClientItsNatNormalEventImpl(ItsNatNormalEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(request);

        this.listenerWrapper = listenerWrapper;
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return (ClientDocumentStfulImpl)getClientDocumentImpl();
    }

    public RequestNormalEventImpl getRequestNormalEvent()
    {
        return (RequestNormalEventImpl)getRequestStfulDocument();
    }

    public void resolveNodePaths()
    {
        // Este método hay que llamarlo lo antes posible para resolver
        // los paths de los nodos cuando dichos nodos no están cacheados,
        // antes de que al ejecutar los listeners se cambie la estructura
        // del DOM servidor y los paths no sean válidos.
        // Derivar y resolver los nodos concretos.
    }

    public int getCommModeDeclared()
    {
        return listenerWrapper.getCommModeDeclared();
    }

    public ItsNatNormalEventListenerWrapperImpl getNormalEventListenerWrapper()
    {
        return (ItsNatNormalEventListenerWrapperImpl)listenerWrapper;
    }

    public String getType()
    {
        return getNormalEventListenerWrapper().getType();
    }
    
    public static String getParameter(RequestNormalEventImpl request,String name)
    {
        name = "itsnat_evt_" + name;
        String param = request.getAttrOrParam(name);
        if (param == null)
            throw new ItsNatException(name + " parameter is not specified",request.getItsNatServletRequest());
        return param;
    }

    public String getParameter(String name)
    {
        return getParameter(getRequestNormalEvent(),name);
    }

    public boolean getParameterBoolean(String name)
    {
        // Usamos getBooleanRelaxed que no provoca error si por ejemplo
        // el parámetro es "undefined".
        return MiscUtil.getBooleanRelaxed(getParameter(name));
    }

    public short getParameterShort(String name)
    {
        return Short.parseShort(getParameter(name));
    }

    public int getParameterInt(String name)
    {
        return Integer.parseInt(getParameter(name));
    }

    public long getParameterLong(String name)
    {
        return Long.parseLong(getParameter(name));
    }

    public Node getParameterNode(String name)
    {
        return getParameterNode(name,true);
    }

    public Node getParameterNode(String name,boolean cacheIfPossible)
    {
        String path = getParameter(name);
        ClientDocumentStfulDelegateImpl clientDoc = getClientDocumentStful().getClientDocumentStfulDelegate();
        return clientDoc.getNodeFromStringPathFromClient(path,cacheIfPossible);
    }
    
}
