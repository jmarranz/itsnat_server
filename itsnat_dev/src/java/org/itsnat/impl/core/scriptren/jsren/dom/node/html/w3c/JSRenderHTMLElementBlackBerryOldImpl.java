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

package org.itsnat.impl.core.scriptren.jsren.dom.node.html.w3c;

import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementBlackBerryOldImpl extends JSRenderHTMLElementW3CImpl
{
    public final static JSRenderHTMLElementBlackBerryOldImpl SINGLETON = new JSRenderHTMLElementBlackBerryOldImpl();

    /** Creates a new instance of JSRenderHTMLElementBlackBerryOldImpl */
    public JSRenderHTMLElementBlackBerryOldImpl()
    {
        // Probada toda la casuística de MSIE con éxito.
        // Contenido de <style> con innerHTML: se inserta pero no se aplica el estilo, pero es igual via DOM
        // Contenido de <script> con innerHTML: se inserta pero no se ejecuta. Idem con DOM
    }

    public String getCurrentStyleObject(String itsNatDocVar,String elemName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // El devolver null es un error extraño de BlackBerry.
        return "(" + itsNatDocVar + ".win.getComputedStyle(" + elemName + ",null) == null ? " + elemName + ".style : " + itsNatDocVar + ".win.getComputedStyle(" + elemName + ",null))";
    }
}

