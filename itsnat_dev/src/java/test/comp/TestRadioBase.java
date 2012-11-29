/*
 * TestRadioBase.java
 *
 * Created on 22 de noviembre de 2006, 8:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp;

import org.itsnat.core.html.ItsNatHTMLDocument;
import javax.swing.ButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import test.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public abstract class TestRadioBase extends TestBaseHTMLDocument implements ChangeListener
{
    protected boolean selected = false;
    protected String idButton;

    /**
     * Creates a new instance of TestRadioBase
     */
    public TestRadioBase(String idButton,ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
        this.idButton = idButton;
    }

    public void stateChanged(ChangeEvent e)
    {
        // Este método es ejecutado ante otros tipos de cambios
        // no sólo por setSelected, por ello lo detectamos detectando
        // el cambio de selección
        ButtonModel model = (ButtonModel)e.getSource();

        String fact = "";
        if (model.isSelected() && !selected)
        {
            selected = true;
            fact = "selected " + idButton;
        }
        else if (!model.isSelected() && selected)
        {
            selected = false;
            fact = "deselected " + idButton;
        }
        if (!fact.equals(""))
        {
            outText("OK " + fact + " "); // Para que se vea
        }
    }
}
