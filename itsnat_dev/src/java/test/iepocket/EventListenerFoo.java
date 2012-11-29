/*
 * EventListenerFoo.java
 *
 * Created on 21 de abril de 2008, 21:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.iepocket;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class EventListenerFoo implements EventListener
{
    public void handleEvent(Event evt)
    {
        throw new RuntimeException("ERROR");
    }
}
