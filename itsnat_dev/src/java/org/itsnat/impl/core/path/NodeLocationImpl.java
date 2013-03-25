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

package org.itsnat.impl.core.path;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.JSRenderImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class NodeLocationImpl
{
    protected ClientDocumentStfulImpl clientDoc;
    protected String id;
    protected String path;
    protected Node node;
    protected boolean used = false;

    /** Creates a new instance of NodeLocationImpl */
    public NodeLocationImpl(Node node,String id,String path,ClientDocumentStfulImpl clientDoc)
    {
        this.node = node;
        this.id = id;
        this.path = path;
        this.clientDoc = clientDoc;

        if ((node != null) && isNull(id) && isNull(path))
            throw new ItsNatException("Node not bound to document tree",node);
    }

    public void throwNullException()
    {
        if (node == null) throw new ItsNatException("No specified node");
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return clientDoc;
    }

    protected void finalize() throws Throwable
    {
        super.finalize();

        if (!used)
        {
            // Esto ayuda al programador final cuando genera código JavaScript y no envia al cliente,
            // pero también puede ayudar en el desarrollo del framework, pues si se detectara
            // esta excepción el paso siguiente podría ser guardar un Exception creado en los constructores
            // para recordar cual fue el contexto en el que se creó.
            throw new ItsNatException("Some nodes have been cached in server but not in client, generated JavaScript code was not sent to the client.");
            //ex.printStackTrace();
            //throw ex;
        }
    }

    public Node getNode()
    {
        return node;
    }

    protected boolean isNull(String str)
    {
        return ((str == null) || str.equals("null"));
    }

    protected boolean isAlreadyCached()
    {
        return !isNull(id) && isNull(path);
    }

    public boolean isCached()
    {
        // O ya estaba cacheado o se acaba de cachear
        return !isNull(id);
    }

    private String getId()
    {
        return id;
    }

    private String getPath()
    {
        return path;
    }

    protected String getIdJS()
    {
        return JSRenderImpl.toLiteralStringJS(getId());
    }

    /* Este método no se necesita fuera */
    protected String getPathJS()
    {
        return JSRenderImpl.toLiteralStringJS(getPath());
    }
/*
    public String toJSArray()
    {
        return toJSArray(true);
    }
*/
    public abstract String toJSNodeLocation(boolean errIfNull);

}
