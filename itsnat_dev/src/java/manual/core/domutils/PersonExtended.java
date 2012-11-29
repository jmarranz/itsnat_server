/*
 * PersonExtended.java
 *
 * Created on 25 de septiembre de 2007, 22:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.core.domutils;

import manual.comp.shared.Person;

/**
 *
 * @author jmarranz
 */
public class PersonExtended extends Person
{
    protected int age;
    protected boolean married;

    public PersonExtended(String firstName,String lastName,int age,boolean married)
    {
        super(firstName,lastName);

        this.age = age;
        this.married = married;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public boolean isMarried()
    {
        return married;
    }

    public void setMarried(boolean married)
    {
        this.married = married;
    }
}
