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

import java.io.Serializable;
import org.itsnat.impl.core.util.HasUniqueId;
import org.itsnat.impl.core.util.UniqueId;

/**
 *
 * @author jmarranz
 */
public abstract class CachedSubtreeImpl implements HasUniqueId,Serializable
{
    protected MarkupTemplateVersionImpl template;
    protected UniqueId idObj;
    protected String markup;
    protected String markCode;

    public CachedSubtreeImpl(MarkupTemplateVersionImpl template,String markup)
    {
        this.template = template;
        this.idObj = template.getUniqueIdGenerator().generateUniqueId("cs"); // cs = cached subtree
        this.markup = markup;
        // Los ids NO deben de tener comas (,)
        this.markCode = getMarkCodeStart() + "," + template.getId() + "," + idObj.getId() + "}";
    }

    public String getId()
    {
        return idObj.getId();
    }

    public UniqueId getUniqueId()
    {
        return idObj;
    }

    public abstract String getCode(boolean resolveEntities);

    public static String getMarkCodeStart()
    {
        return "${itsnat_cached_node";
    }

    public static String getTemplateId(String mark)
    {
        int start = getMarkCodeStart().length() + 1;
        int last = mark.lastIndexOf(',');
        return mark.substring(start,last);
    }

    public static String getNodeId(String mark)
    {
        int start = mark.lastIndexOf(',') + 1;
        return mark.substring(start,mark.length() - 1);
    }

    public String getMarkCode()
    {
        return markCode;
    }
}
