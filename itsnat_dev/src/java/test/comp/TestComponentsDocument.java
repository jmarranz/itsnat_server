/*
 * DocumentLoadListenerTest.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp;

import test.comp.html.TestHTMLModalLayer;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import test.shared.BrowserUtil;
import test.shared.Shared;
import test.comp.free.TestFreeButtonNormalDefault;
import test.comp.free.TestFreeButtonNormalLabel;
import test.comp.free.TestFreeCheckBoxDefault;
import test.comp.free.TestFreeCheckBoxLabel;
import test.comp.free.TestFreeComboBox;
import test.comp.free.TestFreeInclude;
import test.comp.free.TestFreeLabel;
import test.comp.free.TestFreeLabelEditors;
import test.comp.free.TestFreeListCompound;
import test.comp.free.TestFreeListMultiple;
import test.comp.free.TestFreeRadioDefault;
import test.comp.free.TestFreeRadioLabel;
import test.comp.free.TestFreeTable;
import test.comp.free.TestFreeTable2;
import test.comp.free.TestFreeTree;
import test.comp.free.TestFreeTree2;
import test.comp.free.TestFreeTreeRootless;
import test.comp.free.TestFreeTreeTable;
import test.comp.html.TestFileUpload;
import test.comp.html.TestHTMLForm;
import test.comp.html.TestHTMLAnchorDefault;
import test.comp.html.TestHTMLAnchorLabel;
import test.comp.html.TestHTMLButtonDefault;
import test.comp.html.TestHTMLButtonLabel;
import test.comp.html.TestHTMLInputButton;
import test.comp.html.TestHTMLInputCheckBox;
import test.comp.html.TestHTMLInputFile;
import test.comp.html.TestHTMLInputHidden;
import test.comp.html.TestHTMLInputImage;
import test.comp.html.TestHTMLInputPassword;
import test.comp.html.TestHTMLInputRadio;
import test.comp.html.TestHTMLInputReset;
import test.comp.html.TestHTMLInputSubmit;
import test.comp.html.TestHTMLInputText;
import test.comp.html.TestHTMLInputTextFormatted;
import test.comp.html.TestHTMLInputTextFormattedWithFactory;
import test.comp.html.TestHTMLLabel;
import test.comp.html.TestHTMLLabelEditors;
import test.comp.html.TestHTMLSelectComboBoxWithSize;
import test.comp.html.TestHTMLSelectComboBoxWithoutSize;
import test.comp.html.TestHTMLSelectMultiple;
import test.comp.html.TestHTMLTable;
import test.comp.html.TestHTMLTable2;
import test.comp.html.TestHTMLTableNoHeader;
import test.comp.html.TestHTMLTextArea;
import test.shared.TestSerialization;

/**
 *
 * @author jmarranz
 */
public class TestComponentsDocument
{
    protected ItsNatHTMLDocument itsNatDoc;

    /**
     * Creates a new instance of DocumentLoadListenerTest
     */
    public TestComponentsDocument(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = itsNatDoc;

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        // Nota: si AutoBuildComponents es true, setSelectionOnComponentsUsesKeyboard no tendrá efecto pues ya se habrán creado los componentes
        compMgr.setSelectionOnComponentsUsesKeyboard(!BrowserUtil.isMobileBrowser(request));

        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();
        ((EventTarget)view).addEventListener("load",new TestOnLoadListener(itsNatDoc),false);

        //ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        if (!itsNatDoc.getItsNatDocumentTemplate().isAutoBuildComponents())
        {
            compMgr.buildItsNatComponents();
        }

        new TestHTMLLabel(itsNatDoc);
        new TestFreeLabel(itsNatDoc);

        new TestHTMLSelectComboBoxWithoutSize(itsNatDoc);
        new TestHTMLSelectComboBoxWithSize(itsNatDoc);
        new TestHTMLSelectMultiple(itsNatDoc,request);
        new TestHTMLInputButton(itsNatDoc);
        new TestHTMLInputCheckBox(itsNatDoc);
        new TestHTMLInputRadio(itsNatDoc);
        new TestHTMLTextArea(itsNatDoc,request);

        new TestHTMLInputText(itsNatDoc,request);
        new TestHTMLInputTextFormatted(itsNatDoc);
        new TestHTMLInputTextFormattedWithFactory(itsNatDoc);

        new TestHTMLInputFile(itsNatDoc);
        new TestHTMLInputPassword(itsNatDoc);
        new TestFileUpload(itsNatDoc);

        new TestHTMLInputImage(itsNatDoc);
        new TestHTMLButtonDefault(itsNatDoc);
        new TestHTMLButtonLabel(itsNatDoc);
        new TestHTMLInputHidden(itsNatDoc);


        new TestHTMLForm(itsNatDoc);
        new TestHTMLInputSubmit(itsNatDoc);
        new TestHTMLInputReset(itsNatDoc);
        new TestBlurFocusSelect(itsNatDoc);
        new TestResetFromServer(itsNatDoc);
        new TestJSFromServer(itsNatDoc);
        new TestHTMLAnchorDefault(itsNatDoc);
        new TestHTMLAnchorLabel(itsNatDoc);
        new TestFreeButtonNormalDefault(itsNatDoc);
        new TestFreeButtonNormalLabel(itsNatDoc);
        new TestFreeCheckBoxDefault(itsNatDoc);
        new TestFreeCheckBoxLabel(itsNatDoc);
        new TestFreeRadioDefault(itsNatDoc);
        new TestFreeRadioLabel(itsNatDoc);
        new TestFreeComboBox(itsNatDoc);
        new TestFreeListMultiple(itsNatDoc);

        new TestHTMLTable(itsNatDoc);
        new TestHTMLTableNoHeader(itsNatDoc);
        new TestHTMLTable2(itsNatDoc);

        new TestFreeTable(itsNatDoc);
        new TestFreeTable2(itsNatDoc);

        new TestFreeTree(itsNatDoc);
        new TestFreeTree2(itsNatDoc);
        new TestFreeTreeRootless(itsNatDoc);
        new TestFreeTreeTable(itsNatDoc);

        new TestEventListenerChainInComp(itsNatDoc);
        new TestComponentGCTestId(itsNatDoc);

        new TestHTMLModalLayer(itsNatDoc);

        new TestHTMLLabelEditors(itsNatDoc);
        new TestFreeLabelEditors(itsNatDoc);
        new TestFreeInclude(itsNatDoc);
        new TestFreeListCompound(itsNatDoc);

        Shared.setRemoteControlLink(request,response);

        new TestSerialization(request);
    }

}
