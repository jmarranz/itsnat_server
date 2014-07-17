/*
 * Rect.java
 *
 * Created on 11 de febrero de 2008, 19:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.svg;

import java.io.Serializable;

/**
 *
 * @author jmarranz
 */
public class Rect implements Serializable
{
    protected int x;

    /** Creates a new instance of Rect */
    public Rect(int x)
    {
        this.x = x;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }
}
