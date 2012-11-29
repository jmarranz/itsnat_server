/*
 * Person.java
 *
 * Created on 11 de abril de 2007, 17:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp.shared;

/**
 *
 * @author jmarranz
 */
public class Person
{
    protected String firstName;
    protected String lastName;

    public Person(String firstName,String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String toString()
    {
        return firstName + " " + lastName;
    }
}

