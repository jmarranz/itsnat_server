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

import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.itsnat.comp.list.ItsNatListUI;
import org.itsnat.core.ItsNatNode;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLImageElement;

/**
 *
 * @author jmarranz
 */
public abstract class BrowserAdaptor implements CreateItsNatComponentListener,EventListener
{
    protected boolean modeAuto = false;
    protected ItsNatHTMLDocument itsNatDoc;

    public BrowserAdaptor(FeatureShowcaseDocument featShowDoc)
    {
        this.itsNatDoc = featShowDoc.getItsNatHTMLDocument();

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.addCreateItsNatComponentListener(this);

        // For "Core" examples:
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        boolean old = ((ItsNatNode)doc).isInternalMode();
        ((ItsNatNode)doc).setInternalMode(true);
        try
        {
            ((EventTarget)doc).addEventListener("DOMNodeInserted", this,false);
        }
        finally
        {
            ((ItsNatNode)doc).setInternalMode(old);
        }
    }

    public void setModeAuto(boolean modeAuto)
    {
        this.modeAuto = modeAuto;
    }

    protected void fixTabsComboBox(ItsNatFreeComboBox tabsCombo)
    {
        ItsNatListUI tabsComboUI = tabsCombo.getItsNatListUI();
        Text node;
        node = (Text)tabsComboUI.getElementAt(0).getFirstChild();
        addLinkAsParent(node);
        node = (Text)tabsComboUI.getElementAt(1).getFirstChild();
        addLinkAsParent(node);
        node = (Text)tabsComboUI.getElementAt(2).getFirstChild();
        addLinkAsParent(node);
    }

    public void handleEvent(Event evt)
    {
        // Mutation event (node insertion)
        Node node = (Node)evt.getTarget();
        processInsertedTree(node);
    }

    protected abstract void processInsertedElement(Element elem);

    protected void processInsertedTree(Node node)
    {
        if (!(node instanceof Element)) return;

        processInsertedElement((Element)node);

        if (node.hasChildNodes())
        {
            NodeList list = node.getChildNodes();
            for(int i = 0; i < list.getLength(); i++)
                processInsertedTree(list.item(i));
        }
    }

    protected void addLinksToTree(Element elem)
    {
        Element treeNode = ItsNatTreeWalker.getFirstChildElement(elem);
        Element handle = ItsNatTreeWalker.getFirstDeepestChildElement(treeNode);
        Element contentNode = (Element)handle.getParentNode();
        Element icon = ItsNatTreeWalker.getNextSiblingElement(handle);
        Element label = ItsNatTreeWalker.getNextSiblingElement(icon);
        addLinkAsParent(handle); // <a><img></a>
        addLinkAsParent(icon); // <a><img></a>
        addLinkAsContentParent(label); // <span><a>...</a></span>

        // Sub node pattern if defined
        treeNode = ItsNatTreeWalker.getNextSiblingElement(contentNode);
        if (treeNode == null) return; // Tree-table
        handle = ItsNatTreeWalker.getFirstDeepestChildElement(treeNode);
        if (!(handle instanceof HTMLImageElement)) return;
        addLinksToTree(treeNode);
    }

    protected void addLinksToTable(Element elem)
    {
        Element headerOrBody = ItsNatTreeWalker.getFirstChildElement(elem);
        addLinksToTableSection(headerOrBody);
        Element body = ItsNatTreeWalker.getNextSiblingElement(headerOrBody);
        if (body == null) return; // only body
        addLinksToTableSection(body);
    }

    protected void addLinksToFreeTable(Element elem)
    {
        Element headerOrRow = ItsNatTreeWalker.getFirstChildElement(elem);
        if (ItsNatTreeWalker.getNextSiblingElement(headerOrRow) == null)
            addLinkToListItem(headerOrRow); // headerOrRow is the row pattern
        else
            addLinksToTable(elem);
    }

    protected void addLinksToTableSection(Element elem)
    {
        Element row = ItsNatTreeWalker.getFirstChildElement(elem);
        while(row != null)
        {
            addLinkToListItem(row);
            row = ItsNatTreeWalker.getNextSiblingElement(row);
        }
    }

    protected void addLinkToListItem(Element row)
    {
        Element cell = ItsNatTreeWalker.getFirstChildElement(row);
        while(cell != null)
        {
            addLinkAsContentParent(cell);
            cell = ItsNatTreeWalker.getNextSiblingElement(cell);
        }
    }

    protected void addLinkAsParent(Node node)
    {
        HTMLAnchorElement link = createLink();
        Node parentNode = node.getParentNode();
        parentNode.replaceChild(link, node);
        if (node instanceof HTMLImageElement)
            ((HTMLImageElement)node).setBorder("0");
        link.appendChild(node);
    }

    protected void replaceElemWithAnother(Element elem,Element newElem)
    {
        Node parent = elem.getParentNode();
        parent.replaceChild(newElem, elem);
        Node content = ItsNatDOMUtil.extractChildren(elem);
        newElem.appendChild(content);
        if (elem.hasAttributes()) // For instance to copy the id attribute
        {
            NamedNodeMap attribs = elem.getAttributes();
            for(int i = 0; i < attribs.getLength(); i++)
            {
                Attr attr = (Attr)attribs.item(i);
                newElem.setAttribute(attr.getName(), attr.getValue());
            }
        }
    }

    protected void replaceElemWithLink(Element elem)
    {
        HTMLAnchorElement link = createLink();
        replaceElemWithAnother(elem,link);
    }

    protected void addLinkAsContentParent(Element elem)
    {
        Node content = ItsNatDOMUtil.extractChildren(elem);
        if (content instanceof DocumentFragment)
        {
            NodeList children = content.getChildNodes();
            for(int i = 0; i < children.getLength(); i++)
            {
                Node node = children.item(i);
                if (node instanceof HTMLImageElement)
                    ((HTMLImageElement)node).setBorder("0");
            }
        }
        HTMLAnchorElement link = createLink();
        link.appendChild(content);
        elem.appendChild(link);
    }

    public HTMLAnchorElement createLink()
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        HTMLAnchorElement link = (HTMLAnchorElement)doc.createElement("a");
        link.setHref("javascript:;");
        link.setAttribute("style","text-decoration:none; color:inherit;");
        return link;
    }

    public boolean isComponent(Element elem)
    {
        return "true".equals(getItsNatAttribute(elem,"isComponent"));
    }

    public String getItsNatAttribute(Element elem,String name)
    {
        return elem.getAttributeNS("http://itsnat.org/itsnat",name);
    }
}
