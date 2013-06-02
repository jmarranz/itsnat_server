package manual.stateless;

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

import javax.servlet.*;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;


public class servletstless extends HttpServletWrapper
{
  @Override
  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);

    ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();

    //ItsNatServletConfig itsNatConfig = itsNatServlet.getItsNatServletConfig();
    itsNatServlet.addItsNatServletRequestListener(new StatelessGlobalDocumentLoadListener());
    itsNatServlet.addEventListener(new StlessGlobalEventListener());               

    String pathPrefix = getServletContext().getRealPath("/");
    pathPrefix += "/WEB-INF/pages/manual/";

    ItsNatDocumentTemplate docTemplate;
    docTemplate = itsNatServlet.registerItsNatDocumentTemplate("manual.stless.example","text/html", pathPrefix + "stless_example.html");
    docTemplate.addItsNatServletRequestListener(new StlessExampleInitialDocLoadListener());
    docTemplate.setEventsEnabled(false); // Stateless

    docTemplate = itsNatServlet.registerItsNatDocumentTemplate("manual.stless.example.eventReceiver","text/html", pathPrefix + "stless_example_event_receiver.html");
    docTemplate.addItsNatServletRequestListener(new StatelessExampleForProcessingEventDocLoadListener());
    docTemplate.setEventsEnabled(false); // Stateless  

    ItsNatDocFragmentTemplate docFragDesc;
    docFragDesc = itsNatServlet.registerItsNatDocFragmentTemplate("manual.stless.example.fragment","text/html", pathPrefix + "stless_example_fragment.html");        
  }
}
