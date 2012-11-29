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
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatEvent;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;

public class FuncTestNotBrowserExample
{
    protected FuncTestNotBrowserTreeNode treeNode;
    protected FuncTestNotBrowserUtil util;

    public FuncTestNotBrowserExample(FuncTestNotBrowserTreeNode treeNode)
    {
        this.treeNode = treeNode;
        this.util = new FuncTestNotBrowserUtil(getItsNatDocument());

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
        Element elem = treeNode.getList().getItsNatListUI().getContentElementAt(pos);

        util.sendMouseEvent(elem,"dblclick");
        return elem;
    }

    public void updateInputBoxInPlace(Element parent,String value)
    {
        Element inputElem = ItsNatTreeWalker.getFirstChildElement(parent);
        fillTextInput(inputElem,value);
        util.sendHTMLEvent(inputElem,"blur");
    }

    public void removeCity(int pos)
    {
        selectCity(pos);
        pushRemoveButton();
    }

    public void fillCityInputBox(String city)
    {
        fillTextInput(treeNode.getItemInput().getHTMLInputElement(),city);
    }

    public void fillPosInputBox(int pos)
    {
        fillTextInput(treeNode.getPosInput().getHTMLInputElement(),Integer.toString(pos));
    }

    public void fillTextInput(Element elem,String value)
    {
        Event event = util.createHTMLEvent("change");
        ((ItsNatEvent)event).setExtraParam("value",value); // ItsNatHTMLInputText transports the new value by this way
        util.dispatchEvent(elem,event);
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
        Element cityElem = treeNode.getList().getItsNatListUI().getContentElementAt(pos);

        util.sendMouseEvent(cityElem,"click",shiftKey);
    }

    public void removeAll()
    {
        int size = treeNode.getList().getItsNatListUI().getLength();

        if (size == 0) return;

        selectCity(0,false);
        selectCity(size - 1,true);

        pushRemoveButton();
    }


    public boolean checkResult()
    {
        ListModel model = treeNode.getList().getListModel();
        int size = model.getSize();
        if (size != 6) return false;
        String city = (String)model.getElementAt(4);
        return city.equals("Oviedo");
    }

    public void logResult(boolean res)
    {
        String msg;
        if (res) msg = "Test OK";
        else msg = "Test WRONG! Don't touch anything!";

        if (treeNode.isUCWEB())
            treeNode.log(msg);
        else
        {
            ItsNatDocument itsNatDoc = getItsNatDocument();
            itsNatDoc.addCodeToSend("alert(\"" + msg + "\");");
        }
    }

}
