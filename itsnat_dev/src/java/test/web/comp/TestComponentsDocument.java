/*
 * DocumentLoadListenerTest.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp;

import test.web.comp.html.TestHTMLModalLayer;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import test.web.shared.BrowserUtil;
import test.web.shared.Shared;
import test.web.comp.free.TestFreeButtonNormalDefault;
import test.web.comp.free.TestFreeButtonNormalLabel;
import test.web.comp.free.TestFreeCheckBoxDefault;
import test.web.comp.free.TestFreeCheckBoxLabel;
import test.web.comp.free.TestFreeComboBox;
import test.web.comp.free.TestFreeInclude;
import test.web.comp.free.TestFreeLabel;
import test.web.comp.free.TestFreeLabelEditors;
import test.web.comp.free.TestFreeListCompound;
import test.web.comp.free.TestFreeListMultiple;
import test.web.comp.free.TestFreeRadioDefault;
import test.web.comp.free.TestFreeRadioLabel;
import test.web.comp.free.TestFreeTable;
import test.web.comp.free.TestFreeTable2;
import test.web.comp.free.TestFreeTree;
import test.web.comp.free.TestFreeTree2;
import test.web.comp.free.TestFreeTreeRootless;
import test.web.comp.free.TestFreeTreeTable;
import test.web.comp.html.TestFileUpload;
import test.web.comp.html.TestHTMLForm;
import test.web.comp.html.TestHTMLAnchorDefault;
import test.web.comp.html.TestHTMLAnchorLabel;
import test.web.comp.html.TestHTMLButtonDefault;
import test.web.comp.html.TestHTMLButtonLabel;
import test.web.comp.html.TestHTMLInputButton;
import test.web.comp.html.TestHTMLInputCheckBox;
import test.web.comp.html.TestHTMLInputFile;
import test.web.comp.html.TestHTMLInputHidden;
import test.web.comp.html.TestHTMLInputImage;
import test.web.comp.html.TestHTMLInputPassword;
import test.web.comp.html.TestHTMLInputRadio;
import test.web.comp.html.TestHTMLInputReset;
import test.web.comp.html.TestHTMLInputSubmit;
import test.web.comp.html.TestHTMLInputText;
import test.web.comp.html.TestHTMLInputTextFormatted;
import test.web.comp.html.TestHTMLInputTextFormattedWithFactory;
import test.web.comp.html.TestHTMLLabel;
import test.web.comp.html.TestHTMLLabelEditors;
import test.web.comp.html.TestHTMLSelectComboBoxWithSize;
import test.web.comp.html.TestHTMLSelectComboBoxWithoutSize;
import test.web.comp.html.TestHTMLSelectMultiple;
import test.web.comp.html.TestHTMLTable;
import test.web.comp.html.TestHTMLTable2;
import test.web.comp.html.TestHTMLTableNoHeader;
import test.web.comp.html.TestHTMLTextArea;
import test.web.shared.TestSerialization;

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

        new TestHTMLLabelEditors(request,itsNatDoc);
        new TestFreeLabelEditors(request,itsNatDoc);
        new TestFreeInclude(itsNatDoc);
        new TestFreeListCompound(itsNatDoc);

        Shared.setRemoteControlLink(request,response);

        new TestSerialization(request);
    }

}
