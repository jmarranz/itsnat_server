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

package org.itsnat.feashow.features.comp.trees;

public class TreeTableItem
{
    protected String principal;
    protected String secondary;

    public TreeTableItem(String principal,String secondary)
    {
        this.principal = principal;
        this.secondary = secondary;
    }

    public String getPrincipal()
    {
        return principal;
    }

    public void setPrincipal(String principal)
    {
        this.principal = principal;
    }

    public String getSecondary()
    {
        return secondary;
    }

    public void setSecondary(String secondary)
    {
        this.secondary = secondary;
    }

    public String toString()
    {
        return principal + " " + secondary;
    }
}
