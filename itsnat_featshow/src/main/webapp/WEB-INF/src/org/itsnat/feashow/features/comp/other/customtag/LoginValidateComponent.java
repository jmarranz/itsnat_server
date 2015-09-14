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

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.feashow.features.comp.shared.MyCustomComponentBase;
import org.w3c.dom.Element;

public class LoginValidateComponent extends MyCustomComponentBase
{   
    protected ItsNatHTMLInputButton inputComp;

    public LoginValidateComponent(Element parentElem,ItsNatComponentManager compMgr)
    {
        super(LoginUtil.doTemplateLayout("loginValidateTag",parentElem,compMgr),compMgr);

        parentElem = (Element)getNode();
        
        this.inputComp = (ItsNatHTMLInputButton)compMgr.createItsNatComponent(parentElem);
    }

    public static void registerTemplate(ItsNatHttpServlet itsNatServlet,String pathPrefix,String relPath)
    {
        LoginUtil.registerTemplate(itsNatServlet,"loginValidateTag","text/html",pathPrefix,relPath);
    }    
    
    @Override
    public void dispose()
    {
        inputComp.dispose();
    }

    public ItsNatHTMLInputButton getItsNatHTMLInputButton()
    {
        return inputComp;
    }

}

