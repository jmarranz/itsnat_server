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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import javax.swing.tree.DefaultMutableTreeNode;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.NameValue;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLTextAreaElement;
import org.w3c.dom.views.AbstractView;

public abstract class FeatureTreeNode
{
    public static final int NONE_PANEL = -1;
    public static final int EXAMPLE_PANEL = 0;
    public static final int CODE_PANEL = 1;
    public static final int DOC_PANEL = 2;

    protected FeatureShowcaseDocument featShowDoc;
    protected FeatureTreeNode parentFeatureNode;
    protected DefaultMutableTreeNode swingTreeNode;
    protected int currentPanel = NONE_PANEL;
    protected Element logElem;
    protected boolean hasExample;
    protected boolean hasExplanation;
    protected boolean hasSourceCode;
    protected String featureName;
    protected String treeNodeText;
    protected String title;
    protected ItemListener selPanelListener;
    protected LinkedList<FeatureTreeNode> children;
    protected int onloadTargetPanel = NONE_PANEL;

    public FeatureTreeNode()
    {
        this.selPanelListener = new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                int state = e.getStateChange();
                boolean selected = (state == ItemEvent.SELECTED);
                if (!selected) return;
                NameValue tab = (NameValue)e.getItem();
                int panelToLoad = ((Integer)tab.getValue()).intValue();
                selectPanelInternal(panelToLoad);
            }
        };
    }

    public void init(FeatureShowcaseDocument feaShowDoc,FeatureTreeNode parentFeatureNode,
            boolean hasExample,boolean hasExplanation,boolean hasSourceCode,
            String featureName,String treeNodeText,String title)
    {
        this.featShowDoc = feaShowDoc;
        this.parentFeatureNode = parentFeatureNode;
        this.hasExample = hasExample;
        this.hasExplanation = hasExplanation;
        this.hasSourceCode = hasSourceCode;
        this.featureName = featureName;
        this.treeNodeText = treeNodeText;
        this.title = title;

        if (parentFeatureNode != null)
            parentFeatureNode.addChildFeatureTreeNodes(this);
    }

    public int getOnloadTargetPanel()
    {
        return onloadTargetPanel;
    }

    public void setOnloadTargetPanel(int onloadTargetPanel)
    {
        this.onloadTargetPanel = onloadTargetPanel;
    }

    public String getRequestURLOfDocument()
    {
        return featShowDoc.getRequestURLOfDocument();
    }

    public DefaultMutableTreeNode getDefaultMutableTreeNode()
    {
        return swingTreeNode;
    }    

    public void setDefaultMutableTreeNode(DefaultMutableTreeNode swingTreeNode)
    {
        this.swingTreeNode = swingTreeNode;
    }

    public FeatureTreeNode getFeatureTreeNodeParent()
    {
        return parentFeatureNode;
    }

    public LinkedList<FeatureTreeNode> getChildFeatureTreeNodes()
    {
        return children;
    }

    public void addChildFeatureTreeNodes(FeatureTreeNode child)
    {
        if (children == null) this.children = new LinkedList<FeatureTreeNode>();
        children.add(child);
    }

    public int getPanelCode(String panelName)
    {
        if (panelName.equals("ex"))
            return EXAMPLE_PANEL;
        else if (panelName.equals("code"))
            return CODE_PANEL;
        else if (panelName.equals("doc"))
            return DOC_PANEL;
        else if (panelName.equals("default"))
            return getFirstPanel();
        else
            return NONE_PANEL;
    }

    public int getFirstPanel()
    {
        if (hasExample) return EXAMPLE_PANEL;
        else if (hasSourceCode) return CODE_PANEL;
        else if (hasExplanation) return DOC_PANEL;
        else return NONE_PANEL;
    }

    public Element getTabElement(int panel)
    {
        switch(panel)
        {
            case FeatureTreeNode.EXAMPLE_PANEL:
                return featShowDoc.getExampleTabElement();
            case FeatureTreeNode.CODE_PANEL:
                return featShowDoc.getCodeTabElement();
            case FeatureTreeNode.DOC_PANEL:
                return featShowDoc.getDocTabElement();
            default:
                return null;
        }
    }

    public boolean hasExample()
    {
        return hasExample;
    }

    public boolean hasSourceCode()
    {
        return hasSourceCode;
    }

    public boolean hasExplanation()
    {
        return hasExplanation;
    }

    public String getFeatureName()
    {
        return featureName;
    }

    public String getTreeNodeText()
    {
        return treeNodeText;
    }

    public String getTitle()
    {
        return title;
    }

    public FeatureShowcaseDocument getFeatureShowcaseDocument()
    {
        return featShowDoc;
    }

    public ItsNatDocument getItsNatDocument()
    {
        return featShowDoc.getItsNatHTMLDocument();
    }

    public String getExamplePanelName()
    {
        return getFeatureName() + ".ex";
    }

    public String getDocPanelName()
    {
        return getFeatureName() + ".doc";
    }

    public String getCodePanelName()
    {
        return getFeatureName() + ".code";
    }

    public String toString()
    {
        return getTreeNodeText();
    }


    public void endCurrentPanel()
    {
        if (currentPanel == EXAMPLE_PANEL)
            endExamplePanel();

        this.currentPanel = NONE_PANEL;
        this.logElem = null;
    }

    public String getPanelName(int panel)
    {
        switch(panel)
        {
            case FeatureTreeNode.EXAMPLE_PANEL:
                return getExamplePanelName();
            case FeatureTreeNode.CODE_PANEL:
                return getCodePanelName();
            case FeatureTreeNode.DOC_PANEL:
                return getDocPanelName();
        }
        return null;
    }

    public abstract void startExamplePanel();

    public abstract void endExamplePanel();

    public boolean hasLog()
    {
        return (logElem != null);
    }

    public String toString(Object obj)
    {
        if (obj == null)
            return "null";
        else if (obj instanceof Event)
        {
            Event evt = (Event)obj;
            return toString(evt.getCurrentTarget()) + " " + evt.getType();
        }
        else if (obj instanceof AbstractView)
            return "AbstractView";
        else if (obj instanceof Node)
        {
            if (obj instanceof Document)
                return "Document";
            else if (obj instanceof Element)
            {
                Element elem = (Element)obj;
                return "<" + elem.getTagName() + ">";
            }
            else if (obj instanceof CharacterData)
                return ((CharacterData)obj).getData();
            else
                return obj.toString();
        }
        else return obj.toString();
    }

    public void log(Object obj)
    {
        log(toString(obj));
    }

    public void log(Object obj1,Object obj2)
    {
        log(toString(obj1) + " " + toString(obj2));
    }

    public void log(String text)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        if (logElem == null)
        {
            this.logElem = doc.getElementById("logId");
            if (logElem == null) return; // Log element not defined
        }

        logElem.removeAttribute("style"); // makes visible
        int len = logElem.getChildNodes().getLength();
        if (len >= 30)
            logElem.removeChild(logElem.getFirstChild());

        Element msgElem = doc.createElement("div");
        msgElem.appendChild(doc.createTextNode(text));

        logElem.appendChild(msgElem);
    }

    public Element getFeatureBoxElement()
    {
        return featShowDoc.getFeatureBoxElement();
    }

    public void startCodePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        Element parent = getFeatureBoxElement();
        NodeList textAreas = parent.getElementsByTagName("textarea");
        for(int i = 0; i < textAreas.getLength(); i++)
        {
            HTMLTextAreaElement textAreaElem = (HTMLTextAreaElement)textAreas.item(i);
            String classAttr = textAreaElem.getAttribute("class");
            textAreaElem.setAttribute("class",classAttr + ":collapse:nogutter");
            if (classAttr.startsWith("html") || classAttr.startsWith("xml"))
                SyntaxHighlighter.highlightMarkup(textAreaElem,itsNatDoc);
            else // java
                SyntaxHighlighter.highlightJava(textAreaElem,itsNatDoc);
        }
    }

    public void selectFeature()
    {
        String featTitle = getTitle();
        ItsNatDOMUtil.setTextContent(featShowDoc.getFeatureTitleElement(),featTitle);
        String docTitle = featTitle + ". ItsNat: Feature Set & Examples";
        ItsNatDocument itsNatDoc = getItsNatDocument();
        if (itsNatDoc.isLoading() && itsNatDoc.getItsNatDocumentTemplate().isFastLoadMode())
            ItsNatDOMUtil.setTextContent(featShowDoc.getTitleElement(),docTitle);
        else
            itsNatDoc.addCodeToSend("document.title =\"" + docTitle + "\";");

        initTab(featShowDoc.getExampleTabElement(),hasExample());
        initTab(featShowDoc.getCodeTabElement(),hasSourceCode());
        initTab(featShowDoc.getDocTabElement(),hasExplanation());

        initTabPermalink(featShowDoc.getExampleTabPermalinkElement(),hasExample(),"ex");
        initTabPermalink(featShowDoc.getCodeTabPermalinkElement(),hasSourceCode(),"code");
        initTabPermalink(featShowDoc.getDocTabPermalinkElement(),hasExplanation(),"doc");

        ItsNatFreeComboBox tabsCombo = featShowDoc.getTabsComboBox();
        tabsCombo.addItemListener(selPanelListener);
        tabsCombo.setSelectedIndex(-1); // Ensures none is selected

        int panel;
        if (getItsNatDocument().isLoading())
        {
            // This way we directly show the target panel,
            // avoiding problems in fastLoadMode = true.
            panel = getOnloadTargetPanel();
            if (panel == NONE_PANEL) panel = getFirstPanel();
        }
        else panel = getFirstPanel();

        selectPanel(panel);
    }

    public void exitFeature()
    {
        endCurrentPanel();

        ItsNatFreeComboBox tabsCombo = featShowDoc.getTabsComboBox();
        tabsCombo.removeItemListener(selPanelListener);
        tabsCombo.setSelectedIndex(-1);
    }

    public void initTabPermalink(HTMLAnchorElement link,boolean isVisible,String tabName)
    {
        if (isVisible)
        {
            ItsNatDocumentTemplate mainTemplate = getItsNatDocument().getItsNatDocumentTemplate();
            link.setHref("?itsnat_doc_name=" + mainTemplate.getName() + "&feature=" + getFeatureName() + "." + tabName);
        }
        else
            link.setHref("javascript:void(0);");
    }

    public void selectPanel(int panel)
    {
        ItsNatFreeComboBox tabsCombo = featShowDoc.getTabsComboBox();
        tabsCombo.setSelectedIndex(panel);
    }

    private void selectPanelInternal(int panel)
    {
        endCurrentPanel();

        this.currentPanel = panel;

        String panelName = getPanelName(panel);
        featShowDoc.getItsNatFreeInclude().includeFragment(panelName,false);

        switch(currentPanel)
        {
            case EXAMPLE_PANEL: startExamplePanel(); break;
            case CODE_PANEL: startCodePanel(); break;
        }

        if (hasExample())
            setTabNotActive(featShowDoc.getExampleTabElement());
        if (hasSourceCode())
            setTabNotActive(featShowDoc.getCodeTabElement());
        if (hasExplanation())
            setTabNotActive(featShowDoc.getDocTabElement());

        Element tabElem = getTabElement(panel);
        setTabActive(tabElem);
    }

    public void setTabNotActive(Element elem)
    {
        elem.removeAttribute("style");
    }

    public void setTabActive(Element elem)
    {
        elem.setAttribute("style","background: #ED752A; border: 3px #ED752A solid;");
    }

    public void initTab(Element elem,boolean visible)
    {
        if (visible)
            elem.removeAttribute("style");
        else
            elem.setAttribute("style","visibility: hidden;");
    }

    public boolean isJoystickModePreferred()
    {
        return featShowDoc.isJoystickModePreferred();
    }
    
    public boolean isOperaMini()
    {
        return featShowDoc.isOperaMini();
    }    
}
