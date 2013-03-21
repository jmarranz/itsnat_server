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

package org.itsnat.impl.core.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.core.ItsNat;
import org.itsnat.core.ItsNatBoot;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.ItsNatServletContext;
import org.itsnat.core.event.ItsNatAttachedClientEventListener;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.impl.core.*;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.template.ItsNatDocFragmentTemplateImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateImpl;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateAttachedServerImpl;
import org.itsnat.impl.core.util.UniqueIdGenIntList;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatServletImpl extends ItsNatUserDataImpl implements ItsNatServlet
{
    protected ItsNatImpl parent;
    protected Servlet servlet;
    protected UniqueIdGenIntList idGenerator = new UniqueIdGenIntList(true);
    protected Map<String,ItsNatDocumentTemplateImpl> pages = Collections.synchronizedMap(new HashMap<String,ItsNatDocumentTemplateImpl>());
    protected Map<String,ItsNatDocFragmentTemplateImpl> fragments = Collections.synchronizedMap(new HashMap<String,ItsNatDocFragmentTemplateImpl>());
    protected LinkedList<ItsNatServletRequestListener> requestListeners;
    protected LinkedList<ItsNatAttachedClientEventListener> attachedEventListeners;
    protected LinkedList<EventListener> domEventListeners;
    protected LinkedList<CreateItsNatComponentListener> createCompListeners;
    protected ItsNatServletConfigImpl servletConfig;


    /**
     * Creates a new instance of ItsNatServletImpl
     */
    public ItsNatServletImpl(ItsNatImpl parent,Servlet servlet)
    {
        super(true);

        this.parent = parent;
        this.servlet = servlet;

        this.servletConfig = createItsNatServletConfig();
    }

    public static void writeObject(ItsNatServletImpl itsNatServlet,ObjectOutputStream out) throws IOException
    {
        out.writeObject(itsNatServlet.getName());
    }

    public static String readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        return (String)in.readObject();
    }

    public static ItsNatServletImpl getItsNatServletByName(String servletName)
    {
        ItsNatImpl itsNat = (ItsNatImpl)ItsNatBoot.get();
        return itsNat.getItsNatServletByName(servletName); // Si devuelve null es que el servlet no se ha inicializado todavía
    }

    public ItsNatServletConfigImpl createItsNatServletConfig()
    {
        return new ItsNatServletConfigImpl(servlet.getServletConfig(),this);
    }

    public ItsNatServletContext getItsNatServletContext()
    {
        return servletConfig.getItsNatServletContext();
    }

    public ItsNatServletContextImpl getItsNatServletContextImpl()
    {
        return servletConfig.getItsNatServletContextImpl();
    }

    public UniqueIdGenIntList getUniqueIdGenerator()
    {
        return idGenerator;
    }

    public ItsNat getItsNat()
    {
        return getItsNatImpl();
    }

    public ItsNatImpl getItsNatImpl()
    {
        return parent;
    }

    public String getName()
    {
        return servlet.getServletConfig().getServletName();
    }
    
    public Servlet getServlet()
    {
        return servlet;
    }

    public ItsNatServletConfig getItsNatServletConfig()
    {
        return servletConfig;
    }

    public ItsNatServletConfigImpl getItsNatServletConfigImpl()
    {
        return servletConfig;
    }

    protected void registerItsNatDocumentTemplate(ItsNatDocumentTemplateImpl docTemplate)
    {
        Object res = pages.put(docTemplate.getName(),docTemplate);
        if (res != null) throw new ItsNatException("One document template is already registered with this name:" + docTemplate.getName());
    }
    
    public ItsNatDocumentTemplate registerItsNatDocumentTemplate(String name,String mime,Object source)
    {
        ItsNatDocumentTemplateImpl docTemplate = ItsNatDocumentTemplateImpl.createItsNatDocumentTemplate(name, mime,source, this);
        registerItsNatDocumentTemplate(docTemplate);
        return docTemplate;
    }

    public ItsNatDocumentTemplate registerItsNatDocumentTemplateAttachedServer(String name,String mime)
    {
        ItsNatStfulDocumentTemplateAttachedServerImpl docTemplate = ItsNatDocumentTemplateImpl.createItsNatStfulDocumentTemplateAttachedServer(name, mime,this);
        registerItsNatDocumentTemplate(docTemplate);
        return docTemplate;
    }

    public ItsNatDocumentTemplate getItsNatDocumentTemplate(String name)
    {
        return getItsNatDocumentTemplateImpl(name);
    }

    public ItsNatDocumentTemplateImpl getItsNatDocumentTemplateImpl(String name)
    {
        return pages.get(name);
    }

    public ItsNatDocFragmentTemplate registerItsNatDocFragmentTemplate(String name,String mime,Object source)
    {
        ItsNatDocFragmentTemplateImpl fragmentDesc = ItsNatDocFragmentTemplateImpl.createItsNatDocFragmentTemplate(name, mime, source, this);
        Object res = fragments.put(fragmentDesc.getName(),fragmentDesc);
        if (res != null) throw new ItsNatException("One document fragment template is already registered with this name:" + fragmentDesc.getName());
        return fragmentDesc;
    }

    public ItsNatDocFragmentTemplate getItsNatDocFragmentTemplate(String name)
    {
        return getItsNatDocFragmentTemplateImpl(name);
    }

    public ItsNatDocFragmentTemplateImpl getItsNatDocFragmentTemplateImpl(String name)
    {
        return fragments.get(name);
    }

    public void checkIsAlreadyUsed()
    {
        // Si no hay fragmentos registrados no puede haber documentos cargados por usuarios
        // Este chequeo es sobre todo para detectar en tiempo de desarrollo un mal uso
        if (!fragments.isEmpty())
            throw new ItsNatException("Servlet is frozen because some fragment was already registered",this);
    }

    public boolean dispatchItsNatServletRequestListeners(ItsNatServletRequestImpl itsNatRequest)
    {
        if (hasItsNatServletRequestListeners())
        {
            ItsNatServletResponseImpl itsNatResponse = itsNatRequest.getItsNatServletResponseImpl();
            Iterator<ItsNatServletRequestListener> iterator = getItsNatServletRequestListenerIterator();
            itsNatResponse.dispatchItsNatServletRequestListeners(iterator);
            return true;
        }
        return false;
    }

    public boolean hasItsNatServletRequestListeners()
    {
        if (requestListeners == null)
            return false;
        return !requestListeners.isEmpty();
    }

    public LinkedList<ItsNatServletRequestListener> getItsNatServletRequestListenerList()
    {
        if (requestListeners == null)
            this.requestListeners = new LinkedList<ItsNatServletRequestListener>();
        return requestListeners;
    }

    public Iterator<ItsNatServletRequestListener> getItsNatServletRequestListenerIterator()
    {
        // No sincronizamos porque sólo admitimos sólo lectura
        if (requestListeners == null) return null;
        if (requestListeners.isEmpty()) return null;
        return requestListeners.iterator();
    }

    public void addItsNatServletRequestListener(ItsNatServletRequestListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<ItsNatServletRequestListener> requestListeners = getItsNatServletRequestListenerList();
        requestListeners.add(listener);
    }

    public void removeItsNatServletRequestListener(ItsNatServletRequestListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<ItsNatServletRequestListener> requestListeners = getItsNatServletRequestListenerList();
        requestListeners.remove(listener);
    }

    public LinkedList<ItsNatAttachedClientEventListener> getItsNatAttachedClientEventListenerList()
    {
        if (attachedEventListeners == null)
            this.attachedEventListeners = new LinkedList<ItsNatAttachedClientEventListener>();
        return attachedEventListeners;
    }

    public boolean hasItsNatAttachedClientEventListeners()
    {
        // No sincronizamos porque sólo admitimos sólo lectura
        if (attachedEventListeners == null)
            return false;
        return !attachedEventListeners.isEmpty();
    }

    public void getItsNatAttachedClientEventListenerList(LinkedList<ItsNatAttachedClientEventListener> list)
    {
        // No sincronizamos porque sólo admitimos sólo lectura
        if (attachedEventListeners == null)
            return;
        list.addAll(attachedEventListeners);
    }

    public void addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<ItsNatAttachedClientEventListener> attachedEventListeners = getItsNatAttachedClientEventListenerList();
        attachedEventListeners.add(listener);
    }

    public void removeItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<ItsNatAttachedClientEventListener> attachedEventListeners = getItsNatAttachedClientEventListenerList();
        attachedEventListeners.remove(listener);
    }

    public boolean hasEventListenerListeners()
    {
        if (domEventListeners == null)
            return false;
        return !domEventListeners.isEmpty();
    }

    public LinkedList<EventListener> getGlobalEventListenerList()
    {
        if (domEventListeners == null)
            this.domEventListeners = new LinkedList<EventListener>();
        return domEventListeners;
    }

    public void getGlobalEventListenerList(LinkedList<EventListener> list)
    {
        // No sincronizamos porque sólo admitimos sólo lectura
        if (domEventListeners == null)
            return;
        list.addAll(domEventListeners);
    }

    public void addEventListener(EventListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<EventListener> domEventListeners = getGlobalEventListenerList();
        domEventListeners.add(listener);
    }

    public void removeEventListener(EventListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<EventListener> domEventListeners = getGlobalEventListenerList();
        domEventListeners.remove(listener);
    }

    public boolean hasCreateItsNatComponentList()
    {
        if (createCompListeners == null) return false;
        return !createCompListeners.isEmpty();
    }

    public LinkedList<CreateItsNatComponentListener> getCreateItsNatComponentList()
    {
        if (createCompListeners == null)
            this.createCompListeners = new LinkedList<CreateItsNatComponentListener>(); // Sólo se crea si se necesita
        return createCompListeners;
    }

    public Iterator<CreateItsNatComponentListener> getCreateItsNatComponentListenerIterator()
    {
        if (!hasCreateItsNatComponentList()) return null;
        return createCompListeners.iterator();
    }

    public void addCreateItsNatComponentListener(CreateItsNatComponentListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<CreateItsNatComponentListener> list = getCreateItsNatComponentList();
        list.add(listener);
    }

    public void removeCreateItsNatComponentListener(CreateItsNatComponentListener listener)
    {
        checkIsAlreadyUsed(); // Así evitamos sincronizar la lista pues si es sólo lectura admite múltiples hilos

        LinkedList<CreateItsNatComponentListener> list = getCreateItsNatComponentList();
        list.remove(listener);
    }

    public void processRequest(ServletRequest request, ServletResponse response)
    {
        processRequestInternal(request,response,null);
    }
    
    public abstract ItsNatServletRequestImpl processRequestInternal(ServletRequest request, ServletResponse response,ClientDocumentImpl clientDocStateless);
     
    public abstract ItsNatServletRequestImpl createItsNatServletRequest(ServletRequest request,ServletResponse response,ItsNatSessionImpl itsNatSession);
}
