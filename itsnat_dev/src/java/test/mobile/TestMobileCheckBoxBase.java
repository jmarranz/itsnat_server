/*
 * TestMobileCheckBoxBase.java
 *
 * Created on 20 de noviembre de 2006, 22:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.mobile;

import org.itsnat.core.html.ItsNatHTMLDocument;
import javax.swing.ButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author jmarranz
 */
public abstract class TestMobileCheckBoxBase extends TestMobileBase implements ChangeListener
{
    protected boolean selected = false;

    /**
     * Creates a new instance of TestMobileCheckBoxBase
     */
    public TestMobileCheckBoxBase(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
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
            fact = "selected ";
        }
        else if (!model.isSelected() && selected)
        {
            selected = false;
            fact = "deselected ";
        }
        if (!fact.equals(""))
        {
            outText("OK " + fact + " "); // Para que se vea
        }
    }
}
