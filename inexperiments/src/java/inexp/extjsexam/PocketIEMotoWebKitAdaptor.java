
package inexp.extjsexam;

import inexp.BrowserUtil;
import inexp.extjsexam.tab.CustomTableStructure;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.NameValue;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;

/**
 * In Pocket IE and Motorola Symphony (MotoWebKit) only a small subset of HTML elements
 * fires events. ItsNat tries to complete the W3C DOM Event support of these browsers
 * including bubbling and capturing. With links we can help the browser to fire
 * mouse click events, event listeners defined by ItsNat in upper nodes will be dispatched
 * because the event "artificially" does bubbling and capturing.
 *
 * We can avoid "polluting" the templates with links adding these links when the DOM is processed.
 * This in an example of the power of the DOM in server approach of ItsNat.
 *
 * @author jmarranz
 */
public class PocketIEMotoWebKitAdaptor extends BrowserAdaptor
{
    public PocketIEMotoWebKitAdaptor(ExtJSExampleDocument extJSNatDoc)
    {
        super(extJSNatDoc);
    }

    public static boolean isPIEOrMotoWebKit(ItsNatServletRequest request)
    {
        return (BrowserUtil.isMotoWebKit(request) || BrowserUtil.isPocketIE(request));
    }

    public ItsNatComponent before(Node node, String compType, NameValue[] artifacts, ItsNatComponentManager compMgr)
    {
        if (node == null) return null;

        Element elem = (Element)node;
        if ("freeComboBox".equals(compType))
        {
            Element itemElem = ItsNatTreeWalker.getFirstChildElement(elem);
            Element firstA = ItsNatTreeWalker.getFirstChildElement(itemElem);
            Element secondA = ItsNatTreeWalker.getNextSiblingElement(firstA);
            // Replacing this link by a SPAN, because we are going to add a new child link
            // (needed in Symphony to traverse tabs with phone cursor)
            HTMLDocument doc = itsNatDoc.getHTMLDocument();
            Element span = doc.createElement("span");
            span.setAttribute("class",secondA.getAttribute("class"));
            Node content = ItsNatDOMUtil.extractChildren(secondA);
            span.appendChild(content);
            Node parent = secondA.getParentNode();
            parent.replaceChild(span,secondA);
            // Now a link is added to the label:
            Element labelParent = ItsNatTreeWalker.getFirstDeepestChildElement(span);
            addLinkAsContentParent(labelParent);
        }
        else if ("freeTable".equals(compType))
        {
            CustomTableStructure struct = CustomTableStructure.SINGLETON;
            Element body = struct.getBodyElement(null,elem);
            Element rowPattern = ItsNatTreeWalker.getFirstChildElement(body);
            Element rowContent = struct.getRowContentElement(null,-1,rowPattern);
            Element cellPattern = ItsNatTreeWalker.getFirstChildElement(rowContent);
            Element textParent = struct.getCellContentElement(null,-1,-1,cellPattern);
            addLinkAsContentParent(textParent);

            HTMLDocument doc = itsNatDoc.getHTMLDocument();
            Element nameElem = doc.getElementById("nameId");
            addLinkAsContentParent(nameElem);
        }

        return null;
    }

    public ItsNatComponent after(ItsNatComponent comp)
    {
        return comp;
    }
}
