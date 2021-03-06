/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.comp.shared;


public class Circle
{
    protected int radio;

    public Circle(int radio)
    {
        this.radio = radio;
    }

    public int getRadio()
    {
        return radio;
    }

    public void setRadio(int radio)
    {
        this.radio = radio;
    }

    public String toString()
    {
        return Integer.toString(radio);
    }
}
