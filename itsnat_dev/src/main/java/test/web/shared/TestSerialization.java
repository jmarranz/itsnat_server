/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.shared;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatSession;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.core.servlet.ItsNatSessionObjectInputStream;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import test.shared.TestSerializationConfig;

/**
 * Este test sirve para testear que los elementos de ItsNat son serializables
 * para que puedan funcionar en GAE (modo session replication capable).
 * 
 *
 * @author jmarranz
 */
public class TestSerialization implements Serializable,EventListener
{


    public TestSerialization(ItsNatServletRequest request)
    {
        if (!TestSerializationConfig.enable) return;

        testSerialization(request);

        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        itsNatDoc.addEventListener(this);
    }

    public static void testSerialization(ItsNatServletRequest request)
    {
        ClientDocument clientDoc = request.getClientDocument();
        //String file = "c:\\tmp\\itsnat_serialize_test.tmp";

        ByteArrayOutputStream ostream = null;
        ByteArrayInputStream istream = null;
        try
        {
            ItsNatSession session = clientDoc.getItsNatSession();

           ostream = new ByteArrayOutputStream();
           ObjectOutputStream p = new DebuggingObjectOutputStream(ostream);
           p.writeObject(session);
           // p.writeObject(clientDoc); // Write the tree to the stream.
           p.flush();
           ostream.close();    // close the file.

            Thread.sleep(500); // Para que se note que estamos haciendo este test

           byte[] stream = ostream.toByteArray();
           istream = new ByteArrayInputStream(stream);
           ObjectInputStream q = new ItsNatSessionObjectInputStream(istream); // ItsNatSessionObjectInputStream es INTERNO DE ITSNAT sólo usar para estas pruebas
           //clientDoc = (ClientDocument)q.readObject();
           session = (ItsNatSession)q.readObject();
           istream.close();    // close the file.
        }
        catch (Exception ex)
        {
            try
            {
                if (ostream != null) ostream.close();
                if (istream != null) istream.close();
            }
            catch(IOException ex2)
            {
                ex.printStackTrace();
                throw new RuntimeException(ex2);
            }
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void handleEvent(Event evt)
    {
        testSerialization( ((ItsNatEvent)evt).getItsNatServletRequest() );

        ((ItsNatEvent)evt).getItsNatEventListenerChain().continueChain();

        testSerialization( ((ItsNatEvent)evt).getItsNatServletRequest() );
    }

}
