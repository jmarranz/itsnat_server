/*
 * TestInputButtonNormalListener.java
 *
 * Created on 13 de noviembre de 2006, 20:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import javax.swing.ButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import test.web.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public abstract class TestButtonBase extends TestBaseHTMLDocument implements ChangeListener,ActionListener
{
    /**
     * Creates a new instance of TestInputButtonNormalListener
     */
    public TestButtonBase(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);
    }

    public void stateChanged(ChangeEvent e)
    {
        ButtonModel model = (ButtonModel)e.getSource();

        String fact = "";
        if (model.isArmed())
            fact += "armed ";
        if (model.isPressed())
            fact += "pressed ";
        if (model.isRollover())
            fact += "rollover ";
        if (model.isSelected())
            fact += "selected "; // No ocurrirá nunca para este tipo de botón

        if (!fact.equals(""))
        {
            outText("OK " + fact + " "); // Para que se vea
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        outText("OK " + e + " "); // Para que se vea
    }
}
