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

package org.itsnat.feashow.features.comp.autobuild;

import javax.swing.DefaultListModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.ItsNatHTMLForm;
import org.itsnat.comp.button.normal.ItsNatFreeButtonNormal;
import org.itsnat.comp.button.normal.ItsNatFreeButtonNormalLabel;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchor;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchorLabel;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.button.normal.ItsNatHTMLButtonLabel;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.normal.ItsNatHTMLInputImage;
import org.itsnat.comp.button.normal.ItsNatHTMLInputReset;
import org.itsnat.comp.button.normal.ItsNatHTMLInputSubmit;
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBox;
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBoxLabel;
import org.itsnat.comp.button.toggle.ItsNatFreeRadioButton;
import org.itsnat.comp.button.toggle.ItsNatFreeRadioButtonLabel;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputRadio;
import org.itsnat.comp.inc.ItsNatFreeInclude;
import org.itsnat.comp.label.ItsNatFreeLabel;
import org.itsnat.comp.label.ItsNatLabel;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.comp.table.ItsNatFreeTable;
import org.itsnat.comp.table.ItsNatHTMLTable;
import org.itsnat.comp.text.ItsNatHTMLInputFile;
import org.itsnat.comp.text.ItsNatHTMLInputHidden;
import org.itsnat.comp.text.ItsNatHTMLInputPassword;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.comp.tree.ItsNatFreeTree;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.BrowserAdaptor;
import org.itsnat.feashow.FeatureTreeNode;
import org.itsnat.feashow.features.comp.other.custom.LoginComponent;
import org.itsnat.feashow.features.comp.lists.CityListCustomStructure;
import org.w3c.dom.Element;

public class AutoCompBuildAllTypesTreeNode extends FeatureTreeNode
{
    protected Element parentElem;

    public AutoCompBuildAllTypesTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        this.parentElem = itsNatDoc.getDocument().getElementById("compGroupId");

        BrowserAdaptor adaptor = getFeatureShowcaseDocument().getBrowserAdaptor();
        if (adaptor != null) adaptor.setModeAuto(true);

        if (itsNatDoc.getArtifact("cityCustomStruc") == null)
        {
            ItsNatListStructure customStruc = new CityListCustomStructure();
            itsNatDoc.registerArtifact("cityCustomStruc",customStruc);
        }

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.buildItsNatComponents(parentElem);

        // Normal Buttons

        ItsNatHTMLInputButton inputButton = (ItsNatHTMLInputButton)compMgr.findItsNatComponentById("inputButtonId");
        check(inputButton);
        inputButton.setLabelValue("Input Button");

        ItsNatHTMLInputImage inputImage = (ItsNatHTMLInputImage)compMgr.findItsNatComponentById("inputImageId");
        check(inputImage);

        ItsNatHTMLInputSubmit inputSubmit = (ItsNatHTMLInputSubmit)compMgr.findItsNatComponentById("inputSubmitId");
        check(inputSubmit);
        inputSubmit.setLabelValue("Input Submit");

        ItsNatHTMLInputReset inputReset = (ItsNatHTMLInputReset)compMgr.findItsNatComponentById("inputResetId");
        check(inputReset);
        inputReset.setLabelValue("Input Reset");

        ItsNatHTMLButton button = (ItsNatHTMLButton)compMgr.findItsNatComponentById("buttonId");
        check(button);

        ItsNatHTMLButtonLabel buttonLabel = (ItsNatHTMLButtonLabel)compMgr.findItsNatComponentById("buttonLabelId");
        check(buttonLabel);
        buttonLabel.setLabelValue("Button With Label");

        ItsNatHTMLAnchor anchor = (ItsNatHTMLAnchor)compMgr.findItsNatComponentById("anchorId");
        check(anchor);

        ItsNatHTMLAnchorLabel anchorLabel = (ItsNatHTMLAnchorLabel)compMgr.findItsNatComponentById("anchorLabelId");
        check(anchorLabel);
        buttonLabel.setLabelValue("Anchor With Label");


        ItsNatFreeButtonNormal freeButtonNormal = (ItsNatFreeButtonNormal)compMgr.findItsNatComponentById("freeButtonNormalId");
        check(freeButtonNormal);

        ItsNatFreeButtonNormalLabel freeButtonNormalLabel = (ItsNatFreeButtonNormalLabel)compMgr.findItsNatComponentById("freeButtonNormalLabelId");
        check(freeButtonNormalLabel);
        freeButtonNormalLabel.setLabelValue("Free Button Normal Label");


        // Toggle Buttons

        ItsNatHTMLInputCheckBox inputCheckBox = (ItsNatHTMLInputCheckBox)compMgr.findItsNatComponentById("inputCheckBoxId");
        check(inputCheckBox);

        ItsNatHTMLInputRadio inputRadio = (ItsNatHTMLInputRadio)compMgr.findItsNatComponentById("inputRadioButtonId");
        check(inputRadio);

        ItsNatFreeCheckBox freeCheckBox = (ItsNatFreeCheckBox)compMgr.findItsNatComponentById("freeCheckBoxId");
        check(freeCheckBox);

        ItsNatFreeCheckBoxLabel freeCheckBoxLabel = (ItsNatFreeCheckBoxLabel)compMgr.findItsNatComponentById("freeCheckBoxLabelId");
        check(freeCheckBoxLabel);
        freeCheckBoxLabel.setLabelValue("Free Check Box Label");


        ItsNatFreeRadioButton freeRadioButton = (ItsNatFreeRadioButton)compMgr.findItsNatComponentById("freeRadioButtonId");
        check(freeRadioButton);

        ItsNatFreeRadioButtonLabel freeRadioButtonLabel = (ItsNatFreeRadioButtonLabel)compMgr.findItsNatComponentById("freeRadioButtonLabelId");
        check(freeRadioButtonLabel);
        freeRadioButtonLabel.setLabelValue("Free Radio Button Label");


        // Text Fields

        ItsNatHTMLInputText inputText = (ItsNatHTMLInputText)compMgr.findItsNatComponentById("inputTextId");
        check(inputText);
        inputText.setText("Input Text");

        ItsNatHTMLInputPassword inputPassword = (ItsNatHTMLInputPassword)compMgr.findItsNatComponentById("inputPasswordId");
        check(inputPassword);
        inputPassword.setText("Input Password");

        ItsNatHTMLInputHidden inputHidden = (ItsNatHTMLInputHidden)compMgr.findItsNatComponentById("inputHiddenId");
        check(inputHidden);

        ItsNatHTMLInputFile inputFile = (ItsNatHTMLInputFile)compMgr.findItsNatComponentById("inputFileId");
        check(inputFile);

        ItsNatHTMLInputTextFormatted inputTextFormat = (ItsNatHTMLInputTextFormatted)compMgr.findItsNatComponentById("inputTextFormattedId");
        check(inputTextFormat);
        try{ inputTextFormat.setValue("Input Text Formatted"); }catch(Exception ex) { }

        // Text Area
        ItsNatHTMLTextArea textArea = (ItsNatHTMLTextArea)compMgr.findItsNatComponentById("textAreaId");
        check(textArea);
        textArea.setText("Text Area");

        // Labels

        ItsNatLabel label = (ItsNatLabel)compMgr.findItsNatComponentById("labelId");
        check(label);
        try{ label.setValue("Label"); }catch(Exception ex) { }

        ItsNatFreeLabel freeLabel = (ItsNatFreeLabel)compMgr.findItsNatComponentById("freeLabelId");
        check(freeLabel);
        try{ freeLabel.setValue("Free Label"); }catch(Exception ex) { }

        // Lists

        ItsNatHTMLSelectComboBox selectCombo = (ItsNatHTMLSelectComboBox)compMgr.findItsNatComponentById("selectComboId");
        check(selectCombo);
        ((MutableComboBoxModel)selectCombo.getComboBoxModel()).addElement("Option 1");
        ((MutableComboBoxModel)selectCombo.getComboBoxModel()).addElement("Option 2");

        ItsNatHTMLSelectMult selectList = (ItsNatHTMLSelectMult)compMgr.findItsNatComponentById("selectListId");
        check(selectList);
        ((DefaultListModel)selectList.getListModel()).addElement("Option 1");
        ((DefaultListModel)selectList.getListModel()).addElement("Option 2");

        ItsNatFreeComboBox freeCombo = (ItsNatFreeComboBox)compMgr.findItsNatComponentById("freeComboBoxId");
        check(freeCombo);
        ((MutableComboBoxModel)freeCombo.getComboBoxModel()).addElement("Option 1");
        ((MutableComboBoxModel)freeCombo.getComboBoxModel()).addElement("Option 2");

        ItsNatFreeListMultSel freeList = (ItsNatFreeListMultSel)compMgr.findItsNatComponentById("freeListId");
        check(freeList);
        ((DefaultListModel)freeList.getListModel()).addElement("Option 1");
        ((DefaultListModel)freeList.getListModel()).addElement("Option 2");

        // Tables

        ItsNatHTMLTable table = (ItsNatHTMLTable)compMgr.findItsNatComponentById("tableId");
        check(table);
        ((DefaultTableModel)table.getTableModel()).addColumn("Col 1");
        ((DefaultTableModel)table.getTableModel()).addColumn("Col 2");
        ((DefaultTableModel)table.getTableModel()).addRow(new String[]{"Item 1,1","Item 1,2"});
        ((DefaultTableModel)table.getTableModel()).addRow(new String[]{"Item 2,1","Item 2,2"});

        ItsNatFreeTable freeTable = (ItsNatFreeTable)compMgr.findItsNatComponentById("freeTableId");
        check(freeTable);
        ((DefaultTableModel)freeTable.getTableModel()).addColumn("Col 1");
        ((DefaultTableModel)freeTable.getTableModel()).addColumn("Col 2");
        ((DefaultTableModel)freeTable.getTableModel()).addRow(new String[]{"Item 1,1","Item 1,2"});
        ((DefaultTableModel)freeTable.getTableModel()).addRow(new String[]{"Item 2,1","Item 2,2"});
        // Trees
        ItsNatFreeTree freeTree = (ItsNatFreeTree)compMgr.findItsNatComponentById("freeTreeId");
        check(freeTree);
        ((DefaultTreeModel)freeTree.getTreeModel()).setRoot(new DefaultMutableTreeNode("Free Tree"));

        ItsNatFreeTree freeTreeRootless = (ItsNatFreeTree)compMgr.findItsNatComponentById("freeTreeRootlessId");
        check(freeTreeRootless);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root (hidden)");
        root.add(new DefaultMutableTreeNode("Free Tree Rootless"));
        ((DefaultTreeModel)freeTreeRootless.getTreeModel()).setRoot(root);

        ItsNatFreeTree freeTreeTable = (ItsNatFreeTree)compMgr.findItsNatComponentById("freeTreeTableId");
        check(freeTreeTable);
        ((DefaultTreeModel)freeTreeTable.getTreeModel()).setRoot(new DefaultMutableTreeNode("Free Tree Table"));

        ItsNatHTMLForm form = (ItsNatHTMLForm)compMgr.findItsNatComponentById("formId");
        check(form);

        ItsNatFreeInclude freeInclude = (ItsNatFreeInclude)compMgr.findItsNatComponentById("freeIncludeId");
        check(freeInclude);

        LoginComponent customComp = (LoginComponent)compMgr.findItsNatComponentById("customCompId");
        check(customComp);

        ItsNatFreeListMultSel listCustomStruc = (ItsNatFreeListMultSel)compMgr.findItsNatComponentById("listCustomStructureId");
        check(listCustomStruc);
        check(listCustomStruc.getItsNatListStructure() instanceof CityListCustomStructure);
        ((DefaultListModel)listCustomStruc.getListModel()).addElement("Madrid");
        ((DefaultListModel)listCustomStruc.getListModel()).addElement("Barcelona");

        compMgr.removeItsNatComponents(parentElem,true);// Disables/destroys all

        if (adaptor != null) adaptor.setModeAuto(false);
    }

    public void endExamplePanel()
    {
        this.parentElem = null;
    }

    public static void check(ItsNatComponent comp)
    {
        check(comp != null);
    }

    public static void check(boolean eval)
    {
        if (!eval) throw new RuntimeException("Unexpected Error");
    }
}