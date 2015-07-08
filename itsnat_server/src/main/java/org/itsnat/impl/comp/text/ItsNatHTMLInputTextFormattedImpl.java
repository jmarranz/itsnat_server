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

package org.itsnat.impl.comp.text;

import java.beans.PropertyVetoException;
import org.itsnat.comp.text.ItsNatFormattedTextField.ItsNatFormatter;
import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.core.ItsNatException;
import java.text.Format;
import java.text.ParseException;
import java.util.Date;
import javax.swing.event.DocumentEvent;
import org.itsnat.comp.text.ItsNatFormattedTextField.ItsNatFormatterFactory;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.web.ItsNatStfulWebDocComponentManagerImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLInputTextFormattedImpl extends ItsNatHTMLInputTextImpl implements ItsNatHTMLInputTextFormatted
{
    protected Object value;
    protected boolean hasFocus; 
    protected boolean edited;
    protected boolean editValid;
    protected int focusLostBehavior = COMMIT_OR_REVERT;
    protected ItsNatFormatterFactory factory;
    protected ItsNatFormatter formatter;

    /**
     * Creates a new instance of ItsNatHTMLInputTextFormattedImpl
     */
    public ItsNatHTMLInputTextFormattedImpl(HTMLInputElement element,NameValue[] artifacts,ItsNatStfulWebDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        setItsNatFormatterFactory(createDefaultItsNatFormatterFactory());

        init();
    }

    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        enableEventListener("focus");
        enableEventListener("blur");
    }

    public int getFocusLostBehavior()
    {
        return focusLostBehavior;
    }

    public void setFocusLostBehavior(int behavior)
    {
        if ((behavior != COMMIT) && (behavior != COMMIT_OR_REVERT) &&
            (behavior != PERSIST) && (behavior != REVERT))
            throw new ItsNatException("Value is not valid must be one of: ItsNatFormattedTextField.COMMIT, ItsNatFormattedTextField.COMMIT_OR_REVERT, ItsNatFormattedTextField.PERSIST or ItsNatFormattedTextField.REVERT",this);

        this.focusLostBehavior = behavior;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value) throws PropertyVetoException
    {
        setValue(value,true,true);
    }

    public void updateDisplay()
    {
        Object value = getValue();
        try
        {
            setValue(value,true,false);
        }
        catch(PropertyVetoException ex)
        {
            // Es imposible que ocurra porque se le dice fire=false
            throw new ItsNatException("INTERNAL ERROR");
        }
    }

    public void setValue(Object value,boolean toDisplay,boolean fire) throws PropertyVetoException
    {
        Object oldValue = this.value;

        if (fire)
            fireVetoableChange("value", oldValue, value);

        this.value = value;

        if (toDisplay)
        {
            String str;
            try
            {
                str = valueToString(value);

                setEditValid(true);
            }
            catch(ParseException ex)
            {
                // El valor pasado no tiene el formato esperado por el formatter
                this.value = oldValue;

                setEditValid(false);

                throw new ItsNatException(ex,this);
            }
            setText(str);
        }

	if (fire)
	    firePropertyChange("value", oldValue, value);  // Si son iguales no se lanza

        setEdited(false);
    }

    public void commitEdit() throws ParseException,PropertyVetoException
    {
        String str = getText();
        Object newValue = stringToValue(str);
        setValue(newValue,false,true);
    }

    public boolean isEdited()
    {
        return edited;
    }

    private void setEdited(boolean edited)
    {
        this.edited = edited;
    }

    public void setEditValid(boolean isValid)
    {
        // No hacer público
        this.editValid = isValid;
    }

    public boolean isEditValid()
    {
        return editValid;
    }

    public String valueToString(Object value) throws ParseException
    {
        ItsNatFormatter formatter = getItsNatFormatter();
        return formatter.valueToString(value,this);
    }

    public Object stringToValue(String str) throws ParseException
    {
        ItsNatFormatter formatter = getItsNatFormatter();
        return formatter.stringToValue(str,this);
    }

    public void processNormalEvent(Event evt)
    {
        String type = evt.getType();
        if (type.equals("focus"))
            setHasFocus(true);
        else if (type.equals("blur"))
            setHasFocus(false);

        super.processNormalEvent(evt);
    }


    public boolean hasFocus()
    {
        return hasFocus;
    }

    public void setHasFocus(boolean hasFocus)
    {
        this.hasFocus = hasFocus;

        updateDisplay(); // Es útil si el formatter formatea de forma diferente si está siendo editado a cuando está sin el foco, es el caso de usar el formatter factory default con varios formatters
    }

    public void postHandleEventOnChange(Event evt)
    {
        super.postHandleEventOnChange(evt);

        int fb = getFocusLostBehavior();
        if ((fb == COMMIT) || (fb == COMMIT_OR_REVERT))
        {
            try
            {
                commitEdit();

                // Si se llega aquí es que fue válido el valor del control,
                // es posible de todas formas que su presentación visual no
                // esté normalizada de acuerdo al formatter, por tanto reformateamos el valor y lo enviamos
                // al control, no enviamos evento porque no hay cambio en value (y aunque pusiéramos true no se envía si no hay cambio de valor real)
                // Un ejemplo de esto es en el caso de un Date, el formatter
                // admite el 32 de enero pero realmente almacena el 1 de febrero
                // así al actualizar el display lo veremos normalizado como 1 de febrero
                updateDisplay(); // Ya se encarga de hacer también setEditValid(true);
            }
            catch(ParseException ex)
            {
                // El valor editado no tiene el formato esperado por el formatter
                // restauramos al valor antiguo el control en el caso COMMIT_OR_REVERT
                // No lanzamos el evento propiedad "value" porque no ha cambiado (y aunque pusiéramos true no se envía si no hay cambio de valor real)
                setEditValid(false);
                if (fb == COMMIT_OR_REVERT)
                    updateDisplay(); // Restauramos el valor bueno del componente, pone de nuevo setEditValid(true);
                // El el caso de COMMIT el valor erróneo se queda en el control
            }
            catch(PropertyVetoException ex)
            {
                // Idem caso anterior, aunque en este caso ha sido el veto la causa del rechazo, en definitiva el valor no es válido
                setEditValid(false);
                if (fb == COMMIT_OR_REVERT)
                    updateDisplay();
            }
        }
        else if (fb == REVERT)
        {
             // Restauramos el valor bueno del componente sea cual sea el del control
            updateDisplay(); // Ya se encarga de hacer también setEditValid(true);
        }
        else
        {
            // El caso PERSIST es no hacer nada, sea válido o no el valor en el control
            // En ese caso isEditValid() no tiene mucho sentido llamarse pues no sabemos que devolver
        }
    }

    public void postHandleEventOnKeyUp(Event evt)
    {
        super.postHandleEventOnKeyUp(evt);

        // Estudiar si enviar el valor al formatter específico para
        // cada tecla pulsada, con el fin de corregir la edición si es posible
        // y ayudar al usuario. Una idea: si el usuario ha introducido
        // una letra no válida detectarla y quitarla
        // Otra forma diferente sería usar un DocumentFilter
        // Por ahora no hacemos nada
    }

    public void insertUpdate(DocumentEvent e)
    {
        super.insertUpdate(e);

        setEdited(true);
    }

    public void removeUpdate(DocumentEvent e)
    {
        super.removeUpdate(e);

        setEdited(true);
    }

    public void changedUpdate(DocumentEvent e)
    {
        super.changedUpdate(e);

        // No hay atributos
    }

    public ItsNatFormatterFactory createDefaultItsNatFormatterFactory()
    {
        return new ItsNatFormatterFactoryDefaultImpl();
    }

    public ItsNatFormatterFactory getItsNatFormatterFactory()
    {
        return factory; // puede ser null
    }

    public void setItsNatFormatterFactory(ItsNatFormatterFactory factory)
    {
        this.factory = factory;
    }

    public ItsNatFormatter getItsNatFormatter()
    {
        ItsNatFormatter formatter = this.formatter;
        if (formatter == null)
        {
            ItsNatFormatterFactory factory = getItsNatFormatterFactory();
            if (factory != null)
                formatter = factory.getItsNatFormatter(this);
            if (formatter == null)
                formatter = getDefaultFormatterOfValue(getValue());
        }

        if (formatter == null)
            throw new ItsNatException("No formatter is available",this);
        return formatter;
    }

    public void setItsNatFormatter(ItsNatFormatter formatter)
    {
        this.formatter = formatter;
    }

    public void setFormat(Format format)
    {
        // No hacemos un getFormat() porque el verdadero formateador es ItsNatFormatter
        // el cual en teoría puede no estar basado en java.text.Format
        setItsNatFormatter(createItsNatFormatter(format));
    }

    public ItsNatFormatter createItsNatFormatter(Format format)
    {
        return new ItsNatFormatterFormatBasedImpl(format);
    }

    public ItsNatFormatter getDefaultFormatterOfValue(Object value)
    {
        Format format = null;
        if (value != null)
        {
            if (value instanceof Date)
            {
                format = getItsNatComponentManager().getItsNatDocument().getDefaultDateFormat();
            }
            else if (value instanceof Number)
            {
                format = getItsNatComponentManager().getItsNatDocument().getDefaultNumberFormat();
            }
        }

        if (format != null)
            return new ItsNatFormatterFormatBasedImpl(format);
        else
            return new ItsNatFormatterDefaultImpl();
    }
}


