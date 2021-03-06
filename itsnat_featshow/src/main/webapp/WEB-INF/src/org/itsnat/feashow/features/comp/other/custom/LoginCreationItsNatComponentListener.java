/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.comp.other.custom;

import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.NameValue;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class LoginCreationItsNatComponentListener implements CreateItsNatComponentListener
{

    public LoginCreationItsNatComponentListener()
    {
    }

    @Override
    public ItsNatComponent before(Node node, String componentType, NameValue[] artifacts, ItsNatComponentManager compMgr)
    {
        if (node == null) return null;

        if (node.getNodeType() != Node.ELEMENT_NODE)
            return null;

        Element elem = (Element)node;

        if ((componentType != null) && componentType.equals("login"))
            return new LoginComponent(elem,compMgr);

        return null;
    }

    @Override
    public ItsNatComponent after(ItsNatComponent comp)
    {
        return comp;
    }

}
