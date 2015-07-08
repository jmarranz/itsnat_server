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

package org.itsnat.comp;

import org.itsnat.core.NameValue;
import org.w3c.dom.Node;

/**
 * This listener type is used to add new user defined components to the framework.
 * If it was previously registered the listener is called when the framework needs
 * to create a component instance.
 *
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatComponentManager#createItsNatComponent(Node,String,NameValue[])
 * @see org.itsnat.core.tmpl.ItsNatDocumentTemplate#addCreateItsNatComponentListener(CreateItsNatComponentListener)
 */
public interface CreateItsNatComponentListener
{
    /**
     * Is called <i>before</i> calling the default component
     * factory of the framework. This is an opportunity to create a user defined component
     * because the returned object will be used by the framework if non-null, if returned null the framework
     * will use the next user defined factory if any or the default factory.
     *
     * <p>Method parameters are the same as the factory method
     * {@link ItsNatComponentManager#createItsNatComponent(Node,String,NameValue[])}
     * </p>
     *
     *
     * @param node the node to associate the new component.
     * @param compType the component type, may be null.
     * @param artifacts declared artifacts, may be null.
     * @param compMgr the component manager used to create the component.
     * @return the new component, if null the framework will use the next user defined factory if any or the default factory.
     */
    public ItsNatComponent before(Node node,String compType,NameValue[] artifacts,ItsNatComponentManager compMgr);

    /**
     * Is called <i>after</i> a component was created (user or framework defined).
     * This is the opportunity to initialize the component before is returned to normal use.
     * In fact nothing prevents to return a different component (component replaced)
     * or return null (component rejected).
     *
     * @param comp the component to initialize/replace/reject, never is null.
     * @return the same component, a different component or null.
     */
    public ItsNatComponent after(ItsNatComponent comp);
}
