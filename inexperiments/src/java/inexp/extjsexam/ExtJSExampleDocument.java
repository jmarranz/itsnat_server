/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inexp.extjsexam;

import inexp.extjsexam.tab.CarListTab;
import inexp.extjsexam.tab.TVSeriesListTab;
import inexp.extjsexam.tab.IntroductionTab;
import inexp.extjsexam.model.DBSimulator;
import inexp.BrowserUtil;
import javax.swing.DefaultComboBoxModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ExtJSExampleDocument
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected ItsNatFreeComboBox tabCombo;
    protected Element tabPanelElem;
    protected DBSimulator dataModel = new DBSimulator();
    protected boolean mobileBrowser;

    public ExtJSExampleDocument(DBSimulator dataModel,ItsNatServletRequest request)
    {
        this.dataModel = dataModel;
        this.itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();
        this.mobileBrowser = BrowserUtil.isMobileBrowser(request);

        if (PocketIEMotoWebKitAdaptor.isPIEOrMotoWebKit(request))
            new PocketIEMotoWebKitAdaptor(this);
        else if (UCWEBAdaptor.isUCWEB(request))
            new UCWEBAdaptor(this);
        
        itsNatDoc.setJoystickMode(BrowserUtil.isJoystickModePreferred(request));

        Document doc = itsNatDoc.getDocument();
        this.tabPanelElem = itsNatDoc.getDocument().getElementById("tabPanelId");

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        Element tabComboElem = doc.getElementById("tabCompId");
        this.tabCombo = (ItsNatFreeComboBox)compMgr.createItsNatFreeComboBox(tabComboElem, TabComboStructure.SINGLETON, null);

        tabCombo.addItemListener(new TabSelectionDecorator(tabCombo));
        tabCombo.addItemListener(new TabSelectionSwitcher(this));

        DefaultComboBoxModel comboModel = (DefaultComboBoxModel)tabCombo.getComboBoxModel();
        comboModel.addElement(new IntroductionTab(this));
        comboModel.addElement(new CarListTab(this));
        comboModel.addElement(new TVSeriesListTab(this));

        comboModel.setSelectedItem(comboModel.getElementAt(0));

        tabCombo.setJoystickMode(itsNatDoc.isJoystickMode());
    }

    public boolean isMobileBrowser()
    {
        return mobileBrowser;
    }

    public DBSimulator getDataModel()
    {
        return dataModel;
    }

    public ItsNatHTMLDocument getItsNatHTMLDocument()
    {
        return itsNatDoc;
    }

    public Element getTabPanelElement()
    {
        return tabPanelElem;
    }

    public ItsNatFreeComboBox getTabComboBox()
    {
        return tabCombo;
    }

    public DocumentFragment loadDocumentFragment(String name)
    {
        ItsNatServlet servlet = getItsNatHTMLDocument().getItsNatDocumentTemplate().getItsNatServlet();
        ItsNatDocFragmentTemplate template = servlet.getItsNatDocFragmentTemplate(name);
        return template.loadDocumentFragment(itsNatDoc);
    }

}
