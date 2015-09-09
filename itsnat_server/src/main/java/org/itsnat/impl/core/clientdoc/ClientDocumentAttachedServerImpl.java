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

package org.itsnat.impl.core.clientdoc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.browser.web.BrowserGecko;
import org.itsnat.impl.core.servlet.DeserialPendingTask;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionObjectInputStream;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateAttachedServerImpl;
import org.itsnat.impl.core.template.MarkupTemplateImpl;

/**
 * Este cliente es temporal únicamente para el proceso de carga
 *
 * @author jmarranz
 */
public abstract class ClientDocumentAttachedServerImpl extends ClientDocumentWithoutDocumentImpl
{
    protected transient ItsNatStfulDocumentTemplateAttachedServerImpl docTemplate;
    protected StringBuilder clientMarkup = new StringBuilder();

    public ClientDocumentAttachedServerImpl(ItsNatSessionImpl session,
                ItsNatStfulDocumentTemplateAttachedServerImpl docTemplate)
    {
        super(session);
        this.docTemplate = docTemplate;
    }

    public ItsNatStfulDocumentTemplateAttachedServerImpl getItsNatStfulDocumentTemplateAttachedServer()
    {
        return docTemplate;
    }

    @Override
    protected void setInvalidInternal()
    {
        super.setInvalidInternal();

        session.removeClientDocumentAttachedServer(this);
    }

    @Override
    public void registerInSession()
    {
        session.addClientDocumentAttachedServer(this);
    }

    public void addClientMarkup(String markup)
    {
        clientMarkup.append(markup);
    }

    public String getClientMarkup()
    {
        String res = clientMarkup.toString();

        if (getBrowser() instanceof BrowserGecko)
        {
            // Descubierto en FireFox, las variables ${} al menos dentro de un <script src="..." >
            // resulta que al hacer escape() en el browser los { } son convertidos a %257B y %257D respectivamente
            // en vez de %7B %7D como MSIE o Chrome,
            // no se si esto está bien, el caso es que Tomcat resuelve el %25 como % (pues 25 hex es %)
            // y deja el 7B como está.
            // Para resolver este problema convertimos los %7B y %7D  a { y }
            int pos = res.indexOf("$%7B");  // Añadimos el $ para evitar la posibilidad de un legítimo valor "%7B"
            while (pos != -1) // Hay alguno
            {
                res = res.substring(0,pos) + "${" + res.substring(pos + 4);
                int pos2 = res.indexOf("%7D",pos);
                if (pos2 != -1) // raro que fuera -1 sería un olvido o una falsa variable
                    res = res.substring(0,pos2) + "}" + res.substring(pos2 + 3);
                pos = res.indexOf("$%7B",pos);
            }
        }

        this.clientMarkup = null; // YA no se necesita
        return res;
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        ItsNatServletImpl itsNatServlet = docTemplate.getItsNatServletImpl();
        ItsNatServletImpl.writeObject(itsNatServlet,out);

        MarkupTemplateImpl.writeObject(docTemplate, out);

        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        final String servletName = ItsNatServletImpl.readObject(in);

        final String[] templateVerId = MarkupTemplateImpl.readObject(in);

        in.defaultReadObject();
        
        DeserialPendingTask task = new DeserialPendingTask()
        {
            @Override
            public void process(ItsNatServletImpl itsNatServlet,ItsNatServletRequest request, ItsNatServletResponse response)
            {
                ClientDocumentAttachedServerImpl.this.docTemplate = (ItsNatStfulDocumentTemplateAttachedServerImpl)MarkupTemplateImpl.getMarkupTemplate(itsNatServlet,templateVerId);
            }
        };
        ItsNatSessionImpl itsNatSession = ItsNatSessionObjectInputStream.getItsNatSession(in);
        itsNatSession.addDeserialPendingTask(servletName,task); // Lo dejamos pendiente tras la deserialización
    }

    public abstract boolean isOnLoadHanderUsed();
}
