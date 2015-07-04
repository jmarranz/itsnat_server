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

import com.innowhere.relproxy.jproxy.JProxyScriptEngine;
import com.innowhere.relproxy.jproxy.JProxyScriptEngineFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import org.itsnat.core.ItsNat;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.impl.core.servlet.ItsNatServletContextImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.servlet.http.ItsNatHttpServletImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatImpl implements ItsNat
{
    protected final Map<String,ItsNatServletImpl> servletsByName = new HashMap<String,ItsNatServletImpl>();
    protected final Map<String,Object> features = Collections.synchronizedMap(new HashMap<String,Object>());
    protected final ItsNatUserDataImpl userData = new ItsNatUserDataImpl(true);
    protected JProxyScriptEngine jProxyEngine = null;   
    // YA NO SE USA: protected static final boolean oldXerces = calcOldXerces(); // tras el primer valor no cambia

    /**
     * Creates a new instance of ItsNatImpl
     */
    public ItsNatImpl()
    {
    }

    public String getVersion()
    {
        return "1.3.2";
    }

    public ItsNatServletContextImpl getItsNatServletContext(ServletContext context)
    {
        ItsNatServletContextImpl itsNatContext =
                (ItsNatServletContextImpl)context.getAttribute("itsnat_servlet_context");
        if (itsNatContext == null)
        {
            // La primera vez es la única necesaria pero así evitamos sincronizaciones.
            // Es rarísimo que este método se llame en multihilo y menos aún estado
            // el contexto no registrado como atributo.
            // En teoría podríamos usar un javax.servlet.ServletContextListener pero obligamos
            // al programador registrarlo en el web.xml haciendo pública
            // una API con un fin claramente interno.
            // Creándolo la primera vez que se necesita podemos iniciarlo correctamente con el ServletContext
            // afortunadamente la de-serialización la hemos mejorado para no necesitar el ItsNatServletContextImpl
            // hasta el primer request.
            itsNatContext = new ItsNatServletContextImpl(this,context);
            context.setAttribute("itsnat_servlet_context",itsNatContext);
        }

        return itsNatContext;
    }

    public ItsNatServlet createItsNatServlet(Servlet servlet)
    {
        // No se conoce otro tipo de Servlet que el Http
        ItsNatHttpServletImpl itsNatServlet = new ItsNatHttpServletImpl(this,(HttpServlet)servlet);
        String name = itsNatServlet.getName();
        synchronized(servletsByName)
        {
            servletsByName.put(name,itsNatServlet);
        }
        return itsNatServlet;
    }

    public ItsNatServletImpl getItsNatServletByName(String name)
    {
        synchronized(servletsByName)
        {
            return servletsByName.get(name);
        }
    }

    public Object getFeature(String name)
    {
        return features.get(name);
    }

    public Object setFeature(String name, Object value)
    {
        if (value == null) throw new ItsNatException("Null value is not allowed",this);
        throw new ItsNatException("Feature not supported: \"" + name + "\"",this);
        //return features.put(name,value);
    }

/*
    private static boolean calcOldXerces()
    {
        // Xerces 2.6.2 (el que incluye la Sun JVM 1.5) tiene errores en las
        // HTMLCollection devueltas, no se a partir de qué versión deja de ternerlos, habría que determinarlo
        // Tomcat 5.5 pensado para la JVM 1.5 puede funcionar con la JVM 1.4 pero te dice que añadas unos jar
        // entre ellos está el Xerces 2.6.2
        // No estoy seguro si es este error: http://mail-archives.apache.org/mod_mbox/xerces-j-dev/200405.mbox/%3C803282686.1085102760820.JavaMail.apache@nagoya%3E
        // Al menos se sabe que la versión Xerces 2.8.0 incluida en el Tomcat 5.5.17 (includo en el NetBeans 5.5)
        // sí funciona bien.
        // http://today.java.net/pub/n/Tomcat5.5.17Stable
        // Es preciso distinguir versiones porque se sabe que:
        // En el Xerces 2.6.2 (el de la JVM 1.5) no funciona bien el getTBodies
        // el HTMLTableSectionElement.getRows() y HTMLTableRowElement.getCells()
        // La colección "options" del <select> funciona bien en el Xerces antiguo

        String verStr = org.apache.xerces.impl.Version.getVersion();

        // Ej. de formato: "Xerces-J 2.6.2"
        int pos = verStr.lastIndexOf(' ');
        verStr = verStr.substring(pos + 1); // Aisla el "2.6.2"
        int[] version = getXercesFirstSecondVersion(verStr);

        return ((version[0]*10 + version[1]) < 28);
    }

    private static int[] getXercesFirstSecondVersion(String verStr)
    {
        // Formato esperado: "v1.v2.v3..."
        // Al menos debe existir v1.v2. en donde v1 y v2 son enteros
        int[] version = new int[2];

        int pos = verStr.indexOf('.');
        version[0] = Integer.parseInt(verStr.substring(0,pos));

        verStr = verStr.substring(pos + 1);
        pos = verStr.indexOf('.');
        if (pos != -1)
            verStr = verStr.substring(0,pos);
        version[1] = Integer.parseInt(verStr);
        return version;
    }

    public static boolean isOldXerces()
    {
        // YA NO SE USA PORQUE NO USAMOS EL DOM DE XERCES

        // Suponemos siempre "old Xerces" porque aunque tengamos una versión
        // moderna en el classpath si se han cargado clases de la versión
        // antigua de la JVM 1.4 se utilizará la versión antigua
        // Aunque no se use el código para false así ya está hecho para
        // cuando supongamos una versión mínima superior sin errores.

        return true;
    }
*/

    public boolean containsUserValueName(String name)
    {
        return userData.containsUserValueName(name);
    }

    public Object getUserValue(String name)
    {
        return userData.getUserValue(name);
    }

    public Object setUserValue(String name, Object value)
    {
        return userData.setUserValue(name,value);
    }

    public Object removeUserValue(String name)
    {
        return userData.removeUserValue(name);
    }

    public String[] getUserValueNames()
    {
        return userData.getUserValueNames();
    }

    public JProxyScriptEngine getJProxyScriptEngine()
    {
        if (jProxyEngine == null) jProxyEngine = (JProxyScriptEngine)JProxyScriptEngineFactory.create().getScriptEngine(); 
        return jProxyEngine;
    }
    
    public JProxyScriptEngine getJProxyScriptEngineIfConfigured()
    {
        if (jProxyEngine == null || !jProxyEngine.isEnabled())
            return null;
        return jProxyEngine;
    }    
}
