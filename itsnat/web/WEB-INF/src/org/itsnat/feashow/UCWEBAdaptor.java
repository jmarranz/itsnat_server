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
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLLabelElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 * In UCWEB Windows Mobile (6.3) only a small subset of HTML elements
 * fires click events, for instance <div> and <p> do not fire click events. Fortunately <span> elements with onclick handlers fire click events
 * (with some help of ItsNat) but sometimes do not, as alternative (if <span> does not work) links (<a>) can be used too,
 * in spite of this element do not fire click events in UCWEB WinMob ItsNat fix them.
 * By using span or link elements we can help the browser to fire mouse click events,
 * if used an auxiliar <span> element, this element must have an (empty) onclick handler, ItsNat automatically
 * fire a normal click event, this event can be captured and bubbles.
 *
 * Most of Feature Showcase examples are not ready for UCWEB, we can avoid
 * "polluting" the templates with these span/links adding these elements when the DOM is processed.
 * This in an example of the power of the DOM in server approach of ItsNat.
 *
 * The Java version (since 6.0) does not need this adaption.
 *
 * @author jmarranz
 */
public class UCWEBAdaptor extends BrowserAdaptor
{
    protected boolean winMobile; // If false => JavaME

    public UCWEBAdaptor(FeatureShowcaseDocument featShowDoc,
            ItsNatServletRequest request)
    {
        super(featShowDoc);

        this.winMobile = BrowserUtil.isUCWEBWinMobile(request);

        if (!winMobile) // JavaME
            fixTabsComboBox(featShowDoc.getTabsComboBox());
    }

    public static boolean isUCWEB(ItsNatServletRequest request)
    {
        return BrowserUtil.isUCWEB(request);
    }

    protected void processInsertedElement(Element elem)
    {
        String id = elem.getAttribute("id");
        if (id.startsWith("clickableId")) // DOM Event Listeners example
        {
            if (winMobile)
                replaceElemWithSpan(elem); // <span> receives clicks
            else
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

                if (!winMobile) // JavaME
                {
                    if (elem instanceof HTMLTableElement)
                        addLinksToTable(elem);
                }
            }
            else
            {
                // WinMobile and JavaME
                if ( "freeButtonNormal".equals(compType)||
                     "freeLabel".equals(compType)||
                     "freeCheckBox".equals(compType) )
                   addLinkAsContentParent(elem);
                else if ("freeTable".equals(compType))
                    addLinksToFreeTable(elem);

                // In WinMobile <span> and <td> and <li> receive events.

                if (!winMobile) // JavaME
                {
                    if ( "freeRadioButton".equals(compType)) // <span> ignore events
                        addLinkAsContentParent(elem);
                    else if ("freeComboBox".equals(compType) || // <td> ignore events
                             "freeListMultSel".equals(compType)) // "
                    {
                        if (elem instanceof HTMLTableSectionElement) // TBODY
                            addLinksToTableSection(elem);
                        else
                            addLinkToListItem(elem);
                    }
                    // Trees don't need links because <img> elements
                    // receive events (icon and handler are clickable)
                }
            }
        }

        return null;
    }

    public ItsNatComponent after(ItsNatComponent comp)
    {
        return comp;
    }

    protected void addSpanAsContentParent(Element elem)
    {
        Node content = ItsNatDOMUtil.extractChildren(elem);
        HTMLElement span = createSpan();
        span.appendChild(content);
        elem.appendChild(span);
    }

    protected void replaceElemWithSpan(Element elem)
    {
        HTMLElement span = createSpan();
        replaceElemWithAnother(elem, span);
    }

    public HTMLElement createSpan()
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        HTMLElement span = (HTMLElement)doc.createElement("span");
        span.setAttribute("onclick","");
        return span;
    }
}
