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

package org.itsnat.feashow;

import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.NameValue;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLLabelElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 * In Motorola Symphony (MotoWebKit) only a small subset of HTML elements
 * fires events. ItsNat tries to complete the W3C DOM Event support of these browsers
 * including bubbling and capturing. With links we can help the browser to fire
 * mouse click events, event listeners defined by ItsNat in upper nodes will be dispatched
 * because the event "artificially" does bubbling and capturing.
 *
 * Most of Feature Showcase examples are not ready to Symphony, we can avoid
 * "polluting" the templates with links adding these links when the DOM is processed.
 * This in an example of the power of the DOM in server approach of ItsNat.
 *
 *
 * @author jmarranz
 */
public class MotoWebKitAdaptor extends BrowserAdaptor
{
    public MotoWebKitAdaptor(FeatureShowcaseDocument featShowDoc)
    {
        super(featShowDoc);

        fixTabsComboBox(featShowDoc.getTabsComboBox());
    }

    public static boolean isMotoWebKit(ItsNatServletRequest request)
    {
        return BrowserUtil.isMotoWebKit(request);
    }

    protected void processInsertedElement(Element elem)
    {
        String id = elem.getAttribute("id");
        if (id.startsWith("clickableId")) // DOM Event Listeners example
        {
            replaceElemWithLink(elem);
            return;
        }
        else if (id.startsWith("xhrSyncModeId") || // Comm Modes example
                 id.startsWith("xhrAsyncHoldModeId") ||
                 id.startsWith("xhrAsyncModeId") ||
                 id.startsWith("scriptModeId") ||
                 id.startsWith("scriptHoldModeId"))
        {
            addLinkAsContentParent(elem);
            return;
        }
    }

    public ItsNatComponent before(Node node, String compType, NameValue[] artifacts, ItsNatComponentManager compMgr)
    {
        if (node == null) return null;

        Element elem = (Element)node;
        if (modeAuto) // "Auto. Comp. Build: All Types" example
        {
            // Because this example does nothing, is not interesting
            // to make these components "clickable"
        }
        else
        {
            if (compType == null)
            {
                if (elem instanceof HTMLLabelElement)
                    addLinkAsContentParent(elem);
                else if (elem instanceof HTMLTableElement)
                    addLinksToTable(elem);
            }
            else
            {
                if ( "freeButtonNormal".equals(compType)||
                     "freeLabel".equals(compType)||
                     "freeCheckBox".equals(compType)||
                     "freeRadioButton".equals(compType) )
                   addLinkAsContentParent(elem);
                else if ("freeComboBox".equals(compType) ||
                         "freeListMultSel".equals(compType))
                {
                    if (elem instanceof HTMLTableSectionElement) // TBODY
                        addLinksToTableSection(elem);
                    else
                        addLinkToListItem(elem);
                }
                else if ("freeTable".equals(compType))
                    addLinksToFreeTable(elem);
                else if ("freeTree".equals(compType))
                    addLinksToTree(elem);
            }

            // freeButtonNormalLabel, freeCheckBoxLabel, freeRadioButtonLabel
            // are used in "Auto. Comp. Build:..." this example
            // does nothing, no need to be "clicked".
        }

        return null;
    }

    public ItsNatComponent after(ItsNatComponent comp)
    {
        return comp;
    }
}
