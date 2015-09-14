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

package org.itsnat.feashow.features.comp.other.customtag;

import java.io.File;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.NameValue;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class LoginTagCreationItsNatComponentListener implements CreateItsNatComponentListener
{

    public LoginTagCreationItsNatComponentListener()
    {
    }

    @Override
    public ItsNatComponent before(Node node, String componentType, NameValue[] artifacts, ItsNatComponentManager compMgr)
    {
        if (node == null) return null;

        if (node.getNodeType() != Node.ELEMENT_NODE)
            return null;

        Element elem = (Element)node;

        if ((componentType != null) && componentType.equals("loginTag"))
            return new LoginTagComponent(elem,compMgr);

        return null;
    }

    @Override
    public ItsNatComponent after(ItsNatComponent comp)
    {
        return comp;
    }


    public static void registerLayouts(ItsNatHttpServlet itsNatServlet,String pathPrefix)
    {
        registerLayout(itsNatServlet,"loginTag","text/html",pathPrefix,"main/comp/other/custom_tag_component_logintag_frag.html");
    }

    private static void registerLayout(ItsNatHttpServlet itsNatServlet,String name,String mime,String pathPrefix,String relPath)
    {
        String path = pathPrefix + relPath;
        if (!new File(path).exists()) throw new RuntimeException("Not found file:" + path);
        itsNatServlet.registerItsNatDocFragmentTemplate(name,mime, path);
    }
}
