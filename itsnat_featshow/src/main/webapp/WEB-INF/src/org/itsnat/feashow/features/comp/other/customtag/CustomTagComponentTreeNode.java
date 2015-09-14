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
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustomTagComponentTreeNode extends FeatureTreeNode
{
    protected LoginTagComponent loginComp;

    public CustomTagComponentTreeNode()
    {
    }

    @Override
    public void startExamplePanel()
    {
        final ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.loginComp = (LoginTagComponent)compMgr.createItsNatComponentById("loginTagCompId","loginTag",null);

       
        ValidateLoginListener validator = new ValidateLoginListener()
        {
            @Override
            public boolean validate(String user,String password)
            {
                if (!user.equals("admin"))
                {
                    log("Bad user");
                    return false;
                }

                if (!password.equals("1234"))
                {
                    log("Bad password");
                    return false;
                }

                Element loginElem = (Element)loginComp.getNode();
                loginElem.setAttribute("style","display:none");

                Document doc = loginElem.getOwnerDocument();
                Element infoElem = doc.createElement("p");
                infoElem.appendChild(doc.createTextNode("VALID LOGIN!"));
                loginElem.getParentNode().insertBefore(infoElem,loginElem);

                return true;
            }
        };

        loginComp.setValidateLoginListener(validator);
    }

    @Override
    public void endExamplePanel()
    {
        this.loginComp.dispose();
        this.loginComp = null;
    }

}
