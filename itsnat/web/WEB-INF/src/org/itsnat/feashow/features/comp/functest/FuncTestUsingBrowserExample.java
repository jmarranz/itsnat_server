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

package org.itsnat.feashow.features.comp.functest;

import javax.swing.ListModel;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.script.ScriptUtil;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLInputElement;

public class FuncTestUsingBrowserExample
{
    protected FuncTestUsingBrowserTreeNode treeNode;
    protected FuncTestUsingBrowserUtil util;

    public FuncTestUsingBrowserExample(FuncTestUsingBrowserTreeNode treeNode)
    {
        this.treeNode = treeNode;
        this.util = new FuncTestUsingBrowserUtil(getItsNatDocument());

        // To avoid "CTRL ever pressed" simulation on mobile browsers
        // (this test simulates a user using a desktop browser)
        treeNode.getList().setSelectionUsesKeyboard(true);
    }

    public ItsNatDocument getItsNatDocument()
    {
        return treeNode.getItsNatDocument();
    }

    public void startTest()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Runnable testCode = new Runnable()
        {
            public void run()
            {
                removeAll(); // clears a previous bad test

                // In alfabetical order
                insertCity("Barcelona",0);
                insertCity("Madrid",1);
                insertCity("Sevilla",2);
                insertCity("Segovia",3);
                insertCity("Valencia",4);
                insertCity("Oviedores",5);

                // Uh!, "Valencia" is not in alfabetical order
                removeCity(4);

                insertCity("Valencia",5);

                // Ah! "Oviedores" is wrong
                updateCity("Oviedorio",4);

                // Oh No! "Oviedorio" is wrong too!
                updateCityInPlace("Oviedo",4);

                boolean res = checkResult();

                removeAll(); // clears

                logResult(res);
            }
        };
        ClientDocument client = itsNatDoc.getClientDocumentOwner();
        client.startEventDispatcherThread(testCode);
    }

    public void insertCity(String city,int pos)
    {
        fillCityInputBox(city);
        fillPosInputBox(pos);
        pushInsertButton();
    }

    public void updateCity(String city,int pos)
    {
        fillCityInputBox(city);
        fillPosInputBox(pos);
        pushUpdateButton();
    }

    public void updateCityInPlace(String city,int pos)
    {
        Element elem = editInPlace(pos);
        updateInputBoxInPlace(elem,city);
    }

    public Element editInPlace(int pos)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Element elem;
        synchronized(itsNatDoc)
        {
            elem = treeNode.getList().getItsNatListUI().getContentElementAt(pos);
        }
        util.sendMouseEvent(elem,"dblclick");
        return elem;
    }

    public void updateInputBoxInPlace(Element parent,String value)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Element inputElem;
        synchronized(itsNatDoc)
        {
            inputElem = ItsNatTreeWalker.getFirstChildElement(parent);
        }

        fillTextInput(inputElem,value);

        // ItsNat sends a "blur" event when a "change" event to ensure is fired. This blur event removes the editor element used in place.
        // then we check first if this node is present in the document .
        if (ItsNatDOMUtil.isNodeInside(inputElem,itsNatDoc.getDocument()))
            util.sendHTMLEvent(inputElem,"blur",true);
    }

    public void removeCity(int pos)
    {
        selectCity(pos);
        pushRemoveButton();
    }

    public void fillCityInputBox(String city)
    {
        HTMLInputElement input = treeNode.getItemInput().getHTMLInputElement();
        fillTextInput(input,city);
    }

    public void fillPosInputBox(int pos)
    {
        HTMLInputElement input = treeNode.getPosInput().getHTMLInputElement();
        fillTextInput(input,Integer.toString(pos));
    }

    public void fillTextInput(Element elem,String value)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        synchronized(itsNatDoc)
        {
            ScriptUtil script = itsNatDoc.getScriptUtil();
            itsNatDoc.addCodeToSend( script.getSetPropertyCode(elem,"value",value,true) );
        }

        util.sendHTMLEvent(elem,"change",true);
    }

    public void pushInsertButton()
    {
        util.clickElement(treeNode.getInsertButton().getHTMLInputElement());
    }

    public void pushUpdateButton()
    {
        util.clickElement(treeNode.getUpdateButton().getHTMLInputElement());
    }

    public void pushRemoveButton()
    {
        util.clickElement(treeNode.getRemoveButton().getHTMLInputElement());
    }

    public void selectCity(int pos)
    {
        selectCity(pos,false);
    }

    public void selectCity(int pos,boolean shiftKey)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        Element cityElem;
        synchronized(itsNatDoc)
        {
            cityElem = treeNode.getList().getItsNatListUI().getContentElementAt(pos);
        }

        util.sendMouseEvent(cityElem,"click",shiftKey);
    }

    public void removeAll()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        int size;
        synchronized(itsNatDoc)
        {
            size = treeNode.getList().getItsNatListUI().getLength();
        }
        if (size == 0) return;

        selectCity(0,false);
        selectCity(size - 1,true);

        pushRemoveButton();
    }


    public boolean checkResult()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        synchronized(itsNatDoc)
        {
            ListModel model = treeNode.getList().getListModel();
            int size = model.getSize();
            if (size != 6) return false;
            String city = (String)model.getElementAt(4);
            return city.equals("Oviedo");
        }
    }

    public void logResult(boolean res)
    {
        String msg;
        if (res) msg = "Test OK";
        else msg = "Test WRONG! Don't touch anything!";

        ItsNatDocument itsNatDoc = getItsNatDocument();
        synchronized(itsNatDoc)
        {
            itsNatDoc.addCodeToSend("alert(\"" + msg + "\");");
        }
    }

}
