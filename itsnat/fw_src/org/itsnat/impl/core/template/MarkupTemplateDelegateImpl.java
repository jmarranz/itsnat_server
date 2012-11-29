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

package org.itsnat.impl.core.template;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.req.norm.RequestNormalLoadDocImpl;

/**
 *
 * @author jmarranz
 */
public abstract class MarkupTemplateDelegateImpl
{
    protected MarkupTemplateImpl parent;

    public MarkupTemplateDelegateImpl(MarkupTemplateImpl parent)
    {
        this.parent = parent;
    }

    public abstract boolean isItsNatTagsAllowed();
    public abstract MarkupSourceImpl getMarkupSource(RequestNormalLoadDocImpl request);
    public abstract Object getSource();
    public abstract MarkupTemplateVersionImpl getNewestMarkupTemplateVersion(MarkupSourceImpl source,ItsNatServletRequest request, ItsNatServletResponse response);
    public abstract MarkupTemplateVersionImpl getNewestMarkupTemplateVersion(ItsNatServletRequest request, ItsNatServletResponse response);
    public abstract boolean isTemplateAlreadyUsed();
    public abstract boolean canVersionBeSharedBetweenDocs();
}
