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

package org.itsnat.feashow.features.comp.layers;

import javax.swing.DefaultComboBoxModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputRadio;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.feashow.BrowserUtil;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLTextAreaElement;

public class ModalLayerHTMLTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element link;
    protected ItsNatHTMLInputCheckBox cleanModeCheck;

    public ModalLayerHTMLTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        this.link = itsNatDoc.getDocument().getElementById("linkToStartId");
        ((EventTarget)link).addEventListener("click",this,false);

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.cleanModeCheck = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("cleanModeId");
        cleanModeCheck.setSelected(false);
    }

    public void endExamplePanel()
    {
        ((EventTarget)link).addEventListener("click",this,false);
        this.link = null;
    }

    public void handleEvent(Event evt)
    {
        showModalView1();
        showModalView2();
    }

    public void showModalView1()
    {
        float opacity = (float)0.5;
        String background = "black";

        final ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        final ItsNatModalLayer modalLayer = compMgr.createItsNatModalLayer(null,cleanModeCheck.isSelected(),opacity,background,null);

        EventListener unexpEvtListener = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                StringBuilder code = new StringBuilder();
                ItsNatServletRequest request = ((ItsNatEvent)evt).getItsNatServletRequest();
                code.append("if (confirm('Received an unexpected event by a hidden element. Reload?')) ");
                code.append("  window.location.reload(true);");                
                itsNatDoc.addCodeToSend(code.toString());
            }
        };
        modalLayer.addUnexpectedEventListener(unexpEvtListener);

        int zIndex = modalLayer.getZIndex();

        // Note:  right:X%; alongside width and left fools BlackBerry
        String code = "<p style='position:absolute; z-index:" + zIndex + "; background:aqua; width:90%; height:90%; left:5%; top:5%; padding:10px;' >" +
                      "<b>Modal Layer 1</b><br /><br />" +
                      "These form controls do nothing, the are included to test whether " +
                      "they can be touched below a modal layer.<br /><br />" +
                      "<a href='javascript:;'>Click To Exit</a><br /><br />" +
                      "<select size='5'></select><br />" +
                      "<input type='text' /><br />" +
                      "<textarea/><br />" +
                      "<input type='checkbox' /><br />" +
                      "<input type='radio' name='radioTest' /><input type='radio' name='radioTest' /><br />" +
                      "<input type=\"button\" onclick=\"alert('click')\" value='Button'/><br />" +
                      "<button onclick=\"alert('click')\">Button</button>" +
                      "</p>";

        final HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        DocumentFragment frag = itsNatDoc.toDOM(code);
        final Element elem = (Element)frag.getFirstChild();
        doc.getBody().appendChild(elem);

        NodeList links = elem.getElementsByTagName("a");
        final HTMLAnchorElement linkExit = (HTMLAnchorElement)links.item(0);

        final HTMLSelectElement select = (HTMLSelectElement)elem.getElementsByTagName("select").item(0);
        final ItsNatHTMLSelectComboBox selectComp = (ItsNatHTMLSelectComboBox)compMgr.createItsNatComponent(select);
        DefaultComboBoxModel model = (DefaultComboBoxModel)selectComp.getComboBoxModel();
        model.addElement("One");
        model.addElement("Two");
        model.addElement("Three");
        model.addElement("Four");
        model.addElement("Five");

        NodeList inputList = elem.getElementsByTagName("input");

        HTMLInputElement inputText = (HTMLInputElement)inputList.item(0);
        final ItsNatHTMLInputText inputTextComp = (ItsNatHTMLInputText)compMgr.createItsNatComponent(inputText);
        inputTextComp.setText("Hello");

        HTMLTextAreaElement textArea = (HTMLTextAreaElement)elem.getElementsByTagName("textarea").item(0);
        final ItsNatHTMLTextArea textAreaComp = (ItsNatHTMLTextArea)compMgr.createItsNatComponent(textArea);
        textAreaComp.setText("Hello");

        HTMLInputElement inputCheck = (HTMLInputElement)inputList.item(1);
        final ItsNatHTMLInputCheckBox inputCheckComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponent(inputCheck);
        inputCheckComp.setSelected(false);

        HTMLInputElement inputRadio1 = (HTMLInputElement)inputList.item(2);
        final ItsNatHTMLInputRadio inputRadioComp1 = (ItsNatHTMLInputRadio)compMgr.createItsNatComponent(inputRadio1);
        inputRadioComp1.setSelected(false);

        HTMLInputElement inputRadio2 = (HTMLInputElement)inputList.item(3);
        final ItsNatHTMLInputRadio inputRadioComp2 = (ItsNatHTMLInputRadio)compMgr.createItsNatComponent(inputRadio2);
        inputRadioComp2.setSelected(false);

        EventListener listenerExit = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                selectComp.dispose();
                inputTextComp.dispose();
                textAreaComp.dispose();
                inputCheckComp.dispose();
                inputRadioComp1.dispose();
                inputRadioComp2.dispose();

                ((EventTarget)linkExit).removeEventListener("click",this,false);
                doc.getBody().removeChild(elem);
                modalLayer.dispose();
            }
        };

        ((EventTarget)linkExit).addEventListener("click",listenerExit,false);
    }

    public void showModalView2()
    {
        float opacity = (float)0.1; // Semitransparent
        String background = "black";

        final ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        final ItsNatModalLayer modalLayer = compMgr.createItsNatModalLayer(null,cleanModeCheck.isSelected(),opacity,background,null);
        int zIndex = modalLayer.getZIndex();

        // Note:  right:X%; alongside width and left fools BlackBerry
        String code = "<p style='position:absolute; z-index:" + zIndex + "; background:yellow; width:70%; height:70%; left:15%; top:15%; padding:10px;'>" +
                      "<b>Modal Layer 2</b><br /><br />" +
                      "<a href='javascript:;'>Click To Exit</a>" +
                      "</p>";

        final HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        DocumentFragment frag = itsNatDoc.toDOM(code);
        final Element elem = (Element)frag.getFirstChild();
        doc.getBody().appendChild(elem);

        NodeList links = elem.getElementsByTagName("a");
        final HTMLAnchorElement linkExit = (HTMLAnchorElement)links.item(0);

        EventListener listenerExit = new EventListener()
        {
            public void handleEvent(Event evt)
            {
                ((EventTarget)linkExit).removeEventListener("click",this,false);
                doc.getBody().removeChild(elem);
                modalLayer.dispose();
            }
        };

        ((EventTarget)linkExit).addEventListener("click",listenerExit,false);
    }
}
