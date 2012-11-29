
package org.apache.batik.util.gui;

import java.awt.Component;
import javax.swing.JDialog;

/**
 * Esta clase es utilizada por Batik pensando en desktop pero falla en
 * el ámbito applet por lo que el jar que la contiene no está incluído,
 * Engañamos a Batik y mostramos el error en la consola de Java del entorno applet.
 *
 * Así además evitamos los intentos de cargar la clase desde el servidor
 * al no encontrarse en los jar cargados.
 * 
 * @author jmarranz
 */
public class JErrorPane
{
    public JErrorPane(Throwable th, int type)
    {
        System.err.println("Exception code: " + type);
        System.err.println("Exception: ");        
        th.printStackTrace();
    }

    public JDialog createDialog(Component owner, String title)
    {
        // Este método es llamado para mostrar el error, engañamos
        // a Batik para que no haga nada.
        // Los parámetros que se esperan son: JSVGCanvas.this, "ERROR"
        // Y el código del llamador es el siguiente:
        /*
            JDialog dialog = pane.createDialog(JSVGCanvas.this, "ERROR");
            dialog.setModal(false);
            dialog.setVisible(true); // Safe to be called from any thread
         */
        return new JDialogFake(); // Para engañar a Batik, pues nos interesa mostrar el error por la consola
    }

    public static class JDialogFake extends JDialog
    {
        public void setModal(boolean value) { }
        public void setVisible(boolean value)
        {
            // Truco para destruir el diálogo en vez de mostrarlo
            dispose();
        }
    }
}
