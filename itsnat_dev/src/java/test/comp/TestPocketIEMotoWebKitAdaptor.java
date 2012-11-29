/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.comp;

import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.NameValue;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLLabelElement;
import org.w3c.dom.html.HTMLTableElement;
import test.shared.BrowserUtil;

/**
 *
 * @author jmarranz
 */
public class TestPocketIEMotoWebKitAdaptor
{
    public static boolean isPIEOrMotoWebKit(ItsNatServletRequest request)
    {
        return BrowserUtil.isPocketIE(request) ||
               BrowserUtil.isMotoWebKit(request);
    }

    public static void adapt(ItsNatHTMLDocument itsNatDoc)
    {
        CreateItsNatComponentListener listener = new CreateItsNatComponentListener()
        {
            public ItsNatComponent before(Node node, String compType, NameValue[] artifacts, ItsNatComponentManager compMgr)
            {
                if (node == null) return null;

                Element elem = (Element)node;
                ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)compMgr.getItsNatDocument();
                //HTMLDocument doc = itsNatDoc.getHTMLDocument();
                if ( (elem instanceof HTMLLabelElement)||
                      "freeButtonNormal".equals(compType)||
                      "freeButtonNormalLabel".equals(compType)||
                      "freeLabel".equals(compType)||
                      "freeCheckBox".equals(compType)||
                      "freeCheckBoxLabel".equals(compType)||
                      "freeRadioButton".equals(compType) ||
                      "freeRadioButtonLabel".equals(compType)
                   )
                    addLinkContent(elem,itsNatDoc);
                else if ("freeComboBox".equals(compType))
                    addLinkToListItem(elem,itsNatDoc);
                else if ("freeListMultSel".equals(compType))
                    addLinkToTableListCell(elem,itsNatDoc);
                else if ((elem instanceof HTMLTableElement)&& isComponent(elem))
                {
                    if ("tableId2".equals(elem.getAttribute("id")))
                        addLinkToCustomTableCell(elem,itsNatDoc);
                    else
                        addLinkToTableCell(elem,itsNatDoc);
                }
                else if ("freeTable".equals(compType))
                    addLinkToTableCell(elem,itsNatDoc);
                else if ("freeTree".equals(compType))
                    addLinkToTreeNode(elem,itsNatDoc);

                return null;
            }

            public ItsNatComponent after(ItsNatComponent comp)
            {
                return comp;
            }
        };
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.addCreateItsNatComponentListener(listener);
    }

    private static void addLinkToTreeNode(Element elem,ItsNatHTMLDocument itsNatDoc)
    {
        Element treeNode = ItsNatTreeWalker.getFirstChildElement(elem);
        Element handle = ItsNatTreeWalker.getFirstDeepestChildElement(treeNode);
        Element contentNode = (Element)handle.getParentNode();
        Element icon = ItsNatTreeWalker.getNextSiblingElement(handle);
        Element label = ItsNatTreeWalker.getNextSiblingElement(icon);
        addLinkAsParent(handle,itsNatDoc);
        addLinkAsParent(icon,itsNatDoc);
        addLinkContent(label,itsNatDoc); // <span><a>...</a></span>

        // Sub node pattern if defined
        treeNode = ItsNatTreeWalker.getNextSiblingElement(contentNode);
        handle = ItsNatTreeWalker.getFirstDeepestChildElement(treeNode);
        if (!(handle instanceof HTMLImageElement)) return;
        addLinkToTreeNode(treeNode,itsNatDoc);
    }

    private static void addLinkToCustomTableCell(Element elem,ItsNatHTMLDocument itsNatDoc)
    {
        Element headerOrBody = ItsNatTreeWalker.getFirstChildElement(elem);
        addLinkToTableListCell(headerOrBody,itsNatDoc);
        Element body = ItsNatTreeWalker.getNextSiblingElement(headerOrBody);
        Element cell = ItsNatTreeWalker.getFirstDeepestChildElement(body); // Devuelve el <b>
        cell = (Element)cell.getParentNode();
        addLinkContent(cell,itsNatDoc);
    }

    private static void addLinkToTableCell(Element elem,ItsNatHTMLDocument itsNatDoc)
    {
        Element headerOrBody = ItsNatTreeWalker.getFirstChildElement(elem);
        addLinkToTableListCell(headerOrBody,itsNatDoc);
        Element body = ItsNatTreeWalker.getNextSiblingElement(headerOrBody);
        if (body == null) return; // only body
        addLinkToTableListCell(body,itsNatDoc);
    }

    private static void addLinkToTableListCell(Element elem,ItsNatHTMLDocument itsNatDoc)
    {
        Element row = ItsNatTreeWalker.getFirstChildElement(elem);
        addLinkToListItem(row,itsNatDoc);
    }

    private static void addLinkToListItem(Element elem,ItsNatHTMLDocument itsNatDoc)
    {
        Element cell = ItsNatTreeWalker.getFirstChildElement(elem);
        addLinkContent(cell,itsNatDoc);
    }

    private static void addLinkAsParent(Element elem,ItsNatHTMLDocument itsNatDoc)
    {
        HTMLAnchorElement link = createLink(itsNatDoc);
        Node parentNode = elem.getParentNode();
        parentNode.replaceChild(link, elem);
        if (elem instanceof HTMLImageElement)
            ((HTMLImageElement)elem).setBorder("0");
        link.appendChild(elem);
    }

    private static void addLinkContent(Element elem,ItsNatHTMLDocument itsNatDoc)
    {
        HTMLAnchorElement link = createLink(itsNatDoc);
        Node content = ItsNatDOMUtil.extractChildren(elem);
        if (content instanceof DocumentFragment)
        {
            NodeList children = content.getChildNodes();
            int len = children.getLength();
            for(int i = 0; i < len; i++)
            {
                Node node = children.item(i);
                if (node instanceof HTMLImageElement)
                    ((HTMLImageElement)node).setBorder("0");
            }
        }
        link.appendChild(content);
        elem.appendChild(link);
    }

    public static HTMLAnchorElement createLink(ItsNatHTMLDocument itsNatDoc)
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        HTMLAnchorElement link = (HTMLAnchorElement)doc.createElement("a");
        link.setHref("javascript:;");
        link.setAttribute("style","text-decoration:none; color:inherit;");
        return link;
    }

    public static boolean isComponent(Element elem)
    {
        return "true".equals(getItsNatAttribute(elem,"isComponent"));
    }

    public static String getItsNatAttribute(Element elem,String name)
    {
        return elem.getAttributeNS("http://itsnat.org/itsnat",name);
    }

/*
    public static boolean isComponentType(Element elem,String compType)
    {
        return compType.equals(getItsNatAttribute(elem,"compType"));
    }
 */
}
