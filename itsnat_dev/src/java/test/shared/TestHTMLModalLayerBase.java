/*
 * TestAsyncServerTask.java
 *
 * Created on 3 de enero de 2007, 12:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.shared;

import java.io.Serializable;
import javax.swing.DefaultComboBoxModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputRadio;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
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


/**
 *
 * @author jmarranz
 */
public abstract class TestHTMLModalLayerBase extends TestBaseHTMLDocument implements EventListener,Serializable
{
    protected ItsNatHTMLInputCheckBox cleanModeCheck1;
    protected ItsNatHTMLInputCheckBox cleanModeCheck2;

    /**
     * Creates a new instance of TestAsyncServerTask
     */
    public TestHTMLModalLayerBase(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
    }

    public boolean isCleanMode1()
    {
        return cleanModeCheck1.isSelected();
    }

    public boolean isCleanMode2()
    {
        return cleanModeCheck2.isSelected();
    }

    public abstract boolean isMobile();

    public void handleEvent(final Event evt)
    {
        int height = isMobile() ? 700 : 3500;
        String code;
        // Note:  right:X%; alongside width and left fools BlackBerry
        code = "<p style='position:absolute; z-index:1; background:aqua;   width:90%; height:" + height + "px; left:5%;  top:20px;'>This is a Modal Layer (1) <br /><a href='javascript:;'>Click To Exit</a><br /><br /><select size='5'></select><br /><input type='text' /><br /><textarea/><br /><input type='checkbox' /><br /><input type='radio' name='radioTest' /><input type='radio' name='radioTest' /><br /><input type=\"button\" onclick=\"alert('click')\" value='Button'/><br /><button onclick=\"alert('click')\">Button</button><br /><span onclick=\"alert('click')\">Paragraph</span><br /><a href='javascript:;'>Check Server</a></p>";
        showModalView(code,isCleanMode1(),1,(float)0.5,"black");
        code = "<p style='position:absolute; z-index:2; background:yellow; width:70%; height:" + height + "px; left:15%; top:40px;'>This is a Modal Layer (2) <br /><a href='javascript:;'>Click To Exit</a><br /><br /><select size='5'></select><br /><input type='text' /><br /><textarea/><br /><input type='checkbox' /><br /><input type='radio' name='radioTest' /><input type='radio' name='radioTest' /><br /><span onclick=\"alert('click')\">Paragraph</span><br /><a href='javascript:;'>Check Server</a></p>";
        showModalView(code,isCleanMode2(),2,(float)0.1,"black");
        code = "<p style='position:absolute; z-index:3; background:#E8E8E8; width:50%; height:" + height + "px; left:25%; top:60px;'>This is a Modal Layer (3) <br /><a href='javascript:;'>Click To Exit</a><br /><br /><select size='5'></select><br /><input type='text' /><br /><textarea/><br /><input type='checkbox' /><br /><input type='radio' name='radioTest' /><input type='radio' name='radioTest' /><br /><span onclick=\"alert('click')\">Paragraph</span><br /><a href='javascript:;'>Check Server</a></p>";
        showModalView(code,isCleanMode2(),3,(float)1,null); // Caso de layer transparente
    }

    public void showModalView(String code,boolean cleanMode,int zIndex,float opacity,String background)
    {
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        final ItsNatModalLayer modalLayer = compMgr.createItsNatModalLayer(null,cleanMode,opacity,background,null);
        TestUtil.checkError(modalLayer.getZIndex() == zIndex); // Lo más probable es que se haya pulsado dos veces el link de crear los layers

        EventListener unexpEvtListener = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                StringBuffer code = new StringBuffer();
                code.append("if (confirm('Received an unexpected event by a hidden element. Reload?')) ");
                code.append("  window.location.reload(true);");
                
                itsNatDoc.addCodeToSend(code.toString());
            }
        };
        modalLayer.addUnexpectedEventListener(unexpEvtListener);

        final HTMLDocument doc = itsNatDoc.getHTMLDocument();
        DocumentFragment frag = itsNatDoc.toDOM(code);
        final Element elem = (Element)frag.getFirstChild();
        doc.getBody().appendChild(elem);

        NodeList links = elem.getElementsByTagName("a");
        final HTMLAnchorElement linkExit = (HTMLAnchorElement)links.item(0);
        final HTMLAnchorElement linkCheckServ = (HTMLAnchorElement)links.item(1);

        // Link simplemente para actualizar el cliente con cambios en el servidor
        final EventListener listenerNothing = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
            }
        };
        ((EventTarget)linkCheckServ).addEventListener("click",listenerNothing,false);

        final HTMLSelectElement select = (HTMLSelectElement)elem.getElementsByTagName("select").item(0);
        final ItsNatHTMLSelectComboBox selectComp = (ItsNatHTMLSelectComboBox)compMgr.createItsNatComponent(select);
        DefaultComboBoxModel model = (DefaultComboBoxModel)selectComp.getComboBoxModel();
        model.addElement("One " + zIndex);
        model.addElement("Two " + zIndex);
        model.addElement("Three " + zIndex);
        model.addElement("Four " + zIndex);
        model.addElement("Five " + zIndex);

        NodeList inputList = elem.getElementsByTagName("input");

        final HTMLInputElement inputText = (HTMLInputElement)inputList.item(0);
        final ItsNatHTMLInputText inputTextComp = (ItsNatHTMLInputText)compMgr.createItsNatComponent(inputText);
        inputTextComp.setText("Hello");

        final HTMLTextAreaElement textArea = (HTMLTextAreaElement)elem.getElementsByTagName("textarea").item(0);
        final ItsNatHTMLTextArea textAreaComp = (ItsNatHTMLTextArea)compMgr.createItsNatComponent(textArea);
        textAreaComp.setText("Hello");

        final HTMLInputElement inputCheck = (HTMLInputElement)inputList.item(1);
        final ItsNatHTMLInputCheckBox inputCheckComp = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponent(inputCheck);
        inputCheckComp.setSelected(false);

        final HTMLInputElement inputRadio1 = (HTMLInputElement)inputList.item(2);
        final ItsNatHTMLInputRadio inputRadioComp1 = (ItsNatHTMLInputRadio)compMgr.createItsNatComponent(inputRadio1);
        inputRadioComp1.setSelected(false);

        final HTMLInputElement inputRadio2 = (HTMLInputElement)inputList.item(3);
        final ItsNatHTMLInputRadio inputRadioComp2 = (ItsNatHTMLInputRadio)compMgr.createItsNatComponent(inputRadio2);
        inputRadioComp2.setSelected(false);

        EventListener listenerExit = new EventListenerSerial()
        {
            public void handleEvent(Event evt)
            {
                selectComp.dispose();
                inputTextComp.dispose();
                textAreaComp.dispose();
                inputCheckComp.dispose();
                inputRadioComp1.dispose();
                inputRadioComp2.dispose();

                ((EventTarget)linkCheckServ).removeEventListener("click",listenerNothing,false);
                //((EventTarget)elem).removeEventListener("click",this,false);
                ((EventTarget)linkExit).removeEventListener("click",this,false);
                doc.getBody().removeChild(elem);
                modalLayer.dispose();
            }
        };

//        ((EventTarget)elem).addEventListener("click",listenerExit,false);
        ((EventTarget)linkExit).addEventListener("click",listenerExit,false);
    }
}
