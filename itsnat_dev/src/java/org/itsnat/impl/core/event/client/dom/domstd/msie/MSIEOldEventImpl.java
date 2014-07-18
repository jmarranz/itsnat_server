/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.core.event.client.dom.domstd.msie;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.event.client.dom.domstd.ClientItsNatDOMStdEventImpl;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.event.DOMStdEventTypeInfo;
import org.itsnat.impl.core.event.client.dom.domstd.NodeContainerImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

/**
 * Es un intento de ver de forma estándar el evento del Internet Explorer
 *
   http://en.wikipedia.org/wiki/DOM_Events
   http://en.wikipedia.org/wiki/Comparison_of_layout_engines_%28DOM%29#Events
   http://www-128.ibm.com/developerworks/web/library/wa-ie2mozgd/#event_differences
   http://linuxalpha1.eicn.ch/OReilly_books/books/webprog/jscript/ch19_03.htm

   http://www.w3.org/TR/DOM-Level-3-Events/events.html precisa más cosas que DOM-2

 * @author jmarranz
 */
public abstract class MSIEOldEventImpl extends ClientItsNatDOMStdEventImpl
{
    protected MSIEOldOriginalEventImpl originalEvt;
    protected DOMStdEventTypeInfo eventTypeInfo = null;

    /**
     * Creates a new instance of MSIEOldEventImpl
     */
    public MSIEOldEventImpl(ItsNatDOMStdEventListenerWrapperImpl listenerWrapper,RequestNormalEventImpl request)
    {
        super(listenerWrapper,request);

        this.originalEvt = new MSIEOldOriginalEventImpl(this);

        this.stopPropagation = originalEvt.getCancelBubble(); // valor inicial
        this.preventDefault = !originalEvt.getReturnValue(); // Valor inicial, si returnValue es false es que prevent default es true

        checkTampering();
    }

    public MSIEOldOriginalEventImpl getMSIEOriginalEvent()
    {
        return originalEvt;
    }

    public DOMStdEventTypeInfo getEventTypeInfo()
    {
        if (eventTypeInfo == null)
            this.eventTypeInfo = DOMStdEventTypeInfo.getEventTypeInfo(getType());
        return eventTypeInfo;
    }

    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg)
    {
        throw new ItsNatException("Not implemented",this);
    }

    public void resolveNodePaths()
    {
        super.resolveNodePaths();

        getTarget();
    }

    public EventTarget getTarget()
    {
        if (target == null)
            this.target = new NodeContainerImpl(originalEvt.getSrcElement());
        return (EventTarget)target.get();
    }

    protected String getTypeFromClient()
    {
        return originalEvt.getType();
    }

    public short getEventPhase()
    {
        return Event.AT_TARGET;
    }

    public boolean getBubbles()
    {
        return getEventTypeInfo().getBubbles();
    }

    public boolean getCancelable()
    {
        return getEventTypeInfo().getCancelable();
    }

}
