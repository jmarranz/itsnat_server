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
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.text.ItsNatHTMLInputPassword;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.feashow.features.comp.shared.MyCustomComponentBase;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

public class LoginTagComponent extends MyCustomComponentBase implements ActionListener
{
    protected ItsNatHTMLInputText userComp;
    protected ItsNatHTMLInputPassword passwordComp;
    protected ItsNatHTMLInputButton validateComp;
    protected Element logElem;
    protected ValidateLoginListener validateListener;

    public LoginTagComponent(Element parentElem,ItsNatComponentManager compMgr)
    {
        super(doLayout(parentElem,compMgr),compMgr);

        this.userComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("userId");
        this.passwordComp = (ItsNatHTMLInputPassword)compMgr.createItsNatComponentById("passwordId");
        this.validateComp = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("validateId");

        validateComp.getButtonModel().addActionListener(this);
    }

    private static Element doLayout(Element parentElem,ItsNatComponentManager compMgr)
    {
        // parentElem is a <login> element        
        ItsNatDocument itsNatDoc = compMgr.getItsNatDocument();        
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        ItsNatDocFragmentTemplate docFragTemplate = servlet.getItsNatDocFragmentTemplate("loginTag");
        DocumentFragment docFrag = docFragTemplate.loadDocumentFragment(itsNatDoc);
        Element newParentElem = ItsNatTreeWalker.getFirstChildElement(docFrag);        
        parentElem.getParentNode().replaceChild(newParentElem,parentElem);
        return newParentElem;
    }
    
    @Override
    public void dispose()
    {
        userComp.dispose();
        passwordComp.dispose();
        validateComp.dispose();
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
        return userComp.getText();
    }

    public String getPassword()
    {
        return passwordComp.getText();
    }
}

