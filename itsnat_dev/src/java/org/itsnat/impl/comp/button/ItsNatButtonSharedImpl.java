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

package org.itsnat.impl.comp.button;

import java.io.Serializable;
import javax.swing.ButtonModel;
import javax.swing.event.ChangeEvent;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatButtonSharedImpl implements Serializable
{
    protected ItsNatButtonInternal comp;

    /**
     * Creates a new instance of ItsNatButtonSharedImpl
     */
    public ItsNatButtonSharedImpl(ItsNatButtonInternal comp)
    {
        this.comp = comp;
    }

    public void bindDataModel()
    {
        // A partir de ahora los cambios los repercutimos en el DOM por eventos
        // No se debe cambiar el DOM por otra vía que por el objeto dataModel
        ButtonModel dataModel = comp.getButtonModel();
        dataModel.addChangeListener(comp);
    }

    public void unbindDataModel()
    {
        ButtonModel dataModel = comp.getButtonModel();
        dataModel.removeChangeListener(comp);
    }

    public void initialSyncUIWithDataModel()
    {
        syncUIWithDataModel();
    }

    public void syncUIWithDataModel()
    {
        ButtonModel dataModel = comp.getButtonModel();
        comp.setDOMEnabled(dataModel.isEnabled()); // Valor actual, cualquier cambio se notificará via listeners
    }

    public void syncWithDataModel()
    {
        syncUIWithDataModel();
    }

    public void stateChanged(ChangeEvent e)
    {
        comp.syncWithDataModel();
    }

    public boolean handleEvent(Event evt)
    {
        return handleEvent(comp.getButtonModel(),evt);
    }

    public static boolean handleEvent(ButtonModel dataModel,Event evt)
    {
        // Ejecutado como respuesta a eventos del navegador
        // No hay problemas de provocar mutation events no deseados, no hace falta inhibir, que lo haga el usuario si añade listeners
        // Se ha comprobado que pulsando un JButton nunca se hace un setSelection(true),
        // la selección es cosa de los toggle buttons y se hace indirectamente a través del setPressed por lo que
        // no usamos setSelection explícitamente (no se necesita).
        // De acuerdo con la experiencia de Swing:
        // Armed es true cuando el botón se ha pulsado y el ratón está dentro, viene a decir que si se suelta el botón ahora el click será completo
        // Pressed es true cuando el botón fue pulsado hasta que se deja de pulsar (aunque el ratón esté fuera)

        if (!dataModel.isEnabled())
            return false; // Sirve sobre todo para los "free buttons" que no son elementos de formulario y no pueden ser disabled por DOM, emulamos así que el elemento no está activo (ya que el GUI no evita enviar el evento pues un elemento cualquiera no es desactivable)

        String type = evt.getType();
        if (type.equals("click"))
        {
            // La suma de mousedown y mouseup
            // NO es en absoluto aconsejable procesar click y mousedown/mouseup
            // pues es como si se hubiera pulsado dos veces el botón (desastre en el caso de botones seleccionables)
            // En el caso de pulsar el botón soltando fuera del mismo el navegador no se genera el evento "click" lo cual es lo deseable en botones seleccionables pues no cambia el estado (lo mismo ocurre en Swing)
            dataModel.setArmed(true);
            dataModel.setPressed(true); // lanzará el evento por el que podemos saber que ha sido pulsado
            dataModel.setPressed(false); // idem, sabremos que ha dejado de estar pulsado.
            dataModel.setArmed(false);   // "
        }
        else if (type.equals("mousedown"))
        {
            dataModel.setArmed(true); // Debe ir antes de que Pressed
            dataModel.setPressed(true);
        }
        else if (type.equals("mouseup"))
        {
            dataModel.setPressed(false);  // Debe ir antes de que Armed
            dataModel.setArmed(false);
        }
        else if (type.equals("mouseover"))
        {
            dataModel.setRollover(true);
        }
        else if (type.equals("mouseout"))
        {
            dataModel.setRollover(false);
            dataModel.setArmed(false); // pues fuera del botón aunque el botón esté pulsado no está "armado" para disparar (cerrar el click)
        }

        // No hacemos nada con mousemove pues con este evento no
        // podemos saber cuando hacer setRollover(false); y cuando
        // se saliera del botón quedaría como true si no está definido el over/out

        return true;
    }
}
