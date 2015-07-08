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

package org.itsnat.feashow.features.comp.other;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.ItsNatHTMLInput;
import org.itsnat.comp.inc.ItsNatFreeInclude;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class FreeIncludeTreeNode extends FeatureTreeNode implements EventListener
{
    protected ItsNatFreeInclude includeComp;
    protected ItsNatHTMLInput buttonComp;

    public FreeIncludeTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.includeComp = (ItsNatFreeInclude)compMgr.createItsNatComponentById("includeId","freeInclude",null);

        this.buttonComp = (ItsNatHTMLInput)compMgr.createItsNatComponentById("buttonId");
        buttonComp.addEventListener("click",this);

        include();
    }

    public void endExamplePanel()
    {
        this.includeComp.dispose();
        this.includeComp = null;

        this.buttonComp.dispose();
        this.buttonComp = null;
    }

    public void handleEvent(Event evt)
    {
        if (includeComp.isIncluded())
            uninclude();
        else
            include();
    }

    public void include()
    {
        includeComp.includeFragment("feashow.fragmentExample",true);
        buttonComp.getHTMLInputElement().setValue("Remove");
    }

    public void uninclude()
    {
        includeComp.removeFragment();
        buttonComp.getHTMLInputElement().setValue("Include");
    }
}
