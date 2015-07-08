/*
 * TestUtil.java
 *
 * Created on 19 de octubre de 2006, 18:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package test.shared;

/**
 *
 * @author jmarranz
 */
public class TestUtil
{

    /** Creates a new instance of TestUtil */
    public TestUtil()
    {
    }

    public static void checkError(boolean res)
    {
        if (!res)
        {
            throw new RuntimeException("TEST ERROR");
        }
    }

}
