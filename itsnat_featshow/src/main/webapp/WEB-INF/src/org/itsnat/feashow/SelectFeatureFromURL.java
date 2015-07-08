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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.tree.ItsNatFreeTree;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

public class SelectFeatureFromURL
{
    public static void selectFeatureNode(FeatureShowcaseDocument featShowDoc,ItsNatServletRequest request)
    {
        String featureNameComplete = request.getServletRequest().getParameter("feature");
        if (featureNameComplete == null)
        {
            selectDefault(featShowDoc);
        }
        else
        {
            int pos = featureNameComplete.lastIndexOf('.');
            String featureName = featureNameComplete.substring(0,pos);

            DefaultMutableTreeNode dataNode = (DefaultMutableTreeNode)featShowDoc.getDefaultMutableTreeNode(featureName);
            if (dataNode == null)
                selectDefault(featShowDoc);  // Feature not found (may be an old link no longer valid)
            else
            {
                FeatureTreeNode feature = (FeatureTreeNode)dataNode.getUserObject();
                ItsNatFreeTree tree = featShowDoc.getItsNatFreeTree();
                DefaultTreeModel dataModel = (DefaultTreeModel)tree.getTreeModel();
                TreePath path = new TreePath(dataModel.getPathToRoot(dataNode));

                String panelName = featureNameComplete.substring(pos + 1,featureNameComplete.length());
                int panel = feature.getPanelCode(panelName);

                String methodName = request.getServletRequest().getParameter("method");
                if (methodName == null)
                    methodDirect(path,feature,panel); // by default
                else if (methodName.equals("direct"))
                    methodDirect(path,feature,panel);
                else if (methodName.equals("evtbrowser"))
                    methodEventUsingBrowser(path,feature,panel);
                else if (methodName.equals("evtnotbrowser"))
                    methodEventNotBrowser(path,feature,panel);
                else
                    throw new RuntimeException("Unknown method:" + methodName);
            }
        }
    }

    public static void selectDefault(FeatureShowcaseDocument featShowDoc)
    {
        ItsNatFreeTree tree = featShowDoc.getItsNatFreeTree();
        DefaultTreeModel dataModel = (DefaultTreeModel)tree.getTreeModel();

        DefaultMutableTreeNode dataNode = (DefaultMutableTreeNode)dataModel.getRoot();
        FeatureTreeNode feature = (FeatureTreeNode)dataNode.getUserObject();
        methodDirect(new TreePath(dataModel.getRoot()),feature,feature.getFirstPanel());
    }

    public static void methodDirect(TreePath path,FeatureTreeNode feature,int panel)
    {
        FeatureShowcaseDocument featShowDoc = feature.getFeatureShowcaseDocument();
        ItsNatFreeTree tree = featShowDoc.getItsNatFreeTree();

        feature.setOnloadTargetPanel(panel);

        TreeSelectionModel selModel = tree.getTreeSelectionModel();
        selModel.setSelectionPath(path);
    }

    public static void methodEventUsingBrowser(final TreePath path,final FeatureTreeNode feature,final int panel)
    {
        FeatureShowcaseDocument featShowDoc = feature.getFeatureShowcaseDocument();
        final ItsNatFreeTree tree = featShowDoc.getItsNatFreeTree();
        final ItsNatDocument itsNatDoc = feature.getItsNatDocument();
        final ItsNatHTMLInputCheckBox joystickCB = featShowDoc.getJoystickModeCheckBox();

        Runnable dispCode = new Runnable()
        {
            public void run()
            {
                // Joystick mode temporally disabled, because on load time and fast load,
                // event listeners in the component are not added until the load event is fired.

                boolean useJoystick = joystickCB.isSelected();
                if (useJoystick) joystickCB.setSelected(false);

                Element featureElem;
                MouseEvent event1;
                synchronized(itsNatDoc)
                {
                    featureElem = tree.getItsNatTreeUI().getParentElementFromTreePath(path);
                    event1 = createMouseEvent(itsNatDoc);
                }
                ((EventTarget)featureElem).dispatchEvent(event1); // Select feature

                if (panel == feature.getFirstPanel())
                    return;

                for( ; ; )
                {
                    synchronized(itsNatDoc)
                    {
                        if (tree.getTreeSelectionModel().isPathSelected(path))
                            break;
                    }
                    try{ Thread.sleep(200); }catch(InterruptedException ex) { throw new RuntimeException(ex); }
                }

                Element tabElem;
                MouseEvent event2;
                synchronized(itsNatDoc)
                {
                    tabElem = feature.getTabElement(panel);
                    event2 = createMouseEvent(itsNatDoc);
                }

                ((EventTarget)tabElem).dispatchEvent(event2); // Select tab

                if (useJoystick) joystickCB.setSelected(true);
            }
        };
        ClientDocument client = itsNatDoc.getClientDocumentOwner();
        client.startEventDispatcherThread(dispCode);
    }

    public static void methodEventNotBrowser(TreePath path,FeatureTreeNode feature,int panel)
    {
        // Note: this technique may be problematic in fastLoadMode = true if the target
        // panel is not the "example" panel because the example panel is loaded and discarded.

        FeatureShowcaseDocument featShowDoc = feature.getFeatureShowcaseDocument();
        ItsNatFreeTree tree = featShowDoc.getItsNatFreeTree();
        ItsNatDocument itsNatDoc = feature.getItsNatDocument();
        ItsNatHTMLInputCheckBox joystickCB = featShowDoc.getJoystickModeCheckBox();

        // Joystick mode temporally disabled, because on load time and fast load,
        // event listeners in the component are not added until the load event is fired.
        boolean useJoystick = joystickCB.isSelected();
        if (useJoystick) joystickCB.setSelected(false);
            
        Element featureElem = tree.getItsNatTreeUI().getParentElementFromTreePath(path);
        MouseEvent event1 = createMouseEvent(itsNatDoc);
        ((EventTarget)featureElem).dispatchEvent(event1); // Select feature

        if (panel == feature.getFirstPanel())
            return;

        Element tabElem = feature.getTabElement(panel);
        MouseEvent event2 = createMouseEvent(itsNatDoc);
        ((EventTarget)tabElem).dispatchEvent(event2); // Select tab

        if (useJoystick) joystickCB.setSelected(true);
    }

    public static MouseEvent createMouseEvent(ItsNatDocument itsNatDoc)
    {
        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();

        MouseEvent event = (MouseEvent)((DocumentEvent)doc).createEvent("MouseEvents");
        event.initMouseEvent("click",true,true,view,0,
                0,0,0,0,false,false,false,false,(short)0/*left button*/,null);
        return event;
    }

}
