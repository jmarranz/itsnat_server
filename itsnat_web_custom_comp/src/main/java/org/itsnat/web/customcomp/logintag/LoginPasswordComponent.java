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

package org.itsnat.web.customcomp.logintag;

import org.itsnat.web.customcomp.shared.CustomCompUtil;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatHTMLInputPassword;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.web.customcomp.shared.CustomComponentBase;
import org.w3c.dom.Element;

public class LoginPasswordComponent extends CustomComponentBase
{ 
    protected ItsNatHTMLInputPassword inputComp;

    public LoginPasswordComponent(Element parentElem,ItsNatComponentManager compMgr)
    {
        super(CustomCompUtil.doTemplateLayout("loginPasswordTag",parentElem,compMgr),compMgr);

        parentElem = (Element)getNode();
        
        this.inputComp = (ItsNatHTMLInputPassword)compMgr.createItsNatComponent(parentElem);
    }

    public static void registerTemplate(ItsNatHttpServlet itsNatServlet)
    {
        CustomCompUtil.registerTemplate(itsNatServlet,"loginPasswordTag","text/html",LoginTagComponent.class,"custom_tag_component_logintag_password_frag.html");
    }     
    
    @Override
    public void dispose()
    {
        inputComp.dispose();
    }

    public ItsNatHTMLInputPassword getItsNatHTMLInputPassword()
    {
        return inputComp;
    }
}

