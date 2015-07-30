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
 * (C) Innowhere Software Services S.L., Spanish company
 */

package test.web.remtmpl;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

/**
 *
 * @author jmarranz
 */
public class TestRemoteTemplateResultDocLoadListener implements ItsNatServletRequestListener
{
    public TestRemoteTemplateResultDocLoadListener()
    {
    }

    @Override
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new TestRemoteTemplateResultDocument(request.getItsNatDocument());
    }
}
