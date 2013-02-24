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
package org.itsnat.core.event;

/**
 * Is used to command ItsNat to transport the specified node property of a client
 * element and optionally synchronize it with the matched server element as an attribute.
 *
 * <p>After synchronization the server DOM element has the same attribute value
 * as the client property counterpart. If the client property value is null
 * the server element attribute will be removed too.</p>
 *
 * <p>With or without synchronization the property value can be obtained
 * calling {@link org.itsnat.core.event.ItsNatEvent#getExtraParam(String)}</p>
 *
 * @see org.itsnat.core.ItsNatDocument#addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,ParamTransport[],String,long)
 * @author Jose Maria Arranz Santamaria
 */
public class NodePropertyTransport extends SingleParamTransport
{
    private String javaSetMethodName;
    private Class<?> type;
    private String attName;

    /**
     * Creates a new instance ready to transport the node property with the specified name
     * and synchronize it at the server side as an attribute, the attribute name used is the same
     * as the property name.
     *
     * @param name the property name.
     */
    public NodePropertyTransport(String name)
    {
        this(name,name,true);
    }

    /**
     * Creates a new instance ready to transport the node property with the specified name
     * and optionally synchronize it at the server side as an attribute, the attribute name used is the same
     * as the property name.
     *
     *
     * @param name the property name.
     * @param sync if true the server is updated.
     */
    public NodePropertyTransport(String name,boolean sync)
    {
        this(name,name,sync);
    }

    /**
     * Creates a new instance ready to transport the node property with the specified name
     * and synchronize it at the server side as an attribute with the specified name.
     *
     * <p>Use this constructor when the property name differs from the attribute name.</p>
     *
     * @param name the property name.
     * @param attName the attribute name.
     */
    public NodePropertyTransport(String name,String attName)
    {
        this(name,attName,true);
    }

    private NodePropertyTransport(String name,String attName,boolean sync)
    {
        // No hacemos público este constructor pues si se especifica el nombre del atributo
        // como diferente a la propiedad es que queremos sincronizar seguro.
        super(name,sync);

        this.attName = attName;

        this.type = null;

        this.javaSetMethodName = null;
    }

    /**
     * Creates a new instance ready to transport the node property with the specified name
     * and synchronize it at the server side as an attribute. The synchronization is
     * done using reflection calling the method set<i>Name</i>(<i>Class type</i>)
     * of the target DOM element.
     *
     * <p>For instance: if specified name is "value" and specified type is String,
     * the method called will be <code>setValue(String)</code></p>
     *
     * @param name the property name.
     * @param type the class type of the property.
     */
    public NodePropertyTransport(String name,Class<?> type)
    {
        super(name,true);

        this.type = type;

        String javaMethodName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        this.javaSetMethodName = "set" + javaMethodName;
        this.attName = null;
    }

    /**
     * Creates a new instance ready to transport the node property with the specified name
     * and synchronize it at the server side as an attribute. The synchronization is
     * done using reflection calling the set method with the specified name
     * of the target DOM element.
     *
     * <p>For instance: if specified method name is "setValue" and specified type is String,
     * the method called will be <code>setValue(String)</code></p>
     *
     * @param name the property name.
     * @param type the class type of the property.
     */
    public NodePropertyTransport(String name,Class<?> type,String javaSetMethodName)
    {
        super(name,true);

        this.type = type;
        this.javaSetMethodName = javaSetMethodName;
        this.attName = null;
    }

    /**
     * Returns the attribute name.
     *
     * @return the attribute name or null if reflection is used to synchronize.
     */
    public String getAttrName()
    {
        return attName;
    }

    /**
     * Returns the class type of the property value when using reflection to synchronize.
     *
     * @return the class type or null if reflection is not used to synchronize.
     */
    public Class<?> getType()
    {
        return type;
    }

    /**
     * Returns the method name used to synchronize using reflection.
     *
     * @return the method name or null if reflection is not used to synchronize.
     */
    public String getJavaSetMethodName()
    {
        return javaSetMethodName;
    }

}
