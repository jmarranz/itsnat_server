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

package org.itsnat.core.event;

/**
 * Is used to command ItsNat to transport one value from client to server
 * using an event fired on the client and listened in the server.
 *
 * <p>The transported value can be obtained
 * calling {@link org.itsnat.core.event.ItsNatEvent#getExtraParam(String)}</p>
 *
 * @see org.itsnat.core.ItsNatDocument#addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,ParamTransport[],String,long)
 * @author Jose Maria Arranz Santamaria
 */
public class CustomParamTransport extends SingleParamTransport
{
    private String code;

    /**
     * Creates a new instance ready to transport a value obtained in the client using the specified name
     * and the specified JavaScript code. This code is executed to obtain the value before the event
     * is sent to the server.
     *
     * @param name the name used to identify the value.
     * @param code the JavaScript code used to obtain the value to send to the server.
     */
    public CustomParamTransport(String name,String code)
    {
        super(name,false);

        this.code = code;
    }

    /**
     * Returns the JavaScript code used to obtain the value to send to the server.
     *
     * @return code the JavaScript code.
     */
    public String getScriptCode()
    {
        return code;
    }
}
