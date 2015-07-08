/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee. The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.manual.comp;

import javax.servlet.*;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;

public class comp_ex_servlet extends HttpServletWrapper
{
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        String pathPrefix = getServletContext().getRealPath("/");
        pathPrefix += "/WEB-INF/pages/manual/";

        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();

        // Default configuration is valid.

        ItsNatDocumentTemplate docTemplate = itsNatServlet.registerItsNatDocumentTemplate("manual.comp.example","text/html", pathPrefix + "comp_example.xhtml");
        docTemplate.addItsNatServletRequestListener(new CompExampleLoadListener());
    }
}
