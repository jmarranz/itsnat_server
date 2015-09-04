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

package org.itsnat.impl.core.css;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;
import org.itsnat.impl.core.css.lex.SemiColon;
import org.itsnat.impl.core.css.lex.SourceCode;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class CSSStyleDeclarationImpl implements CSSStyleDeclaration,Serializable
{
    protected String cssText = ""; // Caché
    protected Element parent;
    protected Map<String,CSSPropertyImpl> propertyMap = new HashMap<String,CSSPropertyImpl>();
    protected List<CSSPropertyImpl> propertyList = new ArrayList<CSSPropertyImpl>();

    /** Creates a new instance of CSSStyleDeclarationImpl */
    public CSSStyleDeclarationImpl(Element parent)
    {
        this.parent = parent;
    }

    @Override
    public String getCssText()
    {
        rebuild();

        return this.cssText;
    }

    @Override
    public void setCssText(String cssText) throws DOMException
    {
        DOMUtilInternal.setAttribute(parent,"style",cssText); // Actualizamos por sistema pues puede haber cambiado el atributo aunque no sea por aquí
        rebuild(cssText);
    }

    public CSSPropertyImpl getPropertyObject(String propertyName)
    {
        getCssText(); // Actualiza si es necesario
        propertyName = propertyName.toLowerCase();
        return propertyMap.get(propertyName);
    }

    @Override
    public String getPropertyValue(String propertyName)
    {
        CSSPropertyImpl property = getPropertyObject(propertyName);
        if (property == null) return ""; // De acuerdo con la documentación
        return property.getCssTextSourceCode(false).toString();
    }

    @Override
    public CSSValue getPropertyCSSValue(String propertyName)
    {
        // Deberíamos devolver null si es una propiedad "shorthand" pero
        // nos interesa manipular las propiedades via CSSValue
        CSSPropertyImpl property = getPropertyObject(propertyName);
        if (property == null)
            return null;
        return property.getCSSValue();
    }

    @Override
    public String removeProperty(String propertyName) throws DOMException
    {
        propertyName = propertyName.toLowerCase();

        CSSPropertyImpl property = getPropertyObject(propertyName);
        if (property == null)
            return "";
        String res = property.getCssTextSourceCode(false).toString();

        propertyList.remove(property);
        propertyMap.remove(propertyName);

        // Renderizamos de nuevo ahora sin la propiedad quitada
        updateCssTextFromPropertyList();

        return res;
    }

    public void updateCssTextFromPropertyList()
    {
        StringBuilder cssText = new StringBuilder();
        for(int i = 0; i < propertyList.size(); i++ )
        {
             if (i != 0) cssText.append( ';' );
             CSSPropertyImpl currProperty = propertyList.get(i);
             cssText.append( currProperty.getPropertyName() + ":" + currProperty.getCssTextSourceCode(false) );
        }

        this.cssText = cssText.toString(); // Evitamos así la reconstrucción de las listas al llamar a setCssTextSourceCode
        setCssText(this.cssText);
    }

    @Override
    public String getPropertyPriority(String propertyName)
    {
        return ""; // NO soportamos !important
    }

    public void notifyToElementChangedProperty(CSSPropertyImpl property,SourceCode value)
    {
        CSSPropertyImpl propertyElem = getPropertyObject(property.getPropertyName()); // El mero hecho de hacer esta llamada reconstruye la lista si hubiera cambiado el Element por otra vía
        if (propertyElem != null)
        {
            if (propertyElem != property)
            {
                // Substituimos con la nueva y reconstruimos
                addCSSProperty(property,true);
            }
            else
            {
                // Aun así ha cambiado el valor, hay que notificar al Element
                updateCssTextFromPropertyList();
            }
        }
        else // No está, fue eliminada del Element indirectamente, la añadimos de nuevo
        {
            addCSSProperty(property,true);
        }
    }

    @Override
    public void setProperty(String propertyName, String value, String priority) throws DOMException
    {
        // Ignoramos la prioridad (!important) no la soportamos
        CSSPropertyImpl property = getPropertyObject(propertyName);  // El mero hecho de hacer esta llamada reconstruye la lista si hubiera cambiado el Element por otra vía
        if (property != null)
        {
            String currentValue = property.getCssText(false);
            if (!currentValue.equals(value))
            {
                property.setCssText(value,false);
                // Actualizamos el cssText y el Element
                updateCssTextFromPropertyList();
            }
        }
        else // No está, es una nueva propiedad
        {
            addCSSProperty(propertyName,value,true);
        }
    }

    @Override
    public int getLength()
    {
        return propertyList.size();
    }

    @Override
    public String item(int index)
    {
        CSSPropertyImpl currProperty = propertyList.get(index);
        return currProperty.getCssTextSourceCode().toString();
    }

    @Override
    public CSSRule getParentRule()
    {
        // Sólo soportamos propiedades CSS declaradas en atributos style de elementos
        // por ahora
        return null;
    }

    private void rebuild()
    {
        // El atributo puede ser cambiado en cualquier momento
        // por eso obtenemos siempre el valor desde la fuente original

        String cssText = parent.getAttribute("style"); // Nunca es nula (cadena vacía si acaso)
        rebuild(cssText);
    }

    private void addCSSProperty(String propertyName,String value,boolean updateCssText)
    {
        CSSPropertyImpl property = new CSSPropertyImpl(propertyName,value,this);
        addCSSProperty(property,updateCssText);
    }

    private void addCSSProperty(CSSPropertyImpl property,boolean updateCssText)
    {
        propertyList.add(property);
        propertyMap.put(property.getPropertyName(),property);

        // Actualizamos el cssText y el Element
        if (updateCssText)
            updateCssTextFromPropertyList();
    }

    private void rebuild(String cssText)
    {
        if (this.cssText.equals(cssText))
            return; // No es necesario reconstruir las listas

        this.cssText = cssText;

        propertyList.clear();
        propertyMap.clear();

        SourceCode cssTextSource = SourceCode.newSourceCode(cssText);
        SourceCode[] cssTextSourceProps = cssTextSource.split(SemiColon.getSingleton());

        for(int i = 0; i < cssTextSourceProps.length; i++)
        {
            SourceCode cssTextSourceProp = cssTextSourceProps[i];
            addCSSProperty(new CSSPropertyImpl(cssTextSourceProp,this),false);
        }
    }
}
