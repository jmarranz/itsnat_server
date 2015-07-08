/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.core;

/**
 * Is the principal unchecked exception provided by ItsNat.
 * Every checked exception thrown internally is encapsulated
 * inside this exception type.
 *
 * @author Jose Maria Arranz Santamaria
 */
public class ItsNatException extends RuntimeException
{
    protected Object context;

    /**
     * Creates a new instance.
     *
     * @param ex the cause of this exception, is submitted to the parent exception.
     */
    public ItsNatException(Throwable ex)
    {
        super(ex);
    }

    /**
     * Creates a new instance.
     *
     * @param message the detail message, is submitted to the parent exception.
     */
    public ItsNatException(String message)
    {
        super(message);
    }

    /**
     * Creates a new instance.
     * @param ex the cause of this exception, is submitted to the parent exception.
     * @param message the detail message, is submitted to the parent exception.
     */
    public ItsNatException(String message,Throwable ex)
    {
        super(message,ex);
    }

    /**
     * Creates a new instance.
     *
     * @param ex the cause of this exception, is submitted to the parent exception.
     * @param context the object context of this exception.
     */
    public ItsNatException(Throwable ex,Object context)
    {
        super(ex);
        this.context = context;
    }

    /**
     * Creates a new instance.
     *
     * @param message the detail message, is submitted to the parent exception.
     * @param context the object context of this exception.
     */
    public ItsNatException(String message,Object context)
    {
        super(message);
        this.context = context;
    }

    /**
     * Creates a new instance.
     * @param ex the cause of this exception, is submitted to the parent exception.
     * @param message the detail message, is submitted to the parent exception.
     * @param context the object context of this exception.
     */
    public ItsNatException(String message,Throwable ex,Object context)
    {
        super(message,ex);
        this.context = context;
    }

    /**
     * Returns the object context of this exception, for instance the DOM node related with the exception.
     *
     * @return the object context of this exception. May be null.
     */
    public Object getContext()
    {
        return context;
    }
}
