/*
 * ValidateLoginListener.java
 *
 * Created on 4 de junio de 2007, 18:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp.custom;

/**
 *
 * @author jmarranz
 */
public interface ValidateLoginListener
{
    public boolean validate(String user,String password);
}
