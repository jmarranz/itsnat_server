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

package org.itsnat.impl.core;

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.servlet.ItsNatServletContextImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.itsnat.core.ItsNatVariableResolver;
import java.util.HashMap;
import java.util.Map;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;

/**
 *
 * @author jmarranz
 */
public class ItsNatVariableResolverImpl implements ItsNatVariableResolver
{
    protected ItsNatVariableResolverImpl parentResolver;
    protected ItsNatServletRequestImpl request;
    protected ItsNatDocumentImpl itsNatDoc;
    protected ItsNatSessionImpl session;
    protected ItsNatServletContextImpl context;
    protected Map localAttribs = new HashMap();

    /**
     * Creates a new instance of ItsNatVariableResolverImpl
     */
    public ItsNatVariableResolverImpl(ItsNatVariableResolverImpl parent,ItsNatServletRequestImpl request,ItsNatDocumentImpl itsNatDoc,ItsNatSessionImpl session,ItsNatServletContextImpl context)
    {
        // Sólo una de ellas será null
        this.parentResolver = parent;
        this.request = request;
        this.itsNatDoc = itsNatDoc;
        this.session = session;
        this.context = context;
    }

    public ItsNatVariableResolver createItsNatVariableResolver()
    {
        return new ItsNatVariableResolverImpl(this,null,null,null,null);
    }

    public Object getLocalVariable(String name)
    {
        return localAttribs.get(name);
    }

    public Object setLocalVariable(String name,Object value)
    {
        return localAttribs.put(name,value);
    }

    public Object removeLocalVariable(String name)
    {
        return localAttribs.remove(name);
    }

    public void introspect(String refName,Object obj)
    {
        String prefix = "";
        if ((refName != null) && !refName.equals(""))
            prefix = refName + ".";

        Class clasz = obj.getClass();
        BeanInfo beanInfo;
        try
        {
            beanInfo = Introspector.getBeanInfo(clasz);
        }
        catch(IntrospectionException ex)
        {
            throw new ItsNatException(ex,this);
        }

        PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
        try
        {
            for (int i = 0; i < props.length; i++)
            {
                PropertyDescriptor property = props[i];
                String propName = property.getName();
                Method method = property.getReadMethod();
                Object res = method.invoke(obj,(Object[])(Object[])null);  // El cast es para evitar un warning si se compila con JDK 1.5 (caso varargs)
                setLocalVariable(prefix + propName,res);
            }
        }
        catch(IllegalAccessException ex)
        {
            throw new ItsNatException(ex,this);
        }
        catch(InvocationTargetException ex)
        {
            throw new ItsNatException(ex,this);
        }
    }

    public boolean isDisconnected()
    {
        return (parentResolver == null) &&
               (request == null) &&
               (itsNatDoc == null) &&
               (session == null) &&
               (context == null);
    }

    public Object getVariable(String name)
    {
        Object value = getLocalVariable(name);
        if (value != null)
            return value;

        if (parentResolver != null)
            return parentResolver.getVariable(name);
        else if (request != null)
            return request.getVariable(name);
        else if (itsNatDoc != null)
            return itsNatDoc.getVariable(name);
        else if (session != null)
            return session.getVariable(name);
        else if (context != null)
            return context.getVariable(name);

        return null;
    }

    public String resolve(String str)
    {
        // Devolver null si no se ha resuelto ninguna variable (no hay o no se conocen)
        if (str == null) return null;
        if (str.length() == 0) return null;

        boolean resolvedSomeVar = false;

        StringBuffer res = new StringBuffer();
        int pos = 0;
        do
        {
            int first = str.indexOf("${",pos);
            if (first != -1)
            {
                int end = str.indexOf('}',first + 2);
                if (end != -1)
                {
                    res.append( str.substring(pos,first) );
                    String varName = str.substring(first + 2,end);
                    Object value = getVariable(varName);
                    if (value != null)
                    {
                        res.append( value.toString() );
                        resolvedSomeVar = true;
                    }
                    else // Es una variable pero no se conoce, la dejamos como estaba, puede ser una marca de nodo cacheado
                        res.append( str.substring(first,end + 1) ); // Dejamos la variable como está
                    pos = end + 1;
                    if (pos >= str.length())
                        pos = -1;
                }
                else
                {
                    if (pos == 0)
                        return null; // No hay nada que substituir

                    res.append( str.substring(pos) );
                    pos = -1;
                }
            }
            else
            {
                if (pos == 0)
                    return null; // No hay nada que substituir

                res.append( str.substring(pos) );
                pos = -1;
            }
        }
        while (pos != -1);

        if (!resolvedSomeVar) return null;

        return res.toString(); // La nueva cadena con las variables (conocidas) resueltas
    }

    public boolean resolve(Node node)
    {
        boolean resolvedSomeVar = false;
        if (node.hasAttributes())
        {
            NamedNodeMap attribs = node.getAttributes();
            for(int i = 0; i < attribs.getLength(); i++)
            {
                Attr attr = (Attr)attribs.item(i);
                String oldValue = attr.getValue();
                String newValue = resolve(oldValue);
                if (newValue != null)
                {
                    attr.setValue(newValue);
                    resolvedSomeVar = true;
                }
            }
        }

        Node child = node.getFirstChild();
        while(child != null)
        {
            if (child instanceof CharacterData)
            {
                CharacterData textNode = (CharacterData)child;
                String oldValue = textNode.getData();
                String newValue = resolve(oldValue);
                if (newValue != null)
                {
                    DOMUtilInternal.setCharacterDataContent(textNode,newValue);
                    resolvedSomeVar = true;
                }
            }
            else
            {
                if (resolve(child))
                    resolvedSomeVar = true;
            }

            child = child.getNextSibling();
        }

        return resolvedSomeVar;
    }

}
