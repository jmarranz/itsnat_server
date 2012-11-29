/*
 * LoginCreationItsNatComponentListener.java
 *
 * Created on 4 de junio de 2007, 16:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp.custom;

import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.NameValue;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class LoginCreationItsNatComponentListener implements CreateItsNatComponentListener
{
    public LoginCreationItsNatComponentListener()
    {
    }

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

    public ItsNatComponent after(ItsNatComponent comp)
    {
        return comp;
    }
}
