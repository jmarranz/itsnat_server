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
 * Author: Jose Maria Arranz Santamaria
 * (C) Innowhere Software Services S.L., Spanish company, year 2007
 */

package manual.core.misc.remctrl;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/*
 * Esta clase no se incluye en el manual, sólo la necesitamos para compilar
 */

public class RemoteControlTimerMgrGlobalEventListener implements EventListener
{
    public static final int PERIOD = 10000;

    public RemoteControlTimerMgrGlobalEventListener(ItsNatDocument itsNatDoc)
    {
    }

    public void handleEvent(Event evt)
    {

    }
}
