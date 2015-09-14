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

import java.util.LinkedList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.itsnat.feashow.features.comp.other.FormTreeNode;
import org.itsnat.feashow.features.comp.other.FreeIncludeTreeNode;
import org.itsnat.feashow.features.comp.autobuild.AutoCompBuildAllTypesTreeNode;
import org.itsnat.feashow.features.comp.autobuild.mdforms.MarkupDrivenCheckBoxTreeNode;
import org.itsnat.feashow.features.comp.autobuild.mdforms.MarkupDrivenComboBoxTreeNode;
import org.itsnat.feashow.features.comp.autobuild.mdforms.MarkupDrivenInputTextTreeNode;
import org.itsnat.feashow.features.comp.autobuild.mdforms.MarkupDrivenRadioTreeNode;
import org.itsnat.feashow.features.comp.autobuild.mdforms.MarkupDrivenSelMultTreeNode;
import org.itsnat.feashow.features.comp.autobuild.mdforms.MarkupDrivenTextAreaTreeNode;
import org.itsnat.feashow.features.comp.buttons.normal.FreeButtonNormalTreeNode;
import org.itsnat.feashow.features.comp.buttons.normal.HTMLButtonsTreeNode;
import org.itsnat.feashow.features.comp.buttons.normal.InputSubmitFormTreeNode;
import org.itsnat.feashow.features.comp.buttons.normal.MouseUpDownOverOutTreeNode;
import org.itsnat.feashow.features.comp.buttons.toggle.FreeCheckBoxTreeNode;
import org.itsnat.feashow.features.comp.buttons.toggle.FreeRadioButtonTreeNode;
import org.itsnat.feashow.features.comp.buttons.toggle.InputCheckBoxTreeNode;
import org.itsnat.feashow.features.comp.buttons.toggle.InputRadioButtonTreeNode;
import org.itsnat.feashow.features.comp.other.custom.CustomComponentTreeNode;
import org.itsnat.feashow.features.comp.functest.FuncTestNotBrowserTreeNode;
import org.itsnat.feashow.features.comp.functest.FuncTestUsingBrowserTreeNode;
import org.itsnat.feashow.features.comp.labels.FreeLabelCustomEditorTreeNode;
import org.itsnat.feashow.features.comp.labels.FreeLabelCustomRendererTreeNode;
import org.itsnat.feashow.features.comp.labels.FreeLabelTreeNode;
import org.itsnat.feashow.features.comp.labels.LabelTreeNode;
import org.itsnat.feashow.features.comp.layers.ModalLayerHTMLTreeNode;
import org.itsnat.feashow.features.comp.lists.FreeComboBoxTreeNode;
import org.itsnat.feashow.features.comp.lists.FreeListCompoundTreeNode;
import org.itsnat.feashow.features.comp.lists.FreeListCustomStructureTreeNode;
import org.itsnat.feashow.features.comp.lists.FreeListTreeNode;
import org.itsnat.feashow.features.comp.lists.SelectComboBoxTreeNode;
import org.itsnat.feashow.features.comp.lists.SelectListTreeNode;
import org.itsnat.feashow.features.comp.other.FileUploadTreeNode;
import org.itsnat.feashow.features.comp.other.customtag.CustomTagComponentTreeNode;
import org.itsnat.feashow.features.comp.tables.FreeTableTreeNode;
import org.itsnat.feashow.features.comp.tables.TableRowSpanTreeNode;
import org.itsnat.feashow.features.comp.tables.TableTreeNode;
import org.itsnat.feashow.features.comp.text.TextAreaTreeNode;
import org.itsnat.feashow.features.comp.text.fields.InputHiddenTreeNode;
import org.itsnat.feashow.features.comp.text.fields.InputTextBasedTreeNode;
import org.itsnat.feashow.features.comp.text.fields.InputTextFormattedTreeNode;
import org.itsnat.feashow.features.comp.text.fields.InputTextFormattedWithFactoryTreeNode;
import org.itsnat.feashow.features.comp.text.fields.InputTextKeyUpDownTreeNode;
import org.itsnat.feashow.features.comp.trees.FreeTreeRootlessTreeNode;
import org.itsnat.feashow.features.comp.trees.FreeTreeTableTreeNode;
import org.itsnat.feashow.features.comp.trees.FreeTreeTreeNode;
import org.itsnat.feashow.features.core.domutils.MarkupFragmentsTreeNode;
import org.itsnat.feashow.features.core.misc.remctrl.RemoteControlTreeNode;
import org.itsnat.feashow.features.core.domutils.DOMRendererTreeNode;
import org.itsnat.feashow.features.core.domutils.DisconnectNodesTreeNode;
import org.itsnat.feashow.features.core.domutils.ElementCSSInlineStyleTreeNode;
import org.itsnat.feashow.features.core.domutils.FreeElementListTreeNode;
import org.itsnat.feashow.features.core.domutils.FreeElementTableTreeNode;
import org.itsnat.feashow.features.core.ioeaauto.IFrameHTMLAutoBindingTreeNode;
import org.itsnat.feashow.features.core.domutils.PatternBasedElementLabelTreeNode;
import org.itsnat.feashow.features.core.domutils.PatternBasedElementListTreeNode;
import org.itsnat.feashow.features.core.domutils.PatternBasedElementTableTreeNode;
import org.itsnat.feashow.features.core.domutils.PatternBasedElementTreeTreeNode;
import org.itsnat.feashow.features.core.domutils.ToDOMTreeNode;
import org.itsnat.feashow.features.core.domutils.VariableResolverTreeNode;
import org.itsnat.feashow.features.core.ioeaauto.IFrObjEmbSVGASVAutoBindingTreeNode;
import org.itsnat.feashow.features.core.ioeaauto.ObjEmbAppSVGBatikAutoBindingTreeNode;
import org.itsnat.feashow.features.core.ioeaauto.ObjEmbSVGSsrcAutoBindingTreeNode;
import org.itsnat.feashow.features.core.jsutils.EventMonitorTreeNode;
import org.itsnat.feashow.features.core.jsutils.JavaToJavaScriptGenTreeNode;
import org.itsnat.feashow.features.core.listeners.EventTimeoutTreeNode;
import org.itsnat.feashow.features.core.listeners.AsynchronousTaskTreeNode;
import org.itsnat.feashow.features.core.listeners.AutoSyncServerFromClientTreeNode;
import org.itsnat.feashow.features.core.listeners.CometNotifierTreeNode;
import org.itsnat.feashow.features.core.listeners.ContinueEventListenerTreeNode;
import org.itsnat.feashow.features.core.listeners.DOMEventListenerTreeNode;
import org.itsnat.feashow.features.core.listeners.EvtListChainCtrlTreeNode;
import org.itsnat.feashow.features.core.listeners.MutationEventListenerTreeNode;
import org.itsnat.feashow.features.core.listeners.CommModesTreeNode;
import org.itsnat.feashow.features.core.listeners.TimersTreeNode;
import org.itsnat.feashow.features.core.listeners.UserEventListenerTreeNode;
import org.itsnat.feashow.features.core.otherns.SVGInHTMLMimeASVTreeNode;
import org.itsnat.feashow.features.core.otherns.SVGInHTMLMimeTreeNode;
import org.itsnat.feashow.features.core.serverevts.ServerEventsNotBrowserTreeNode;
import org.itsnat.feashow.features.core.serverevts.ServerEventsUsingBrowserTreeNode;

public class FeatureTreeBuilder
{
    protected FeatureShowcaseDocument feaShowDoc;

    public FeatureTreeBuilder(FeatureShowcaseDocument feaShowDoc)
    {
        this.feaShowDoc = feaShowDoc;
    }

    protected FeatureTreeNode addRoot(boolean hasExample,boolean hasExplanation,boolean hasSourceCode,
                    String featureName,String treeNodeText,String title,
                    FeatureTreeNode feature)
    {
        if (feature == null) feature = new NoCodeTreeNode();
        feature.init(feaShowDoc,null,hasExample,hasExplanation,hasSourceCode,featureName,treeNodeText,title);
        feaShowDoc.getFeaturesMap().put(featureName,feature);
        return feature;
    }

    protected FeatureTreeNode addNode(boolean hasExample,boolean hasExplanation,boolean hasSourceCode,
                    String featureName,String treeNodeText,String title,
                    FeatureTreeNode feature,FeatureTreeNode parentFeature)
    {
        if (feature == null) feature = new NoCodeTreeNode();
        feature.init(feaShowDoc,parentFeature,hasExample,hasExplanation,hasSourceCode,featureName,treeNodeText,title);
        feaShowDoc.getFeaturesMap().put(featureName,feature);
        return feature;
    }

    public void buildSwingTree(FeatureTreeNode featureRoot)
    {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(featureRoot);
        featureRoot.setDefaultMutableTreeNode(rootNode);

        DefaultTreeModel dataModel = (DefaultTreeModel)feaShowDoc.getItsNatFreeTree().getTreeModel();
        dataModel.setRoot(rootNode);

        addFeatureChildrenToSwingTree(featureRoot);
    }

    public void addFeatureChildrenToSwingTree(FeatureTreeNode feature)
    {
        LinkedList<FeatureTreeNode> childList = feature.getChildFeatureTreeNodes();
        if (childList == null) return;
        for(FeatureTreeNode featureChild : childList)
        {
            addFeatureToSwingTree(featureChild);
        }
    }

    protected void addFeatureToSwingTree(FeatureTreeNode feature)
    {
        DefaultTreeModel dataModel = (DefaultTreeModel)feaShowDoc.getItsNatFreeTree().getTreeModel();
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(feature);
        feature.setDefaultMutableTreeNode(childNode);

        FeatureTreeNode parentFeature = feature.getFeatureTreeNodeParent();
        DefaultMutableTreeNode parentNode = parentFeature.getDefaultMutableTreeNode();
        int count = dataModel.getChildCount(parentNode);
        dataModel.insertNodeInto(childNode,parentNode,count);

        addFeatureChildrenToSwingTree(feature);
    }

    public FeatureTreeNode buildFeatureTreeModel()
    {
        FeatureTreeNode rootNode = addRoot(false,true,false,
                        "feashow.root",
                        "ItsNat",
                        "Introduction to ItsNat",
                        null);

        FeatureTreeNode childNodeL1;
        FeatureTreeNode childNodeL2;
        FeatureTreeNode childNodeL3;
        FeatureTreeNode childNodeL4;

        childNodeL1 = addNode(false,true,true,
                        "feashow.featShowcase",
                        "Feature Showcase Overview",
                        "Feature Showcase Overview",
                        null,rootNode);

        childNodeL1 = addNode(false,true,false,
                        "feashow.core",
                        "Core",
                        "Core",
                        null,rootNode);

            childNodeL2 = addNode(false,true,false,
                            "feashow.core.man",
                            "Core Examples in Manual",
                            "Core Examples in Manual",
                            null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                                "feashow.core.man.htmlExampleInManual",
                                "HTML Example in Manual",
                                "HTML Example in Manual",
                                null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                                "feashow.core.man.xmlExampleInManual",
                                "XML Example in Manual",
                                "XML Example in Manual",
                                null,childNodeL2);

            childNodeL2 = addNode(false,true,false,
                            "feashow.core.listeners",
                            "Listeners",
                            "Listeners",
                            null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.listeners.domEventListener",
                        "DOM Event Listeners",
                        "DOM Event Listeners",
                        new DOMEventListenerTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.listeners.commModes",
                        "Communication Modes",
                        "Communication Modes with the Server",
                        new CommModesTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.listeners.mutationEventListener",
                        "Mutation Event Listeners",
                        "Mutation Event Listeners",
                        new MutationEventListenerTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.listeners.autoSyncServerFromClient",
                        "Auto Sync Server From Client",
                        "Auto Sync Server From Client",
                        new AutoSyncServerFromClientTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.listeners.continueEventListener",
                        "Continue Event Listeners",
                        "Continue Event Listeners",
                        new ContinueEventListenerTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.listeners.userEventListener",
                        "User Defined Event Listeners",
                        "User Defined Event Listeners",
                        new UserEventListenerTreeNode(),childNodeL2);

                 childNodeL3 = addNode(true,true,true,
                        "feashow.core.listeners.timers",
                        "AJAX (or SCRIPT) Timers",
                        "AJAX (or SCRIPT) Timers",
                        new TimersTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.listeners.asyncTask",
                        "Asynchronous Task",
                        "Asynchronous Task",
                        new AsynchronousTaskTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.listeners.cometNotifier",
                        "Comet Notifier",
                        "Comet Notifier",
                        new CometNotifierTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.listeners.eventTimeout",
                        "Event Timeout",
                        "Event Timeout",
                        new EventTimeoutTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.listeners.evtListChainCtrl",
                        "Event Listener Chain Control",
                        "Event Listener Chain Control",
                        new EvtListChainCtrlTreeNode(),childNodeL2);

            childNodeL2 = addNode(false,true,false,
                            "feashow.core.domutils",
                            "DOM Utils",
                            "DOM Utils",
                            null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.freeElementList",
                        "Free Element Lists",
                        "Free Element Lists",
                        new FreeElementListTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.freeElementTable",
                        "Free Element Tables",
                        "Free Element Tables",
                        new FreeElementTableTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.domRenderer",
                        "DOM Renderers",
                        "DOM Renderers",
                        new DOMRendererTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.patternBasedElementLabel",
                        "Pattern Based Element Labels",
                        "Pattern Based Element Labels",
                        new PatternBasedElementLabelTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.patternBasedElementList",
                        "Pattern Based Element Lists",
                        "Pattern Based Element Lists",
                        new PatternBasedElementListTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.patternBasedElementTable",
                        "Pattern Based Element Tables",
                        "Pattern Based Element Tables",
                        new PatternBasedElementTableTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.patternBasedElementTree",
                        "Pattern Based Element Trees",
                        "Pattern Based Element Trees",
                        new PatternBasedElementTreeTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.variableResolver",
                        "Variable Resolver",
                        "Variable Resolver",
                        new VariableResolverTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.elementCSSInlineStyle",
                        "W3C's ElementCSSInlineStyle",
                        "W3C's ElementCSSInlineStyle",
                        new ElementCSSInlineStyleTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.toDOM",
                        "String to DOM",
                        "String to DOM",
                        new ToDOMTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.markupFragments",
                        "Markup Fragments",
                        "Markup Fragments",
                        new MarkupFragmentsTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.domutils.disconnectNodes",
                        "Disconnect Nodes from Client",
                        "Disconnect Nodes from Client",
                        new DisconnectNodesTreeNode(),childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.core.otherns",
                    "Other Namespaces",
                    "Other Namespaces",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.otherns.svgInXHTMLMime",
                        "SVG in XHTML and XHTML MIME",
                        "SVG in XHTML and MIME application/xhtml+xml",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.otherns.svgInHTMLMime",
                        "SVG in XHTML and HTML MIME",
                        "SVG inline in XHTML and MIME text/html",
                        new SVGInHTMLMimeTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.otherns.svgInHTMLMimeASV",
                        "SVG in XHTML, HTML MIME and ASV",
                        "SVG inline in XHTML, MIME text/html and ASV",
                        new SVGInHTMLMimeASVTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.otherns.svgInHTMLMimeSVGWeb",
                        "SVG in XHTML, HTML MIME and SVGWeb",
                        "SVG inline in XHTML, MIME text/html and SVGWeb",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.otherns.svgPure",
                        "Pure SVG",
                        "Pure SVG",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.otherns.xulPure",
                        "Pure XUL",
                        "Pure XUL",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.otherns.xmlGeneration",
                        "XML Generation",
                        "XML Generation",
                        null,childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.core.ioeaauto",
                    "IFr/Obj/Emb/Applet Auto-Binding",
                    "IFrame/Object/Embed/Applet Auto-Binding",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.ioeaauto.iframeHTMLAutoBinding",
                        "HTML IFrame Example",
                        "HTML IFrame Auto-Binding Example",
                        new IFrameHTMLAutoBindingTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.ioeaauto.ioeSVGASVAutoBinding",
                        "SVG IFr/Obj/Emb Example",
                        "SVG IFrame/Object/Embed Auto-Binding Example",
                        new IFrObjEmbSVGASVAutoBindingTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.ioeaauto.oeSVGSsrcAutoBinding",
                        "SVG Obj/Emb Ssrc Example",
                        "SVG Object/Embed Ssrc Auto-Binding Example",
                        new ObjEmbSVGSsrcAutoBindingTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.ioeaauto.oeaSVGBatikAutoBinding",
                        "SVG App/Obj/Emb Batik Example",
                        "SVG Applet/Object/Embed Batik Auto-Binding Example",
                        new ObjEmbAppSVGBatikAutoBindingTreeNode(),childNodeL2);

            childNodeL2 = addNode(false,true,false,
                            "feashow.core.jsutils",
                            "JavaScript Utils",
                            "JavaScript Utils",
                            null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.jsutils.javaToJavaScriptGen",
                        "Java to JavaScript Generation",
                        "Java to JavaScript Generation",
                        new JavaToJavaScriptGenTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.jsutils.eventMonitor",
                        "Event Monitor",
                        "Event Monitor",
                        new EventMonitorTreeNode(),childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.core.serverevts",
                    "Server-sent Events",
                    "Server-sent Events",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.serverevts.usingBrowser",
                        "Using The Browser",
                        "Using The Browser",
                        new ServerEventsUsingBrowserTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.serverevts.notBrowser",
                        "Not Using The Browser",
                        "Not Using The Browser",
                        new ServerEventsNotBrowserTreeNode(),childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.core.referrer",
                    "Referrers",
                    "Referrers",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.referrer.referrerPull",
                        "Referrer Pull & Fwd/Back Buttons",
                        "Referrer Pull & Fwd/Back Button",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.referrer.referrerPush",
                        "Referrer Push & Fwd/Back Button",
                        "Referrer Push & Fwd/Back Button",
                        null,childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.core.degraded",
                    "Degraded Modes",
                    "Degraded Modes",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.degraded.disabledEvents",
                        "Disabled Events",
                        "Disabled Events",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.degraded.disabledScript",
                        "Disabled JavaScript",
                        "Disabled JavaScript",
                        null,childNodeL2);


            childNodeL2 = addNode(false,true,false,
                    "feashow.core.emash",
                    "Extreme Mashups",
                    "Extreme Mashups",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.emash.extremeMashupHTMLMime",
                        "Extreme Mashup HTML Mime",
                        "Extreme Mashup (a.k.a. Attached Server) HTML Mime",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.emash.extremeMashupXHTMLMime",
                        "Extreme Mashup XHTML Mime",
                        "Extreme Mashup (a.k.a. Attached Server) XHTML Mime",
                        null,childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.core.misc",
                    "Misc. Features",
                    "Misc. Core Features",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.misc.bookmarking",
                        "Bookmarking with AJAX",
                        "Bookmarking with AJAX",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.misc.prettyURL",
                        "Pretty URLs",
                        "Pretty URLs",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.misc.remoteCtrl",
                        "Remote View/Control",
                        "Remote View/Control (a.k.a. Attached Client)",
                        new RemoteControlTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.core.misc.remoteTemplate",
                        "Remote Templates",
                        "Remote Templates",
                        null,childNodeL2);

        // Components

        childNodeL1 = addNode(false,true,false,
                        "feashow.comp",
                        "Components",
                        "Components",
                        null,rootNode);

            childNodeL2 = addNode(true,true,true,
                            "feashow.comp.exampleInManual",
                            "Component Based Example in Manual",
                            "Component Based Example in Manual",
                            null,childNodeL1);

            childNodeL2 = addNode(false,true,false,
                    "feashow.comp.buttons",
                    "Buttons",
                    "Buttons",
                    null,childNodeL1);

                childNodeL3 = addNode(false,true,false,
                        "feashow.comp.buttons.normal",
                        "Normal",
                        "Normal Buttons",
                        null,childNodeL2);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.buttons.normal.htmlButtons",
                            "HTML Buttons",
                            "HTML Buttons",
                            new HTMLButtonsTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.buttons.normal.inputSubmitForm",
                            "Input Submit with Form",
                            "Input Submit with Form",
                            new InputSubmitFormTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.buttons.normal.freeButton",
                            "Free Button",
                            "Free Button",
                            new FreeButtonNormalTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.buttons.normal.mouseUpDown",
                            "Mouse Up/Down/Over/Out",
                            "Mouse Up/Down/Over/Out",
                            new MouseUpDownOverOutTreeNode(),childNodeL3);

                childNodeL3 = addNode(false,true,false,
                        "feashow.comp.buttons.toggle",
                        "Toggle",
                        "Toggle Buttons",
                        null,childNodeL2);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.buttons.toggle.inputCheckBox",
                            "Input Check Box",
                            "Input Check Box",
                            new InputCheckBoxTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.buttons.toggle.inputRadio",
                            "Input Radio",
                            "Input Radio",
                            new InputRadioButtonTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.buttons.toggle.freeCheckBox",
                            "Free Check Box",
                            "Free Check Box",
                            new FreeCheckBoxTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.buttons.toggle.freeRadio",
                            "Free Radio Button",
                            "Free Radio Button",
                            new FreeRadioButtonTreeNode(),childNodeL3);

            childNodeL2 = addNode(false,true,false,
                    "feashow.comp.text",
                    "Text",
                    "Text Components",
                    null,childNodeL1);

                childNodeL3 = addNode(false,true,false,
                        "feashow.comp.text.fields",
                        "Text Fields",
                        "Text Fields",
                        null,childNodeL2);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.text.fields.inputTextBased",
                            "Input Text Based",
                            "Input Text Based",
                            new InputTextBasedTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.text.fields.inputHidden",
                            "Input Hidden",
                            "Input Hidden",
                            new InputHiddenTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.text.fields.inputTextFormatted",
                            "Input Text Formatted",
                            "Input Text Formatted",
                            new InputTextFormattedTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.text.fields.inputTextFormattedWithFactory",
                            "Input Text Formatted with Factory",
                            "Input Text Formatted with Factory",
                            new InputTextFormattedWithFactoryTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.text.fields.inputTextKeyUpDown",
                            "Key Up/Down",
                            "Key Up/Down",
                            new InputTextKeyUpDownTreeNode(),childNodeL3);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.text.textArea",
                        "Text Area",
                        "Text Area",
                        new TextAreaTreeNode(),childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.comp.labels",
                    "Labels",
                    "Labels",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.labels.label",
                        "Label",
                        "Label",
                        new LabelTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.labels.freeLabel",
                        "Free Label",
                        "Free Label",
                        new FreeLabelTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.labels.freeLabelCustomEditor",
                        "Custom Editor",
                        "Custom Editor",
                        new FreeLabelCustomEditorTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.labels.freeLabelCustomRenderer",
                        "Custom Renderer",
                        "Custom Renderer",
                        new FreeLabelCustomRendererTreeNode(),childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.comp.lists",
                    "Lists",
                    "Lists",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.lists.selectComboBox",
                        "Select Combo Box",
                        "Select Combo Box",
                        new SelectComboBoxTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.lists.selectList",
                        "Select List",
                        "Select List",
                        new SelectListTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.lists.freeComboBox",
                        "Free Combo Box",
                        "Free Combo Box",
                        new FreeComboBoxTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.lists.freeList",
                        "Free List",
                        "Free List",
                        new FreeListTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.lists.freeListCustomStructure",
                        "Free List Custom Structure",
                        "Free List Custom Structure",
                        new FreeListCustomStructureTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.lists.freeListCompound",
                        "Free List Compound",
                        "Free List Compound",
                        new FreeListCompoundTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.lists.freeListSVGEmb",
                        "Free List SVG Embedded",
                        "Free List SVG Embedded",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.lists.freeListSVGPure",
                        "Free List Pure SVG and XHTML",
                        "Free List Pure SVG and XHTML Embedded",
                        null,childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.comp.tables",
                    "Tables",
                    "Tables",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.tables.table",
                        "Table",
                        "Table",
                        new TableTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.tables.freeTable",
                        "Free Table",
                        "Free Table",
                        new FreeTableTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.tables.tableRowSpan",
                        "Table using Row Span",
                        "Table using Row Span",
                        new TableRowSpanTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.tables.freeTableSVGEmb",
                        "Free Table SVG Embedded",
                        "Free Table SVG Embedded",
                        null,childNodeL2);

            childNodeL2 = addNode(false,true,true,
                    "feashow.comp.trees",
                    "Trees",
                    "Trees",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.trees.freeTreeUsingUL",
                        "Tree using <ul>",
                        "Tree using <ul>",
                        new FreeTreeTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.trees.freeTreeUsingTable",
                        "Tree using <table>",
                        "Tree using <table>",
                        new FreeTreeTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.trees.freeTreeRootless",
                        "Tree Rootless",
                        "Tree Rootless",
                        new FreeTreeRootlessTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.trees.freeTreeTable",
                        "Tree-Table",
                        "Tree-Table",
                        new FreeTreeTableTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.trees.freeTreeTableNoHTMLTable",
                        "Tree-Table without <table>",
                        "Tree-Table without <table>",
                        new FreeTreeTableTreeNode(),childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.comp.layers",
                    "Modal Layers",
                    "Modal Layers",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.layers.modalLayerHTML",
                        "HTML Modal Layers",
                        "HTML Modal Layers",
                        new ModalLayerHTMLTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.layers.modalLayerSVG",
                        "SVG Modal Layers",
                        "SVG Modal Layers",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.layers.modalLayerXUL",
                        "XUL Modal Layers",
                        "XUL Modal Layers",
                        null,childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.comp.other",
                    "Other Components",
                    "Other Components",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.other.form",
                        "Form",
                        "Form",
                        new FormTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.other.fileUpload",
                        "File Upload",
                        "File Upload",
                        new FileUploadTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.other.freeInclude",
                        "Free Include",
                        "Free Include",
                        new FreeIncludeTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.other.customComponent",
                        "Custom Components",
                        "Custom Components",
                        new CustomComponentTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.other.customTagComponent",
                        "Custom Tag Components",
                        "Custom Tag Components",
                        new CustomTagComponentTreeNode(),childNodeL2);                
                
            childNodeL2 = addNode(false,true,false,
                    "feashow.comp.autobuild",
                    "Auto. Component Build",
                    "Automatic Component Build",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.autobuild.allTypes",
                        "Auto. Comp. Build: All Types",
                        "Auto. Comp. Build: All Types",
                        new AutoCompBuildAllTypesTreeNode(),childNodeL2);

                childNodeL3 = addNode(false,true,false,
                        "feashow.comp.autobuild.markupDrivenForms",
                        "Markup Driven Forms",
                        "Markup Driven Forms",
                        null,childNodeL2);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.autobuild.mdforms.markupDrivenCheckBox",
                            "Check Box",
                            "Markup Driven Check Box",
                            new MarkupDrivenCheckBoxTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.autobuild.mdforms.markupDrivenRadio",
                            "Radio Buttons",
                            "Markup Driven Radio",
                            new MarkupDrivenRadioTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.autobuild.mdforms.markupDrivenInputText",
                            "Input Text",
                            "Markup Driven Input Text",
                            new MarkupDrivenInputTextTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.autobuild.mdforms.markupDrivenTextArea",
                            "Text Area",
                            "Markup Driven Text Area",
                            new MarkupDrivenTextAreaTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.autobuild.mdforms.markupDrivenComboBox",
                            "Combo Box",
                            "Markup Driven Combo Box",
                            new MarkupDrivenComboBoxTreeNode(),childNodeL3);

                    childNodeL4 = addNode(true,true,true,
                            "feashow.comp.autobuild.mdforms.markupDrivenSelMult",
                            "Select Multiple",
                            "Markup Driven Select Multiple",
                            new MarkupDrivenSelMultTreeNode(),childNodeL3);

            childNodeL2 = addNode(false,true,false,
                    "feashow.comp.functest",
                    "Server-Driven Func. Testing",
                    "Server-Driven Functional Testing",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.functest.usingBrowser",
                        "Func. Testing Using Browser",
                        "Functional Testing Using Browser",
                        new FuncTestUsingBrowserTreeNode(),childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.functest.notBrowser",
                        "Func. Testing Not Using Browser",
                        "Func. Testing Not Using Browser",
                        new FuncTestNotBrowserTreeNode(),childNodeL2);

            childNodeL2 = addNode(false,true,false,
                    "feashow.comp.degraded",
                    "Degraded Modes & Components",
                    "Degraded Modes & Components",
                    null,childNodeL1);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.degraded.disabledEventsAndComp",
                        "Disabled Events & Components",
                        "Disabled Events & Components",
                        null,childNodeL2);

                childNodeL3 = addNode(true,true,true,
                        "feashow.comp.degraded.disabledScriptAndComp",
                        "Disabled JavaScript & Components",
                        "Disabled JavaScript & Components",
                        null,childNodeL2);

            childNodeL2 = addNode(true,true,true,
                    "feashow.comp.xmlAndComponents",
                    "XML & Components",
                    "XML & Components",
                    null,childNodeL1);

        childNodeL1 = addNode(false,true,false,
                        "feashow.stless",
                        "Stateless",
                        "Stateless",
                        null,rootNode);
        
            childNodeL2 = addNode(true,true,true,
                            "feashow.stless.man.statelessExampleInManual",
                            "Stateless Example in Manual",
                            "Stateless Example in Manual",
                            null,childNodeL1);
            
            childNodeL2 = addNode(true,true,true,
                            "feashow.stless.comp.statelessFreeListExample",
                            "Stateless Free List Example",
                            "Stateless Free List Example",
                            null,childNodeL1);                
            
        return rootNode;
    }
}
