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
public class MarkupTemplateNormalDelegateImpl extends MarkupTemplateDelegateImpl
{
    protected MarkupSourceImpl source;
    protected volatile MarkupTemplateVersionImpl currentTemplateVersion; // volatile evita en JDK 1.5 y superiores el problema del "Double-checked locking", para JVM 1.4 y menores ha de usarse el ThreadLocal
    protected final Object currentTemplateVersionMonitor = new Object();

    public MarkupTemplateNormalDelegateImpl(MarkupTemplateImpl parent,MarkupSourceImpl source)
    {
        super(parent);

        this.source = source;
    }

    public MarkupSourceImpl getMarkupSource()
    {
        return source;
    }

    @Override
    public boolean isItsNatTagsAllowed()
    {
        return true;
    }

    @Override
    public MarkupSourceImpl getMarkupSource(RequestNormalLoadDocImpl request)
    {
        return source;
    }

    @Override
    public Object getSource()
    {
        return source.getSource();
    }

    @Override
    public MarkupTemplateVersionImpl getNewestMarkupTemplateVersion(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        return getNewestMarkupTemplateVersion(source,request,response);
    }

    @Override
    public MarkupTemplateVersionImpl getNewestMarkupTemplateVersion(MarkupSourceImpl source,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        if (currentTemplateVersion == null)
        {
            // La primera vez que se accede a la página
            // Técnica alternativa: http://java.sun.com/developer/technicalArticles/Interviews/bloch_effective_08_qa.html en "Best Practices for Lazy Initialization"
            synchronized(currentTemplateVersionMonitor)
            {
                if (currentTemplateVersion == null)
                {
                    this.currentTemplateVersion = parent.createMarkupTemplateVersion(source.createInputSource(request,response),source.getCurrentTimestamp(request,response),request,response);
                    return currentTemplateVersion;
                }
                else // Caso de otro hilo que estaba esperando
                    return getNewestMarkupTemplateVersionCurrentExists(source,request,response); // Es prácticamente imposible de que se haya cambiado el template mientras se cargaba la versión anterior, pero por si acaso, si no ha cambiado no hace nada
            }
        }
        else return getNewestMarkupTemplateVersionCurrentExists(source,request,response);
    }


    private MarkupTemplateVersionImpl getNewestMarkupTemplateVersionCurrentExists(MarkupSourceImpl source,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        // Ya existe un currentTemplateVersion, vemos si tenemos que crear otro porque ha cambiado el documento
        final MarkupTemplateVersionImpl currentVersion = this.currentTemplateVersion;
        if (currentVersion.isInvalid(source,request,response))
        {
            synchronized(currentTemplateVersionMonitor)
            {
                if ((currentVersion != this.currentTemplateVersion) &&  // Un hilo concurrente lo cambió antes
                     !currentTemplateVersion.isInvalid(source,request,response))    // Verificamos si la versión que generó el hilo concurrente nos vale
                     return currentTemplateVersion;  // Caso muy raro (probabilísticamente) pero posible

                currentTemplateVersion.cleanDOMPattern();
                this.currentTemplateVersion = parent.createMarkupTemplateVersion(source.createInputSource(request,response),source.getCurrentTimestamp(request,response),request,response);
            }
        }
        return currentTemplateVersion;
    }

    @Override
    public boolean isTemplateAlreadyUsed()
    {
        // Esto es para detectar un mal uso de ItsNat en tiempo de desarrollo
        // no nos importan los hilos
        return (currentTemplateVersion != null);
    }

    @Override
    public boolean canVersionBeSharedBetweenDocs()
    {
        return true;
    }
}
