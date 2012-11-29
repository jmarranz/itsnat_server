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
import org.itsnat.impl.core.util.ThreadLocalDCL;

/**
 *
 * @author jmarranz
 */
public class MarkupTemplateNormalDelegateImpl extends MarkupTemplateDelegateImpl
{
    protected MarkupSourceImpl source;
    protected final ThreadLocal currentTemplateThreadLocal = ThreadLocalDCL.newThreadLocal();
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

    public boolean isItsNatTagsAllowed()
    {
        return true;
    }

    public MarkupSourceImpl getMarkupSource(RequestNormalLoadDocImpl request)
    {
        return source;
    }

    public Object getSource()
    {
        return source.getSource();
    }

    public MarkupTemplateVersionImpl getNewestMarkupTemplateVersion(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        return getNewestMarkupTemplateVersion(source,request,response);
    }

    public MarkupTemplateVersionImpl getNewestMarkupTemplateVersion(MarkupSourceImpl source,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        if (currentTemplateThreadLocal != null) // JVM 1.4 o menor
        {
            if (currentTemplateThreadLocal.get() == null) // Por primera vez con este hilo
            {
                synchronized(currentTemplateVersionMonitor)
                {
                    currentTemplateThreadLocal.set(Boolean.TRUE); // Este hilo así se entera que currentTemplateVersion está ya definido o se define ahora
                    if (currentTemplateVersion == null)
                    {
                        this.currentTemplateVersion = parent.createMarkupTemplateVersion(source.createInputSource(request,response),source.getCurrentTimestamp(request,response),request,response);
                        return currentTemplateVersion;
                    }
                    else  // Caso de otro hilo que estaba esperando
                        return getNewestMarkupTemplateVersionCurrentExists(source,request,response); // Es prácticamente imposible de que se haya cambiado el template mientras se cargaba la versión anterior, pero por si acaso, si no ha cambiado no hace nada
                }
            }
            else return getNewestMarkupTemplateVersionCurrentExists(source,request,response);
        }
        else // JVM 1.5 y superior
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
    }


    private MarkupTemplateVersionImpl getNewestMarkupTemplateVersionCurrentExists(MarkupSourceImpl source,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        // Ya existe un currentTemplateVersion, vemos si tenemos que crear otro porque ha cambiado el documento
        if (currentTemplateThreadLocal != null) // JVM 1.4 o menor
        {
            // Nos curamos así en salud pues no se si la versión para 1.5 es fiable para 1.4
            synchronized(currentTemplateVersionMonitor)
            {
                if (currentTemplateVersion.isInvalid(source,request,response))  // Verificamos si la versión que generó el hilo concurrente nos vale
                {
                    currentTemplateVersion.cleanDOMPattern();
                    this.currentTemplateVersion = parent.createMarkupTemplateVersion(source.createInputSource(request,response),source.getCurrentTimestamp(request,response),request,response);
                }
            }
        }
        else  // JVM 1.5 o superior.
        {
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
        }
        return currentTemplateVersion;
    }

    public boolean isTemplateAlreadyUsed()
    {
        // Esto es para detectar un mal uso de ItsNat en tiempo de desarrollo
        // no nos importan los hilos
        return (currentTemplateVersion != null);
    }

    public boolean canVersionBeSharedBetweenDocs()
    {
        return true;
    }
}
