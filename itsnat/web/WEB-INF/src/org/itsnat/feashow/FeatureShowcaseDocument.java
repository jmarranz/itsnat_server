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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.inc.ItsNatFreeInclude;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.itsnat.comp.list.ItsNatListUI;
import org.itsnat.comp.tree.ItsNatFreeTree;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.NameValue;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatEventListenerChain;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.feashow.features.core.misc.remctrl.RemoteControlTimerMgrGlobalEventListener;
import org.itsnat.feashow.features.core.otherns.EventListenerSerial;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLAnchorElement;

public class FeatureShowcaseDocument implements TreeSelectionListener,ItemListener
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected String requestURL;
    protected Element titleElement;
    protected Element featureTitleElement;
    protected ItsNatFreeTree tree;
    protected ItsNatHTMLInputCheckBox joystickModeCheck;
    protected ItsNatFreeInclude include;
    protected ItsNatFreeComboBox tabsCombo;
    protected Element exampleTab;
    protected Element docTab;
    protected Element codeTab;
    protected HTMLAnchorElement exampleTabLink;
    protected HTMLAnchorElement docTabLink;
    protected HTMLAnchorElement codeTabLink;
    protected FeatureTreeNode selectedExample;
    protected MainTreeDecorator mainTreeDecorator;
    protected Map features = new HashMap();
    protected BrowserAdaptor browserAdaptor;
    protected boolean joystickModePreferred;
    protected boolean ucweb;
    protected boolean bolt;

    public FeatureShowcaseDocument(ItsNatServletRequest request)
    {
        this.itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();
        this.joystickModePreferred = BrowserUtil.isJoystickModePreferred(request);
        this.requestURL = ((HttpServletRequest)request.getServletRequest()).getRequestURL().toString();
        
        EventListener global = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                ItsNatEvent itsNatEvt = (ItsNatEvent)evt;
                ItsNatEventListenerChain chain = itsNatEvt.getItsNatEventListenerChain();
                try
                {
                    chain.continueChain();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();

                    ItsNatServletRequest request = itsNatEvt.getItsNatServletRequest();
                    if (BrowserUtil.isUCWEB(request))
                        itsNatDoc.addCodeToSend("document.body.innerHTML = 'Unexpected Error! Reload';");
                    else
                    {
                        itsNatDoc.addCodeToSend("var res = confirm('Unexpected Error! Reload?');");
                        itsNatDoc.addCodeToSend("if (res) window.location.reload(true);");
                    }
                    itsNatDoc.setInvalid();
                    chain.stop();
                }
           }
        };
        itsNatDoc.addEventListener(global); // Comment this sentence to show errors in technical form

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.setSelectionOnComponentsUsesKeyboard(!BrowserUtil.isMobileBrowser(request));

        Document doc = itsNatDoc.getDocument();

        this.titleElement = doc.getElementById("titleId");
        this.featureTitleElement = doc.getElementById("featureTitleId");

        this.joystickModeCheck = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("mainTreeJoystickModeId");
        joystickModeCheck.setSelected(joystickModePreferred);
        joystickModeCheck.getToggleButtonModel().addItemListener(this);

        Element tabsParent = doc.getElementById("tabsParentId");
        this.tabsCombo = compMgr.createItsNatFreeComboBox(tabsParent, null, null);
        tabsCombo.setJoystickMode(joystickModeCheck.isSelected());
        DefaultComboBoxModel tabsModel = (DefaultComboBoxModel)tabsCombo.getComboBoxModel();
        tabsModel.addElement(new NameValue("Example ",new Integer(FeatureTreeNode.EXAMPLE_PANEL)));
        tabsModel.addElement(new NameValue("Source Code ",new Integer(FeatureTreeNode.CODE_PANEL)));
        tabsModel.addElement(new NameValue("Explanation ",new Integer(FeatureTreeNode.DOC_PANEL)));

        ItsNatListUI tabsComboUI = tabsCombo.getItsNatListUI();

        this.exampleTab = tabsComboUI.getElementAt(0);
        this.codeTab = tabsComboUI.getElementAt(1);
        this.docTab = tabsComboUI.getElementAt(2);

        this.exampleTabLink = (HTMLAnchorElement)ItsNatTreeWalker.getFirstChildElement(exampleTab);
        this.codeTabLink = (HTMLAnchorElement)ItsNatTreeWalker.getFirstChildElement(codeTab);
        this.docTabLink = (HTMLAnchorElement)ItsNatTreeWalker.getFirstChildElement(docTab);

        // Adaptors are called *after* tabs combo box construction because this
        // component requires special processing.
        if (MotoWebKitAdaptor.isMotoWebKit(request))
            this.browserAdaptor = new MotoWebKitAdaptor(this);
        else if (UCWEBAdaptor.isUCWEB(request))
            this.browserAdaptor = new UCWEBAdaptor(this,request);
        else if (SkyFireAdaptor.isSkyFire(request))
            SkyFireAdaptor.fix(this);
        this.ucweb = BrowserUtil.isUCWEB(request);
        this.bolt = BrowserUtil.isBolt(request);

        Element treeElem = doc.getElementById("examplesTreeId");
        this.tree = compMgr.createItsNatFreeTree(treeElem,null);
        tree.setJoystickMode(joystickModeCheck.isSelected());

        this.include = compMgr.createItsNatFreeInclude(doc.getElementById("featureBoxId"),null);

        buildMainTree();

        SelectFeatureFromURL.selectFeatureNode(this,request);

        itsNatDoc.addEventListener(new RemoteControlTimerMgrGlobalEventListener(itsNatDoc));
    }

    public void buildMainTree()
    {
        this.mainTreeDecorator = new MainTreeDecorator(tree);
        mainTreeDecorator.bind();

        tree.setItsNatTreeCellRenderer(new MainTreeRenderer(tree.getItsNatTreeCellRenderer()));

        tree.setItsNatTreeCellEditor(null); // to avoid edition

        TreeSelectionModel selModel = tree.getTreeSelectionModel();
        selModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        selModel.addTreeSelectionListener(this);

        FeatureTreeBuilder treeBuilder = new FeatureTreeBuilder(this);
        FeatureTreeNode rootNode = treeBuilder.buildFeatureTreeModel();
        treeBuilder.buildSwingTree(rootNode);
    }

    public String getRequestURLOfDocument()
    {
        return requestURL;
    }

    public boolean isUCWEB()
    {
        return ucweb;
    }

    public boolean isBolt()
    {
        return bolt;
    }

    public boolean isJoystickModePreferred()
    {
        return joystickModePreferred;
    }

    public ItsNatHTMLDocument getItsNatHTMLDocument()
    {
        return itsNatDoc;
    }

    public ItsNatFreeComboBox getTabsComboBox()
    {
        return tabsCombo;
    }

    public Map getFeaturesMap()
    {
        return features;
    }

    public Element getFeatureBoxElement()
    {
        return include.getElement();
    }

    public ItsNatFreeTree getItsNatFreeTree()
    {
        return tree;
    }

    public Element getTitleElement()
    {
        return titleElement;
    }

    public Element getFeatureTitleElement()
    {
        return featureTitleElement;
    }

    public Element getExampleTabElement()
    {
        return exampleTab;
    }

    public Element getDocTabElement()
    {
        return docTab;
    }

    public Element getCodeTabElement()
    {
        return codeTab;
    }

    public HTMLAnchorElement getExampleTabPermalinkElement()
    {
        return exampleTabLink;
    }

    public HTMLAnchorElement getDocTabPermalinkElement()
    {
        return docTabLink;
    }

    public HTMLAnchorElement getCodeTabPermalinkElement()
    {
        return codeTabLink;
    }

    public ItsNatFreeInclude getItsNatFreeInclude()
    {
        return include;
    }

    public DefaultMutableTreeNode getDefaultMutableTreeNode(String featureName)
    {
        FeatureTreeNode feature = (FeatureTreeNode)features.get(featureName);
        if (feature == null)
            return null;
        return feature.getDefaultMutableTreeNode();
    }

    public BrowserAdaptor getBrowserAdaptor()
    {
        return browserAdaptor;
    }

    public ItsNatHTMLInputCheckBox getJoystickModeCheckBox()
    {
        return joystickModeCheck;
    }

    public void valueChanged(TreeSelectionEvent e)
    {
//        TreeSelectionModel selModel = (TreeSelectionModel)e.getSource();

        TreePath[] paths = e.getPaths();

        DefaultMutableTreeNode dataNode = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
        FeatureTreeNode example = (FeatureTreeNode)dataNode.getUserObject();

        if (selectedExample == example)
            return;
        if (selectedExample != null)
            selectedExample.exitFeature();

        this.selectedExample = example;

        selectedExample.selectFeature();
    }

    public void itemStateChanged(ItemEvent e)
    {
        boolean selected = e.getStateChange() == ItemEvent.SELECTED;
        tree.setJoystickMode(selected);
        tabsCombo.setJoystickMode(selected);
    }
}
