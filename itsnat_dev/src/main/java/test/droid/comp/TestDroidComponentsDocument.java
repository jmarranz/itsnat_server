/*
 * DocumentLoadListenerTest.java
 *
 * Created on 5 de octubre de 2006, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.droid.comp;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;

/**
 *
 * @author jmarranz
 */
public class TestDroidComponentsDocument
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of DocumentLoadListenerTest
     */
    public TestDroidComponentsDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = itsNatDoc;

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        // Nota: si AutoBuildComponents es true, setSelectionOnComponentsUsesKeyboard no tendrá efecto pues ya se habrán creado los componentes
        //compMgr.setSelectionOnComponentsUsesKeyboard(!BrowserUtil.isMobileBrowser(request));

        //Document doc = itsNatDoc.getDocument();
        //AbstractView view = ((DocumentView)doc).getDefaultView();
        //((EventTarget)view).addEventListener("load",new TestOnLoadListener(itsNatDoc),false);

        //ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        if (!itsNatDoc.getItsNatDocumentTemplate().isAutoBuildComponents())
        {
            compMgr.buildItsNatComponents();
        }

        new TestDroidCheckBox(itsNatDoc);
        
        /*
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
                */
    }

}
