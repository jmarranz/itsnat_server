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

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

public class FuncTestNotBrowserUtil
{
    protected ItsNatDocument itsNatDoc;

    public FuncTestNotBrowserUtil(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public void sendHTMLEvent(Element elem,String type)
    {
        Event event = createHTMLEvent(type);
        dispatchEvent(elem,event);
    }

    public void clickElement(Element elem)
    {
        sendMouseEvent(elem,"click");
    }

    public void sendMouseEvent(Element elem,String type)
    {
        sendMouseEvent(elem,type,false);
    }

    public void sendMouseEvent(Element elem,String type,boolean shiftKey)
    {
        Event event = createMouseEvent(type,shiftKey);
        dispatchEvent(elem,event);
    }

    public void dispatchEvent(Element elem,Event event)
    {
        ((EventTarget)elem).dispatchEvent(event);
    }

    public MouseEvent createMouseEvent(String type,boolean shiftKey)
    {
        Document doc = itsNatDoc.getDocument();
        MouseEvent event = (MouseEvent)((DocumentEvent)doc).createEvent("MouseEvents");
        AbstractView view = ((DocumentView)doc).getDefaultView();
        event.initMouseEvent(type,true,true,view,0,
                0,0,0,0,false,false,shiftKey,false,(short)0/*left button*/,null);
        return event;
    }

    public Event createHTMLEvent(String type)
    {
        Document doc = itsNatDoc.getDocument();
        Event event = ((DocumentEvent)doc).createEvent("HTMLEvents");
        event.initEvent(type,true,true);
        return event;
    }

}
