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

package org.itsnat.core;

import org.w3c.dom.Node;

/**
 * Is throw by ItsNat when a DOM node is the source of the error.
 *
 * @author Jose Maria Arranz Santamaria
 */
public class ItsNatDOMException extends ItsNatException
{

    /**
     * Creates a new instance.
     *
     * @param ex the cause of this exception, is submitted to the parent exception.
     * @param node the node involved.
     */
    public ItsNatDOMException(Throwable ex,Node node)
    {
        super(ex,node);
    }

    /**
     * Creates a new instance.
     *
     * @param message the detail message, is submitted to the parent exception.
     * @param node the node involved.
     */
    public ItsNatDOMException(String message,Node node)
    {
        super(message,node);
    }

    /**
     * Creates a new instance.
     *
     * @param message the detail message, is submitted to the parent exception.
     * @param ex the cause of this exception, is submitted to the parent exception.
     * @param node the node involved.
     */
    public ItsNatDOMException(String message,Throwable ex,Node node)
    {
        super(message,ex,node);
    }

    /**
     * Returns the node involved.
     *
     * @return the node involved.
     */
    public Node getOffendingNode()
    {
        return (Node)getContext();
    }
}
