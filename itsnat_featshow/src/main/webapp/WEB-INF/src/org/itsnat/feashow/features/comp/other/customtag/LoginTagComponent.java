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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.feashow.features.comp.shared.MyCustomComponentBase;
import org.w3c.dom.Element;

public class LoginTagComponent extends MyCustomComponentBase implements ActionListener
{
    protected LoginUserComponent userComp;
    protected LoginPasswordComponent passwordComp;
    protected LoginValidateComponent validateComp;
    protected Element logElem;
    protected ValidateLoginListener validateListener;

    public LoginTagComponent(Element parentElem,ItsNatComponentManager compMgr)
    {
        super(LoginUtil.doTemplateLayout("loginTag",parentElem,compMgr),compMgr); // parentElem is a <login> element 

        parentElem = (Element)getNode();
        
        Element userElem = ItsNatDOMUtil.getElementById("userId", parentElem);
        userElem.removeAttribute("id"); // Good practice to avoid duplicity
        Element passElem = ItsNatDOMUtil.getElementById("passwordId", parentElem);
        passElem.removeAttribute("id");        
        Element validateElem = ItsNatDOMUtil.getElementById("validateId", parentElem);        
        validateElem.removeAttribute("id");
        
        this.userComp = (LoginUserComponent)compMgr.createItsNatComponent(userElem,"loginUser",null);
        this.passwordComp = (LoginPasswordComponent)compMgr.createItsNatComponent(passElem,"loginPassword",null);
        this.validateComp = (LoginValidateComponent)compMgr.createItsNatComponent(validateElem,"loginValidate",null);

        validateComp.getItsNatHTMLInputButton().getButtonModel().addActionListener(this);
    }
    
    public static void registerTemplate(ItsNatHttpServlet itsNatServlet,String pathPrefix,String relPath)
    {
        LoginUtil.registerTemplate(itsNatServlet,"loginTag","text/html",pathPrefix,relPath);
    }
    
    @Override
    public void dispose()
    {
        userComp.dispose();
        passwordComp.dispose();
        validateComp.dispose();
    }

    public LoginUserComponent getLoginUserComponent()
    {
        return userComp;
    }
    
    public LoginPasswordComponent getLoginPasswordComponent()
    {
        return passwordComp;
    }    
    
    public LoginValidateComponent getLoginValidateComponent()
    {
        return validateComp;
    }      
    
    public ValidateLoginListener getValidateLoginListener()
    {
        return validateListener;
    }

    public void setValidateLoginListener(ValidateLoginListener listener)
    {
        this.validateListener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (validateListener != null)
        {
            validateListener.validate(getUser(),getPassword());
        }
    }

    public String getUser()
    {
        return userComp.getItsNatHTMLInputText().getText();
    }

    public String getPassword()
    {
        return passwordComp.getItsNatHTMLInputPassword().getText();
    }
}

