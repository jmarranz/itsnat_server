
package inexp.extjsexam;

import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLImageElement;

/**
 *
 * @author jmarranz
 */
public abstract class BrowserAdaptor implements CreateItsNatComponentListener
{
    protected ItsNatHTMLDocument itsNatDoc;

    public BrowserAdaptor(ExtJSExampleDocument extJSNatDoc)
    {
        this.itsNatDoc = extJSNatDoc.getItsNatHTMLDocument();

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.addCreateItsNatComponentListener(this);
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

    protected HTMLAnchorElement createLink()
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        HTMLAnchorElement link = (HTMLAnchorElement)doc.createElement("a");
        link.setHref("javascript:;");
        link.setAttribute("style","text-decoration:none; color:inherit;");
        return link;
    }
}
