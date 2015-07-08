/*
 * LoginComponent.java
 *
 * Created on 4 de junio de 2007, 16:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp.custom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import manual.comp.shared.MyCustomComponentBase;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.text.ItsNatHTMLInputPassword;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class LoginComponent extends MyCustomComponentBase implements ActionListener
{
    protected ItsNatHTMLInputText userComp;
    protected ItsNatHTMLInputPassword passwordComp;
    protected ItsNatHTMLInputButton validateComp;
    protected Element logElem;
    protected ValidateLoginListener validateListener;

    public LoginComponent(Element parentElem,ItsNatComponentManager compMgr)
    {
        super(parentElem,compMgr);

        this.userComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("userId");
        this.passwordComp = (ItsNatHTMLInputPassword)compMgr.createItsNatComponentById("passwordId");
        this.validateComp = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("validateId");

        validateComp.getButtonModel().addActionListener(this);
    }

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

